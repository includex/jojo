#!/usr/bin/env python3
"""Strict, log-only parity gate for the S_22 battle trace.

This intentionally does not use screenshots, timestamps, or frame counts.
It compares the final state of every (round, camp) interval, requires growth
evidence rather than silently skipping it, and only compares the round-6
217-to-5 presentation/callback sequence when the trace records an explicit
targeted callback.  A missing observation is a failed gate, never a pass.
"""
from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path
from typing import Any, Iterable

from compare_battle_camp_boundaries import GROWTH_FIELDS, camp_boundaries, compare

FORMAT = "jojo-yingchuan-full-battle-trace/v1"
SOURCE_DEFAULT = Path("/Users/ain/workspace/jojo_mobile/sgccz-desktop/build/diagnostic-s22-harm-r8/traces/S_22.json")
PORT_DEFAULT = Path("build/port-full-battle-batch-v30/traces/S_22.json")
FOCAL_ROUND, FOCAL_CAMP, FOCAL_ACTOR, FOCAL_TARGET, FOCAL_ACTION = 6, 2, 217, 5, 25
_ANIME = re.compile(r"(?:anime|action)[_-]?(\d+)", re.IGNORECASE)


def _unit(frame: dict[str, Any], character: int) -> list[Any] | None:
    for row in frame.get("units", []):
        if isinstance(row, list) and len(row) >= 18 and row[1] == character:
            return row
    return None


def _presentation_action(row: list[Any]) -> int | None:
    playing = row[14]
    if not isinstance(playing, str):
        return None
    match = _ANIME.search(playing)
    return int(match.group(1)) if match else None


def _schema_errors(trace: Any, label: str) -> list[str]:
    if not isinstance(trace, dict):
        return [f"{label}: root is not an object"]
    errors: list[str] = []
    if trace.get("format") != FORMAT:
        errors.append(f"{label}: unexpected format {trace.get('format')!r}")
    frames = trace.get("frames")
    if not isinstance(frames, list) or not frames:
        return errors + [f"{label}: frames must be a non-empty array"]
    for index, frame in enumerate(frames):
        if not isinstance(frame, dict):
            errors.append(f"{label}: frame[{index}] is not an object")
            continue
        for field in ("round", "camp", "end", "units"):
            if field not in frame:
                errors.append(f"{label}: frame[{index}] lacks {field}")
        if not isinstance(frame.get("units"), list):
            errors.append(f"{label}: frame[{index}].units is not an array")
    return errors


def _boundary_growth_gaps(path: Path, *, limit: int = 8) -> dict[str, Any]:
    """Return missing growth components at each camp-boundary sample."""
    gaps: list[dict[str, Any]] = []
    missing_count = 0
    checked_units = 0
    for boundary in camp_boundaries(path):
        for unit in boundary["units"]:
            checked_units += 1
            growth = unit.get("growth")
            missing = [field for field in GROWTH_FIELDS
                       if not isinstance(growth, dict) or growth.get(field) is None]
            if missing:
                missing_count += 1
                if len(gaps) < limit:
                    gaps.append({"round": boundary["round"], "camp": boundary["camp"],
                                 "id": unit["id"], "missing": missing})
    return {"checkedUnits": checked_units, "missingCount": missing_count,
            "missingSamples": gaps, "complete": missing_count == 0}


def _callback_record(item: Any) -> dict[str, Any] | None:
    """Normalize only explicit, target-bearing callback objects.

    The recorder has no implicit actor/target relationship in its unit rows;
    co-presence on a frame is deliberately not promoted to a callback.
    """
    if not isinstance(item, dict) or not isinstance(item.get("kind"), str):
        return None
    return {key: item[key] for key in sorted(item)
            if key not in {"t", "time", "frame", "f"}}


def _focal_sequence(trace: dict[str, Any]) -> dict[str, Any]:
    """Find the known R6 C2 217->5 attack without inventing its target."""
    starts: list[int] = []
    callbacks: list[dict[str, Any]] = []
    saw_callback_field = False
    for index, frame in enumerate(trace["frames"]):
        if (frame.get("round"), frame.get("camp")) != (FOCAL_ROUND, FOCAL_CAMP):
            continue
        actor, target = _unit(frame, FOCAL_ACTOR), _unit(frame, FOCAL_TARGET)
        if actor is None or target is None or _presentation_action(actor) != FOCAL_ACTION:
            continue
        starts.append(index)
        if "callbacks" in frame:
            saw_callback_field = True
            for callback in frame.get("callbacks", []):
                normalized = _callback_record(callback)
                if normalized is not None:
                    callbacks.append(normalized)
    targeted = [callback for callback in callbacks if
                callback.get("actor") == FOCAL_ACTOR and callback.get("target") == FOCAL_TARGET]
    return {
        "presentationStarts": starts[:8],
        "presentationStartCount": len(starts),
        "callbackFieldObserved": saw_callback_field,
        "targetedCallbacks": targeted[:32],
        "targetedCallbackCount": len(targeted),
        "complete": bool(starts) and bool(targeted),
    }


def _terminal_sequence(trace: dict[str, Any]) -> dict[str, Any]:
    frames = trace["frames"]
    end_indexes = [index for index, frame in enumerate(frames) if frame.get("end") is True]
    callbacks: list[dict[str, Any]] = []
    for index in end_indexes:
        for callback in frames[index].get("callbacks", []):
            normalized = _callback_record(callback)
            if normalized is not None:
                callbacks.append(normalized)
    terminal = bool(trace.get("summary", {}).get("end")) and bool(end_indexes)
    return {"summaryEnd": bool(trace.get("summary", {}).get("end")),
            "endFrames": end_indexes[:8], "endFrameCount": len(end_indexes),
            "terminalCallbacks": callbacks[:32], "terminalCallbackCount": len(callbacks),
            "complete": terminal and bool(callbacks)}


def _sequence_mismatch(source: Any, port: Any) -> dict[str, Any] | None:
    return None if source == port else {"source": source, "port": port}


def build_report(source_path: Path, port_path: Path) -> dict[str, Any]:
    missing = [str(path) for path in (source_path, port_path) if not path.is_file()]
    if missing:
        return {"format": "jojo-s22-trace-parity-gate/v1", "passed": False,
                "missingTraces": missing}
    try:
        source, port = json.loads(source_path.read_text(encoding="utf-8")), json.loads(port_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        return {"format": "jojo-s22-trace-parity-gate/v1", "passed": False,
                "readError": str(error)}
    schema_errors = _schema_errors(source, "source") + _schema_errors(port, "port")
    if schema_errors:
        return {"format": "jojo-s22-trace-parity-gate/v1", "passed": False,
                "schemaErrors": schema_errors}

    boundaries = compare(camp_boundaries(source_path), camp_boundaries(port_path))
    source_growth, port_growth = _boundary_growth_gaps(source_path), _boundary_growth_gaps(port_path)
    source_focal, port_focal = _focal_sequence(source), _focal_sequence(port)
    source_terminal, port_terminal = _terminal_sequence(source), _terminal_sequence(port)
    blockers: list[dict[str, Any]] = []
    for side, growth in (("source", source_growth), ("port", port_growth)):
        if not growth["complete"]:
            blockers.append({"observation": "growth", "side": side,
                             "reason": "all boundary units need level, abilities, posts, arm, and experience",
                             "samples": growth["missingSamples"]})
    for side, focal in (("source", source_focal), ("port", port_focal)):
        if not focal["complete"]:
            blockers.append({"observation": "round6-217-to-5-callback", "side": side,
                             "reason": "requires anime25 presentation plus an explicit callback with actor=217 and target=5",
                             "evidence": focal})
    for side, terminal in (("source", source_terminal), ("port", port_terminal)):
        if not terminal["complete"]:
            blockers.append({"observation": "terminal-callback-sequence", "side": side,
                             "reason": "requires summary.end, an end frame, and explicit callbacks on an end frame",
                             "evidence": terminal})
    tactical_mismatch = boundaries["profiles"]["tactical"]["mismatchCount"] > 0
    growth_mismatch = boundaries["profiles"]["growth"]["mismatchCount"] > 0
    focal_mismatch = _sequence_mismatch(source_focal["targetedCallbacks"], port_focal["targetedCallbacks"])
    terminal_mismatch = _sequence_mismatch(source_terminal["terminalCallbacks"], port_terminal["terminalCallbacks"])
    passed = not (boundaries["boundaryMismatchCount"] or tactical_mismatch or growth_mismatch or
                  blockers or focal_mismatch or terminal_mismatch)
    return {
        "format": "jojo-s22-trace-parity-gate/v1",
        "policy": {"screenshotsUsed": False, "timestampsIgnored": True,
                   "missingEvidenceFails": True, "growthRequired": list(GROWTH_FIELDS)},
        "source": str(source_path), "port": str(port_path),
        "campBoundaries": boundaries,
        "growthEvidence": {"source": source_growth, "port": port_growth},
        "round6Attack": {"source": source_focal, "port": port_focal,
                           "callbackOrderMismatch": focal_mismatch},
        "terminalSequence": {"source": source_terminal, "port": port_terminal,
                             "callbackOrderMismatch": terminal_mismatch},
        "comparisonBlockers": blockers, "passed": passed,
    }


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", nargs="?", type=Path, default=SOURCE_DEFAULT)
    parser.add_argument("port", nargs="?", type=Path, default=PORT_DEFAULT)
    parser.add_argument("--output", type=Path)
    args = parser.parse_args(argv)
    report = build_report(args.source, args.port)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    print(encoded)
    return 0 if report["passed"] else 1 if "missingTraces" not in report else 2


if __name__ == "__main__":
    raise SystemExit(main())
