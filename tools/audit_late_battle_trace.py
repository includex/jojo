#!/usr/bin/env python3
"""Audit late-battle trace coverage without loading multi-GB frame arrays.

This is an observability gate, not a gameplay oracle.  It proves that authored
door/trap, center, attackAction/HP and FightLayer paths were visible in a trace
and reports unexercised paths separately from missing instrumentation.
"""

from __future__ import annotations

import argparse
import gzip
import json
from pathlib import Path
import re
from typing import Any, Iterator, TextIO

from authored_observation_contracts import canonical_observation, exact_contract_errors


def _open_text(path: Path) -> TextIO:
    return gzip.open(path, "rt", encoding="utf-8") if path.suffix == ".gz" else path.open(encoding="utf-8")


def iter_frames(path: Path, chunk_size: int = 256 * 1024) -> Iterator[dict[str, Any]]:
    """Yield objects from the top-level frames array using bounded memory."""
    decoder = json.JSONDecoder()
    marker = re.compile(r'"frames"\s*:\s*\[')
    with _open_text(path) as handle:
        buffer = ""
        match = marker.search(buffer)
        while match is None:
            chunk = handle.read(chunk_size)
            if not chunk:
                raise ValueError("trace has no frames array")
            buffer = (buffer + chunk)[-max(64, chunk_size * 2):]
            match = marker.search(buffer)
        buffer = buffer[match.end():]
        cursor = 0
        eof = False
        while True:
            while True:
                while cursor < len(buffer) and buffer[cursor] in " \t\r\n,":
                    cursor += 1
                if cursor < len(buffer) or eof:
                    break
                buffer, cursor = "", 0
                chunk = handle.read(chunk_size)
                eof = not chunk
                buffer += chunk
            if cursor < len(buffer) and buffer[cursor] == "]":
                return
            try:
                value, end = decoder.raw_decode(buffer, cursor)
            except json.JSONDecodeError:
                if eof:
                    raise ValueError("trace ended inside a frame")
                buffer = buffer[cursor:] + handle.read(chunk_size)
                cursor = 0
                continue
            if not isinstance(value, dict):
                raise ValueError("frame entry is not an object")
            yield value
            cursor = end
            if cursor > chunk_size:
                buffer, cursor = buffer[cursor:], 0


def unit_map(frame: dict[str, Any]) -> dict[int, list[Any]]:
    result: dict[int, list[Any]] = {}
    for row in frame.get("units") or []:
        if isinstance(row, list) and len(row) > 17 and isinstance(row[1], int):
            result[row[1]] = row
    return result


def authored_features(script_path: Path | None) -> dict[str, bool]:
    text = script_path.read_text(encoding="utf-8") if script_path and script_path.is_file() else ""
    return {
        "objects": "stage.setObjects(" in text,
        "center": "stage.center(" in text,
        "attackAction": "stage.attackAction(" in text,
        "fight": "stage.startFight(" in text,
        "status": "stage.setUnitStatus(" in text,
    }


def audit_trace(path: Path, scenario: str | None = None, script_path: Path | None = None) -> dict[str, Any]:
    scenario = scenario or path.name.split(".", 1)[0]
    authored = authored_features(script_path)
    fields_seen = {name: False for name in ("camera", "mapObjectRevision", "mapObjects", "fight")}
    observations: list[str] = []
    map_revisions: set[int] = set()
    map_snapshots = 0
    distinct_cameras: set[tuple[float, float]] = set()
    status_transitions = 0
    previous_status: dict[int, tuple[Any, ...]] = {}
    fight_frames = 0
    fight_actions: set[int] = set()
    attacks: list[dict[str, Any]] = []
    frame_count = 0

    for frame in iter_frames(path):
        frame_count += 1
        for field in fields_seen:
            fields_seen[field] |= field in frame
        camera = frame.get("camera")
        if isinstance(camera, list) and len(camera) == 2 and all(isinstance(v, (int, float)) for v in camera):
            distinct_cameras.add((float(camera[0]), float(camera[1])))
        revision = frame.get("mapObjectRevision")
        if isinstance(revision, int):
            map_revisions.add(revision)
        if isinstance(frame.get("mapObjects"), list):
            map_snapshots += 1
        fight = frame.get("fight")
        if isinstance(fight, dict):
            fight_frames += 1
            for fighter in fight.get("units") or []:
                if isinstance(fighter, list) and len(fighter) > 2 and isinstance(fighter[2], int):
                    fight_actions.add(fighter[2])

        units = unit_map(frame)
        for unit_id, row in units.items():
            details = row[17] if isinstance(row[17], dict) else {}
            statuses = tuple(details.get("statuses") or [])
            if unit_id in previous_status and statuses and statuses != previous_status[unit_id]:
                status_transitions += 1
            if statuses:
                previous_status[unit_id] = statuses

        observation = frame.get("observation")
        if isinstance(observation, str):
            observations.append(observation)
            match = re.fullmatch(r"transition:attackAction:(-?\d+):(-?\d+):(\d+)", observation)
            if match:
                attacker, target, flags = map(int, match.groups())
                target_row = units.get(target)
                attacks.append({
                    "attacker": attacker,
                    "target": target,
                    "flags": flags,
                    "targetHpBefore": target_row[5] if target_row else None,
                    "attackerAnimation": False,
                    "targetReaction": False,
                    "targetHpReduced": False,
                })

        for attack in attacks:
            attacker_row = units.get(attack["attacker"])
            target_row = units.get(attack["target"])
            if attacker_row and len(attacker_row) > 14 and isinstance(attacker_row[14], str):
                attack["attackerAnimation"] |= attacker_row[14].startswith(("anime21_", "anime25_"))
            if target_row and len(target_row) > 14 and isinstance(target_row[14], str):
                attack["targetReaction"] |= target_row[14].startswith(("anime26_", "anime32_"))
            before = attack["targetHpBefore"]
            if target_row and isinstance(before, (int, float)) and isinstance(target_row[5], (int, float)):
                attack["targetHpReduced"] |= target_row[5] < before

    prefixes = {
        "objects": "transition:objects:",
        "center": "transition:camera:center:",
        "attackAction": "transition:attackAction:",
        "fight": "transition:fight:",
        "status": "transition:setUnitStatus:",
    }
    exercised = {name: sum(value.startswith(prefix) for value in observations) for name, prefix in prefixes.items()}
    exact_sequences = {
        "objects": [canonical_observation(value) for value in observations if value.startswith(prefixes["objects"])],
        "center": [canonical_observation(value) for value in observations if value.startswith(prefixes["center"])],
        "setUnitStatus": [canonical_observation(value) for value in observations if value.startswith(prefixes["status"])],
    }
    capability_errors = [name for name, seen in fields_seen.items() if not seen]
    evidence_errors: list[str] = []
    if authored["objects"] and exercised["objects"] and map_snapshots < 2:
        evidence_errors.append("object call was exercised without before/after map snapshots")
    if authored["center"] and exercised["center"] and len(distinct_cameras) < 2:
        evidence_errors.append("center call was exercised without an observable camera transition")
    incomplete_attacks = [item for item in attacks if not all(
        item[key] for key in ("attackerAnimation", "targetReaction", "targetHpReduced")
    )]
    if incomplete_attacks:
        evidence_errors.append(f"{len(incomplete_attacks)} attackAction chains lack attack/reaction/HP evidence")
    if authored["fight"] and exercised["fight"] and (fight_frames == 0 or not fight_actions):
        evidence_errors.append("FightLayer commands were exercised without live fighter/action state")
    evidence_errors.extend(exact_contract_errors(scenario, exact_sequences))
    unexercised = [name for name in prefixes if authored[name] and exercised[name] == 0]

    return {
        "format": "jojo-late-battle-trace-audit/v1",
        "scenario": scenario,
        "trace": str(path),
        "frames": frame_count,
        "authored": authored,
        "fieldsSeen": fields_seen,
        "exercised": exercised,
        "authoredObservationSequences": exact_sequences,
        "mapObjectRevisions": len(map_revisions),
        "mapObjectSnapshots": map_snapshots,
        "distinctCameraPositions": len(distinct_cameras),
        "statusTransitions": status_transitions,
        "fightFrames": fight_frames,
        "fightActions": sorted(fight_actions),
        "attackChains": attacks,
        "unexercisedAuthoredPaths": sorted(set(unexercised)),
        "capabilityErrors": capability_errors,
        "evidenceErrors": evidence_errors,
        "passed": not capability_errors and not evidence_errors and not unexercised,
    }


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("trace", type=Path)
    parser.add_argument("--scenario")
    parser.add_argument("--script", type=Path)
    parser.add_argument("--output", type=Path)
    parser.add_argument("--allow-unexercised", action="store_true")
    args = parser.parse_args(argv)
    report = audit_trace(args.trace, args.scenario, args.script)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    print(encoded)
    return 0 if report["passed"] or (args.allow_unexercised and not report["capabilityErrors"] and not report["evidenceErrors"]) else 1


if __name__ == "__main__":
    raise SystemExit(main())
