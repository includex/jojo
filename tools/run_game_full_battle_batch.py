#!/usr/bin/env python3
"""Run isolated production LibGDX battles and retain auditable trace evidence."""

from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
import gzip
import json
import os
from pathlib import Path
import subprocess
import sys
import tempfile
import shutil
import re
from datetime import datetime, timezone

TOOLS_DIR = Path(__file__).resolve().parent
if str(TOOLS_DIR) not in sys.path:
    sys.path.insert(0, str(TOOLS_DIR))
from audit_late_battle_trace import iter_frames
from authored_observation_contracts import (
    EXACT_CONTRACTS as AUTHORED_EXACT_OBSERVATION_CONTRACTS,
    canonical_observation,
    exact_contract_errors,
)

SCENARIOS = tuple(f"S_{index:02d}" for index in range(58))
FORMAT = "jojo-game-full-battle-batch-manifest/v1"
AUTHORED_OBSERVATION_CONTRACTS = {
    "S_47": {"attackAction": 4},
    "S_52": {"objects": 6},
    "S_57": {"objects": 6, "center": 9, "setUnitStatus": 1},
}
OBSERVATION_PREFIXES = {
    "attackAction": "transition:attackAction:",
    "objects": "transition:objects:",
    "center": "transition:camera:center:",
    "setUnitStatus": "transition:setUnitStatus:",
}

def scenario_id(value: str) -> str:
    value = value.upper()
    if value not in SCENARIOS:
        raise argparse.ArgumentTypeError(f"scenario must be S_00 through S_57: {value}")
    return value


def selected_scenarios(values: list[str] | None) -> list[str]:
    if not values:
        return list(SCENARIOS)
    selected: list[str] = []
    for value in values:
        for token in value.split(","):
            item = scenario_id(token.strip())
            if item not in selected:
                selected.append(item)
    return selected


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--scenario", action="append", help="one ID or a comma-separated list; repeatable")
    parser.add_argument("--list", action="store_true")
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--resume", action="store_true")
    parser.add_argument("--output", type=Path, default=Path("build/game-full-battle-batch/manifest.json"))
    parser.add_argument("--runner", type=Path, default=Path("desktop/build/install/desktop/bin/desktop"))
    parser.add_argument("--runner-arg", action="append", default=[])
    parser.add_argument("--timeout-seconds", type=float, default=900.0)
    parser.add_argument("--max-sim-seconds", type=float, default=1800.0)
    parser.add_argument("--seed", type=int, default=1000)
    parser.add_argument("--math-seed", type=int, default=305419896)
    parser.add_argument("--time-scale", type=float, default=8.0)
    parser.add_argument("--jobs", type=int, default=1, help="number of independent battle processes to run in parallel")
    parser.add_argument("--gzip-traces", action="store_true", help="gzip each completed trace without dropping frames")
    options = parser.parse_args(argv)
    if options.timeout_seconds <= 0 or options.max_sim_seconds <= 0 or options.time_scale <= 0 or options.jobs <= 0:
        parser.error("timeout, max simulation time, time scale, and jobs must be positive")
    try:
        options.scenarios = selected_scenarios(options.scenario)
    except argparse.ArgumentTypeError as error:
        parser.error(str(error))
    options.output = options.output.resolve()
    options.runner = options.runner.resolve()
    return options


def trace_path(manifest_path: Path, scenario: str, compressed: bool = False) -> Path:
    suffix = ".json.gz" if compressed else ".json"
    return manifest_path.parent / "traces" / f"{scenario}{suffix}"


def invocation(options: argparse.Namespace, scenario: str, output: Path) -> list[str]:
    return [
        str(options.runner), *options.runner_arg,
        "--battle", f"--scenario={scenario}", f"--full-battle-trace={output}",
        f"--full-battle-seed={options.seed}", f"--full-battle-math-seed={options.math_seed}",
        f"--full-battle-time-scale={options.time_scale:g}",
        f"--full-battle-max-sim-seconds={options.max_sim_seconds:g}",
    ]


def inspect_trace(path: Path, expected: str) -> dict[str, object]:
    result: dict[str, object] = {"reason": "trace-missing", "terminal": False, "frames": 0, "error": None}
    if not path.is_file():
        result["error"] = f"trace was not created: {path}"
        return result
    try:
        # Loading multi-gigabyte frame arrays just to inspect their terminal
        # metadata caused the batch coordinator itself to exhaust memory.
        # The recorder writes config/reason before frames and summary last, so
        # validate those bounded edges while leaving every frame on disk.
        if (path.suffix == ".gz" or path.stat().st_size > 128 * 1024 * 1024):
            head, tail = trace_edges(path)
            format_match = re.search(r'"format"\s*:\s*"([^"]+)"', head)
            config_match = re.search(r'"config"\s*:\s*(\{.*?\})\s*,\s*"reason"\s*:', head)
            reason_match = re.search(r'"reason"\s*:\s*"([^"]*)"', head)
            summary_matches = list(re.finditer(r',\s*"summary"\s*:\s*', tail))
            if not summary_matches or not tail.rstrip().endswith("}"):
                raise ValueError("streamed trace is missing its terminal summary")
            summary_text = tail[summary_matches[-1].end():].rstrip()
            summary = json.loads(summary_text[:-1])
            config = json.loads(config_match.group(1)) if config_match else None
            trace = {
                "format": format_match.group(1) if format_match else None,
                "config": config,
                "reason": reason_match.group(1) if reason_match else None,
                "summary": summary,
                "frames": None,
            }
        elif path.suffix == ".gz":
            with gzip.open(path, "rt", encoding="utf-8") as handle:
                trace = json.load(handle)
        else:
            trace = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        result["reason"] = "trace-invalid-json"
        result["error"] = f"invalid trace JSON: {error}"
        return result
    frames = trace.get("frames")
    summary = trace.get("summary")
    config = trace.get("config")
    errors: list[str] = []
    if not isinstance(trace.get("format"), str) or not trace["format"].endswith("-full-battle-trace/v1"):
        errors.append("unsupported trace format")
    streamed_frames = frames is None
    if not streamed_frames and (not isinstance(frames, list) or not frames or not all(isinstance(row, dict) for row in frames)):
        errors.append("frames must be a non-empty object array")
        frames = frames if isinstance(frames, list) else []
    frame_count = summary.get("frameCount", 0) if isinstance(summary, dict) and streamed_frames else len(frames or [])
    if not isinstance(summary, dict) or not isinstance(frame_count, int) or frame_count <= 0 or (
        not streamed_frames and summary.get("frameCount") != frame_count
    ):
        errors.append("summary.frameCount does not match frames")
        summary = summary if isinstance(summary, dict) else {}
    if not isinstance(config, dict) or config.get("scenario") != expected or config.get("driver") != "production-input":
        errors.append("config scenario/production-input evidence mismatch")
    evidence = summary.get("gameScenario") if isinstance(summary, dict) else None
    expected_script = f"Game/data/RS/{expected}"
    if not isinstance(evidence, dict):
        errors.append("gameScenario evidence missing")
    else:
        loaded_map = evidence.get("loadedMap")
        calls = evidence.get("loadBgCalls")
        if evidence.get("requestedScenario") != expected or evidence.get("expectedScript") != expected_script or evidence.get("loadedScript") != expected_script:
            errors.append("requested/loaded script evidence mismatch")
        if not isinstance(calls, list) or not calls or not isinstance(loaded_map, dict):
            errors.append("loadBg/map evidence missing")
        elif not any(isinstance(call, dict) and call.get("mapIndex") == loaded_map.get("mapIndex") for call in calls):
            errors.append("loadBg map index mismatch")
        if isinstance(loaded_map, dict) and (
            not loaded_map.get("textureName") or not isinstance(loaded_map.get("width"), int) or loaded_map.get("width", 0) <= 0
            or not isinstance(loaded_map.get("height"), int) or loaded_map.get("height", 0) <= 0
        ):
            errors.append("loaded map texture/dimensions missing")
    reason = trace.get("reason") if isinstance(trace.get("reason"), str) else "trace-schema-invalid"
    terminal = reason == "battle-end" and summary.get("end") is True
    if not terminal:
        errors.append(f"non-natural terminal: {reason}")
    coverage = inspect_authored_observation_contract(path, expected)
    errors.extend(coverage["errors"])
    result.update(
        reason=reason,
        terminal=terminal,
        frames=frame_count,
        authoredCoverage=coverage,
        error="; ".join(errors) or None,
    )
    return result


def inspect_authored_observation_contract(path: Path, scenario: str) -> dict[str, object]:
    required = AUTHORED_OBSERVATION_CONTRACTS.get(scenario, {})
    if not required:
        return {"required": {}, "observed": {}, "initialMapObjects": None, "errors": []}
    observed = {name: 0 for name in required}
    observed_sequences = {name: [] for name in required}
    initial_map_objects: int | None = None
    try:
        for frame in iter_frames(path):
            objects = frame.get("mapObjects")
            if initial_map_objects is None and isinstance(objects, list):
                initial_map_objects = len(objects)
            observation = frame.get("observation")
            if not isinstance(observation, str):
                continue
            for name in observed:
                if observation.startswith(OBSERVATION_PREFIXES[name]):
                    observed[name] += 1
                    # Runtime-resolved IDs are useful evidence but are path
                    # dependent. Exact contracts compare the authored payload
                    # retained before this suffix.
                    observed_sequences[name].append(canonical_observation(observation))
    except (OSError, ValueError, json.JSONDecodeError) as error:
        return {
            "required": required,
            "observed": observed,
            "initialMapObjects": initial_map_objects,
            "errors": [f"authored observation scan failed: {error}"],
        }
    errors = [
        f"authored {name} coverage {observed[name]} < {minimum}"
        for name, minimum in required.items()
        if observed[name] < minimum
    ]
    errors.extend(exact_contract_errors(scenario, observed_sequences))
    if scenario in {"S_52", "S_57"} and (initial_map_objects or 0) < 12:
        errors.append(f"initial map-object snapshot has {initial_map_objects or 0} rows, expected at least 12")
    return {
        "required": required,
        "observed": observed,
        "observedSequences": observed_sequences,
        "initialMapObjects": initial_map_objects,
        "errors": errors,
    }


def trace_edges(path: Path, edge_bytes: int = 512 * 1024) -> tuple[str, str]:
    """Read bounded decoded edges from plain or gzip traces."""
    if path.suffix != ".gz":
        with path.open("rb") as handle:
            head_bytes = handle.read(edge_bytes)
            handle.seek(max(0, path.stat().st_size - edge_bytes))
            tail_bytes = handle.read()
    else:
        head_bytes = b""
        tail_bytes = b""
        with gzip.open(path, "rb") as handle:
            while chunk := handle.read(1024 * 1024):
                if len(head_bytes) < edge_bytes:
                    head_bytes += chunk[:edge_bytes - len(head_bytes)]
                tail_bytes = (tail_bytes + chunk)[-edge_bytes:]
    return head_bytes.decode("utf-8", errors="ignore"), tail_bytes.decode("utf-8", errors="ignore")


def atomic_json(path: Path, value: object) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    descriptor, temporary = tempfile.mkstemp(prefix=f".{path.name}.", dir=path.parent)
    try:
        with os.fdopen(descriptor, "w", encoding="utf-8") as handle:
            json.dump(value, handle, ensure_ascii=False, indent=2)
            handle.write("\n")
        os.replace(temporary, path)
    finally:
        if os.path.exists(temporary):
            os.unlink(temporary)


def prior_results(path: Path, resume: bool) -> dict[str, dict[str, object]]:
    if not resume or not path.is_file():
        return {}
    data = json.loads(path.read_text(encoding="utf-8"))
    if data.get("format") != FORMAT or not isinstance(data.get("results"), list):
        raise ValueError(f"not a compatible batch manifest: {path}")
    return {row["scenario"]: row for row in data["results"] if isinstance(row, dict) and row.get("scenario") in SCENARIOS}


def run_scenario(options: argparse.Namespace, scenario: str) -> dict[str, object]:
    """Run one isolated desktop process; the caller serializes manifest writes."""
    output = trace_path(options.output, scenario, options.gzip_traces)
    output.parent.mkdir(parents=True, exist_ok=True)
    output.unlink(missing_ok=True)
    try:
        process = subprocess.run(
            invocation(options, scenario, output), cwd=Path.cwd(), capture_output=True, text=True,
            timeout=options.timeout_seconds, check=False,
        )
        exit_code, timed_out, process_error = process.returncode, False, None
        if exit_code:
            process_error = (process.stderr or process.stdout)[-2000:].strip() or f"runner exited {exit_code}"
    except subprocess.TimeoutExpired:
        exit_code, timed_out, process_error = 124, True, f"runner timed out after {options.timeout_seconds:g}s"
    checked = inspect_trace(output, scenario)
    errors = [str(item) for item in (process_error, checked["error"]) if item]
    row: dict[str, object] = {
        "scenario": scenario,
        "exit": exit_code,
        "reason": "process-timeout" if timed_out else checked["reason"],
        "terminal": checked["terminal"],
        "frames": checked["frames"],
        "error": "; ".join(errors) or None,
        "trace": str(output.relative_to(options.output.parent)),
        "authoredCoverage": checked.get("authoredCoverage"),
        "passed": exit_code == 0 and not timed_out and not errors and checked["terminal"] is True,
    }
    if options.gzip_traces and output.is_file() and output.suffix != ".gz":
        compressed = output.with_suffix(output.suffix + ".gz")
        with output.open("rb") as source, gzip.open(compressed, "wb", compresslevel=6) as target:
            shutil.copyfileobj(source, target, length=1024 * 1024)
        output.unlink()
        row["trace"] = str(compressed.relative_to(options.output.parent))
    return row


def main(argv: list[str] | None = None) -> int:
    options = parse_args(sys.argv[1:] if argv is None else argv)
    if options.list:
        print("\n".join(options.scenarios))
        return 0
    if options.dry_run:
        for scenario in options.scenarios:
            print(subprocess.list2cmdline(invocation(options, scenario, trace_path(options.output, scenario, options.gzip_traces))))
        return 0
    if not options.runner.is_file():
        print(f"runner is missing: {options.runner}; run ./gradlew :desktop:installDist", file=sys.stderr)
        return 2
    previous = prior_results(options.output, options.resume)
    created = datetime.now(timezone.utc).isoformat()
    manifest: dict[str, object] = {
        "format": FORMAT, "createdAt": created, "updatedAt": created,
        "config": {"scenarios": options.scenarios, "runner": str(options.runner), "timeScale": options.time_scale,
                   "maxSimulationSeconds": options.max_sim_seconds, "timeoutSeconds": options.timeout_seconds,
                   "seed": options.seed, "mathSeed": options.math_seed, "jobs": options.jobs,
                   "gzipTraces": options.gzip_traces},
        "results": [],
    }
    pending: list[str] = []
    for scenario in options.scenarios:
        prior = previous.get(scenario)
        prior_relative = prior.get("trace") if isinstance(prior, dict) else None
        prior_path = options.output.parent / prior_relative if isinstance(prior_relative, str) else trace_path(options.output, scenario, options.gzip_traces)
        if options.resume and prior and prior.get("passed") is True and prior_path.is_file() and inspect_trace(prior_path, scenario)["error"] is None:
            print(f"[game-full-battle] {scenario}: resumed")
            continue
        pending.append(scenario)
    for scenario in pending:
        print(f"[game-full-battle] {scenario}: running", flush=True)
    with ThreadPoolExecutor(max_workers=options.jobs) as executor:
        futures = {executor.submit(run_scenario, options, scenario): scenario for scenario in pending}
        for future in as_completed(futures):
            scenario = futures[future]
            try:
                row = future.result()
            except Exception as error:  # retain a manifest row even for an unexpected harness failure
                row = {"scenario": scenario, "exit": 125, "reason": "harness-error", "terminal": False,
                       "frames": 0, "error": f"{type(error).__name__}: {error}",
                       "trace": str(trace_path(options.output, scenario, options.gzip_traces).relative_to(options.output.parent)), "passed": False}
            previous[scenario] = row
            manifest["results"] = [previous[key] for key in sorted(previous)]
            manifest["updatedAt"] = datetime.now(timezone.utc).isoformat()
            atomic_json(options.output, manifest)
            print(f"[game-full-battle] {scenario}: {'passed' if row['passed'] else 'failed'} reason={row['reason']} frames={row['frames']}", flush=True)
    manifest["results"] = [previous[key] for key in sorted(previous)]
    atomic_json(options.output, manifest)
    selected = [previous[item] for item in options.scenarios if item in previous]
    failures = [row for row in selected if not row["passed"]]
    print(f"[game-full-battle] complete: {len(selected) - len(failures)}/{len(selected)} passed; manifest={options.output}")
    return 1 if failures else 0


if __name__ == "__main__":
    raise SystemExit(main())
