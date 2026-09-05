#!/usr/bin/env node
/** Regenerate the battle render evidence consumed by render_parity_scope.json. */
import assert from "node:assert/strict";
import { existsSync, mkdirSync, readFileSync, rmSync, writeFileSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";
import { randomUUID } from "node:crypto";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const electron = resolve(sourceRoot, "node_modules/.bin/electron");
const classpath = process.env.JOJO_VERIFICATION_CLASSPATH;
assert.ok(classpath, "JOJO_VERIFICATION_CLASSPATH must contain verification runtimeClasspath");

const runId = randomUUID();
const startedNs = Date.now() * 1_000_000;
const marker = resolve(root, "build/render-events/fresh-battle-render-parity.started.json");
const manifest = resolve(root, "build/render-events/fresh-battle-render-parity.manifest.json");
const frameDir = resolve(root, "build/render-frames");
mkdirSync(resolve(root, "build/render-events"), { recursive: true });
mkdirSync(resolve(root, "build/reports/render-events"), { recursive: true });
mkdirSync(frameDir, { recursive: true });
rmSync(manifest, { force: true });
writeFileSync(marker, JSON.stringify({ format: "jojo-render-freshness/v1", runId, startedNs }, null, 2));

const artifacts = new Set([marker]);
function run(command, args, cwd = root) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8", maxBuffer: 32 * 1024 * 1024, timeout: 600_000 });
  if (result.error || result.status !== 0) {
    throw new Error(`${command} failed status=${result.status} signal=${result.signal}: ${result.error?.message ?? ""}\n${result.stdout ?? ""}\n${result.stderr ?? ""}`);
  }
}
function clean(...paths) { for (const path of paths) rmSync(path, { force: true }); }
function game(args) {
  run("java", ["-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED", "-cp", classpath,
    "com.jojo.game.verification.VerificationDesktopLauncher", ...args]);
}
function compareLogs(expected, actual, report) {
  run("python3", [resolve(root, "tools/compare_render_logs.py"), expected, actual,
    "--float-tolerance=0", `--json-out=${report}`]);
  [expected, actual, report].forEach(path => artifacts.add(path));
}
function compareFrames(expected, actual, report, extraArgs = []) {
  run("python3", [resolve(root, "tools/compare_battle_render_frames.py"), expected, actual, report, ...extraArgs]);
  [expected, actual, report].forEach(path => artifacts.add(path));
}

// This existing verifier now compares all three source/game dialogue PNGs.
const actorReport = resolve(root, "build/yingchuan-actor-state.json");
clean(actorReport, ...[1, 2, 3].map(step => resolve(root, `build/yingchuan-dialogue-${step}-game.png`)));
run("node", [resolve(root, "tools/verify_yingchuan_actor_state.mjs")]);
artifacts.add(actorReport);
for (const step of [1, 2, 3]) {
  artifacts.add(resolve(root, `build/yingchuan-dialogue-${step}-game.png`));
  artifacts.add(resolve(sourceRoot, `build/python-source-battle-verification-dialogue${step}.png`));
  artifacts.add(resolve(sourceRoot, `build/python-source-battle-verification-dialogue${step}.json`));
}

const routes = ["hp-camps-partial", "outline-highlight", "hit-impact", "cleanup", "death-action", "death-hidden"];
for (const route of routes) {
  const phase = `battle-character-${route}`;
  const sourceLog = resolve(sourceRoot, `build/render-events/original-${phase}.jsonl`);
  const gameLog = resolve(root, `build/render-events/game-${phase}.jsonl`);
  const report = resolve(root, `build/reports/render-events/${phase}.json`);
  const sourcePng = resolve(frameDir, `source-${phase}.png`);
  const gamePng = resolve(frameDir, `game-${phase}.png`);
  const pixelReport = resolve(frameDir, `${phase}.json`);
  clean(sourceLog, sourceLog.replace(/\.jsonl$/, ".state.json"), gameLog, report, sourcePng, gamePng, pixelReport);
  run(electron, [".", `--render-battle-character-route=${route}`, `--render-event-log=${sourceLog}`,
    `--render-frame-png=${sourcePng}`, `--verification-run-id=${runId}-${route}`], sourceRoot);
  game(["--battle", "--scenario=S_00", `--capture-state=${phase}-fixture`, `--render-event-log=${gameLog}`]);
  game(["--battle", "--scenario=S_00", `--capture-state=${phase}-fixture`, `--capture=${gamePng}`]);
  compareLogs(sourceLog, gameLog, report);
  compareFrames(sourcePng, gamePng, pixelReport);
}

{
  const phase = "battle-dialogue-blending";
  const sourceLog = resolve(root, `build/render-events/original-${phase}.jsonl`);
  const gameLog = resolve(root, `build/render-events/game-${phase}.jsonl`);
  const report = resolve(root, `build/render-events/${phase}-diff.json`);
  const sourcePng = resolve(frameDir, `source-${phase}.png`);
  const gamePng = resolve(frameDir, `game-${phase}.png`);
  const pixelReport = resolve(frameDir, `${phase}.json`);
  clean(sourceLog, sourceLog.replace(/\.jsonl$/, ".state.json"), gameLog, report, sourcePng, gamePng, pixelReport);
  run(electron, [".", "--render-battle-dialogue-blending", `--render-event-log=${sourceLog}`,
    `--render-frame-png=${sourcePng}`, `--verification-run-id=${runId}-dialogue-blending`], sourceRoot);
  game(["--battle", "--scenario=S_00", "--capture-state=battle-dialogue-blending-fixture", `--render-event-log=${gameLog}`]);
  game(["--battle", "--scenario=S_00", "--capture-state=battle-dialogue-blending-fixture", `--capture=${gamePng}`]);
  compareLogs(sourceLog, gameLog, report);
  compareFrames(sourcePng, gamePng, pixelReport, ["--max-structural-mae=1.3", "--max-structural-changed-ratio=0.015"]);
}

for (const artifact of artifacts) assert.ok(existsSync(artifact), `fresh render artifact missing: ${artifact}`);
writeFileSync(manifest, JSON.stringify({
  format: "jojo-render-freshness/v1", runId, startedNs, completedNs: Date.now() * 1_000_000,
  artifacts: [...artifacts].map(path => path.startsWith(root + "/") ? path.slice(root.length + 1) : path),
}, null, 2));
console.log(`FRESH_BATTLE_RENDER_PARITY_OK runId=${runId} artifacts=${artifacts.size}`);
