#!/usr/bin/env node
/**
 * Smoke regression for the original-menu modal render fixtures.  It keeps the
 * source Cocos stack (modal + SayLayer) and the real LibGDX framebuffer in
 * the same test, so a future renderer change cannot silently remove a modal
 * capture state or produce an empty/invalid PNG.
 */
import assert from "node:assert/strict";
import { existsSync, readFileSync, statSync, writeFileSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const report = resolve(root, "build/yingchuan-modal-captures.json");
const fixtures = [
  ["TerrainLayer", "yingchuan-terrain"], ["PropertyLayer", "yingchuan-property"],
  ["TreasureLayer", "yingchuan-treasure"], ["SettingLayer", "yingchuan-setting"],
  ["SaveLayer", "yingchuan-save"], ["LoadGameLayer", "yingchuan-load"],
  ["ForcesListLayer", "yingchuan-forces"], ["HelperLayer", "yingchuan-helper"],
];

function run(command, args, cwd) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8", maxBuffer: 16 * 1024 * 1024 });
  assert.equal(result.status, 0, `${command} failed\n${result.stdout}\n${result.stderr}`);
  return `${result.stdout}\n${result.stderr}`;
}
function pngSize(path) {
  const data = readFileSync(path);
  assert.deepEqual([...data.subarray(0, 8)], [137,80,78,71,13,10,26,10], `${path} is not PNG`);
  return [data.readUInt32BE(16), data.readUInt32BE(20)];
}

const results = [];
for (const [sourceLayer, state] of fixtures) {
  const sourceStack = resolve(sourceRoot, `build/python-source-battle-verification-layer-${sourceLayer}-open-stack.json`);
  assert.ok(existsSync(sourceStack), `source fixture missing: ${sourceLayer}`);
  const stack = JSON.parse(readFileSync(sourceStack, "utf8"));
  assert.equal(stack.requestedPresent, true, `source did not attach ${sourceLayer}`);
  assert.ok(stack.overlaysBefore?.some(layer => layer.name === "SayLayer" && layer.active), `${sourceLayer} lost original SayLayer stack`);
  const capture = `/tmp/jojo-${state}.png`;
  const output = run("./gradlew", [":desktop:run", "--no-daemon", `--args=--battle --scenario=S_00 --capture-state=${state} --capture=${capture}`], root);
  assert.match(output, /RENDER_CAPTURE_OK:/, `game did not report ${state} capture`);
  assert.ok(existsSync(capture) && statSync(capture).size > 4096, `empty game capture: ${state}`);
  const [width, height] = pngSize(capture);
  assert.ok(width >= 1280 && height >= 720, `unexpected ${state} framebuffer ${width}×${height}`);
  results.push({ sourceLayer, state, width, height });
}
writeFileSync(report, JSON.stringify({ result: "ok", fixtures: results }, null, 2));
console.log(`YINGCHUAN_MODAL_CAPTURES_OK fixtures=${results.length}`);
