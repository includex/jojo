#!/usr/bin/env python3
"""Compare source/port Yingchuan traces at stable camp-entry boundaries."""
import argparse
import json
from pathlib import Path


def boundaries(trace):
    result = {}
    previous = None
    for row in trace["frames"]:
        key = (row["round"], row["camp"])
        if key != previous:
            result[key] = row
            previous = key
    return result


def units(row):
    # Character ID is stable across the original's global BattleUnit indices
    # and the port's faction-local keys.
    return {unit[1]: unit for unit in row["units"] if unit[9] and unit[10]}


def state(unit):
    return {
        "camp": unit[2], "x": unit[3], "y": unit[4], "hp": unit[5], "mp": unit[6],
        "dir": unit[7], "action": unit[8], "acted": unit[11], "ai": unit[12], "aiValue": unit[13],
    }


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path)
    parser.add_argument("port", type=Path)
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    source = json.loads(args.source.read_text())
    port = json.loads(args.port.read_text())
    sb, pb = boundaries(source), boundaries(port)
    records = []
    for key in sorted(sb.keys() & pb.keys()):
        su, pu = units(sb[key]), units(pb[key])
        differences = []
        for character in sorted(su.keys() | pu.keys()):
            if character not in su or character not in pu:
                differences.append({"character": character, "source": state(su[character]) if character in su else None, "port": state(pu[character]) if character in pu else None})
                continue
            source_state, port_state = state(su[character]), state(pu[character])
            changed = {field: [source_state[field], port_state[field]] for field in source_state if source_state[field] != port_state[field]}
            if changed:
                differences.append({"character": character, "fields": changed})
        records.append({
            "round": key[0], "camp": key[1],
            "sourceTime": sb[key]["t"], "portTime": pb[key]["t"],
            "timestampDelta": pb[key]["t"] - sb[key]["t"],
            "differenceCount": len(differences), "differences": differences,
        })
    source_rng = [event["value"] for event in source["rng"] if event["flag"] == 0]
    port_rng = [event["value"] for event in port["rng"] if event["flag"] == 0]
    mismatch = next((i for i, pair in enumerate(zip(source_rng, port_rng)) if pair[0] != pair[1]), None)
    report = {
        "sourceSummary": source.get("summary"), "portSummary": port.get("summary"),
        "commonBoundaries": len(records),
        "sourceDefaultRngCount": len(source_rng), "portDefaultRngCount": len(port_rng),
        "firstDefaultRngMismatch": mismatch,
        "boundaries": records,
    }
    encoded = json.dumps(report, ensure_ascii=False, indent=2)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(encoded)
    print(encoded)


if __name__ == "__main__":
    main()
