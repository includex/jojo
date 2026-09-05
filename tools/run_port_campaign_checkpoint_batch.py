#!/usr/bin/env python3
"""Run R_00..R_58 campaign checkpoints through production screens and input.

Each checkpoint starts from TitleScreen in an isolated desktop process.  The
runner deliberately does not use --verify, --scenario, --battle, scripted
choices, direct AST fixtures, or the standalone full-battle recorder.
CampaignE2eDriver remains the sole input driver.
"""

from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime, timezone
import importlib.util
import json
import os
from pathlib import Path
import subprocess
import sys
import tempfile
from typing import Any


CHECKPOINTS = tuple(f"R_{index:02d}" for index in range(59))
FORMAT = "jojo-port-campaign-checkpoint-batch-manifest/v1"
PROHIBITED_ARGUMENT_PREFIXES = (
    "--verify",
    "--scenario=",
    "--battle",
    "--capture",
    "--choice-trace=",
    "--random-trace=",
    # These values are owned by this harness. DesktopLauncher resolves the
    # first matching argument, so allowing an earlier runner-arg would let a
    # caller redirect the trace or replace the requested stop point.
    "--campaign-e2e-",
    # Full-battle tracing changes battle RNG/delta and writes one recorder per
    # BattleLayer. It is deliberately separate from campaign route evidence.
    "--full-battle-",
    "--yingchuan-entry-flow-trace=",
)


def _load_verifier():
    path = Path(__file__).with_name("verify_campaign_route_e2e.py")
    spec = importlib.util.spec_from_file_location("jojo_campaign_route_verifier", path)
    module = importlib.util.module_from_spec(spec)
    assert spec.loader is not None
    spec.loader.exec_module(module)
    return module


VERIFIER = _load_verifier()


def checkpoint_id(value: str) -> str:
    value = value.upper()
    if value not in CHECKPOINTS:
        raise argparse.ArgumentTypeError(f"checkpoint must be R_00 through R_58: {value}")
    return value


def selected_checkpoints(values: list[str] | None) -> list[str]:
    if not values:
        return list(CHECKPOINTS)
    selected: list[str] = []
    for value in values:
        for token in value.split(","):
            item = checkpoint_id(token.strip())
            if item not in selected:
                selected.append(item)
    return selected


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--checkpoint", action="append", help="one R_NN or comma-separated list; repeatable")
    parser.add_argument("--scene-index", type=int, default=1)
    parser.add_argument("--list", action="store_true")
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--resume", action="store_true")
    parser.add_argument("--output", type=Path, default=Path("build/port-campaign-checkpoints/manifest.json"))
    parser.add_argument("--runner", type=Path, default=Path("desktop/build/install/desktop/bin/desktop"))
    parser.add_argument("--runner-arg", action="append", default=[])
    parser.add_argument("--timeout-seconds", type=float, default=7200.0)
    parser.add_argument("--jobs", type=int, default=1)
    options = parser.parse_args(argv)
    if options.scene_index < 0:
        parser.error("scene index must be non-negative")
    if options.timeout_seconds <= 0 or options.jobs <= 0:
        parser.error("timeout and jobs must be positive")
    try:
        options.checkpoints = selected_checkpoints(options.checkpoint)
    except argparse.ArgumentTypeError as error:
        parser.error(str(error))
    forbidden = [arg for arg in options.runner_arg if arg.startswith(PROHIBITED_ARGUMENT_PREFIXES)]
    if forbidden:
        parser.error(f"runner arguments bypass production campaign routing: {', '.join(forbidden)}")
    options.output = options.output.resolve()
    options.runner = options.runner.resolve()
    return options


def trace_path(manifest_path: Path, checkpoint: str) -> Path:
    return manifest_path.parent / "traces" / f"{checkpoint}.json"


def invocation(options: argparse.Namespace, checkpoint: str, trace: Path) -> list[str]:
    command = [
        str(options.runner),
        *options.runner_arg,
        f"--campaign-e2e-trace={trace}",
        f"--campaign-e2e-stop={checkpoint}:{options.scene_index}",
    ]
    assert not any(
        argument.startswith(PROHIBITED_ARGUMENT_PREFIXES)
        for argument in options.runner_arg
    ), command
    return command


def inspect_trace(path: Path, checkpoint: str, scene_index: int) -> dict[str, Any]:
    result: dict[str, Any] = {
        "verified": False,
        "checkpointStatus": "trace-missing",
        "actualStopPoint": None,
        "numberedCoverage": 0,
        "transitions": [],
        "forwardJumps": [],
        "error": None,
    }
    if not path.is_file():
        result["error"] = f"trace was not created: {path}"
        return result
    try:
        trace = json.loads(path.read_text(encoding="utf-8"))
        coverage = VERIFIER.validate(trace, checkpoint, scene_index)
    except (OSError, json.JSONDecodeError, AssertionError, KeyError, TypeError, ValueError) as error:
        result["checkpointStatus"] = "trace-invalid"
        result["error"] = f"campaign route verification failed: {error}"
        return result
    result.update(
        verified=True,
        checkpointStatus=coverage["checkpointStatus"],
        actualStopPoint=coverage["actualStopPoint"],
        numberedCoverage=coverage["numberedCoverage"],
        scenarioModules=coverage["scenarioModules"],
        battleModules=coverage["battleModules"],
        transitions=coverage["transitions"],
        forwardJumps=coverage["forwardJumps"],
    )
    return result


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


def prior_results(
    path: Path,
    resume: bool,
    selected: set[str] | None = None,
) -> dict[str, dict[str, Any]]:
    if not resume or not path.is_file():
        return {}
    data = json.loads(path.read_text(encoding="utf-8"))
    if data.get("format") != FORMAT or not isinstance(data.get("results"), list):
        raise ValueError(f"not a compatible checkpoint manifest: {path}")
    return {
        row["checkpoint"]: row
        for row in data["results"]
        if isinstance(row, dict)
        and row.get("checkpoint") in CHECKPOINTS
        and (selected is None or row.get("checkpoint") in selected)
    }


def run_checkpoint(options: argparse.Namespace, checkpoint: str) -> dict[str, Any]:
    trace = trace_path(options.output, checkpoint)
    trace.parent.mkdir(parents=True, exist_ok=True)
    trace.unlink(missing_ok=True)
    command = invocation(options, checkpoint, trace)
    try:
        process = subprocess.run(
            command,
            cwd=Path.cwd(),
            capture_output=True,
            text=True,
            timeout=options.timeout_seconds,
            check=False,
        )
        exit_code = process.returncode
        timed_out = False
        process_error = None
        if exit_code:
            process_error = (process.stderr or process.stdout)[-4000:].strip() or f"runner exited {exit_code}"
    except subprocess.TimeoutExpired as error:
        exit_code = 124
        timed_out = True
        text = ((error.stderr or "") if isinstance(error.stderr, str) else "") or (
            (error.stdout or "") if isinstance(error.stdout, str) else ""
        )
        process_error = f"runner timed out after {options.timeout_seconds:g}s"
        if text:
            process_error += f"; tail={text[-2000:].strip()}"

    checked = inspect_trace(trace, checkpoint, options.scene_index)
    errors = [str(value) for value in (process_error, checked["error"]) if value]
    reached = checked["checkpointStatus"] == "reached"
    classified_overshoot = checked["checkpointStatus"] == "authored-forward-jump-overshoot"
    reason = "process-timeout" if timed_out else checked["checkpointStatus"]
    return {
        "checkpoint": checkpoint,
        "requestedStopPoint": {"module": checkpoint, "sceneIndex": options.scene_index},
        "actualStopPoint": checked["actualStopPoint"],
        "exit": exit_code,
        "reason": reason,
        "verified": checked["verified"],
        "reached": reached,
        "classifiedOvershoot": classified_overshoot,
        "numberedCoverage": checked["numberedCoverage"],
        "scenarioModules": checked.get("scenarioModules", []),
        "battleModules": checked.get("battleModules", []),
        "transitions": checked["transitions"],
        "forwardJumps": checked["forwardJumps"],
        "error": "; ".join(errors) or None,
        "trace": str(trace.relative_to(options.output.parent)),
        # An authorized overshoot is useful branch evidence, but it must not
        # masquerade as evidence that the requested checkpoint was reached.
        "passed": exit_code == 0 and not errors and reached,
    }


def aggregate_manifest(manifest: dict[str, Any], results: dict[str, dict[str, Any]]) -> None:
    ordered = [results[key] for key in sorted(results)]
    jumps: dict[tuple[str, str, str], dict[str, Any]] = {}
    for row in ordered:
        for jump in row.get("forwardJumps", []):
            key = (str(jump.get("from")), str(jump.get("to")), str(jump.get("kind")))
            jumps[key] = jump
    manifest["results"] = ordered
    manifest["observedForwardJumps"] = [jumps[key] for key in sorted(jumps)]
    manifest["summary"] = {
        "requested": len(ordered),
        "reached": sum(row.get("reached") is True for row in ordered),
        "authoredForwardJumpOvershoots": sum(row.get("classifiedOvershoot") is True for row in ordered),
        "failed": sum(row.get("passed") is not True for row in ordered),
    }


def main(argv: list[str] | None = None) -> int:
    options = parse_args(sys.argv[1:] if argv is None else argv)
    if options.list:
        print("\n".join(options.checkpoints))
        return 0
    if options.dry_run:
        for checkpoint in options.checkpoints:
            print(subprocess.list2cmdline(invocation(
                options,
                checkpoint,
                trace_path(options.output, checkpoint),
            )))
        return 0
    if not options.runner.is_file():
        print(f"runner is missing: {options.runner}; run ./gradlew :desktop:installDist", file=sys.stderr)
        return 2

    previous = prior_results(options.output, options.resume, set(options.checkpoints))
    created = datetime.now(timezone.utc).isoformat()
    manifest: dict[str, Any] = {
        "format": FORMAT,
        "createdAt": created,
        "updatedAt": created,
        "executionContract": {
            "start": "TitleScreen:new-game-click",
            "screenDriver": "CampaignE2eDriver",
            "inputTransport": "installed-InputProcessor",
            "branchPolicy": "production UI; game-start option when present, otherwise first option",
            "directAstAdvance": False,
            "skipDelay": False,
            "fullBattleRecorder": False,
            "battleTimeScale": 1,
        },
        "config": {
            "checkpoints": options.checkpoints,
            "sceneIndex": options.scene_index,
            "runner": str(options.runner),
            "timeoutSeconds": options.timeout_seconds,
            "jobs": options.jobs,
        },
        "results": [],
        "observedForwardJumps": [],
    }

    pending: list[str] = []
    for checkpoint in options.checkpoints:
        prior = previous.get(checkpoint)
        prior_trace = options.output.parent / prior["trace"] if prior and isinstance(prior.get("trace"), str) else None
        if (
            options.resume
            and prior
            and prior.get("passed") is True
            and prior_trace is not None
            and inspect_trace(prior_trace, checkpoint, options.scene_index)["checkpointStatus"] == "reached"
        ):
            print(f"[port-campaign] {checkpoint}: resumed")
        else:
            pending.append(checkpoint)
            print(f"[port-campaign] {checkpoint}: running", flush=True)

    with ThreadPoolExecutor(max_workers=options.jobs) as executor:
        futures = {executor.submit(run_checkpoint, options, checkpoint): checkpoint for checkpoint in pending}
        for future in as_completed(futures):
            checkpoint = futures[future]
            try:
                row = future.result()
            except Exception as error:  # retain evidence of harness failures
                row = {
                    "checkpoint": checkpoint,
                    "requestedStopPoint": {"module": checkpoint, "sceneIndex": options.scene_index},
                    "actualStopPoint": None,
                    "exit": 125,
                    "reason": "harness-error",
                    "verified": False,
                    "reached": False,
                    "classifiedOvershoot": False,
                    "numberedCoverage": 0,
                    "scenarioModules": [],
                    "battleModules": [],
                    "transitions": [],
                    "forwardJumps": [],
                    "error": f"{type(error).__name__}: {error}",
                    "trace": str(trace_path(options.output, checkpoint).relative_to(options.output.parent)),
                    "passed": False,
                }
            previous[checkpoint] = row
            aggregate_manifest(manifest, previous)
            manifest["updatedAt"] = datetime.now(timezone.utc).isoformat()
            atomic_json(options.output, manifest)
            print(
                f"[port-campaign] {checkpoint}: "
                f"{'passed' if row['passed'] else row['reason']} "
                f"actual={row['actualStopPoint']} coverage={row['numberedCoverage']}",
                flush=True,
            )

    aggregate_manifest(manifest, previous)
    manifest["updatedAt"] = datetime.now(timezone.utc).isoformat()
    atomic_json(options.output, manifest)
    selected = [previous[checkpoint] for checkpoint in options.checkpoints if checkpoint in previous]
    failures = [row for row in selected if not row["passed"]]
    overshoots = [row for row in selected if row.get("classifiedOvershoot")]
    print(
        f"[port-campaign] complete: {len(selected) - len(failures)}/{len(selected)} reached; "
        f"overshoots={len(overshoots)}; manifest={options.output}"
    )
    return 1 if failures else 0


if __name__ == "__main__":
    raise SystemExit(main())
