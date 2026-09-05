#!/usr/bin/env python3
"""Stream a compact, investigation-friendly summary of a battle trace.

Only one frame is resident at a time.  This is intentionally a small standard
library tool: traces are often much larger than the machine's available RAM.
"""
from __future__ import annotations

import argparse
import json
import re
from collections import defaultdict
from pathlib import Path
from typing import Any

try:
    from audit_late_battle_trace import iter_frames
except ModuleNotFoundError:  # imported from a repository-level test runner
    import importlib.util
    import sys
    sys.path.insert(0, str(Path(__file__).parent))
    _audit_path = Path(__file__).with_name("audit_late_battle_trace.py")
    _spec = importlib.util.spec_from_file_location("audit_late_battle_trace", _audit_path)
    _audit = importlib.util.module_from_spec(_spec)
    assert _spec.loader is not None
    _spec.loader.exec_module(_audit)
    iter_frames = _audit.iter_frames


_ACTION = re.compile(r"^(?:r(?P<round>\d+)/)?(?P<camp>[A-Z_]+)/(?P<actor>-?\d+):(?P<from>[^-]+)->(?P<to>[^:]+):target=(?P<target>[^:]+):magic=(?P<magic>[^:]+)")
_DAMAGE = re.compile(r"(?:hp=)(-?\d+)/(?:-?\d+):harm=(-?\d+)")
_DRIVER_MARKERS = (
    "S_57:player-unit-select", "move-destination", "player-command-attack",
    "player-attack-target", "command-wait", "pan",
)


def _unit_map(frame: dict[str, Any]) -> dict[int, list[Any]]:
    return {r[1]: r for r in frame.get("units") or []
            if isinstance(r, list) and len(r) > 6 and isinstance(r[1], int)}


def _strings(value: Any):
    if isinstance(value, str):
        yield value
    elif isinstance(value, dict):
        for item in value.values():
            yield from _strings(item)
    elif isinstance(value, list):
        for item in value:
            yield from _strings(item)


def _attempt(value: Any, frame: dict[str, Any]) -> dict[str, Any] | None:
    if isinstance(value, dict):
        # New recorders may already provide structured attempts.
        if any(k in value for k in ("actor", "attacker", "action")):
            row = dict(value)
            row.setdefault("frame", frame.get("f"))
            return row
        return None
    if not isinstance(value, str):
        return None
    match = _ACTION.search(value)
    if not match:
        return None
    row = match.groupdict()
    row.update(frame=frame.get("f"), round=frame.get("round", row.get("round")))
    row["actor"] = int(row.pop("actor"))
    row["target"] = None if row["target"] in ("null", "None", "") else int(row["target"])
    return row


def summarize_trace(trace: Path, scenario: str | None = None, leaders: list[int] | None = None,
                    verbose_units: bool = False) -> dict[str, Any]:
    leaders = leaders or []
    previous: dict[int, tuple[Any, ...]] = {}
    rounds: dict[str, dict[str, Any]] = {}
    leader_hp: dict[str, list[dict[str, Any]]] = defaultdict(list)
    damage: list[dict[str, Any]] = []
    attempts: list[dict[str, Any]] = []
    deaths: list[dict[str, Any]] = []
    revivals: list[dict[str, Any]] = []
    authored: list[dict[str, Any]] = []
    rejected: list[dict[str, Any]] = []
    stale: list[dict[str, Any]] = []
    rejection_count = stale_count = 0
    counts: list[dict[str, Any]] = []
    previous_counts: tuple[Any, ...] | None = None
    previous_actions: list[Any] = []
    driver_markers: dict[str, dict[str, Any]] = {}
    terminal: dict[str, Any] = {"end": False, "outcome": None, "callbacks": {}}
    frame_count = 0
    for frame in iter_frames(trace):
        frame_count += 1
        number = frame.get("f", frame_count)
        # Driver markers describe inputs issued by the production harness.
        # They are evidence of driver activity, not proof that an attack was
        # adjudicated or dealt damage.
        frame_marker_hits: dict[str, int] = defaultdict(int)
        for value in _strings(frame):
            for marker in _DRIVER_MARKERS:
                if marker in value:
                    frame_marker_hits[marker] += 1
        for marker, occurrences in frame_marker_hits.items():
            entry = driver_markers.setdefault(marker, {"count": 0, "frames": 0,
                                                        "firstFrame": number, "lastFrame": number})
            entry["count"] += occurrences
            entry["frames"] += 1
            entry["lastFrame"] = number
        key = str(frame.get("round", "unknown"))
        bucket = rounds.setdefault(key, {"round": frame.get("round"), "frames": 0,
                                         "unitSnapshots": 0, "stateChanges": 0, "actionAttempts": 0})
        bucket["frames"] += 1
        units = _unit_map(frame)
        count_key = tuple(frame.get(k) for k in ("playerCount", "friendCount", "enemyCount"))
        if count_key != previous_counts and any(value is not None for value in count_key):
            counts.append({k: frame[k] for k in ("f", "round", "camp", "playerCount", "friendCount", "enemyCount") if k in frame})
            previous_counts = count_key
        for unit_id, row in units.items():
            state = {"frame": number, "x": row[3] if len(row) > 3 else None, "y": row[4] if len(row) > 4 else None,
                     "hp": row[5] if len(row) > 5 else None, "mp": row[6] if len(row) > 6 else None}
            compact = tuple(state[k] for k in ("x", "y", "hp", "mp"))
            bucket["unitSnapshots"] += 1
            selected = verbose_units or unit_id in leaders
            if selected:
                history = bucket.setdefault("units", {}).setdefault(str(unit_id), [])
                if not history or compact != tuple(history[-1][k] for k in ("x", "y", "hp", "mp")):
                    history.append(state)
            old = previous.get(unit_id)
            if old:
                if compact != old:
                    bucket["stateChanges"] += 1
                if old[2] > 0 and state["hp"] <= 0:
                    deaths.append({"frame": number, "round": frame.get("round"), "unit": unit_id, "hp": state["hp"]})
                if old[2] <= 0 and state["hp"] > 0:
                    revivals.append({"frame": number, "round": frame.get("round"), "unit": unit_id, "hp": state["hp"]})
                if state["hp"] != old[2]:
                    damage.append({"frame": number, "round": frame.get("round"), "unit": unit_id,
                                   "before": old[2], "after": state["hp"], "amount": old[2] - state["hp"]})
            previous[unit_id] = (state["x"], state["y"], state["hp"], state["mp"])
            if unit_id in leaders and (not leader_hp[str(unit_id)] or leader_hp[str(unit_id)][-1]["hp"] != state["hp"]):
                leader_hp[str(unit_id)].append({"frame": number, "round": frame.get("round"), "hp": state["hp"]})
        raw_actions = list(_strings(frame.get("actions")))
        # Production recorders expose an append-only action journal on every
        # frame.  Consume only its suffix; otherwise S57 repeats the same
        # journal thousands of times and the "summary" becomes larger than
        # the source trace.
        if raw_actions[:len(previous_actions)] == previous_actions:
            action_values = raw_actions[len(previous_actions):]
        else:
            action_values = raw_actions
        previous_actions = raw_actions
        values = action_values + list(_strings(frame.get("observation")))
        for value in values:
            attempt = _attempt(value, frame)
            if attempt:
                attempts.append(attempt)
                bucket["actionAttempts"] += 1
            lower = value.lower()
            if "center" in lower or "setunitstatus" in lower:
                authored.append({"frame": number, "value": value})
            if any(token in lower for token in ("reject", "rejected", "input-denied")):
                rejection_count += 1
                if len(rejected) < 20:
                    rejected.append({"frame": number, "value": value})
            if "stale" in lower or "old-target" in lower or "target-invalid" in lower:
                stale_count += 1
                if len(stale) < 20:
                    stale.append({"frame": number, "value": value})
        if frame.get("end"):
            terminal["end"] = True
            terminal["frame"] = number
        if frame.get("outcome") is not None:
            terminal["outcome"] = frame["outcome"]
        for outcome_key in ("scriptedOutcome", "result", "flowResult"):
            if frame.get(outcome_key) is not None:
                terminal["outcome"] = frame[outcome_key]
        if frame.get("callback") is not None or frame.get("callbacks") is not None:
            terminal["callbacks"][str(number)] = frame.get("callbacks", frame.get("callback"))
    # Do not json.load the document here: even a small-looking fixture can
    # contain a multi-gigabyte frames member.  Callers that need metadata can
    # pass --scenario; the filename remains a useful fallback.
    scenario_value = scenario or trace.name.split(".", 1)[0]
    report = {"format": "jojo-battle-trace-summary/v1", "trace": str(trace), "scenario": scenario_value,
              "frameCount": frame_count, "rounds": list(rounds.values()), "actionAttempts": attempts,
              "actualDamage": damage, "deaths": deaths, "revivals": revivals,
              "sourceLeaderHpHistories": dict(leader_hp), "playerCounts": counts,
              "driverInputMarkers": driver_markers,
              "inputRejections": {"count": rejection_count, "examples": rejected},
              "staleTargetEpisodes": {"count": stale_count, "examples": stale},
              "authoredSequences": authored, "terminal": terminal}
    return report


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--trace", required=True, type=Path)
    parser.add_argument("--scenario")
    parser.add_argument("--leaders", default="", help="comma-separated unit IDs")
    parser.add_argument("--output", type=Path)
    parser.add_argument("--verbose-units", action="store_true",
                        help="retain position/HP/MP histories for every unit")
    args = parser.parse_args(argv)
    leaders = [int(x.strip()) for x in args.leaders.split(",") if x.strip()]
    report = summarize_trace(args.trace, args.scenario, leaders, args.verbose_units)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    if args.output:
        print(f"wrote {args.output} ({len(encoded)} bytes)")
    else:
        print(encoded)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
