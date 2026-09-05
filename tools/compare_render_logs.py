#!/usr/bin/env python3
"""Deterministic semantic comparator for original and game render logs.

Supported inputs:
  * canonical logs: {"draws": [{path, rect, asset, opacity, blend, visible, text}]}
  * renderer JSONL: one {nodePath, drawType, x, y, w, h, assetFrameId, ...} per line
  * original fixture snapshots: {"snapshot": {"nodes": [...]}}
  * LibGDX composition traces: {viewport, backgroundId, units, ...}

Geometry is normalized by each log's viewport, so Cocos' 1488x800 and the
game's 1280x688 describe the same coordinate space.  No image comparison is
performed.  Exit status is 0 only when all semantic fields and draw order
match; 1 means a render difference and 2 means invalid input/arguments.
"""
from __future__ import annotations

import argparse
import json
import math
import re
import sys
from dataclasses import dataclass, asdict
from pathlib import Path
from typing import Any, Iterable


TIMING_KEYS = frozenset({
    "time", "timestamp", "elapsed", "elapsedms", "deltatime", "delta",
    "frametime", "fps", "duration", "capturedat", "generatedat",
})
# `phase` and `layer` remain in the diagnostic record, but are not pixels or
# draw state. The Cocos owner can legitimately be HallLayer while the game
# delegates the identical draw to DialogueLayer. Compare the emitted output,
# not implementation ownership.
SEMANTIC_FIELDS = ("draw_type", "rect", "asset", "opacity", "blend", "visible", "text")


@dataclass(frozen=True)
class Draw:
    path: str
    occurrence: int
    phase: Any = None
    layer: Any = None
    draw_type: Any = None
    rect: tuple[float, ...] | None = None
    asset: Any = None
    opacity: Any = None
    blend: Any = None
    visible: Any = None
    text: Any = None

    @property
    def key(self) -> str:
        return f"{self.path}#{self.occurrence}"


def _without_timing(value: Any) -> Any:
    if isinstance(value, dict):
        return {
            key: _without_timing(child)
            for key, child in sorted(value.items())
            if re.sub(r"[^a-z]", "", key.lower()) not in TIMING_KEYS
        }
    if isinstance(value, list):
        return [_without_timing(child) for child in value]
    return value


def _viewport(value: Any, fallback: tuple[float, float] = (1.0, 1.0)) -> tuple[float, float]:
    if isinstance(value, dict):
        value = value.get("viewport") or value.get("visibleSize") or value.get("designSize")
    if isinstance(value, dict):
        value = [value.get("width"), value.get("height")]
    if isinstance(value, list) and len(value) >= 2 and all(isinstance(v, (int, float)) for v in value[:2]):
        return float(value[0]), float(value[1])
    return fallback


def _rect(rect: Any, viewport: tuple[float, float]) -> tuple[float, ...] | None:
    if not isinstance(rect, (list, tuple)) or len(rect) != 4:
        return None
    width, height = viewport
    divisors = (width, height, width, height)
    return tuple(float(value) / divisor for value, divisor in zip(rect, divisors))


def _append(draws: list[Draw], counts: dict[str, int], path: str, **fields: Any) -> None:
    occurrence = counts.get(path, 0)
    counts[path] = occurrence + 1
    draws.append(Draw(path=path, occurrence=occurrence, **fields))


def _canonical(data: dict[str, Any]) -> list[Draw]:
    viewport = _viewport(data)
    draws: list[Draw] = []
    counts: dict[str, int] = {}
    for index, raw in enumerate(data.get("draws", [])):
        if not isinstance(raw, dict):
            raise ValueError(f"draws[{index}] must be an object")
        path = str(raw.get("path", f"draw/{index}"))
        _append(draws, counts, path,
                phase=_without_timing(raw.get("phase")),
                layer=_without_timing(raw.get("layer")),
                draw_type=_without_timing(raw.get("drawType") or raw.get("type")),
                rect=_rect(raw.get("rect") or raw.get("geometry"), viewport),
                asset=_without_timing(raw.get("asset")),
                opacity=_without_timing(raw.get("opacity")),
                blend=_without_timing(raw.get("blend")),
                visible=_without_timing(raw.get("visible")),
                text=_without_timing(raw.get("text")))
    return draws


def _cocos(data: dict[str, Any]) -> list[Draw]:
    snapshot = data["snapshot"]
    viewport = _viewport(snapshot)
    draws: list[Draw] = []
    counts: dict[str, int] = {}
    for node in snapshot.get("nodes", []):
        sprite = node.get("sprite") if isinstance(node.get("sprite"), dict) else None
        labels = node.get("labels") or []
        rich = [entry.get("string") for entry in node.get("richTextComponents", []) if isinstance(entry, dict)]
        if not sprite and not labels and not rich:
            continue
        center, size = node.get("screen"), node.get("size")
        anchor, scale = node.get("anchor", [0.5, 0.5]), node.get("scale", [1, 1])
        rect = None
        if all(isinstance(v, list) and len(v) >= 2 for v in (center, size, anchor, scale)):
            w, h = abs(size[0] * scale[0]), abs(size[1] * scale[1])
            rect = _rect([center[0] - w * anchor[0], center[1] - h * anchor[1], w, h], viewport)
        asset = None
        blend = None
        if sprite:
            asset = {
                "texture": sprite.get("nativeUrl") or sprite.get("texture"),
                "frame": sprite.get("rect"),
                "rotated": sprite.get("rotated", False),
                "flipX": sprite.get("flipX", False),
                "flipY": sprite.get("flipY", False),
            }
            material = sprite.get("material")
            if material:
                blend = {"material": material}
        text: Any = labels + rich
        if len(text) == 1:
            text = text[0]
        elif not text:
            text = None
        _append(draws, counts, str(node.get("path") or node.get("name") or "node"),
                draw_type="sprite" if sprite else ("rich-text" if rich else "label"),
                rect=rect, asset=_without_timing(asset), opacity=node.get("opacity"),
                blend=_without_timing(blend), visible=node.get("visible", True), text=text)
    return draws


def _composition(data: dict[str, Any]) -> list[Draw]:
    viewport = _viewport(data)
    draws: list[Draw] = []
    counts: dict[str, int] = {}
    if data.get("backgroundId") is not None:
        _append(draws, counts, "background", asset=f"background/{data['backgroundId']}", visible=True)
    for unit in data.get("units", []):
        _append(draws, counts, f"units/{unit.get('id')}", rect=_rect(unit.get("rect"), viewport),
                asset=f"unit/{unit.get('asset')}", opacity=unit.get("opacity", 255),
                blend=unit.get("blend", "alpha"), visible=unit.get("visible", True),
                text=None)
    for index, head in enumerate(data.get("heads", [])):
        _append(draws, counts, f"heads/{index}", rect=_rect(head.get("rect"), viewport),
                asset=head.get("asset") or head.get("id"), opacity=head.get("opacity", 255),
                blend=head.get("blend", "alpha"), visible=head.get("visible", True))
    dialogue = data.get("dialogue")
    if isinstance(dialogue, dict):
        for name, rect_key, asset_key in (("panel", "panelRect", "panelAsset"), ("face", "faceRect", "faceAsset")):
            if dialogue.get(rect_key) is not None:
                _append(draws, counts, f"dialogue/{name}", rect=_rect(dialogue[rect_key], viewport),
                        asset=dialogue.get(asset_key), opacity=dialogue.get(f"{name}Opacity", 255),
                        blend=dialogue.get(f"{name}Blend", "alpha"), visible=True)
        _append(draws, counts, "dialogue/text", text=dialogue.get("text"), visible=True)
        if dialogue.get("speaker") is not None or dialogue.get("speakerId") is not None:
            _append(draws, counts, "dialogue/speaker", text=dialogue.get("speaker") or dialogue.get("speakerId"), visible=True)
    for section in ("modal", "hallCommand", "hallMenu", "hallManagement", "hallInfo"):
        value = data.get(section)
        if not isinstance(value, dict):
            continue
        # Dict iteration is insertion ordered in the serialized trace and is
        # therefore the renderer's declared draw order.
        for key, child in value.items():
            if key.endswith("Rect"):
                _append(draws, counts, f"{section}/{key[:-4]}", rect=_rect(child, viewport), visible=True)
            elif key.endswith("Rects") and isinstance(child, list):
                for index, child_rect in enumerate(child):
                    _append(draws, counts, f"{section}/{key[:-5]}/{index}", rect=_rect(child_rect, viewport), visible=True)
            elif key.lower() in {"text", "title", "speaker", "label"}:
                _append(draws, counts, f"{section}/{key}", text=child, visible=True)
    return draws


def adapt(data: Any) -> tuple[str, list[Draw]]:
    if not isinstance(data, dict):
        raise ValueError("top-level JSON must be an object")
    if isinstance(data.get("draws"), list):
        return "canonical", _canonical(data)
    if isinstance(data.get("snapshot"), dict) and isinstance(data["snapshot"].get("nodes"), list):
        return "cocos-snapshot", _cocos(data)
    if "viewport" in data and any(key in data for key in ("units", "backgroundId", "dialogue", "hallCommand")):
        return "game-composition", _composition(data)
    raise ValueError("unrecognized render log format")


def load_input(path: Path) -> Any:
    """Read either a JSON document or the renderer JSONL event stream."""
    source = path.read_text(encoding="utf-8")
    try:
        document = json.loads(source)
        # A one-record JSONL stream is also a valid JSON document. Detect it
        # by schema so it does not fall through to the document adapters.
        if isinstance(document, dict) and ("nodePath" in document or all(key in document for key in ("x", "y", "w", "h"))):
            events = [document]
        else:
            return document
    except json.JSONDecodeError as document_error:
        events: list[dict[str, Any]] = []
        try:
            for line_number, line in enumerate(source.splitlines(), 1):
                if not line.strip():
                    continue
                event = json.loads(line)
                if not isinstance(event, dict):
                    raise ValueError(f"JSONL line {line_number} must be an object")
                events.append(event)
        except (json.JSONDecodeError, ValueError) as line_error:
            raise ValueError(f"invalid JSON document ({document_error}) and JSONL ({line_error})") from line_error
        # A stable scene can legitimately submit no visible draws (the source
        # End scene ignores keyboard-back phase 5). Treat two empty JSONL
        # streams as exact empty canonical logs instead of making that actual
        # renderer state impossible to verify.
        if not events:
            return {"viewport": [1280.0, 688.0], "draws": []}
    # Both instrumented runtimes use renderer coordinates in their declared
    # viewport. Timing fields remain outside `draws` and are intentionally
    # excluded from semantic comparison.
    root_sizes = [
        (float(event["w"]), float(event["h"]))
        for event in events
        if event.get("x") == 0 and event.get("y") == 0
        and isinstance(event.get("w"), (int, float)) and isinstance(event.get("h"), (int, float))
    ]
    viewport = max(root_sizes, key=lambda size: size[0] * size[1]) if root_sizes else (1280.0, 688.0)
    return {
        "viewport": list(viewport),
        "draws": [
            {
                "path": event.get("nodePath") or event.get("path"),
                "phase": event.get("phase"),
                "layer": event.get("layer"),
                "drawType": event.get("drawType") or event.get("type"),
                "rect": [event.get("x"), event.get("y"), event.get("w"), event.get("h")],
                "asset": _renderer_asset(event),
                "opacity": event.get("opacity"),
                "blend": event.get("blend"),
                "visible": event.get("visible"),
                "text": event.get("text"),
            }
            for event in events
        ],
    }


def _renderer_asset(event: dict[str, Any]) -> Any:
    """Keep atlas crop/mirroring in the semantic frame identity when logged.

    A texture URL or SpriteFrame hash is not sufficient for Cocos animation
    frames: multiple directions and animation ticks share the same atlas and
    runtime SpriteFrame name.  Older logs remain compatible until both
    renderers opt into the extended fields.
    """
    identity = event.get("assetId") if event.get("assetId") is not None else event.get("assetFrameId")
    source_rect = event.get("sourceRect")
    flip_x = event.get("flipX")
    flip_y = event.get("flipY")
    if source_rect is None and flip_x is None and flip_y is None:
        return identity
    return _without_timing({
        "id": identity,
        "sourceRect": source_rect,
        "flipX": flip_x,
        "flipY": flip_y,
    })


def _equal(expected: Any, actual: Any, tolerance: float) -> bool:
    if isinstance(expected, (int, float)) and isinstance(actual, (int, float)):
        return math.isclose(float(expected), float(actual), rel_tol=0.0, abs_tol=tolerance)
    if type(expected) is not type(actual):
        return False
    if isinstance(expected, (list, tuple)):
        return len(expected) == len(actual) and all(_equal(a, b, tolerance) for a, b in zip(expected, actual))
    if isinstance(expected, dict):
        return expected.keys() == actual.keys() and all(_equal(expected[k], actual[k], tolerance) for k in expected)
    return expected == actual


def compare(expected: list[Draw], actual: list[Draw], tolerance: float) -> list[dict[str, Any]]:
    diffs: list[dict[str, Any]] = []
    expected_order, actual_order = [d.key for d in expected], [d.key for d in actual]
    if expected_order != actual_order:
        mismatch = next((i for i, pair in enumerate(zip(expected_order, actual_order)) if pair[0] != pair[1]), min(len(expected_order), len(actual_order)))
        diffs.append({"kind": "draw-order", "index": mismatch,
                      "expected": expected_order[mismatch:mismatch + 5], "actual": actual_order[mismatch:mismatch + 5]})
    expected_by_key, actual_by_key = {d.key: d for d in expected}, {d.key: d for d in actual}
    for key in sorted(expected_by_key.keys() - actual_by_key.keys()):
        diffs.append({"kind": "missing-draw", "path": key, "expected": asdict(expected_by_key[key])})
    for key in sorted(actual_by_key.keys() - expected_by_key.keys()):
        diffs.append({"kind": "unexpected-draw", "path": key, "actual": asdict(actual_by_key[key])})
    for key in sorted(expected_by_key.keys() & actual_by_key.keys()):
        left, right = expected_by_key[key], actual_by_key[key]
        for field in SEMANTIC_FIELDS:
            expected_value, actual_value = getattr(left, field), getattr(right, field)
            if not _equal(expected_value, actual_value, tolerance):
                diffs.append({"kind": "field", "path": key, "field": field,
                              "expected": expected_value, "actual": actual_value})
    return diffs


def text_report(report: dict[str, Any]) -> str:
    status = "PASS" if report["equal"] else "FAIL"
    lines = [f"RENDER_LOG_COMPARE_{status} expected={report['expectedFormat']} actual={report['actualFormat']} diffs={report['differenceCount']}"]
    for diff in report["differences"]:
        where = f" {diff.get('path', '')}".rstrip()
        field = f".{diff['field']}" if "field" in diff else ""
        lines.append(f"- {diff['kind']}{where}{field}: expected={diff.get('expected')!r} actual={diff.get('actual')!r}")
    return "\n".join(lines)


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("expected", type=Path, help="original/canonical expected JSON log")
    parser.add_argument("actual", type=Path, help="game/canonical actual JSON log")
    parser.add_argument("--float-tolerance", type=float, default=1e-5,
                        help="absolute tolerance for float serialization only (default: 1e-5)")
    parser.add_argument("--json-out", type=Path, help="write the machine-readable diff report")
    parser.add_argument("--text-out", type=Path, help="write the actionable text report")
    parser.add_argument("--max-diffs", type=int, default=200, help="maximum differences emitted")
    args = parser.parse_args(argv)
    if args.float_tolerance < 0 or args.max_diffs < 1:
        parser.error("tolerance must be nonnegative and max-diffs must be positive")
    try:
        expected_format, expected = adapt(load_input(args.expected))
        actual_format, actual = adapt(load_input(args.actual))
    except (OSError, json.JSONDecodeError, ValueError) as error:
        print(f"render log input error: {error}", file=sys.stderr)
        return 2
    all_diffs = compare(expected, actual, args.float_tolerance)
    report = {
        "equal": not all_diffs,
        "expected": str(args.expected), "actual": str(args.actual),
        "expectedFormat": expected_format, "actualFormat": actual_format,
        "floatTolerance": args.float_tolerance,
        "expectedDrawCount": len(expected), "actualDrawCount": len(actual),
        "differenceCount": len(all_diffs), "truncated": len(all_diffs) > args.max_diffs,
        "differences": all_diffs[:args.max_diffs],
    }
    machine = json.dumps(report, ensure_ascii=False, indent=2, sort_keys=True)
    human = text_report(report)
    print(human)
    if args.json_out:
        args.json_out.parent.mkdir(parents=True, exist_ok=True)
        args.json_out.write_text(machine + "\n", encoding="utf-8")
    if args.text_out:
        args.text_out.parent.mkdir(parents=True, exist_ok=True)
        args.text_out.write_text(human + "\n", encoding="utf-8")
    return 0 if report["equal"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
