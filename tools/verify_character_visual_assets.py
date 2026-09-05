#!/usr/bin/env python3
"""Verify Cocos portrait, HallUnit, and gate visual assets byte-for-byte."""
from __future__ import annotations

import glob
import hashlib
import json
import re
import sys
from pathlib import Path

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


def checksum(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_character_visual_assets.py <cocos-assets> <port-assets>")
    assets, exported = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game/config.54cec.json").read_text())
    native_versions = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    rules = [
        (re.compile(r"Pmapobj2/(\d+)"), "hall-units"),
        (re.compile(r"Head/(\d+)"), "heads"),
        (re.compile(r"Gate/Gate_(\d+)-1"), "gates"),
    ]
    counts = {directory: 0 for _, directory in rules}
    seen: set[tuple[str, str]] = set()
    for path_index, entry in config["paths"].items():
        matched = next(((rule.search(entry[0]), directory) for rule, directory in rules if rule.fullmatch(entry[0])), None)
        if matched is None:
            continue
        match, directory = matched
        index = int(path_index)
        version = native_versions.get(index)
        if version is None:
            continue
        uuid = decode_uuid(config["uuids"][index])
        candidates = [Path(value) for value in glob.glob(str(assets / "Game/native" / uuid[:2] / f"{uuid}.{version}.*"))]
        if len(candidates) != 1:
            raise AssertionError(f"source visual path cannot be resolved: {entry[0]}")
        source = candidates[0]
        target = exported / directory / f"{match.group(1)}{source.suffix.lower()}"
        key = (directory, target.name)
        if key in seen:
            continue
        seen.add(key)
        if not target.is_file() or checksum(source) != checksum(target):
            raise AssertionError(f"character visual differs: {entry[0]} → {target}")
        counts[directory] += 1
    print("CHARACTER_VISUAL_ASSETS_OK " + " ".join(f"{key}={value}" for key, value in counts.items()))


if __name__ == "__main__":
    main()
