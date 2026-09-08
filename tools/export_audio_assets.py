#!/usr/bin/env python3
"""Export Cocos audio clips under the names used by the original UIFrame."""

import json
import shutil
import sys
from pathlib import Path


# The click and cancel sounds are not in the Game bundle and carry no readable
# name: `UIFrame.addTouchEventListener` plays whatever AudioClip is assigned to
# `Manager.clickEff`/`cancelEff` in the Welcome scene, and those are referenced
# by uuid only.  Export them under stable names the game can ask for.
UI_CLIPS = {
    "c27c9ce7-c4e4-46bb-9b8b-8467d955b907": "ui-click",
    "606eb979-280d-40de-8195-4267f0ef1007": "ui-cancel",
}


def export_ui_clips(source_root: Path, destination: Path) -> int:
    exported = 0
    for uuid, name in UI_CLIPS.items():
        native = next(source_root.glob(f"main/native/{uuid[:2]}/{uuid}.*.mp3"), None)
        if native is None:
            continue
        shutil.copy2(native, destination / f"{name}.mp3")
        exported += 1
    return exported


def main(source_root: Path, destination: Path) -> None:
    destination.mkdir(parents=True, exist_ok=True)
    clips = export_ui_clips(source_root, destination)
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
