#!/usr/bin/env python3
"""Block screenshot verification until every required render-log state passes."""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

import verify_render_parity_reports


def _verify_layer_inventory(
    scope: dict, scope_path: Path, repository: Path
) -> tuple[set[str], list[str]]:
    """Return required layer names and exhaustive-inventory diagnostics."""
    inventory_name = scope.get("layerInventory")
    if not inventory_name:
        return set(), []
    inventory_path = repository / inventory_name
    if not inventory_path.is_file():
        return set(), [f"BLOCKED layer-inventory: missing {inventory_name}"]
    try:
        inventory = json.loads(inventory_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        return set(), [f"BLOCKED layer-inventory: invalid {inventory_name}: {error}"]

    diagnostics: list[str] = []
    entries = inventory.get("layers")
    if not isinstance(entries, list):
        return set(), ["BLOCKED layer-inventory: layers must be an array"]
    by_source: dict[str, dict] = {}
    by_name: dict[str, str] = {}
    valid_classes = {"required", "infrastructure/no-render"}
    for index, entry in enumerate(entries):
        if not isinstance(entry, dict):
            diagnostics.append(f"BLOCKED layer-inventory[{index}]: entry must be an object")
            continue
        source = entry.get("source")
        name = entry.get("name")
        classification = entry.get("classification")
        reason = entry.get("reason")
        if not isinstance(source, str) or not source:
            diagnostics.append(f"BLOCKED layer-inventory[{index}]: missing source")
            continue
        if source in by_source:
            diagnostics.append(f"BLOCKED layer-inventory: duplicate source {source}")
        by_source[source] = entry
        if not isinstance(name, str) or not name:
            diagnostics.append(f"BLOCKED layer-inventory {source}: missing name")
        elif name in by_name:
            diagnostics.append(f"BLOCKED layer-inventory: duplicate name {name} ({by_name[name]}, {source})")
        else:
            by_name[name] = source
        if classification not in valid_classes:
            diagnostics.append(f"BLOCKED layer-inventory {source}: invalid classification {classification!r}")
        if not isinstance(reason, str) or not reason.strip():
            diagnostics.append(f"BLOCKED layer-inventory {source}: missing classification reason")

    source_directory = inventory.get("sourceDirectory")
    source_globs = inventory.get("sourceGlobs", [])
    if not isinstance(source_directory, str) or not source_directory:
        diagnostics.append("BLOCKED layer-inventory: missing sourceDirectory")
    elif not isinstance(source_globs, list) or not source_globs or not all(isinstance(pattern, str) for pattern in source_globs):
        diagnostics.append("BLOCKED layer-inventory: sourceGlobs must be a non-empty string array")
    else:
        source_root = (repository / source_directory).resolve()
        if not source_root.is_dir():
            diagnostics.append(f"BLOCKED layer-inventory: source directory missing {source_directory}")
        else:
            discovered = {
                path.relative_to(source_root).as_posix()
                for pattern in source_globs
                for path in source_root.glob(pattern)
                if path.is_file()
            }
            inventoried = set(by_source)
            for source in sorted(discovered - inventoried):
                diagnostics.append(f"BLOCKED layer-inventory: unclassified recovered layer {source}")
            for source in sorted(inventoried - discovered):
                diagnostics.append(f"BLOCKED layer-inventory: stale/missing recovered layer {source}")

    required = {
        entry["name"] for entry in by_source.values()
        if entry.get("classification") == "required" and isinstance(entry.get("name"), str)
    }
    mapped: set[str] = set()
    for phase in scope.get("phases", []):
        for state in phase.get("states", []):
            label = f"{phase.get('id', 'unknown')}/{state.get('id', 'unknown')}"
            if "sourceLayers" not in state:
                diagnostics.append(f"BLOCKED {label}: missing sourceLayers declaration")
                continue
            layers = state.get("sourceLayers", [])
            if not isinstance(layers, list) or not all(isinstance(layer, str) for layer in layers):
                diagnostics.append(f"BLOCKED {label}: sourceLayers must be a string array")
                continue
            for layer in layers:
                if layer not in by_name:
                    diagnostics.append(f"BLOCKED {label}: unknown sourceLayer {layer}")
                mapped.add(layer)
    for layer in sorted(required - mapped):
        diagnostics.append(f"BLOCKED layer-coverage: required {layer} has no parity state")
    if not diagnostics:
        diagnostics.append(
            f"PASS layer-inventory: inventoried={len(by_source)} required={len(required)} mapped={len(required & mapped)}"
        )
    return required, diagnostics


def verify_scope(scope_path: Path, repository: Path) -> tuple[bool, list[str]]:
    scope = json.loads(scope_path.read_text(encoding="utf-8"))
    messages: list[str] = []
    failures = 0
    total = 0
    freshness = None
    freshness_name = scope.get("freshnessManifest")
    if freshness_name:
        freshness_path = repository / freshness_name
        try:
            freshness = json.loads(freshness_path.read_text(encoding="utf-8"))
            if freshness.get("format") != "jojo-render-freshness/v1" or not freshness.get("runId"):
                raise ValueError("invalid format/runId")
            marker_name = scope.get("freshnessMarker")
            if marker_name:
                marker = json.loads((repository / marker_name).read_text(encoding="utf-8"))
                if marker.get("runId") != freshness.get("runId") or marker.get("startedNs") != freshness.get("startedNs"):
                    raise ValueError("start marker and completed manifest are from different runs")
        except (OSError, ValueError, json.JSONDecodeError) as error:
            messages.append(f"BLOCKED freshness-manifest: {error}")
            failures += 1
    _, inventory_messages = _verify_layer_inventory(scope, scope_path, repository)
    messages.extend(inventory_messages)
    failures += sum(message.startswith("BLOCKED") for message in inventory_messages)
    for phase in scope.get("phases", []):
        phase_id = phase.get("id", "unknown")
        for state in phase.get("states", []):
            total += 1
            state_id = state.get("id", "unknown")
            report_name = state.get("report")
            label = f"{phase_id}/{state_id}"
            if not report_name:
                failures += 1
                messages.append(f"BLOCKED {label}: no strict comparison report")
                continue
            report_path = repository / report_name
            if not report_path.is_file():
                failures += 1
                messages.append(f"BLOCKED {label}: missing {report_name}")
                continue
            okay, detail = verify_render_parity_reports.verify(report_path, repository)
            if okay and state.get("freshRequired"):
                if freshness is None:
                    okay = False
                    detail += "; missing fresh run manifest"
                else:
                    report = json.loads(report_path.read_text(encoding="utf-8"))
                    required_paths = [report_path]
                    for name in (report.get("expected"), report.get("actual")):
                        path = Path(name)
                        required_paths.append(path if path.is_absolute() else repository / path)
                    required_paths.extend(repository / name for name in state.get("freshArtifacts", []))
                    declared = set(freshness.get("artifacts", []))
                    started_ns = int(freshness.get("startedNs", 0))
                    stale = []
                    for path in required_paths:
                        declaration = str(path.relative_to(repository)) if path.is_relative_to(repository) else str(path)
                        if not path.is_file() or declaration not in declared or path.stat().st_mtime_ns < started_ns:
                            stale.append(declaration)
                        elif declaration.startswith("build/render-frames/") and declaration.endswith(".json"):
                            pixel = json.loads(path.read_text(encoding="utf-8"))
                            if pixel.get("format") not in ("jojo-fresh-battle-frame-compare/v1", "jojo-fresh-battle-frame-compare/v2") or pixel.get("passed") is not True:
                                stale.append(declaration + "(failed pixel comparison)")
                        elif declaration == "build/yingchuan-actor-state.json":
                            dialogue = json.loads(path.read_text(encoding="utf-8"))
                            if dialogue.get("result") != "ok" or [row.get("step") for row in dialogue.get("states", [])] != [1, 2, 3]:
                                stale.append(declaration + "(missing dialogue steps)")
                    if stale:
                        okay = False
                        detail += f"; stale/not-in-run={stale} runId={freshness.get('runId')}"
                    else:
                        detail += f"; fresh runId={freshness.get('runId')}"
            if not okay:
                failures += 1
            messages.append(f"{'PASS' if okay else 'BLOCKED'} {label}: {detail}")
    messages.append(
        f"SCREENSHOT_GATE_{'OPEN' if failures == 0 else 'BLOCKED'} "
        f"states={total} failures={failures}"
    )
    return failures == 0, messages


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--scope",
        type=Path,
        default=Path(__file__).with_name("render_parity_scope.json"),
    )
    parser.add_argument(
        "--repository",
        type=Path,
        default=Path(__file__).resolve().parents[1],
    )
    args = parser.parse_args(argv)
    okay, messages = verify_scope(args.scope.resolve(), args.repository.resolve())
    print("\n".join(messages))
    return 0 if okay else 1


if __name__ == "__main__":
    sys.exit(main())
