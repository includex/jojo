#!/usr/bin/env python3
"""Compare original/port battle state at camp boundaries with bounded memory.

Frame rates and accelerated clocks intentionally differ between the Cocos and
LibGDX runners. The comparator keeps only the final frame of the current
``(round, camp)`` interval. Differences are classified so presentation
direction and bookkeeping/configuration noise cannot hide tactical divergence.
"""

from __future__ import annotations

import argparse
from collections import deque
from collections.abc import Iterable, Iterator
import json
from pathlib import Path
from typing import Any

from audit_late_battle_trace import iter_frames


PROFILE_NAMES = (
    "tactical", "direction", "turnBookkeeping", "aiConfig",
    "statusRepresentation", "growth",
)

GROWTH_FIELDS = ("level", "abilities", "posts", "arm", "experience")


def canonical_unit(row: list[Any]) -> dict[str, Any] | None:
    """Return stable, named comparison fields for one trace unit row."""
    if len(row) < 18 or not isinstance(row[1], int):
        return None
    details = row[17] if isinstance(row[17], dict) else {}
    # Old Cocos diagnostics did not serialize growth at all.  Newer port
    # traces put the components directly in metadata rather than under a
    # ``growth`` object.  Preserve both representations here; callers which
    # require growth evidence still decide whether absent components fail.
    growth = details.get("growth")
    if not isinstance(growth, dict):
        growth = {field: details.get(field) for field in GROWTH_FIELDS}
    return {
        "id": row[1],  # source character id
        "tactical": {
            "camp": row[2], "x": row[3], "y": row[4],
            "hp": row[5], "mp": row[6], "visible": row[9],
        },
        "direction": row[7],
        "turnBookkeeping": {"acted": row[11]},
        "aiConfig": {"ai": row[12], "value": row[13]},
        "statusRepresentation": {
            "statuses": details.get("statuses"),
            "statusRounds": details.get("statusRounds"),
        },
        "growth": growth,
    }


def canonical_state(frame: dict[str, Any]) -> dict[str, Any]:
    units = [unit for row in frame.get("units") or [] if isinstance(row, list)
             if (unit := canonical_unit(row)) is not None]
    units.sort(key=lambda unit: unit["id"])
    return {
        "round": frame.get("round"), "camp": frame.get("camp"),
        "end": frame.get("end"), "units": units,
    }


def camp_boundaries(path: Path) -> Iterator[dict[str, Any]]:
    """Yield the last state in each camp interval; never retain the trace."""
    current_key: tuple[Any, Any] | None = None
    last: dict[str, Any] | None = None
    for frame in iter_frames(path):
        key = (frame.get("round"), frame.get("camp"))
        if current_key is not None and key != current_key and last is not None:
            yield canonical_state(last)
        current_key = key
        last = frame
    if last is not None:
        yield canonical_state(last)


def _key(boundary: dict[str, Any]) -> tuple[Any, Any]:
    return boundary.get("round"), boundary.get("camp")


class _Lookahead:
    """A small queue around an iterator which also counts read input."""

    def __init__(self, values: Iterable[dict[str, Any]], size: int):
        self._iterator = iter(values)
        self._size = size
        self.items: deque[dict[str, Any]] = deque()
        self.count = 0
        self._eof = False
        self.fill()

    def fill(self) -> None:
        while not self._eof and len(self.items) < self._size:
            try:
                self.items.append(next(self._iterator))
                self.count += 1
            except StopIteration:
                self._eof = True

    def pop(self) -> dict[str, Any]:
        value = self.items.popleft()
        self.fill()
        return value

    def drain(self) -> Iterator[dict[str, Any]]:
        while self.items:
            yield self.pop()


def _find_key(items: deque[dict[str, Any]], key: tuple[Any, Any]) -> int | None:
    for index, item in enumerate(items):
        if index and _key(item) == key:
            return index
    return None


def _unit_differences(source: dict[str, Any], port: dict[str, Any]) -> tuple[dict[str, int], list[dict[str, Any]]]:
    source_units = {unit["id"]: unit for unit in source["units"]}
    port_units = {unit["id"]: unit for unit in port["units"]}
    counts = {name: 0 for name in PROFILE_NAMES}
    differences: list[dict[str, Any]] = []
    for unit_id in sorted(source_units.keys() | port_units.keys()):
        source_unit = source_units.get(unit_id)
        port_unit = port_units.get(unit_id)
        changed: dict[str, Any] = {}
        if source_unit is None or port_unit is None:
            # Roster presence is tactical state. Do not manufacture direction,
            # bookkeeping, AI, and status mismatches for an absent unit.
            counts["tactical"] += 1
            changed["tactical"] = {
                "source": source_unit.get("tactical") if source_unit else None,
                "port": port_unit.get("tactical") if port_unit else None,
            }
            differences.append({"id": unit_id, "differences": changed})
            continue
        for profile in PROFILE_NAMES:
            source_value = source_unit.get(profile)
            port_value = port_unit.get(profile)
            if profile == "growth":
                # Older traces do not carry growth details. Compare this
                # profile only when both sides provide the complete value.
                if (not isinstance(source_value, dict) or
                        not isinstance(port_value, dict) or
                        any(source_value.get(field) is None or
                            port_value.get(field) is None
                            for field in GROWTH_FIELDS)):
                    continue
            if source_value != port_value:
                counts[profile] += 1
                changed[profile] = {"source": source_value, "port": port_value}
        if changed:
            differences.append({"id": unit_id, "differences": changed})
    return counts, differences


def compare(
    source: Iterable[dict[str, Any]], port: Iterable[dict[str, Any]], *,
    max_details: int = 8, lookahead: int = 8,
) -> dict[str, Any]:
    """Compare ordered boundary streams using O(units + lookahead) memory."""
    if max_details < 0:
        raise ValueError("max_details must be non-negative")
    if lookahead < 1:
        raise ValueError("lookahead must be positive")

    source_stream = _Lookahead(source, lookahead + 1)
    port_stream = _Lookahead(port, lookahead + 1)
    profiles = {name: {"mismatchCount": 0, "unitMismatchCount": 0, "firstMismatch": None}
                for name in PROFILE_NAMES}
    common_count = boundary_mismatch_count = state_mismatch_count = 0
    end_mismatch_count = 0
    missing_count = extra_count = 0
    missing_samples: list[list[Any]] = []
    extra_samples: list[list[Any]] = []
    first_mismatch: dict[str, Any] | None = None

    def record_unmatched(kind: str, boundary: dict[str, Any]) -> None:
        nonlocal boundary_mismatch_count, missing_count, extra_count, first_mismatch
        boundary_mismatch_count += 1
        key = list(_key(boundary))
        if kind == "missingInPort":
            missing_count += 1
            if len(missing_samples) < max_details:
                missing_samples.append(key)
        else:
            extra_count += 1
            if len(extra_samples) < max_details:
                extra_samples.append(key)
        if first_mismatch is None:
            first_mismatch = {"kind": kind, "round": key[0], "camp": key[1]}

    while source_stream.items and port_stream.items:
        source_boundary = source_stream.items[0]
        port_boundary = port_stream.items[0]
        source_key, port_key = _key(source_boundary), _key(port_boundary)
        if source_key != port_key:
            source_offset = _find_key(source_stream.items, port_key)
            port_offset = _find_key(port_stream.items, source_key)
            if source_offset is not None and (port_offset is None or source_offset <= port_offset):
                for _ in range(source_offset):
                    record_unmatched("missingInPort", source_stream.pop())
            elif port_offset is not None:
                for _ in range(port_offset):
                    record_unmatched("extraInPort", port_stream.pop())
            else:
                record_unmatched("missingInPort", source_stream.pop())
                record_unmatched("extraInPort", port_stream.pop())
            continue

        source_boundary, port_boundary = source_stream.pop(), port_stream.pop()
        common_count += 1
        unit_counts, unit_differences = _unit_differences(source_boundary, port_boundary)
        changed_profiles = [name for name, count in unit_counts.items() if count]
        for name, count in unit_counts.items():
            if count:
                profiles[name]["mismatchCount"] += 1
                profiles[name]["unitMismatchCount"] += count
                if profiles[name]["firstMismatch"] is None:
                    profile_units = [
                        {
                            "id": unit["id"],
                            "source": unit["differences"][name]["source"],
                            "port": unit["differences"][name]["port"],
                        }
                        for unit in unit_differences
                        if name in unit["differences"]
                    ]
                    shown = profile_units[:max_details]
                    profiles[name]["firstMismatch"] = {
                        "round": source_key[0], "camp": source_key[1],
                        "sourceEnd": source_boundary.get("end"),
                        "portEnd": port_boundary.get("end"),
                        "units": shown,
                        "omittedUnits": len(profile_units) - len(shown),
                    }
        end_changed = source_boundary.get("end") != port_boundary.get("end")
        if end_changed:
            end_mismatch_count += 1
            changed_profiles.append("outcome")
        if changed_profiles:
            state_mismatch_count += 1
            if first_mismatch is None:
                shown = unit_differences[:max_details]
                first_mismatch = {
                    "kind": "state", "round": source_key[0], "camp": source_key[1],
                    "sourceEnd": source_boundary.get("end"),
                    "portEnd": port_boundary.get("end"),
                    "profiles": changed_profiles, "units": shown,
                    "omittedUnits": len(unit_differences) - len(shown),
                }

    for boundary in source_stream.drain():
        record_unmatched("missingInPort", boundary)
    for boundary in port_stream.drain():
        record_unmatched("extraInPort", boundary)

    mismatch_count = boundary_mismatch_count + state_mismatch_count
    return {
        "format": "jojo-battle-camp-boundary-compare/v2",
        "sourceBoundaries": source_stream.count, "portBoundaries": port_stream.count,
        "commonBoundaries": common_count,
        "missingInPort": missing_samples, "missingInPortCount": missing_count,
        "extraInPort": extra_samples, "extraInPortCount": extra_count,
        "boundaryMismatchCount": boundary_mismatch_count,
        "stateMismatchCount": state_mismatch_count, "endMismatchCount": end_mismatch_count,
        "mismatchCount": mismatch_count,
        "profiles": profiles, "firstMismatch": first_mismatch,
        # Retain the old key, capped to the one useful diagnostic.
        "mismatches": [first_mismatch] if first_mismatch else [],
        "detailsLimit": max_details, "passed": mismatch_count == 0,
    }


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("port", type=Path)
    parser.add_argument("--output", type=Path)
    parser.add_argument("--max-details", type=int, default=8,
                        help="maximum unit/key samples in diagnostics (default: 8)")
    parser.add_argument("--lookahead", type=int, default=8,
                        help="bounded interval resynchronization window (default: 8)")
    args = parser.parse_args(argv)
    report = compare(camp_boundaries(args.source), camp_boundaries(args.port),
                     max_details=args.max_details, lookahead=args.lookahead)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    print(encoded)
    return 0 if report["passed"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
