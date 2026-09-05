#!/usr/bin/env python3
"""Export Cocos audio clips under the names used by the original UIFrame."""

import json
import shutil
import sys
from pathlib import Path


def main(source_root: Path, destination: Path) -> None:
    destination.mkdir(parents=True, exist_ok=True)
    clips = 0
    for metadata in source_root.glob("Game/import/*/*.json"):
        try:
            payload = metadata.read_text(encoding="utf-8")
        except UnicodeDecodeError:
            continue
        if "cc.AudioClip" not in payload:
            continue
        try:
            data = json.loads(payload)
            name = next(value[1] for value in data[5] if isinstance(value, list) and len(value) > 1 and isinstance(value[1], str))
        except (IndexError, KeyError, StopIteration, TypeError, ValueError):
            continue
        if not (name.startswith("Se") or "AudioTrack" in name):
            continue
        uuid = metadata.name.split(".", 1)[0]
        native = next(source_root.glob(f"Game/native/{uuid[:2]}/{uuid}.*.mp3"), None)
        if native is None:
            continue
        target = destination / f"{name}.mp3"
        shutil.copy2(native, target)
        clips += 1
    print(f"Exported {clips} original audio clips to {destination}")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        raise SystemExit("usage: export_audio_assets.py <cocos-assets> <destination>")
    main(Path(sys.argv[1]), Path(sys.argv[2]))
