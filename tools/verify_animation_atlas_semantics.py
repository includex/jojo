#!/usr/bin/env python3
"""Guard the source-to-game frame-stride/rate contracts used by battle visuals."""

from __future__ import annotations

import argparse
import re
from pathlib import Path


def require(text: str, needle: str, label: str) -> None:
    if needle not in text:
        raise AssertionError(f"missing {label}: {needle}")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "source",
        nargs="?",
        type=Path,
        default=Path("/Users/ain/workspace/jojo_mobile/sgccz-desktop/recovered-js/modules"),
    )
    parser.add_argument("--repo", type=Path, default=Path(__file__).resolve().parents[1])
    args = parser.parse_args()

    javascript = {path: path.read_text(encoding="utf-8") for path in args.source.rglob("*.js")}
    create2_calls = [
        (path, line)
        for path, body in javascript.items()
        for line in body.splitlines()
        if "CreateAnime2(" in line
    ]
    create_calls = [
        (path, line)
        for path, body in javascript.items()
        for line in body.splitlines()
        if re.search(r"\.CreateAnime\(", line)
    ]
    direct_clips = sum(body.count("AnimationClip.createWithSpriteFrames") for body in javascript.values())

    assert len(create2_calls) == 1, f"CreateAnime2 call count changed: {len(create2_calls)}"
    assert "CreateAnime2(i, e.ret, 48, 8, 0, a[t], o[t])" in create2_calls[0][1]
    assert len(create_calls) == 6, f"CreateAnime call count changed: {len(create_calls)}"
    assert direct_clips == 4, f"direct SpriteFrame clip count changed: {direct_clips}"

    ui_frame = javascript[args.source / "framework" / "UIFrame.js"]
    require(ui_frame, "(h + a) * (r + i)", "CreateAnime2 row stride")
    require(ui_frame, "u += 1 / 24 * n", "CreateAnime2 frame hold")
    require(ui_frame, "C * (w + 2 * i) + i", "CreateAnime padded row stride")
    require(ui_frame, "R += 1 / 24 * H", "CreateAnime authored tick duration")

    battle_layer = (args.repo / "core/src/main/kotlin/com/jojo/game/BattleScreen.kt").read_text()
    object_timeline = (args.repo / "core/src/main/kotlin/com/jojo/game/BattleObjectAnimationTimeline.kt").read_text()
    battle_timeline = (args.repo / "core/src/main/kotlin/com/jojo/game/BattleSpriteTimeline.kt").read_text()
    hall_timeline = (args.repo / "core/src/main/kotlin/com/jojo/game/HallUnitRender.kt").read_text()
    magic = (args.repo / "core/src/main/kotlin/com/jojo/game/MagicEffectCatalog.kt").read_text()
    state = (args.repo / "core/src/main/kotlin/com/jojo/game/BattleUnitStateAnimation.kt").read_text()
    menu = (args.repo / "core/src/main/kotlin/com/jojo/game/MenuLayer.kt").read_text()

    require(object_timeline, "const val FRAME_SIZE = 48", "object frame size")
    require(object_timeline, "const val FRAME_TICKS = 8", "object frame hold")
    require(object_timeline, "row * FRAME_SIZE", "object unpadded stride")
    require(battle_layer, "0 -> 0 to 4", "object 0 start/count")
    require(battle_layer, "1 -> 4 to 2", "object 1 start/count")
    require(battle_layer, "else -> 6 to 2", "object 2 start/count")
    require(battle_layer, "::mapObjectAnimationClock", "independent visual clock")

    require(battle_timeline, "selectedIndex * (selectedAtlas.height + inset * 2) + inset", "CreateAnime padded stride")
    require(battle_timeline, "* 24f).toInt()", "CreateAnime 24fps ticks")
    require(hall_timeline, "elapsedSeconds / .125f", "HallUnit three-tick hold")
    require(magic, "if (uses24Fps) 36f else 18f", "Meff 12/24fps times 1.5")
    require(state, "framesPerSecond: Int = 3", "status 3fps")
    require(menu, "const val WEATHER_FPS = 6f", "weather 6fps")
    require(menu, "const val WEATHER_FRAME_COUNT = 4", "weather four-frame strip")
    require(menu, "fun weatherFrameAt", "weather frame mapper")

    print(
        "ANIMATION_ATLAS_SEMANTICS_OK "
        f"CreateAnime2={len(create2_calls)} CreateAnime={len(create_calls)} "
        f"directClips={direct_clips} unresolved=0"
    )


if __name__ == "__main__":
    main()
