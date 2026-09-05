#!/usr/bin/env python3
"""Join real LWJGL choice traces to the source choice inventory."""
from __future__ import annotations

import json
import sys
from pathlib import Path


def main() -> None:
    if len(sys.argv) != 4:
        raise SystemExit("usage: verify_scenario_choice_coverage.py INVENTORY TRACE_DIR OUTPUT")
    inventory = json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
    trace_paths = sorted(Path(sys.argv[2]).glob("*.json"))
    if not trace_paths:
        raise SystemExit("SCENARIO_CHOICE_COVERAGE_MISSING_TRACES")
    declared = {
        (choice["module"], choice["function"], choice["line"]): choice["options"]
        for choice in inventory["choices"]
    }
    covered: set[tuple[str, str, int, int]] = set()
    executed = 0
    for trace_path in trace_paths:
        trace = json.loads(trace_path.read_text(encoding="utf-8"))
        if not trace["choices"]:
            raise SystemExit(f"SCENARIO_CHOICE_COVERAGE_EMPTY_TRACE {trace_path.name}")
        for entry in trace["choices"]:
            site = (entry["module"], entry["function"], entry["line"])
            expected_options = declared.get(site)
            if expected_options is None:
                raise SystemExit(f"SCENARIO_CHOICE_COVERAGE_UNKNOWN_SITE {site}")
            if entry["optionCount"] != expected_options or not 0 <= entry["option"] < expected_options:
                raise SystemExit(f"SCENARIO_CHOICE_COVERAGE_INVALID_OPTION {entry}")
            covered.add((*site, entry["option"]))
            executed += 1
    all_options = {(*site, option) for site, count in declared.items() for option in range(count)}
    payload = {
        "format": "jojo-scenario-choice-coverage/v1",
        "fixtureTraces": len(trace_paths),
        "executedChoiceSelections": executed,
        "declaredChoiceSites": len(declared),
        "declaredChoiceOptions": len(all_options),
        "coveredChoiceOptions": len(covered),
        "uncoveredChoiceOptions": len(all_options - covered),
        "coveragePercent": round(100 * len(covered) / len(all_options), 2),
        "covered": [dict(module=m, function=f, line=line, option=option) for m, f, line, option in sorted(covered)],
    }
    output = Path(sys.argv[3])
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("SCENARIO_CHOICE_COVERAGE_OK " + " ".join(f"{key}={value}" for key, value in payload.items() if isinstance(value, (int, float))))


if __name__ == "__main__":
    main()
