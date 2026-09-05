#!/usr/bin/env python3
"""Compare original/game battle state at camp boundaries with bounded memory.

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
    # Old Cocos diagnostics did not serialize growth at all.  Newer game
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


def _unit_differences(source: dict[str, Any], game: dict[str, Any]) -> tuple[dict[str, int], list[dict[str, Any]]]:
    source_units = {unit["id"]: unit for unit in source["units"]}
    game_units = {unit["id"]: unit for unit in game["units"]}
    counts = {name: 0 for name in PROFILE_NAMES}
    differences: list[dict[str, Any]] = []
    for unit_id in sorted(source_units.keys() | game_units.keys()):
        source_unit = source_units.get(unit_id)
        game_unit = game_units.get(unit_id)
        changed: dict[str, Any] = {}
        if source_unit is None or game_unit is None:
            # Roster presence is tactical state. Do not manufacture direction,
            # bookkeeping, AI, and status mismatches for an absent unit.
            counts["tactical"] += 1
            changed["tactical"] = {
                "source": source_unit.get("tactical") if source_unit else None,
                "game": game_unit.get("tactical") if game_unit else None,
            }
            differences.append({"id": unit_id, "differences": changed})
            continue
        for profile in PROFILE_NAMES:
            source_value = source_unit.get(profile)
            game_value = game_unit.get(profile)
            if profile == "growth":
                # Older traces do not carry growth details. Compare this
                # profile only when both sides provide the complete value.
                if (not isinstance(source_value, dict) or
                        not isinstance(game_value, dict) or
                        any(source_value.get(field) is None or
                            game_value.get(field) is None
                            for field in GROWTH_FIELDS)):
                    continue
            if source_value != game_value:
                counts[profile] += 1
                changed[profile] = {"source": source_value, "game": game_value}
        if changed:
            differences.append({"id": unit_id, "differences": changed})
    return counts, differences


def compare(
    source: Iterable[dict[str, Any]], game: Iterable[dict[str, Any]], *,
    max_details: int = 8, lookahead: int = 8,
) -> dict[str, Any]:
    """Compare ordered boundary streams using O(units + lookahead) memory."""
    if max_details < 0:
        raise ValueError("max_details must be non-negative")
    if lookahead < 1:
        raise ValueError("lookahead must be positive")

    source_stream = _Lookahead(source, lookahead + 1)
    game_stream = _Lookahead(game, lookahead + 1)
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
        if kind == "missingInGame":
            missing_count += 1
            if len(missing_samples) < max_details:
                missing_samples.append(key)
        else:
            extra_count += 1
            if len(extra_samples) < max_details:
                extra_samples.append(key)
        if first_mismatch is None:
            first_mismatch = {"kind": kind, "round": key[0], "camp": key[1]}

    while source_stream.items and game_stream.items:
        source_boundary = source_stream.items[0]
        game_boundary = game_stream.items[0]
        source_key, game_key = _key(source_boundary), _key(game_boundary)
        if source_key != game_key:
            source_offset = _find_key(source_stream.items, game_key)
            game_offset = _find_key(game_stream.items, source_key)
            if source_offset is not None and (game_offset is None or source_offset <= game_offset):
                for _ in range(source_offset):
                    record_unmatched("missingInGame", source_stream.pop())
            elif game_offset is not None:
                for _ in range(game_offset):
                    record_unmatched("extraInGame", game_stream.pop())
            else:
                record_unmatched("missingInGame", source_stream.pop())
                record_unmatched("extraInGame", game_stream.pop())
            continue

        source_boundary, game_boundary = source_stream.pop(), game_stream.pop()
        common_count += 1
        unit_counts, unit_differences = _unit_differences(source_boundary, game_boundary)
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
                            "game": unit["differences"][name]["game"],
                        }
                        for unit in unit_differences
                        if name in unit["differences"]
                    ]
                    shown = profile_units[:max_details]
                    profiles[name]["firstMismatch"] = {
                        "round": source_key[0], "camp": source_key[1],
                        "sourceEnd": source_boundary.get("end"),
                        "gameEnd": game_boundary.get("end"),
                        "units": shown,
                        "omittedUnits": len(profile_units) - len(shown),
                    }
        end_changed = source_boundary.get("end") != game_boundary.get("end")
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
                    "gameEnd": game_boundary.get("end"),
                    "profiles": changed_profiles, "units": shown,
                    "omittedUnits": len(unit_differences) - len(shown),
                }

    for boundary in source_stream.drain():
        record_unmatched("missingInGame", boundary)
    for boundary in game_stream.drain():
        record_unmatched("extraInGame", boundary)

    mismatch_count = boundary_mismatch_count + state_mismatch_count
    return {
        "format": "jojo-battle-camp-boundary-compare/v2",
        "sourceBoundaries": source_stream.count, "gameBoundaries": game_stream.count,
        "commonBoundaries": common_count,
        "missingInGame": missing_samples, "missingInGameCount": missing_count,
        "extraInGame": extra_samples, "extraInGameCount": extra_count,
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
    parser.add_argument("game", type=Path)
    parser.add_argument("--output", type=Path)
    parser.add_argument("--max-details", type=int, default=8,
                        help="maximum unit/key samples in diagnostics (default: 8)")
    parser.add_argument("--lookahead", type=int, default=8,
                        help="bounded interval resynchronization window (default: 8)")
    args = parser.parse_args(argv)
    report = compare(camp_boundaries(args.source), camp_boundaries(args.game),
                     max_details=args.max_details, lookahead=args.lookahead)
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded + "\n", encoding="utf-8")
    print(encoded)
    return 0 if report["passed"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
