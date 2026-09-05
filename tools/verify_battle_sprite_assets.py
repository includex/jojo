#!/usr/bin/env python3
"""Verify every exported BattleUnit atlas against Cocos source assets.

This is intentionally a source-to-game resource verifier, not a visual
approximation: paths are decoded from Cocos config, bytes are compared, then
every authored animeBR SpriteFrame index is checked against each atlas using
the exact CreateAnime overflow rule.
"""
from __future__ import annotations

import hashlib
import json
import re
import sys
from pathlib import Path

from PIL import Image

HEX = "0123456789abcdef"
BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
TEMPLATE = list("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
POSITIONS = [index for index, char in enumerate(TEMPLATE) if char == "x"]
ATLAS = {0: ("atk", 64), 1: ("mov", 48), 2: ("spc", 48)}


def decode_uuid(value: str) -> str:
    if len(value) != 22:
        return value
    chars = list(TEMPLATE)
    chars[0], chars[1] = value[0], value[1]
    write = 2
    for read in range(2, 22, 2):
        left, right = BASE64.index(value[read]), BASE64.index(value[read + 1])
        for nibble in (left >> 2, ((left & 3) << 2) | (right >> 4), right & 15):
            chars[POSITIONS[write]] = HEX[nibble]
            write += 1
    return "".join(chars)


def sha1(path: Path) -> str:
    return hashlib.sha1(path.read_bytes()).hexdigest()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: verify_battle_sprite_assets.py <cocos-assets> <exported-maps-dir>")
    assets, exported = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game" / "config.54cec.json").read_text())
    native_versions = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    source_atlases: list[tuple[Path, Path]] = []
    for path_index, entry in config["paths"].items():
        match = re.fullmatch(r"Unit_(atk|mov|spc)(2?)/(\d+)", entry[0])
        if not match:
            continue
        index = int(path_index)
        asset_hash = native_versions.get(index)
        if asset_hash is None:
            continue
        uuid = decode_uuid(config["uuids"][index])
        candidates = list((assets / "Game" / "native" / uuid[:2]).glob(f"{uuid}.{asset_hash}.*"))
        if len(candidates) != 1:
            raise AssertionError(f"missing/ambiguous Cocos atlas for {entry[0]}")
        kind, variant, identifier = match.groups()
        source_atlases.append((candidates[0], exported / "units" / f"{kind}{variant}" / f"{identifier}{candidates[0].suffix.lower()}"))

    missing = [str(game) for _, game in source_atlases if not game.is_file()]
    changed = [(str(source), str(game)) for source, game in source_atlases if game.is_file() and sha1(source) != sha1(game)]
    if missing or changed:
        raise AssertionError(f"atlas export mismatch: missing={len(missing)} changed={len(changed)}")

    # Battle.fire serializes these independently of the Unit_* atlases.
    # Verify the cursor plus every BattleScreen.areas entry as byte-exact
    # source assets, since the rendering path depends on their index order.
    selection_sources = {
        "cursor": assets / "resources" / "native" / "1c" / "1c7024e3-5858-4465-b00b-1722c8905a4c.391ef.png",
        "range-red": assets / "resources" / "native" / "cb" / "cb6ab8a1-3d46-41c6-97da-e8cce7ad6efa.de1ce.png",
        "range-green": assets / "resources" / "native" / "a2" / "a294fe3c-c3f1-4ee9-99cf-8038813f3827.c2fd4.png",
        "range-blue": assets / "resources" / "native" / "25" / "250a6266-245c-4854-96fe-18875e1e8641.159c1.png",
        "range-red-box": assets / "resources" / "native" / "74" / "74d85b9d-5c4c-4052-902b-ca8587d15f5e.77a56.png",
        "range-green-box": assets / "resources" / "native" / "db" / "db50b8c2-384b-4e27-9fc1-063a06c6a4ae.1765b.png",
    }
    selection_mismatch = [
        name for name, source in selection_sources.items()
        if not source.is_file() or not (exported / "selection" / f"{name}.png").is_file()
        or sha1(source) != sha1(exported / "selection" / f"{name}.png")
    ]
    if selection_mismatch:
        raise AssertionError(f"battle selection export mismatch: {selection_mismatch}")

    clips = json.loads((exported / "battle-anime.json").read_text())
    references: dict[str, list[int]] = {kind: [] for kind, _ in ATLAS.values()}
    for frames in clips.values():
        for frame in frames:
            sprite = frame.get("sprite")
            if sprite and "idx" in sprite:
                references[ATLAS[sprite["t"]][0]].append(sprite["idx"])

    overflow = 0
    checked = 0
    for source, game in source_atlases:
        kind = game.parent.name.removesuffix("2")
        _, cell_height = next(value for value in ATLAS.values() if value[0] == kind)
        with Image.open(game) as image:
            checked += 1
            for index in references[kind]:
                row = index * (cell_height + 2) + 1
                if row + cell_height > image.height:
                    overflow += 1
    print(
        "BATTLE_SPRITE_ASSETS_OK "
        f"atlases={checked} source_bytes_equal={len(source_atlases)} "
        f"clips={len(clips)} references={sum(map(len, references.values()))} "
        f"create_anime_row_fallbacks={overflow} selection_frames={len(selection_sources)}"
    )


if __name__ == "__main__":
    main()
