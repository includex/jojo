#!/usr/bin/env node
/** Smoke-test the production BattleScreen input paths using its real runtime.
 *
 * The shared production-input driver dispatches through the BattleScreen's
 * installed InputProcessor. The trace records accepted input provenance and
 * tactical state changes so source substrings alone cannot pass this gate.
 */
import assert from "node:assert/strict";
import { existsSync, readFileSync, unlinkSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const battleLayer = resolve(root, "core/src/main/kotlin/com/jojo/game/BattleScreen.kt");
const trace = resolve(root, "build/yingchuan-input-progress-trace.json");
try { unlinkSync(trace); } catch { /* ignore stale trace */ }
const source = readFileSync(battleLayer, "utf8");
for (const contract of [
  "Input.Keys.ENTER || keycode == Input.Keys.SPACE",
  "Input.Keys.ENTER, Input.Keys.SPACE -> confirmBattleChoice()",
  "endTurn()",
  "touchDown",
]) assert.ok(source.includes(contract), `missing BattleScreen input contract: ${contract}`);

const result = spawnSync("./gradlew", [
  ":desktop:run", "--no-daemon",
  `--args=--battle --scenario=S_00 --full-battle-trace=${trace} --full-battle-time-scale=8 --full-battle-max-sim-seconds=180 --full-battle-seed=1000 --full-battle-math-seed=305419896`,
], { cwd: root, encoding: "utf8", maxBuffer: 32 * 1024 * 1024 });
if (result.status !== 0) throw new Error(`game runner failed (${result.status})\n${result.stdout}\n${result.stderr}`);
assert.ok(existsSync(trace), "game did not write input progress trace");
const data = JSON.parse(readFileSync(trace, "utf8"));
assert.ok(data.frames.length >= 2, "runtime stopped before processing the first input continuation");
assert.equal(data.config?.driver, "production-input");
for (const context of ["S_00:open-battle-menu", "S_00:end-round-menu-command", "S_00:auto-battle-toggle", "S_00:auto-battle-confirm"]) {
  assert.ok(data.inputs?.includes(context), `missing production input provenance: ${context}`);
}
assert.equal(data.frames[0].end, false, "battle is terminal before first input");
const rounds = new Set(data.frames.map(frame => frame.round));
assert.ok(rounds.size > 1, `end-turn progression did not advance round: ${JSON.stringify([...rounds])}`);
const activeFrames = data.frames.filter(frame => frame.phase === "PLAYER_INPUT");
assert.ok(activeFrames.length > 0, "no player-input frame was observed");
const firstCollocated = data.frames.findIndex(frame => frame.collocation);
assert.ok(firstCollocated > 0, "entrusted battle did not visibly transition false -> true");
assert.ok(data.frames.slice(firstCollocated).every(frame => frame.collocation), "entrusted battle was disabled again");
assert.ok(data.frames.slice(firstCollocated).some(frame => frame.camp === 0 && frame.aiPresentation),
  "no collocated player AI presentation was observed");
console.log(JSON.stringify({
  result: "ok",
  frames: data.frames.length,
  rounds: [...rounds],
  playerInputFrames: activeFrames.length,
  terminal: data.summary?.outcome ?? null,
}, null, 2));
