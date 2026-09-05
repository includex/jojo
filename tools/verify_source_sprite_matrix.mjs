#!/usr/bin/env node
/**
 * Exhaustive source-Cocos ↔ ported-LibGDX SpriteFrame conformance runner.
 *
 * It derives every authored clip from the exported original `animeBR` data,
 * adds each source-generated `_1` mirror clip, samples every 24fps interval
 * through Cocos `BattleUnit.setAction2`, then asks the actual Kotlin timeline
 * for the identical intervals in one JVM.  Thus this is a renderer-selection
 * check, not a duplicated JS implementation of the frame rules.
 */
import assert from "node:assert/strict";
import { readFileSync, writeFileSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const args = process.argv.slice(2);
const option = (name, fallback) => {
  const value = args.find(argument => argument.startsWith(`--${name}=`));
  return value ? value.substring(name.length + 3) : fallback;
};
const character = Number(option("character", "479"));
const reportPath = resolve(option("report", "build/source-sprite-matrix.json"));

function run(command, commandArgs, cwd) {
  const result = spawnSync(command, commandArgs, { cwd, encoding: "utf8", maxBuffer: 32 * 1024 * 1024 });
  const output = `${result.stdout}\n${result.stderr}`;
  if (result.status !== 0) throw new Error(`${command} failed (${result.status})\n${output}`);
  return output;
}

const anime = JSON.parse(readFileSync(resolve(root, "core/build/generated/map-assets/battle-anime.json"), "utf8"));
const authored = Object.keys(anime).map(key => {
  const match = key.match(/^anime(\d+)(?:_(\d+))?$/);
  assert.ok(match, `unexpected original anime key: ${key}`);
  return { action: Number(match[1]), direction: match[2] === undefined ? null : Number(match[2]), key };
});
const cases = [
  ...authored.map(({ action, direction }) => ({ action, direction: direction ?? 2, kind: "authored" })),
  ...[...new Set(authored.filter(item => item.direction === 3).map(item => item.action))]
    .map(action => ({ action, direction: 1, kind: "generated-mirror" })),
];

const sourceOutput = run("./node_modules/.bin/electron", [
  ".", "--verify-python-battle", "--capture-python-battle-actions",
  "--capture-python-battle-action-all-frames",
  `--capture-python-battle-character-id=${character}`,
  `--capture-python-battle-action-cases=${cases.map(item => `${item.action}:${item.direction}`).join(",")}`,
], sourceRoot);

const framesByCase = new Map(cases.map(item => [`${item.action}:${item.direction}`, []]));
for (const match of sourceOutput.matchAll(/action=(\d+) direction=(\d+) sample f(\d+)=(\{.*\})/g)) {
  const [, action, direction, index, json] = match;
  const key = `${action}:${direction}`;
  const frames = framesByCase.get(key);
  assert.ok(frames, `Cocos emitted an unknown action case ${key}`);
  frames[Number(index)] = JSON.parse(json);
}
for (const item of cases) {
  const key = `${item.action}:${item.direction}`;
  assert.ok(framesByCase.get(key)?.length, `Cocos emitted no frames for ${key}`);
}

const matrixArg = cases.map(item => {
  const frames = framesByCase.get(`${item.action}:${item.direction}`);
  const ticks = frames.map(frame => Math.floor(frame.stateTime * 24 + 1e-6));
  return `${item.action}:${item.direction}:${ticks.join(",")}`;
}).join(";");
const portOutput = run("./gradlew", [":desktop:dumpSpriteMatrix", "--no-daemon", `--args=--cases=${matrixArg}`], root);
const portByCase = new Map(cases.map(item => [`${item.action}:${item.direction}`, []]));
for (const match of portOutput.matchAll(/PORT_SPRITE_FRAME action=(\d+) direction=(\d+) f(\d+) tick=(\d+) source=(\w+) x=(\d+) y=(\d+) width=(\d+) height=(\d+) flipX=(\w+)/g)) {
  const [, action, direction, index, tick, source, x, y, width, height, flipX] = match;
  const frames = portByCase.get(`${action}:${direction}`);
  assert.ok(frames, `port emitted an unknown action case ${action}:${direction}`);
  frames[Number(index)] = { tick: Number(tick), source, x: Number(x), y: Number(y), width: Number(width), height: Number(height), flipX: flipX === "true" };
}

let verifiedFrames = 0;
for (const item of cases) {
  const key = `${item.action}:${item.direction}`;
  const sourceFrames = framesByCase.get(key);
  const portFrames = portByCase.get(key);
  assert.equal(portFrames.length, sourceFrames.length, `port did not emit every sample for ${key}`);
  for (let index = 0; index < sourceFrames.length; index++) {
    const source = sourceFrames[index];
    const port = portFrames[index];
    const tick = Math.floor(source.stateTime * 24 + 1e-6);
    const encoded = Number(source.frameName);
    const spriteIndex = Number.isFinite(encoded) ? ((encoded >>> 24) & 255) : null;
    const expected = spriteIndex === null ? (source.originalRect || source.rect) : {
      x: 0,
      y: spriteIndex * (source.rect.height + 2) + 1,
      width: source.rect.width,
      height: source.rect.height,
    };
    assert.deepEqual(port, {
      tick,
      source: port.source,
      x: expected.x,
      y: expected.y,
      width: expected.width,
      height: expected.height,
      // Cocos uses a negative BattleUnit scale for its generated `_1` clip;
      // LibGDX stores the equivalent final transform on TextureRegion.flipX.
      flipX: !!source.flipX !== (source.scale?.[0] < 0),
    }, `SpriteFrame mismatch in ${key}, sample ${index}`);
    verifiedFrames++;
  }
  process.stdout.write(`SOURCE_SPRITE_MATRIX_OK action=${item.action} direction=${item.direction} kind=${item.kind} frames=${sourceFrames.length}\n`);
}
const report = { character, cases: cases.length, verifiedFrames, result: "ok" };
writeFileSync(reportPath, JSON.stringify(report, null, 2));
console.log(`SOURCE_SPRITE_MATRIX_COMPLETE ${JSON.stringify(report)}`);
