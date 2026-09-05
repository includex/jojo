#!/usr/bin/env node
import { readFileSync } from "node:fs";
import { spawnSync } from "node:child_process";
import { resolve, dirname } from "node:path";
import { fileURLToPath } from "node:url";
const [fixturePath, sourcePath, gamePath] = process.argv.slice(2);
if (!fixturePath || !sourcePath || !gamePath) throw new Error("usage: verifier fixture.json source-trace.json game-trace.json");
const root = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const sourceHarness = resolve(root, "tools/win_conditions_source_trace_harness.js");
const run = spawnSync("node", [sourceHarness, fixturePath, sourcePath], { encoding: "utf8" });
if (run.status !== 0) throw new Error(`original trace harness failed: ${run.stderr || run.stdout}`);
const source = JSON.parse(readFileSync(sourcePath, "utf8"));
const game = JSON.parse(readFileSync(gamePath, "utf8"));
if (JSON.stringify(source) !== JSON.stringify(game)) {
  throw new Error(`WinConditionsLayer source/game trace mismatch\nSOURCE=${JSON.stringify(source)}\nPORT=${JSON.stringify(game)}`);
}
console.log(`WIN_CONDITIONS_PAIRWISE_OK cases=${source.length} steps=${source.reduce((n, item) => n + item.steps.length, 0)}`);
