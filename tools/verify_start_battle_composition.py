#!/usr/bin/env python3
"""Verify StartBattleLayer source nodes against the port's submitted quads."""

import json
import sys


def close(actual, expected, label, tolerance=0.16):
    if len(actual) != len(expected) or any(abs(a - e) > tolerance for a, e in zip(actual, expected)):
        raise AssertionError(f"{label}: actual={actual}, expected={expected}")


def rect(center, size):
    return [center[0] - size[0] / 2, center[1] - size[1] / 2, size[0], size[1]]


if len(sys.argv) != 3:
    raise SystemExit("usage: verify_start_battle_composition.py SOURCE.json PORT.json")
source = json.load(open(sys.argv[1], encoding="utf-8"))
port = json.load(open(sys.argv[2], encoding="utf-8"))
snapshot = source["snapshot"]
nodes = snapshot["nodes"]
scale = port["viewport"][1] / snapshot["visibleSize"]["height"]
canvas = [snapshot["visibleSize"]["width"] / 2, snapshot["visibleSize"]["height"] / 2]


def node(path):
    return next(value for value in nodes if value.get("path") == path)


bg = node("Canvas/Layer/bg")
close(port["outerRect"], rect([canvas[0] * scale, canvas[1] * scale], [bg["size"][0] * scale, bg["size"][1] * scale]), "outer")

roster = node("Canvas/Layer/bg/scrollview")
roster_center = [(canvas[0] + roster["position"][0]) * scale, (canvas[1] + roster["position"][1]) * scale]
close(port["rosterClipRect"], rect(roster_center, [roster["size"][0] * scale, roster["size"][1] * scale]), "roster clip")

selected = node("Canvas/Layer/bg/bg")
selected_center = [(canvas[0] + selected["position"][0]) * scale, (canvas[1] + selected["position"][1]) * scale]
close(port["selectedPanelRect"], rect(selected_center, [selected["size"][0] * scale, selected["size"][1] * scale]), "selected panel")

info = node("Canvas/Layer/bg/box1")
info_center = [(canvas[0] + info["position"][0]) * scale, (canvas[1] + info["position"][1]) * scale]
close(port["infoPanelRect"], rect(info_center, [info["size"][0] * scale, info["size"][1] * scale]), "info panel")

title = node("Canvas/Layer/bg/box1/bg1")
title_center = [(canvas[0] + info["position"][0] + title["position"][0]) * scale,
                (canvas[1] + info["position"][1] + title["position"][1]) * scale]
close(port["infoTitleRect"], rect(title_center, [title["size"][0] * scale, title["size"][1] * scale]), "info title")

for key, path in [("confirmRect", "Canvas/Layer/bg/button0"), ("cancelRect", "Canvas/Layer/bg/button1")]:
    button = node(path)
    center = [(canvas[0] + button["position"][0]) * scale, (canvas[1] + button["position"][1]) * scale]
    close(port[key], rect(center, [button["size"][0] * scale, button["size"][1] * scale]), key)

# Selected-row frames are repeated prefab nodes. Reconstruct their complete
# parent transform rather than comparing hand-entered screenshot coordinates.
content = node("Canvas/Layer/bg/bg/scrollview/view/content")
row_path = "Canvas/Layer/bg/bg/scrollview/view/content/node"
frame_path = row_path + "/bg"
rows = [(index, value) for index, value in enumerate(nodes) if value.get("path") == row_path]
expected_frames = []
for row_index, (start, row) in enumerate(rows[:len(port["slots"])]):
    child = next(value for value in nodes[start + 1:] if value.get("path") == frame_path)
    center = [(canvas[0] + selected["position"][0] + content["position"][0] + row["position"][0] + child["position"][0]) * scale,
              (canvas[1] + selected["position"][1] + content["position"][1] + row["position"][1] + child["position"][1]) * scale]
    size = [child["size"][0] * child["scale"][0] * scale, child["size"][1] * child["scale"][1] * scale]
    expected_frames.append(rect(center, size))
for slot, expected in zip(port["slots"], expected_frames):
    close(slot["frameRect"], expected, f"slot {slot['index']}")

if len(port["roster"]) != 4 or port["selectedUnitId"] != 0 or port["backgroundId"] != 71:
    raise AssertionError("fixture membership, cursor, or background differs")
print(f"START_BATTLE_COMPOSITION_OK scale={scale:.2f} roster={len(port['roster'])} slots={len(port['slots'])} geometry=exact")
