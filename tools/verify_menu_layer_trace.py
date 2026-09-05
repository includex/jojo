#!/usr/bin/env python3
import json, subprocess, sys
from pathlib import Path
root = Path(__file__).resolve().parents[1]; fixture = (Path(sys.argv[1]).resolve() if len(sys.argv) == 2 else root / "tools/menu_layer_trace_cases.json")
source = json.loads(subprocess.check_output(["node", str(root / "tools/menu_layer_source_trace_harness.js"), str(fixture)], text=True))
game = json.loads(subprocess.check_output([str(root / "gradlew"), "-q", f"-DmenuFixture={fixture}", ":core:menuLayerTrace"], cwd=root, text=True))
ids = {case["id"] for case in source}
if any(identifier.startswith("switch-") for identifier in ids):
    if not all(case["initial"]["switchWeatherSheet"] is not None and all(not button["interactable"] for button in case["initial"]["buttons"]) for case in source):
        raise SystemExit("MENU_LAYER_SWITCH_COVERAGE_MISSING disabled buttons or target sheet")
    if not all(case["inputs"][-1]["events"] == ["remove", "callback"] for case in source):
        raise SystemExit("MENU_LAYER_SWITCH_COVERAGE_MISSING final remove/callback lifecycle")
    if source != game: raise SystemExit("MENU_LAYER_TRACE_MISMATCH\nsource="+json.dumps(source)+"\nport="+json.dumps(game))
    print(f"MENU_LAYER_SWITCH_TRACE_OK cases={len(source)}")
    raise SystemExit(0)
required = {f"cmd-{index}" for index in range(12)} | {"cmd-12-inactive", "cmd-12-edit", "cmd-13", "flag-lock-cancel"}
if ids != required:
    raise SystemExit(f"MENU_LAYER_TRACE_COVERAGE_MISSING expected={sorted(required)} actual={sorted(ids)}")
if {case["initial"]["weatherSheet"] for case in source} != {1, 2, 3, 4, 5}:
    raise SystemExit("MENU_LAYER_TRACE_COVERAGE_MISSING weather sheets 1..5")
if any(case["initial"]["frames"] != [0, 1, 2, 3, 0] for case in source):
    raise SystemExit("MENU_LAYER_TRACE_COVERAGE_MISSING 6fps frame boundaries")
if source != game: raise SystemExit("MENU_LAYER_TRACE_MISMATCH\nsource="+json.dumps(source)+"\nport="+json.dumps(game))
print(f"MENU_LAYER_TRACE_OK cases={len(source)} commands=0..13 weatherSheets=1..5 frameBoundaries=0,1,2,3,0")
