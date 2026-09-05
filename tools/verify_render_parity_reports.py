#!/usr/bin/env python3
"""Re-run every comparison referenced by strict render-parity reports.

This is the pre-screenshot gate: a stale report that says ``equal`` is not
enough.  The referenced JSON/JSONL inputs are loaded again with the current
comparator, and every report must still have the same draw counts and zero
semantic differences.
"""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

import compare_render_logs


def verify(report_path: Path, repository: Path) -> tuple[bool, str]:
    report = json.loads(report_path.read_text(encoding="utf-8"))
    expected_name, actual_name = report.get("expected"), report.get("actual")
    if not expected_name or not actual_name:
        return False, f"{report_path}: missing expected/actual paths"

    def resolve(name: str) -> Path:
        path = Path(name)
        return path if path.is_absolute() else repository / path

    expected_path, actual_path = resolve(expected_name), resolve(actual_name)
    try:
        expected_format, expected = compare_render_logs.adapt(compare_render_logs.load_input(expected_path))
        actual_format, actual = compare_render_logs.adapt(compare_render_logs.load_input(actual_path))
    except (OSError, ValueError, json.JSONDecodeError) as error:
        return False, f"{report_path}: {error}"
    tolerance = float(report.get("floatTolerance", 1e-5))
    differences = compare_render_logs.compare(expected, actual, tolerance)
    declared_counts = (report.get("expectedDrawCount"), report.get("actualDrawCount"))
    current_counts = (len(expected), len(actual))
    okay = not differences and declared_counts == current_counts and expected_format == report.get("expectedFormat") and actual_format == report.get("actualFormat")
    return okay, (
        f"{'PASS' if okay else 'FAIL'} {report_path.name}: "
        f"draws={len(expected)}/{len(actual)} diffs={len(differences)}"
    )


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("reports", nargs="+", type=Path)
    parser.add_argument("--repository", type=Path, default=Path(__file__).resolve().parents[1])
    args = parser.parse_args(argv)
    failures = 0
    for report in args.reports:
        okay, message = verify(report.resolve(), args.repository.resolve())
        print(message)
        failures += not okay
    print(f"RENDER_PARITY_REPORT_GATE_{'PASS' if failures == 0 else 'BLOCKED'} reports={len(args.reports)} failures={failures}")
    return 0 if failures == 0 else 1


if __name__ == "__main__":
    sys.exit(main())
