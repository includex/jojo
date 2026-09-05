#!/usr/bin/env python3
"""Prove every exported Mmap/HM visual asset comes from its Cocos source."""
from __future__ import annotations

import glob
import hashlib
import json
import re
import sys
from pathlib import Path

from PIL import Image, ImageChops

HEX = "0123456789abcdef"
BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
TEMPLATE = list("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
POSITIONS = [index for index, value in enumerate(TEMPLATE) if value == "x"]


def decode_uuid(value: str) -> str:
    if len(value) != 22:
        return value
    chars = list(TEMPLATE)
    chars[0], chars[1] = value[0], value[1]
    write = 2
    for index in range(2, 22, 2):
        left, right = BASE64.index(value[index]), BASE64.index(value[index + 1])
        for nibble in (left >> 2, ((left & 3) << 2) | (right >> 4), right & 15):
            chars[POSITIONS[write]] = HEX[nibble]
            write += 1
    return "".join(chars)


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_map_assets.py <cocos-assets> <port-assets>")
    assets, port = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game/config.54cec.json").read_text())
    native_versions = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    mmaps = battles = metadata_only = 0
    for path_index, entry in config["paths"].items():
        match = re.fullmatch(r"(Mmap/Mmap|HM/HM)_(\d+)-1", entry[0])
        if not match:
            continue
        index = int(path_index)
        asset_hash = native_versions.get(index)
        # config.54cec also contains import-only dependency duplicates for
        # maps already represented by a native visual entry.  Cocos cannot
        # render these records directly; only native-version entries name an
        # image file and are therefore part of the renderable source set.
        if asset_hash is None:
            metadata_only += 1
            continue
        uuid = decode_uuid(config["uuids"][index])
        candidates = [Path(path) for path in glob.glob(str(assets / "Game/native" / uuid[:2] / f"{uuid}.{asset_hash}.*"))]
        if len(candidates) != 1:
            raise AssertionError(f"cannot resolve source map: {entry[0]}")
        source = candidates[0]
        kind, map_id = match.groups()
        target = port / (f"{map_id}{source.suffix.lower()}" if kind.startswith("Mmap") else f"battle-maps/{map_id}{source.suffix.lower()}")
        if not target.exists() or digest(source) != digest(target):
            raise AssertionError(f"map bytes differ: {entry[0]} → {target}")
        if kind.startswith("Mmap"):
            mmaps += 1
        else:
            battles += 1
    # HM_1 additionally has a source-WebGL PNG export.  This is the map
    # texture rendered for direct Cocos/LibGDX screenshot comparison.
    source_png = assets.parent / "build/battle-map-source.png"
    target_png = port / "battle-maps/1.png"
    if not source_png.exists() or not target_png.exists():
        raise AssertionError("HM_1 source framebuffer fixture is missing")
    source_image, target_image = Image.open(source_png).convert("RGBA"), Image.open(target_png).convert("RGBA")
    if source_image.size != target_image.size or ImageChops.difference(source_image, target_image).getbbox():
        raise AssertionError("HM_1 Cocos framebuffer texture differs from port map texture")
    print(f"MAP_ASSETS_OK mmaps={mmaps} battleMaps={battles} metadataOnly={metadata_only} hm1FramebufferPixels=exact")


if __name__ == "__main__":
    main()
