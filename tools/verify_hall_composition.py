#!/usr/bin/env python3
"""Compare the live source Hall node tree with the game renderer frame log."""

import json
import math
import sys


def close(actual, expected, label, tolerance=0.15):
    if len(actual) != len(expected) or any(abs(a - e) > tolerance for a, e in zip(actual, expected)):
        raise AssertionError(f"{label}: actual={actual}, expected={expected}")


def rect_from_center(center, size):
    return [center[0] - size[0] / 2, center[1] - size[1] / 2, size[0], size[1]]


if len(sys.argv) != 3:
    raise SystemExit("usage: verify_hall_composition.py SOURCE_FIXTURE.json GAME_TRACE.json")

source = json.load(open(sys.argv[1], encoding="utf-8"))
game = json.load(open(sys.argv[2], encoding="utf-8"))
snapshot = source["snapshot"]
scale = game["viewport"][1] / snapshot["visibleSize"]["height"]
close(game["viewport"], [snapshot["visibleSize"]["width"] * scale, snapshot["visibleSize"]["height"] * scale], "viewport")

nodes = snapshot["nodes"]
map_node = next(node for node in nodes if node.get("path") == "Canvas/Layer/map")
canvas_center = [snapshot["visibleSize"]["width"] / 2, snapshot["visibleSize"]["height"] / 2]

game_units = {unit["id"]: unit for unit in game["units"]}
source_units = [node for node in nodes if isinstance(node.get("hallUnit"), dict)]
for node in source_units:
    unit = node["hallUnit"]
    candidate = game_units[unit["id"]]
    if candidate["direction"] != unit["dir"] or candidate["action"] != unit["action"]:
        raise AssertionError(f"unit {unit['id']} state differs: {candidate} vs {unit}")
    center = [
        (canvas_center[0] + node["position"][0] * map_node["scale"][0]) * scale,
        (canvas_center[1] + node["position"][1] * map_node["scale"][1]) * scale,
    ]
    size = [node["size"][0] * map_node["scale"][0] * scale, node["size"][1] * map_node["scale"][1] * scale]
    close(candidate["rect"], rect_from_center(center, size), f"unit {unit['id']} rect")

source_heads = [node for node in nodes if node.get("path") == "Canvas/Layer/map/head"]
game_heads = sorted(game["heads"], key=lambda head: head["rect"][0])
expected_heads = []
for head in source_heads:
    # Head.prefab's 64x80 parent is a Mask around its larger 80x100 face.
    # The visible submission rectangle is therefore the parent bounds.
    center = [
        (canvas_center[0] + (head["position"][0] + head["size"][0] / 2) * map_node["scale"][0]) * scale,
        (canvas_center[1] + (head["position"][1] - head["size"][1] / 2) * map_node["scale"][1]) * scale,
    ]
    size = [head["size"][0] * map_node["scale"][0] * scale, head["size"][1] * map_node["scale"][1] * scale]
    expected_heads.append(rect_from_center(center, size))
if len(game_heads) != len(expected_heads):
    raise AssertionError(f"head membership differs: game={len(game_heads)}, source={len(expected_heads)}")
for index, (candidate, expected) in enumerate(zip(game_heads, sorted(expected_heads))):
    close(candidate["rect"], expected, f"head {index} rect")

bg = next(node for node in nodes if node.get("path") == "Canvas/Layer/bg0")
panel = next(node for node in nodes if node.get("path") == "Canvas/Layer/bg0/bg2")
face = next(node for node in nodes if node.get("path") == "Canvas/Layer/bg0/face")
dialogue = game["dialogue"]
panel_center = [(canvas_center[0] + bg["position"][0] + panel["position"][0]) * scale,
                (canvas_center[1] + bg["position"][1] + panel["position"][1]) * scale]
close(dialogue["panelRect"], rect_from_center(panel_center, [panel["size"][0] * scale, panel["size"][1] * scale]), "dialogue panel")
face_center = [(canvas_center[0] + bg["position"][0] + face["position"][0]) * scale,
               (canvas_center[1] + bg["position"][1] + face["position"][1]) * scale]
face_size = [face["size"][0] * face["scale"][0] * scale, face["size"][1] * face["scale"][1] * scale]
close(dialogue["faceRect"], rect_from_center(face_center, face_size), "dialogue face")
rich = next(node for node in nodes if node.get("path") == "Canvas/Layer/bg0/bg2/richtext")
rich_left = (canvas_center[0] + bg["position"][0] + panel["position"][0] + rich["position"][0]) * scale
close(dialogue["textBaseline"][:1], [rich_left], "dialogue text left edge")

fixture = source["fixture"]
scene_type, scene_index = fixture["background"]
expected_background_id = scene_index + 41 if scene_type == 2 else (scene_index + 1 if scene_type == 0 else 115)
if game["backgroundId"] != expected_background_id or sorted(game_units) != sorted(unit[0] for unit in fixture["units"]):
    raise AssertionError("background or Hall unit membership differs")
source_text = rich["richTextComponents"][0]["string"]
speaker_label = next(node for node in nodes if node.get("path") == "Canvas/Layer/bg0/label")
source_speaker = speaker_label["labels"][0]
if dialogue["text"] != source_text or dialogue["speakerId"] != 0 or source_speaker != "조조":
    raise AssertionError("dialogue semantic content differs")

print(f"HALL_COMPOSITION_OK scale={scale:.2f} units={len(game_units)} heads={len(game_heads)} panel=exact face=exact")
