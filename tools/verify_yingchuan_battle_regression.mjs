#!/usr/bin/env node
/**
 * Deterministic smoke/regression check for the S_00 (Yingchuan) battle.
 *
 * This is intentionally a behavioural check, rather than a screenshot test:
 * it catches the two user-visible failures which are otherwise easy to miss
 * when a scripted capture state is used:
 *   - the opening street actors must not acquire a backwards movement action;
 *   - the battle must not terminate as an enemy victory during round one.
 *
 * The command currently fails against a game that reproduces the reported
 * early game-over.  Keeping the failure explicit makes this a useful guard
 * while BattleScreen/turn setup is repaired.
 */
import assert from "node:assert/strict";
import { existsSync, readFileSync, unlinkSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const suppliedTrace = process.argv[2];
const trace = suppliedTrace
  ? resolve(suppliedTrace)
  : resolve(root, "build/yingchuan-regression-trace.json");

// Standalone use still launches the application. Gradle's verification task
// supplies a trace produced by its own JavaExec task, avoiding a nested Gradle
// invocation while `desktop:check` already owns the build.
if (!suppliedTrace) {
  try { unlinkSync(trace); } catch { /* stale evidence is not a test result */ }
  const args = [
    ":desktop:run", "--no-daemon",
    "--args=--battle --scenario=S_00 --full-battle-trace=" + trace +
      // Visible AI callbacks intentionally make the battle longer than the old
      // instant-resolution shortcut. Keep enough simulated time to reach the
      // authored terminal result while still running 8x faster than real time.
      " --full-battle-time-scale=8 --full-battle-max-sim-seconds=600" +
      " --full-battle-seed=1000 --full-battle-math-seed=305419896",
  ];
  const run = spawnSync("./gradlew", args, {
    cwd: root,
    encoding: "utf8",
    maxBuffer: 32 * 1024 * 1024,
  });
  if (run.status !== 0) {
    throw new Error(`game battle runner failed (${run.status})\n${run.stdout}\n${run.stderr}`);
  }
}
assert.ok(existsSync(trace), `game did not write trace: ${trace}`);
const data = JSON.parse(readFileSync(trace, "utf8"));
assert.equal(data.format, "jojo-yingchuan-full-battle-trace/v1");
assert.equal(data.engine, "jojo-game", "trace must be emitted by the production LibGDX runtime");
assert.equal(data.config?.driver, "production-input",
  "standalone battle must be driven through the installed production InputProcessor");
assert.ok(data.frames.length > 0, "game trace contains no frames");

const firstEnd = data.frames.find(frame => frame.end);
const prematureEnemyLoss = firstEnd && data.summary?.outcome === "ENEMY_VICTORY" && firstEnd.round <= 8;
if (prematureEnemyLoss) {
  const first = data.frames[0];
  const unitsByFaction = first.units.reduce((counts, unit) => {
    counts[unit[2]] = (counts[unit[2]] ?? 0) + 1;
    return counts;
  }, {});
  throw new Error([
    "YINGCHUAN_EARLY_GAME_OVER",
    `reason=${data.reason}`,
    `outcome=${data.summary?.outcome ?? "null"}`,
    `round=${firstEnd.round}`,
    `camp=${firstEnd.camp}`,
    `frame=${firstEnd.f}`,
    `time=${firstEnd.t}`,
    `firstScript=${first.script}`,
    `firstMaxRounds=${first.maxRounds ?? "missing"}`,
    `firstFactionCounts=${JSON.stringify(unitsByFaction)}`,
    `firstPlayerCount=${first.playerCount ?? "missing"}`,
    `firstFriendCount=${first.friendCount ?? "missing"}`,
    `firstEnemyCount=${first.enemyCount ?? "missing"}`,
  ].join(" "));
}
const first = data.frames[0];
assert.equal(first.end, false, "Yingchuan battle must be non-terminal on its first rendered frame");
assert.equal(first.maxRounds, 20, "S_00 setGlobalData must apply its authored 20-round limit before outcome()");
assert.equal(first.playerCount, 1, "full-battle roster bootstrap must materialize Cao Cao as the player actor");
assert.equal(data.reason, "battle-end", `Yingchuan full battle did not finish naturally (reason=${data.reason})`);
assert.equal(data.summary?.end, true, "full-battle summary must be terminal");
assert.equal(data.summary?.outcome, "PLAYER_VICTORY", "deterministic Yingchuan run must end in player victory");
assert.equal(data.summary?.frameCount, data.frames.length, "trace summary frame count drifted from recorded frames");
assert.equal(data.frames.at(-1)?.end, true, "last recorded frame must expose the terminal outcome");

// The standalone runner must use the same visible menu and entrusted-battle
// controls as a player. These entries are appended only after dispatch through
// Gdx.input.inputProcessor; direct state mutation cannot satisfy this gate.
const requiredInputs = [
  "S_00:open-battle-menu", "S_00:end-round-menu-command",
  "S_00:auto-battle-toggle", "S_00:auto-battle-confirm",
];
for (const input of requiredInputs) {
  assert.ok(data.inputs?.includes(input), `missing production input provenance: ${input}`);
}

const firstCollocated = data.frames.findIndex(frame => frame.collocation);
assert.ok(firstCollocated > 0, "entrusted battle never changed from false to true");
assert.equal(data.frames[0].collocation, false, "trace must begin before entrusted battle is enabled");
assert.equal(data.frames[firstCollocated - 1].collocation, false,
  "entrusted battle transition has no preceding false frame");
assert.ok(data.frames.slice(firstCollocated).every(frame => frame.collocation),
  "entrusted battle was unexpectedly disabled after being confirmed");

const rounds = [...new Set(data.frames.map(frame => frame.round))];
assert.deepEqual(rounds, Array.from({ length: data.summary.round }, (_, index) => index + 1),
  `battle rounds are not a continuous 1..${data.summary.round} sequence`);
for (let index = 1; index < data.frames.length; index += 1) {
  const delta = data.frames[index].round - data.frames[index - 1].round;
  assert.ok(delta === 0 || delta === 1,
    `battle round skipped or moved backwards at frame ${data.frames[index].f}: delta=${delta}`);
}

const playerAiFrames = data.frames.slice(firstCollocated).filter(frame => {
  const presentation = frame.aiPresentation;
  return frame.camp === 0 && presentation &&
    frame.units.some(unit => unit[1] === presentation.actor && unit[2] === 0);
});
const playerAiStages = new Set(playerAiFrames.map(frame => frame.aiPresentation.stage));
for (const stage of ["FOCUS_DELAY", "MOVING", "ACTION_DELAY", "ACTION", "COMPLETE"]) {
  assert.ok(playerAiStages.has(stage), `collocated player AI never exposed visible ${stage} phase`);
}
const playerMove = playerAiFrames.find(frame =>
  frame.aiPresentation.stage === "MOVING" &&
  (frame.aiPresentation.from[0] !== frame.aiPresentation.to[0] ||
    frame.aiPresentation.from[1] !== frame.aiPresentation.to[1]) &&
  frame.units.some(unit => unit[1] === frame.aiPresentation.actor && unit[17]?.visual &&
    (unit[17].visual[0] !== frame.aiPresentation.from[0] || unit[17].visual[1] !== frame.aiPresentation.from[1]))
);
assert.ok(playerMove, "collocated player turn never exposed move2 interpolation");
const completedPlayerMove = data.frames.slice(firstCollocated).find(frame => {
  const presentation = frame.aiPresentation;
  if (!presentation || presentation.stage !== "COMPLETE" || presentation.deferred !== false ||
      (presentation.from[0] === presentation.to[0] && presentation.from[1] === presentation.to[1])) return false;
  const actor = frame.units.find(unit => unit[1] === presentation.actor && unit[2] === 0);
  return actor && actor[3] === presentation.to[0] && actor[4] === presentation.to[1];
});
assert.ok(completedPlayerMove, "collocated player move never committed at the completion callback");

// A rendered AI turn must expose the callback phases instead of committing a
// whole camp between two trace frames. Camp 2 is the source Enemy camp.
const enemyAiFrames = data.frames.filter(frame => frame.camp === 2 && frame.aiPresentation);
const enemyAiStages = new Set(enemyAiFrames.map(frame => frame.aiPresentation.stage));
for (const stage of ["FOCUS_DELAY", "MOVING", "ACTION_DELAY", "ACTION", "COMPLETE"]) {
  assert.ok(enemyAiStages.has(stage), `enemy AI never exposed visible ${stage} phase`);
}
assert.ok(new Set(enemyAiFrames.map(frame => frame.aiPresentation.actor)).size > 1,
  "enemy AI presentation did not advance actor-by-actor");
for (const frame of enemyAiFrames) {
  const presentation = frame.aiPresentation;
  if (presentation.stage !== "COMPLETE") assert.equal(presentation.deferred, true,
    `AI actor ${presentation.actor} exposed a resolved model before callbacks at frame ${frame.f}`);
  const actor = frame.units.find(unit => unit[1] === presentation.actor);
  if (["FOCUS_DELAY", "MOVING"].includes(presentation.stage) && actor) {
    assert.deepEqual(actor.slice(3, 5), presentation.from,
      `AI actor ${presentation.actor} logical position changed before move2 completion at frame ${frame.f}`);
  }
  if (["FOCUS_DELAY", "MOVING", "ACTION_DELAY"].includes(presentation.stage) && presentation.target >= 0) {
    const target = frame.units.find(unit => unit[1] === presentation.target);
    assert.ok(target, `AI target ${presentation.target} was removed before hit/death callbacks at frame ${frame.f}`);
    assert.equal(target[5], presentation.targetHpBefore,
      `AI target ${presentation.target} HP changed before authored hit at frame ${frame.f}`);
  }
}
assert.ok(enemyAiFrames.some(frame => {
  if (frame.aiPresentation.stage !== "MOVING") return false;
  const actor = frame.units.find(unit => unit[1] === frame.aiPresentation.actor);
  return actor && actor[17]?.visual &&
    (actor[17].visual[0] !== actor[3] || actor[17].visual[1] !== actor[4]);
}), "enemy movement never exposed move2 interpolation");
assert.ok(enemyAiFrames.some(frame => {
  if (frame.aiPresentation.stage !== "ACTION") return false;
  const actor = frame.units.find(unit => unit[1] === frame.aiPresentation.actor);
  return actor && [5, 21, 25].includes(actor[8]);
}), "enemy attack/strategy animation was not visible during ACTION");

// S_00's first two cinematic attacks are especially sensitive to callback
// ordering. Source BattleScreen.playAtkAnime starts the target reaction at the
// attack clip's `hit` event, then resumes Python when that reaction finishes:
//   474 -> 235: anime21 hit 22 + anime32 14 = 36 ticks
//   477 -> 334: anime25 hit 11 + anime32 14 = 25 ticks
// A previous game displayed anime32 immediately and waited 44/29 ticks.
const unitAt = (frame, characterId) => frame.units.find(unit => unit[1] === characterId);
const transition = (characterId, predicate, afterFrame = -1) => data.frames.find(frame => {
  const unit = unitAt(frame, characterId);
  return frame.f > afterFrame && unit && predicate(unit);
});
const assertCinematicAttack = ({ attacker, target, action, hitTicks, completeTicks }) => {
  const start = transition(attacker, unit => unit[8] === action);
  assert.ok(start, `missing cinematic attack anime${action} for ${attacker}`);
  const hit = transition(target, unit => unit[8] === 32, start.f);
  assert.ok(hit, `missing target anime32 for ${target}`);
  const complete = transition(attacker, unit => unit[8] !== action, hit.f);
  assert.ok(complete, `cinematic attack anime${action} never returned to default`);
  const stepTolerance = Math.max(...data.frames.slice(1, 12).map(frame => frame.dt || 0)) + 0.02;
  assert.ok(hit.t > start.t, `target ${target} reacted before anime${action}'s hit event`);
  assert.ok(Math.abs((hit.t - start.t) - hitTicks / 24) <= stepTolerance,
    `anime${action} hit drift: observed=${hit.t - start.t}, source=${hitTicks / 24}`);
  assert.ok(Math.abs((complete.t - start.t) - completeTicks / 24) <= stepTolerance,
    `anime${action} callback drift: observed=${complete.t - start.t}, source=${completeTicks / 24}`);
  return {
    attacker, target, action,
    start: { frame: start.f, time: start.t },
    hit: { frame: hit.f, time: hit.t, sourceTick: hitTicks },
    complete: { frame: complete.f, time: complete.t, sourceTick: completeTicks },
  };
};
const cinematicTimings = [
  assertCinematicAttack({ attacker: 474, target: 235, action: 21, hitTicks: 22, completeTicks: 36 }),
  assertCinematicAttack({ attacker: 477, target: 334, action: 25, hitTicks: 11, completeTicks: 25 }),
];

// The live trace records the original ScrollView.content.position rather
// than the game renderer's internal delta.
// Source dump: viewport=1488.372093x800, content=1920x1920 and initial
// content.position=(-104.1860465,464), yielding these asymmetric limits.
for (const frame of data.frames) {
  assert.ok(Array.isArray(frame.camera), `missing camera trace at frame ${frame.f}`);
  assert.ok(frame.camera[0] >= -216.01 && frame.camera[0] <= 216.01,
    `camera x escaped source ScrollView clamp at frame ${frame.f}: ${frame.camera[0]}`);
  assert.ok(frame.camera[1] >= -560.01 && frame.camera[1] <= 560.01,
    `camera y escaped source ScrollView clamp at frame ${frame.f}: ${frame.camera[1]}`);
}

const interpolatedMoves = data.frames.flatMap(frame => frame.units
  .filter(unit => unit[8] === 20 && unit[17]?.visual)
  .filter(unit => unit[17].visual[0] !== unit[3] || unit[17].visual[1] !== unit[4])
  .map(unit => ({ frame: frame.f, time: frame.t, id: unit[1], tile: [unit[3], unit[4]], visual: unit[17].visual })));
assert.ok(interpolatedMoves.length > 0,
  "scripted battle moves were teleported instead of exposing move2 interpolation");

// A backwards walk is represented by the source MOVE action (20) paired
// with the authored direction.  Record all observed transitions so a failed
// run can be compared directly with the original full trace.
const movement = data.frames.flatMap(frame => frame.units
  .filter(unit => unit[1] === 210 || unit[1] === 211)
  .filter(unit => unit[8] === 20)
  .map(unit => ({ frame: frame.f, time: frame.t, id: unit[1], x: unit[3], y: unit[4], direction: unit[7], action: unit[8] })));
const movementDirectionMismatches = [];
for (let index = 1; index < data.frames.length; index += 1) {
  const previous = new Map(data.frames[index - 1].units.map(unit => [unit[1], unit]));
  for (const unit of data.frames[index].units) {
    const before = previous.get(unit[1]);
    const from = before?.[17]?.visual, to = unit[17]?.visual;
    if (!before || before[8] !== 20 || unit[8] !== 20 || before[7] !== unit[7] ||
        !from || !to || (from[0] === to[0] && from[1] === to[1])) continue;
    // A fast trace frame can enter movement after several short segments or
    // cross an A* corner.  In either case the previous sample belongs to a
    // different authored direction, so its net delta cannot prove whether the
    // current clip walks forwards.  Compare only consecutive samples inside
    // the same MOVE-direction segment; this still catches a genuinely flipped
    // walk for every segment that is observable for two frames.
    const forward = unit[7] === 0 ? to[1] < from[1]
      : unit[7] === 1 ? to[0] > from[0]
      : unit[7] === 2 ? to[1] > from[1]
      : unit[7] === 3 ? to[0] < from[0]
      : false;
    if ((unit[1] === 210 || unit[1] === 211) && !forward) {
      movementDirectionMismatches.push({ frame: data.frames[index].f, id: unit[1], from, to, direction: unit[7] });
    }
  }
}
if (movementDirectionMismatches.length) {
  throw new Error(`YINGCHUAN_BACKWARD_WALK_DIRECTION ${JSON.stringify(movementDirectionMismatches.slice(0, 8))}`);
}
console.log(JSON.stringify({
  result: "ok",
  frames: data.frames.length,
  terminal: data.summary?.outcome ?? null,
  movementSamples: movement.slice(0, 32),
  movementDirectionMismatches,
  interpolatedMoveSamples: interpolatedMoves.slice(0, 12),
  cinematicTimings,
}, null, 2));
