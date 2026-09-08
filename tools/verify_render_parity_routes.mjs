#!/usr/bin/env node
/**
 * Run every producer declared in `tools/render_parity_routes.json`.
 *
 * `render_parity_scope.json` declares 162 states but, before this driver, only
 * a handful had a task that could regenerate their comparison report. The rest
 * were validated from whatever happened to sit in the gitignored
 * `build/render-events/`, much of it hand-run months earlier, so a clean
 * checkout could not reproduce them and a regression could not fail them.
 *
 * Pass one or more route ids to run a subset.
 */
import assert from "node:assert/strict";
import { mkdirSync, readFileSync, rmSync } from "node:fs";
import { dirname, resolve } from "node:path";
import { spawnSync } from "node:child_process";

// Chromium converts `capturePage` output into the attached display's colour
// profile; pin it so every source artifact of a run is comparable.
const SRGB_CAPTURE = "--force-color-profile=srgb";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const electron = resolve(sourceRoot, "node_modules/.bin/electron");
const classpath = process.env.JOJO_VERIFICATION_CLASSPATH;
assert.ok(classpath, "JOJO_VERIFICATION_CLASSPATH must contain verification runtimeClasspath");

// The battle-init route observes BattleInitLayer during the Hall -> Battle
// transition. The harness used to exclude that route from its Hall fallback
// jump, but this restored profile returns to Hall with the map already loaded
// and no selectable scenario row, so the "natural" entry never arrives and the
// route dead-ends. The fallback calls HallLayer.jumpScene, which is the
// original's own production entry, so it is a valid way in. The harness lives
// in a sibling checkout that this repository cannot version, so check it here
// rather than failing several minutes into the run with a scene dump.
function assertHarnessDrivesBattleInit() {
  const harness = resolve(sourceRoot, "electron/main.cjs");
  const text = readFileSync(harness, "utf8");
  assert.ok(
    !text.includes("sourceBattleVerifyMode && !sourceBattleInitRoute && !sourceBattleJumpRequested"),
    `${harness} still excludes the battle-init route from its Hall fallback jump. `
    + "Remove the `!sourceBattleInitRoute &&` term from that condition so HallLayer.jumpScene can enter the battle.",
  );
}

const table = JSON.parse(readFileSync(resolve(root, "tools/render_parity_routes.json"), "utf8"));

// The scope file owns where each state's report lives; drift between the two
// silently turns a produced report into a "missing" one, which is how these
// states came to be validated from months-old hand-run artifacts.
{
  const scope = JSON.parse(readFileSync(resolve(root, "tools/render_parity_scope.json"), "utf8"));
  const declared = new Map();
  for (const phase of scope.phases) for (const state of phase.states) {
    if (state.report && !declared.has(state.id)) declared.set(state.id, state.report);
  }
  for (const route of table.routes) {
    assert.equal(route.report, declared.get(route.id),
      `render_parity_routes.json route ${route.id} writes ${route.report} but render_parity_scope.json reads ${declared.get(route.id)}`);
  }
}
const selected = process.argv.slice(2);
const routes = selected.length ? table.routes.filter(route => selected.includes(route.id)) : table.routes;
assert.ok(routes.length, `no routes matched ${selected.join(",")}`);

function run(command, args, cwd = root) {
  const result = spawnSync(command, args, { cwd, encoding: "utf8", maxBuffer: 32 * 1024 * 1024, timeout: 600_000 });
  if (result.error || result.status !== 0) {
    throw new Error(`${command} failed status=${result.status} signal=${result.signal}: ${result.error?.message ?? ""}\n${result.stdout ?? ""}\n${result.stderr ?? ""}`);
  }
  return result.stdout ?? "";
}

const failures = [];
if (routes.some(route => route.id === "battle-init")) assertHarnessDrivesBattleInit();

for (const route of routes) {
  // A state the Electron harness has no fixture for cannot be compared here at
  // all. Say so instead of running it and reporting whatever screen the harness
  // happened to fall back to.
  if (route.noSourceRoute) {
    console.log(`RENDER_PARITY_ROUTE_NO_SOURCE ${route.id} (${route.noSourceRoute})`);
    continue;
  }
  const sourceLog = resolve(root, `build/render-events/source-${route.id}.jsonl`);
  const gameLog = resolve(root, `build/render-events/game-${route.id}.jsonl`);
  const report = resolve(root, route.report);
  mkdirSync(dirname(report), { recursive: true });
  mkdirSync(dirname(sourceLog), { recursive: true });
  for (const stale of [sourceLog, gameLog, report]) rmSync(stale, { force: true });

  /** Capture both sides once and compare them. */
  function attempt() {
    run(electron, [".", SRGB_CAPTURE, ...route.source, `--render-event-log=${sourceLog}`], sourceRoot);
    // Battle overlays live on BattleScreen, which only exists once the launcher
    // is told to open a battle; the isolated fixture screens must not get that
    // flag or they fall through to the ordinary scenario route.
    const gameArgs = route.battle ? ["--battle", `--scenario=${route.scenario ?? "S_00"}`] : [];
    run("java", ["-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED", "-cp", classpath,
      "com.jojo.game.verification.VerificationDesktopLauncher", ...gameArgs,
      `--capture-state=${route.game}`, `--render-event-log=${gameLog}`]);
    const animated = (route.animatedFields ?? []).map(field => `--animated-field=${field}`);
    run("python3", [resolve(root, "tools/compare_render_logs.py"), sourceLog, gameLog,
      `--float-tolerance=${table.floatTolerance}`, ...animated, `--json-out=${report}`]);
  }

  try {
    try {
      attempt();
    } catch (first) {
      // The original is driven by wall-clock waits, so a loaded machine can
      // capture it before a layer has finished laying out or after a timed
      // overlay has removed itself.  A real difference reproduces; a capture
      // race does not.  The retry is announced so the flakiness stays visible
      // instead of quietly becoming the normal result.
      console.log(`RENDER_PARITY_ROUTE_RETRY ${route.id}: ${String(first.message).split("\n").slice(0, 3).join(" | ")}`);
      attempt();
    }
    // A route whose game-side log is a stored copy of the original can only ever
    // agree; say so rather than letting it read as evidence.
    console.log(route.cannedReplay
      ? `RENDER_PARITY_ROUTE_CIRCULAR ${route.id} (${route.cannedReplay})`
      : `RENDER_PARITY_ROUTE_OK ${route.id}`);
  } catch (error) {
    failures.push(route.id);
    console.log(`RENDER_PARITY_ROUTE_FAIL ${route.id}: ${String(error.message).split("\n").slice(0, 6).join(" | ")}`);
  }
}

if (failures.length) {
  console.log(`RENDER_PARITY_ROUTES_BLOCKED routes=${routes.length} failures=${failures.length} ids=${failures.join(",")}`);
  process.exit(1);
}
const circular = routes.filter(route => route.cannedReplay).length;
const unreachable = routes.filter(route => route.noSourceRoute).length;
console.log(`RENDER_PARITY_ROUTES_OK routes=${routes.length} circular=${circular} noSource=${unreachable}`);
