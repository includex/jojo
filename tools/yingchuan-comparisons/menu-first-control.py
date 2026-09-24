"""Compare the first round-two menu interaction captured through real pointer input.

Usage: python3 menu-first-control.py <source screens.json> <port yingchuan-walkthrough.json>
"""

import json
import sys
from pathlib import Path


def load(path):
    file = Path(path)
    return json.loads(file.read_text()), file.parent


source, source_dir = load(sys.argv[1])
port, port_dir = load(sys.argv[2])
source_by_key = {row["label"]: row for row in source["captures"]}
port_by_key = {row["key"]: row for row in port["captures"]}
stages = (
    ("menu-open", "menu-first-control-open", True, False),
    ("terrain-open", "menu-first-control-terrain", False, True),
    ("terrain-closed", "menu-first-control-returned", False, False),
)
checks = {
    "source_complete": source.get("complete") is True and source.get("failure") is None,
    "port_complete": port.get("menuFirstControlComplete") is True
    and port.get("menuFirstControlFailure") is None,
    "source_pointer_inputs": [row["label"] for row in source.get("inputs", [])]
    == ["open-battle-menu", "open-terrain", "close-terrain"],
    "source_driver_handoff": source.get("automaticDriverHandoff", {}).get("installed") is True,
    "source_player_input_ready": "player-input-ready" in source_by_key,
    "port_three_captures": len(port_by_key) == 3,
}

for source_key, port_key, menu, terrain in stages:
    s = source_by_key.get(source_key, {})
    p = port_by_key.get(port_key, {})
    actor = next((u for u in p.get("units", []) if u.get("characterId") == 0), None)
    checks[source_key] = (
        s.get("round") == p.get("round") == 2
        and s.get("camp") == 0
        and p.get("turnPhase") == "PLAYER_INPUT"
        and s.get("menu") is p.get("battleMenuOpen") is menu
        and s.get("terrain") is p.get("terrainOpen") is terrain
        and s.get("selectedUnit") is None
        and p.get("selectedUnitId") is None
        and s.get("unit0") == {"x": 10, "y": 5, "acted": False}
        and actor is not None
        and (actor["x"], actor["y"], actor["hasActed"]) == (10, 5, False)
        and (source_dir / s.get("file", "")).is_file()
        and (port_dir / p.get("file", "")).is_file()
    )

result = {"checks": checks, "allPass": all(checks.values())}
print(json.dumps(result, ensure_ascii=False, indent=2))
sys.exit(0 if result["allPass"] else 1)
