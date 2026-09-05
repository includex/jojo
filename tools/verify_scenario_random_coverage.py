#!/usr/bin/env python3
"""Join real LWJGL model.random() traces to the recovered scenario inventory."""
from __future__ import annotations

import json
import sys
from pathlib import Path


def main() -> None:
    if len(sys.argv) != 4:
        raise SystemExit("usage: verify_scenario_random_coverage.py INVENTORY TRACE_DIR OUTPUT")
    inventory = json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
    traces = sorted(Path(sys.argv[2]).glob("*.json"))
    if not traces:
        raise SystemExit("SCENARIO_RANDOM_COVERAGE_MISSING_TRACES")
    declared = {(call["module"], call["function"], call["line"]) for call in inventory["randomCalls"]}
    covered: set[tuple[str, str, int]] = set()
    values: dict[tuple[str, str, int], set[int]] = {}
    for trace_path in traces:
        trace = json.loads(trace_path.read_text(encoding="utf-8"))
        random = trace.get("random", [])
        if not random:
            raise SystemExit(f"SCENARIO_RANDOM_COVERAGE_EMPTY_TRACE {trace_path.name}")
        for entry in random:
            site = (entry["module"], entry["function"], entry["line"])
            if site not in declared:
                raise SystemExit(f"SCENARIO_RANDOM_COVERAGE_UNKNOWN_SITE {site}")
            value = entry["value"]
            if not 0 <= value <= 100:
                raise SystemExit(f"SCENARIO_RANDOM_COVERAGE_INVALID_VALUE {entry}")
            covered.add(site)
            values.setdefault(site, set()).add(value)
    payload = {
        "format": "jojo-scenario-random-coverage/v1",
        "fixtureTraces": len(traces),
        "declaredRandomSites": len(declared),
        "coveredRandomSites": len(covered),
        "uncoveredRandomSites": len(declared - covered),
        "coveragePercent": round(100 * len(covered) / len(declared), 2),
        "covered": [
            dict(module=m, function=f, line=line, values=sorted(values[(m, f, line)]))
            for m, f, line in sorted(covered)
        ],
        "uncovered": [
            dict(module=m, function=f, line=line)
            for m, f, line in sorted(declared - covered)
        ],
    }
    output = Path(sys.argv[3])
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("SCENARIO_RANDOM_COVERAGE_OK " + " ".join(f"{key}={value}" for key, value in payload.items() if isinstance(value, (int, float))))


if __name__ == "__main__":
    main()
