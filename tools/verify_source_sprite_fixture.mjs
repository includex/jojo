#!/usr/bin/env node
/**
 * Runs the recovered Cocos client and the Kotlin desktop fixture with one
 * identical BattleUnit.setAction2 request, then compares the source-selected
 * SpriteFrame geometry.  It intentionally compares atlas/frame semantics
 * before framebuffer pixels: colour-space and map backdrop differences must
 * never conceal a wrong sprite, vertical inversion, or mirroring decision.
 */
import assert from "node:assert/strict";
import { mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join, resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const read = (name, fallback) => {
  const index = process.argv.indexOf(`--${name}`);
  return index >= 0 ? Number(process.argv[index + 1]) : fallback;
};
const character = read("character", 479);
const action = read("action", 6);
const direction = read("direction", 2);
const camp = read("camp", 2);
const allFrames = process.argv.includes("--all-frames");
const reportIndex = process.argv.indexOf("--report");
const reportPath = reportIndex >= 0 ? resolve(process.argv[reportIndex + 1]) : null;
const scratch = mkdtempSync(join(tmpdir(), "jojo-source-sprite-"));

function run(command, args, cwd) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8" });
  const output = `${result.stdout}\n${result.stderr}`;
  if (result.status !== 0) throw new Error(`${command} failed (${result.status})\n${output}`);
  return output;
}

try {
  // The source command samples the original Cocos AnimationState at four
  // authored timeline points and writes its rect/stateTime to stdout.
  const sourceOutput = run("./node_modules/.bin/electron", [
    ".", "--verify-python-battle", "--capture-python-battle-actions",
    ...(allFrames ? ["--capture-python-battle-action-all-frames"] : []),
    `--capture-python-battle-character-id=${character}`,
    `--capture-python-battle-action=${action}`,
    `--capture-python-battle-direction=${direction}`,
  ], sourceRoot);
  const sourceFrames = [...sourceOutput.matchAll(/action(?:=\d+ direction=\d+)? sample f(\d+)=(\{.*\})/g)].map(([, index, json]) => ({
    index: Number(index), ...JSON.parse(json),
  }));
  assert.ok(allFrames ? sourceFrames.length >= 1 : sourceFrames.length === 4,
    "original Cocos did not produce the requested frame samples");

  const ticks = sourceFrames.map(source => Math.floor(source.stateTime * 24 + 1e-6));
  const gameOutput = run("./gradlew", [":desktop:dumpSpriteMatrix", "--no-daemon", `--args=--action=${action} --direction=${direction} --ticks=${ticks.join(",")}`], root);
  const gameFrames = [...gameOutput.matchAll(/GAME_SPRITE_FRAME (?:action=\d+ direction=\d+ )?f(\d+) tick=(\d+) source=(\w+) x=(\d+) y=(\d+) width=(\d+) height=(\d+) flipX=(\w+)/g)];
  assert.equal(gameFrames.length, sourceFrames.length, "game did not emit every requested frame");
  for (const source of sourceFrames) {
    const tick = ticks[source.index];
    const [, index, gameTick, atlas, x, y, width, height, flipX] = gameFrames[source.index];
    assert.equal(Number(index), source.index);
    assert.equal(Number(gameTick), tick);
    const encoded = Number(source.frameName);
    const sourceIndex = Number.isFinite(encoded) ? ((encoded >>> 24) & 255) : null;
    const sourceAtlas = Number.isFinite(encoded) ? ((encoded >>> 16) & 255) : null;
    const expectedRect = sourceIndex === null ? (source.originalRect || source.rect) : {
      x: 0,
      y: sourceIndex * (source.rect.height + 2) + 1,
      width: source.rect.width,
      height: source.rect.height,
    };
    assert.deepEqual(
      { x: Number(x), y: Number(y), width: Number(width), height: Number(height), flipX: flipX === "true" },
      // Cocos may express a mirror on the BattleUnit node (`scaleX = -1`)
      // while LibGDX expresses the identical final transform through its
      // texture-flip draw argument.  Compare the composed render transform,
      // not either engine's internal storage representation.
      {
        x: expectedRect.x,
        y: expectedRect.y,
        width: expectedRect.width,
        height: expectedRect.height,
        flipX: !!source.flipX !== (source.scale?.[0] < 0),
      },
      `SpriteFrame mismatch at source sample f${source.index}`,
    );
    process.stdout.write(`SOURCE_SPRITE_FIXTURE_OK f${source.index} tick=${tick} atlas=${atlas} rect=${source.rect.x},${source.rect.y},${source.rect.width}x${source.rect.height}\n`);
  }
  if (reportPath) {
    writeFileSync(reportPath, JSON.stringify({ character, action, direction, camp, allFrames, frames: sourceFrames.length, result: "ok" }, null, 2));
  }
} finally {
  rmSync(scratch, { recursive: true, force: true });
}
