#!/usr/bin/env python3
"""Static oracle for synchronous battle-stage APIs omitted by broad trace runs."""

import ast
import json
import re
from pathlib import Path


HERE = Path(__file__).resolve().parent
SPEC = json.loads((HERE / "battle_stage_immediate_contracts.json").read_text(encoding="utf-8"))
ROOT = Path(SPEC["sourceRoot"])
PY = ROOT / "decompiled-python"
JS = ROOT / "recovered-js/modules"


def calls(path: Path, method: str):
    tree = ast.parse(path.read_text(encoding="utf-8"), filename=str(path))
    found = []
    for node in ast.walk(tree):
        if isinstance(node, ast.Call) and isinstance(node.func, ast.Attribute) and node.func.attr == method:
            found.append((node.lineno, [ast.literal_eval(arg) for arg in node.args]))
    return sorted(found)


center = calls(PY / "S_57.py", "center")
assert len(center) == SPEC["contracts"]["stage.center"]["callCount"]
center_args = [args for _, args in center]
branch = [[5, 20], [11, 20], [13, 20], [11, 20], [13, 20], [11, 20], [13, 20], [11, 20], [13, 20]]
assert center_args == branch + branch, center
camera = SPEC["contracts"]["stage.center"]["camera"]
map_width = camera["s57MapTiles"][0] * camera["tileSize"]
viewport_width = camera["sourceTraceViewport"][0]
limit = (map_width - viewport_width) / 2
for key, expected in camera["s57ExpectedPositions"].items():
    x, y = map(int, key.split(","))
    actual = [max(-limit, min(limit, map_width / 2 - x * 96)), y * 96 - map_width / 2]
    assert all(abs(a - b) < 1e-6 for a, b in zip(actual, expected)), (key, actual, expected)

max_round = []
for name in ("S_11.py", "S_38.py", "S_44.py"):
    max_round.extend((name, line, args) for line, args in calls(PY / name, "setMaxRound"))
assert len(max_round) == SPEC["contracts"]["stage.setMaxRound"]["callCount"]
assert [(name, args) for name, _, args in max_round] == [
    ("S_11.py", [12]), ("S_38.py", [30]), ("S_38.py", [30]), ("S_44.py", [15])
]

levels = calls(PY / "S_04.py", "addLv")
assert len(levels) == SPEC["contracts"]["stage.unit.addLv"]["callCount"]
source = (PY / "S_04.py").read_text(encoding="utf-8")
level_ids = [int(value) for value in re.findall(r"stage\.unit\((\d+)\)\.addLv\(1\)", source)]
assert level_ids == list(range(474, 481)) + list(range(483, 490)) + list(range(492, 499))

battle_layer = (JS / "battle/BattleLayer.js").read_text(encoding="utf-8")
battle_unit = (JS / "battle/BattleUnit.js").read_text(encoding="utf-8")
unit = (JS / "game-data/Unit.js").read_text(encoding="utf-8")
assert "this._scrollView.content.position = c;\nthis.dispatchEvent(\"MAP_SCROLLING\", c);" in battle_layer
assert "this.eFlag() & N.ENABLED_FEATURE.ZJHH && (t += 4);\nthis.setProperty(N.BATTLE_ATTR_INDEX.MAX_ROUND, t);" in battle_layer
assert "e.prototype.addLv = function(t) {\nthis._unit.addLv(t);\n};" in battle_unit
assert "t.prototype.addLv = function(t) {\nthis.setLevel(this.lv() + t);\n};" in unit

print("BATTLE_STAGE_IMMEDIATE_CONTRACTS_OK center=18 maxRound=4 addLv=21")
