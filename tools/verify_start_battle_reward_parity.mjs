#!/usr/bin/env node
/**
 * Regenerate the StartBattleLayer/BattleSortLayer/RewardLayer render-event
 * comparisons that `render_parity_scope.json` declares.
 *
 * These eight states had no producing task at all: the reports left in
 * `build/render-events/*-port-diff.json` were hand-run artifacts from before
 * the desktop -> verification module split, and the scope file had since been
 * renamed to expect `*-game-diff.json`, so the gate could only ever report
 * them as missing.
 */
import assert from "node:assert/strict";
import { mkdirSync, rmSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

// Chromium converts `capturePage` output into the attached display's colour
// profile; pin it so every source artifact of a run is comparable.
const SRGB_CAPTURE = "--force-color-profile=srgb";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const electron = resolve(sourceRoot, "node_modules/.bin/electron");
const classpath = process.env.JOJO_VERIFICATION_CLASSPATH;
assert.ok(classpath, "JOJO_VERIFICATION_CLASSPATH must contain verification runtimeClasspath");

const eventDir = resolve(root, "build/render-events");
mkdirSync(eventDir, { recursive: true });

function run(command, args, cwd = root) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8", maxBuffer: 32 * 1024 * 1024, timeout: 600_000 });
  if (result.error || result.status !== 0) {
    throw new Error(`${command} failed status=${result.status} signal=${result.signal}: ${result.error?.message ?? ""}\n${result.stdout ?? ""}\n${result.stderr ?? ""}`);
  }
  return result.stdout ?? "";
}

// `sourceState` selects the Electron harness route; `gameState` selects the
// verification launcher's capture fixture; `report` is the path the scope file
// declares for this state.
const states = [
  { id: "start-battle", sourceState: "start-battle", gameState: "start-battle-fixture" },
  { id: "start-battle-unit-info", sourceState: "start-battle-unit-info", gameState: "start-battle-unit-info-fixture" },
  { id: "start-battle-sort-open", sourceState: "start-battle-sort-open", gameState: "start-battle-sort-open-fixture" },
  { id: "start-battle-sort-select", sourceState: "start-battle-sort-select", gameState: "start-battle-sort-select-fixture" },
  { id: "start-battle-sort-cancel", sourceState: "start-battle-sort-cancel", gameState: "start-battle-sort-cancel-fixture" },
  { id: "reward-basic", sourceState: "reward-basic", gameState: "reward-basic-fixture" },
  { id: "reward-card-1", sourceState: "reward-card-1", gameState: "reward-card-1-fixture" },
  { id: "reward-card-2", sourceState: "reward-card-2", gameState: "reward-card-2-fixture" },
];

for (const state of states) {
  const sourceLog = resolve(eventDir, `source-${state.id}.jsonl`);
  const gameLog = resolve(eventDir, `game-${state.id}.jsonl`);
  const report = resolve(eventDir, `${state.id}-game-diff.json`);
  rmSync(sourceLog, { force: true });
  rmSync(gameLog, { force: true });
  rmSync(report, { force: true });

  run(electron, [".", SRGB_CAPTURE, `--render-event-log=${sourceLog}`, `--render-event-state=${state.sourceState}`], sourceRoot);
  run("java", ["-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED", "-cp", classpath,
    "com.jojo.game.verification.VerificationDesktopLauncher",
    `--capture-state=${state.gameState}`, `--render-event-log=${gameLog}`]);
  run("python3", [resolve(root, "tools/compare_render_logs.py"), sourceLog, gameLog,
    "--float-tolerance=1e-5", `--json-out=${report}`]);
  console.log(`START_BATTLE_REWARD_PARITY_STATE_OK ${state.id}`);
}

console.log(`START_BATTLE_REWARD_PARITY_OK states=${states.length}`);
