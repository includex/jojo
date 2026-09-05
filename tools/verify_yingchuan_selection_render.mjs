#!/usr/bin/env node
/** Verifies the source Control._process selection overlay against the port capture. */
import assert from "node:assert/strict";
import { existsSync, readFileSync, writeFileSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const sourceStatePath = resolve(sourceRoot, "build/python-source-battle-verification-selection.json");
const portCapture = "/tmp/jojo-yingchuan-selection.png";
const report = resolve(root, "build/yingchuan-selection-render.json");

function run(command, args, cwd) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8", maxBuffer: 16 * 1024 * 1024 });
  if (result.status !== 0) throw new Error(`${command} failed (${result.status})\n${result.stdout}\n${result.stderr}`);
  return `${result.stdout}\n${result.stderr}`;
}

run("./node_modules/.bin/electron", [".", "--verify-python-battle", "--capture-python-battle-selection"], sourceRoot);
const source = JSON.parse(readFileSync(sourceStatePath, "utf8"));
assert.deepEqual(source.selected, { id: 43, characterId: 210, x: 10, y: 17 }, "unexpected source selection fixture");
const sourceMove = source.tiles.filter(tile => tile.tag === 1).length;
const sourceAttack = source.tiles.filter(tile => tile.tag === 3).length;
// The source's scenario scheduler can advance by one movement point before
// this read-only fixture is installed.  Treat the freshly captured source
// overlay as the oracle instead of pinning an incidental count.
assert.ok(sourceMove > 0, "source did not emit a move-range overlay");
assert.ok(sourceAttack > 0, "source did not emit an attack-range overlay");

const portOutput = run("./gradlew", [
  ":desktop:run", "--no-daemon",
  `--args=--battle --scenario=S_00 --capture-state=yingchuan-selection --capture=${portCapture}`,
], root);
const port = portOutput.match(/SELECTION_CAPTURE_STATE: unit=(\d+)@(\d+),(\d+) move=(\d+) moveFrame=(\w+) attack=(\d+) cursor=(true|false)/);
assert.ok(port, "port did not emit the selection overlay fixture");
assert.deepEqual(port.slice(1, 4).map(Number), [210, 10, 17], "port selected a different source unit");
assert.equal(Number(port[4]), sourceMove, "move-range tile count differs from source");
assert.equal(port[5], "green", "fixture must use the source non-Control Mine range frame");
assert.equal(Number(port[6]), sourceAttack, "attack RED_BOX tile count differs from source");
assert.equal(port[7], "true", "source Mark_14 cursor was not rendered");
assert.ok(existsSync(portCapture), "port selection framebuffer was not written");

const result = { unit: 210, tile: [10, 17], moveTiles: sourceMove, attackTiles: sourceAttack, result: "ok" };
writeFileSync(report, JSON.stringify(result, null, 2));
console.log(`YINGCHUAN_SELECTION_RENDER_OK ${JSON.stringify(result)}`);
