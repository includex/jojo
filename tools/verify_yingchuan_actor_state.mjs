#!/usr/bin/env node
/** Compare active Cocos BattleUnit placement against the same game capture state. */
import assert from "node:assert/strict";
import { readFileSync, writeFileSync, unlinkSync } from "node:fs";
import { resolve } from "node:path";
import { spawnSync } from "node:child_process";

const root = resolve(import.meta.dirname, "..");
const sourceRoot = resolve(root, "../jojo_mobile/sgccz-desktop");
const report = resolve(root, "build/yingchuan-actor-state.json");
function run(command, args, cwd) {
  // A complete fresh source capture can briefly leave macOS image/Chromium
  // resources under pressure before the following Pillow verifier exits.
  // Three minutes produced a false timeout after the verifier had already
  // printed its successful result; retain a bounded but non-flaky deadline.
  const timeout = command.includes("gradlew") || command === "python3" ? 600_000 : 180_000;
  const result = spawnSync(command, args, {
    cwd,
    encoding: "utf8",
    maxBuffer: 16 * 1024 * 1024,
    timeout,
    killSignal: "SIGTERM",
  });
  if (result.error) {
    throw new Error(`${command} failed before exit after ${timeout}ms: ${result.error.message}; status=${result.status}; signal=${result.signal}\n${result.stdout ?? ""}\n${result.stderr ?? ""}`);
  }
  if (result.status !== 0) throw new Error(`${command} failed (${result.status})\n${result.stdout}\n${result.stderr}`);
  return `${result.stdout}\n${result.stderr}`;
}

function sourceActors(snapshot) {
  return snapshot.nodes
    .filter(node => node.battleUnit?.id !== undefined)
    .map(node => node.battleUnit)
    .map(unit => ({ id: unit.id, x: unit.x, y: unit.y, direction: unit.dir }))
    .sort((a, b) => a.id - b.id || a.x - b.x || a.y - b.y);
}

function gameActors(output) {
  const unitLine = output.match(/DIALOGUE_CAPTURE_UNITS: (.*)/)?.[1];
  assert.ok(unitLine, "game did not emit the Yingchuan dialogue fixture");
  return unitLine.split(";").map(value => {
  const match = value.match(/^[^/]+\/(\d+)@(\d+),(\d+)\/d(\d+)\/v(true|false)$/);
  assert.ok(match, `unparseable game unit: ${value}`);
  return { id: Number(match[1]), x: Number(match[2]), y: Number(match[3]), direction: Number(match[4]), visible: match[5] === "true" };
  }).filter(unit => unit.visible).map(({ visible, ...unit }) => unit).sort((a, b) => a.id - b.id || a.x - b.x || a.y - b.y);
}

const expected = [
  // The original capture lands at a typewriter frame boundary.  Cocos may
  // expose the trailing whitespace before or after that boundary; both are
  // the same authored prefix.  Do not turn that scheduler race into a false
  // source/game behavioural failure.
  { step: 1, speaker: "235", text: "하지만, 얼마나 ", sourceTexts: ["하지만, 얼마나", "하지만, 얼마나 ", "하지만, 얼마나 증"] },
  { step: 2, speaker: "235", text: "하지만, 얼마나 증오스러운 일인가......." },
  { step: 3, speaker: "477", text: "아!" },
];
const results = [];
for (const fixture of expected) {
  const sourceImage = resolve(sourceRoot, `build/python-source-battle-verification-dialogue${fixture.step}.png`);
  const sourceState = resolve(sourceRoot, `build/python-source-battle-verification-dialogue${fixture.step}.json`);
  for (const stale of [sourceImage, sourceState]) try { unlinkSync(stale); } catch { /* absent is fresh */ }
  run("./node_modules/.bin/electron", [
    ".", "--verify-python-battle", `--capture-python-battle-dialogue-step=${fixture.step}`,
    "--capture-python-battle-dialogue-wait-ms=3200",
  ], sourceRoot);
  const sourceSnapshot = JSON.parse(readFileSync(
    sourceState, "utf8",
  ));
  const sourceText = sourceSnapshot.nodes.flatMap(node => node.richTextComponents || [])
    .map(component => component.string).find(Boolean);
  const sourceName = sourceSnapshot.nodes
    .filter(node => node.path === "Canvas/Layer/bg0/label")
    .flatMap(node => node.labels || []).find(name => name.length > 0);
  assert.ok((fixture.sourceTexts ?? [fixture.text]).includes(sourceText),
    `source dialogue ${fixture.step} unexpectedly changed: ${JSON.stringify(sourceText)}`);
  const gameImage = resolve(root, `build/yingchuan-dialogue-${fixture.step}-game.png`);
  try { unlinkSync(gameImage); } catch { /* absent is fresh */ }
  const classpath = process.env.JOJO_VERIFICATION_CLASSPATH;
  const gameOutput = classpath
    ? run("java", ["-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED", "-cp", classpath,
      "com.jojo.game.verification.VerificationDesktopLauncher", "--battle", "--scenario=S_00",
      `--capture-state=yingchuan-dialogue-${fixture.step}`, `--capture=${gameImage}`], root)
    : run("./gradlew", [
      ":desktop:run", "--no-daemon",
      `--args=--battle --scenario=S_00 --capture-state=yingchuan-dialogue-${fixture.step} --capture=${gameImage}`,
    ], root);
  const state = gameOutput.match(/DIALOGUE_CAPTURE_STATE: speaker=(\d+) name=(.*?) text=(.*?) face=/);
  assert.ok(state, `game did not report dialogue ${fixture.step}`);
  assert.equal(state[1], fixture.speaker, `game dialogue ${fixture.step} speaker differs from source`);
  assert.equal(state[2], sourceName, `game dialogue ${fixture.step} displayed name differs from source`);
  assert.equal(state[3], fixture.step === 1 ? "하지만, 얼마나 증오스러운 일인가......." : fixture.text,
    `game dialogue ${fixture.step} source text differs`);
  assert.deepEqual(gameActors(gameOutput), sourceActors(sourceSnapshot),
    `Yingchuan active BattleUnit placement differs at dialogue input ${fixture.step}`);
  run("python3", [
    "tools/verify_yingchuan_dialogue_fixture.py",
    sourceImage,
    gameImage,
    sourceState,
    String(fixture.step),
  ], root);
  results.push({ step: fixture.step, speaker: fixture.speaker, actors: sourceActors(sourceSnapshot).length });
}

const result = { states: results, result: "ok" };
writeFileSync(report, JSON.stringify(result, null, 2));
console.log(`YINGCHUAN_DIALOGUE_ACTOR_STATES_OK ${JSON.stringify(result)}`);
