#!/usr/bin/env python3
"""Prove the 28 TerrainLayer Game/Terrain SpriteFrames are copied verbatim."""
from __future__ import annotations

import glob
import hashlib
import json
import sys
from pathlib import Path

from export_map_assets import decode_uuid


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_terrain_layer_assets.py <cocos-assets> <game-assets>")
    assets, game = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game" / "config.54cec.json").read_text())
    native = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    for terrain_id in range(28):
        original: Path | None = None
        for path_index, entry in config["paths"].items():
            if entry[0] != f"Terrain/{terrain_id}":
                continue
            index = int(path_index)
            uuid = decode_uuid(config["uuids"][index])
            version = native.get(index)
            pattern = f"{uuid}.{version}.*" if version else f"{uuid}.*"
            for bundle in ("Game", "resources"):
                found = glob.glob(str(assets / bundle / "native" / uuid[:2] / pattern))
                if found:
                    original = Path(found[0])
                    break
            if original:
                break
        if original is None:
            raise SystemExit(f"missing source Game/Terrain/{terrain_id}")
        copied = game / "terrain-icons" / f"{terrain_id}.png"
        if not copied.exists() or digest(original) != digest(copied):
            raise SystemExit(f"terrain icon mismatch: {terrain_id}")
    print("TERRAIN_LAYER_ASSETS_OK count=28")


if __name__ == "__main__":
    main()
