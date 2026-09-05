#!/usr/bin/env python3
"""Verify original Cocos effect, selection, and Mark atlases used in battle rendering."""
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


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_battle_effect_assets.py <cocos-assets> <port-assets>")
    assets, exported = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game/config.54cec.json").read_text())
    native_versions = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    rules = [
        (re.compile(r"Meff/Meff_(\d+)-1"), "effects", lambda value: value),
        (re.compile(r"U_select/(U_select(?:_?\d+)?)(?:-1)?"), "select", lambda value: value.removeprefix("U_select").lstrip("_") or "0"),
        (re.compile(r"Mark/Mark_(\d+)(?:-1)?"), "marks", lambda value: value),
    ]
    counts = {directory: 0 for _, directory, _ in rules}
    seen: set[tuple[str, str]] = set()
    for path_index, entry in config["paths"].items():
        found = next(((rule.fullmatch(entry[0]), directory, name) for rule, directory, name in rules if rule.fullmatch(entry[0])), None)
        if found is None:
            continue
        match, directory, name = found
        version = native_versions.get(int(path_index))
        if version is None:
            continue
        uuid = decode_uuid(config["uuids"][int(path_index)])
        candidates = [Path(value) for value in glob.glob(str(assets / "Game/native" / uuid[:2] / f"{uuid}.{version}.*"))]
        if len(candidates) != 1:
            raise AssertionError(f"cannot resolve source effect: {entry[0]}")
        source = candidates[0]
        target = exported / directory / f"{name(match.group(1))}{source.suffix.lower()}"
        if (directory, target.name) in seen:
            continue
        seen.add((directory, target.name))
        if not target.is_file() or digest(source) != digest(target):
            raise AssertionError(f"battle effect differs: {entry[0]} → {target}")
        counts[directory] += 1
    print("BATTLE_EFFECT_ASSETS_OK " + " ".join(f"{key}={value}" for key, value in counts.items()))


if __name__ == "__main__":
    main()
