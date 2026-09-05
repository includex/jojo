#!/usr/bin/env python3
"""Extract original Cocos visual assets and binary gameplay tables into stable LibGDX resources."""
from __future__ import annotations

import glob
import json
import re
import shutil
import sys
from pathlib import Path

from PIL import Image, ImageOps


def crop_cocos_frame(
    atlas: Image.Image,
    *,
    x: int,
    y: int,
    width: int,
    height: int,
    uv_top_to_bottom: bool = False,
) -> Image.Image:
    """Export one Cocos SpriteFrame into a normal top-origin PNG.

    `rect` is always Cocos/WebGL bottom-origin.  The verifier normalizes the
    framebuffer into a top-origin PNG before it is written; this conversion
    is therefore performed exactly once here.  A SpriteFrame whose UVs run
    top-to-bottom is represented by `uv_top_to_bottom`, rather than by an
    ad-hoc transpose at its call site.
    """
    frame = atlas.crop((x, atlas.height - y - height, x + width, atlas.height - y))
    return frame.transpose(Image.Transpose.FLIP_TOP_BOTTOM) if uv_top_to_bottom else frame

HEX = "0123456789abcdef"
BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
UUID_TEMPLATE = list("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
UUID_POSITIONS = [i for i, value in enumerate(UUID_TEMPLATE) if value == "x"]


def decode_uuid(value: str) -> str:
    if len(value) != 22:
        return value
    chars = list(UUID_TEMPLATE)
    chars[0], chars[1] = value[0], value[1]
    write = 2
    for read in range(2, 22, 2):
        left, right = BASE64.index(value[read]), BASE64.index(value[read + 1])
        for nibble in (left >> 2, ((left & 3) << 2) | (right >> 4), right & 15):
            chars[UUID_POSITIONS[write]] = HEX[nibble]
            write += 1
    return "".join(chars)


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: export_map_assets.py <cocos-assets> <output-dir>")
    assets, output = map(Path, sys.argv[1:])
    config = json.loads((assets / "Game" / "config.54cec.json").read_text())
    native_versions = dict(zip(config["versions"]["native"][::2], config["versions"]["native"][1::2]))
    import_versions = dict(zip(config["versions"]["import"][::2], config["versions"]["import"][1::2]))
    output.mkdir(parents=True, exist_ok=True)

    # Battle TuoGuanLayer uses standalone native textures rather than the
    # runtime UI atlas. Keep their exact transparent padding; SpriteFrame
    # geometry depends on img2's authored 1280x264 original size.
    auto_battle_dir = output / "ui" / "auto-battle"
    for source, name in (
        (assets / "resources" / "native" / "f7" / "f756dc44-3e7a-4a22-a4a1-28dd7348ce3f.50d2a.png", "img3.png"),
        (assets / "resources" / "native" / "21" / "2110e4bf-3344-42aa-b4ff-8183c4cb93f6.52abe.png", "img2.png"),
        (assets / "resources" / "native" / "73" / "73a0903d-d80e-4e3c-aa67-f999543c08f5.7661e.png", "checkmark.png"),
    ):
        if source.exists():
            auto_battle_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, auto_battle_dir / name)

    # CommandLayer's six action pictograms are independent SpriteFrames in
    # the recovered prefab (not the seven button-node UUIDs).  Each source
    # texture is an exact 16x16 native PNG; preserve it verbatim so the
    # renderer can apply the prefab's 2x node scale and SpriteFrame trim.
    command_icon_sources = {
        "command1": "f8/f8fed99a-8483-41f1-a1ab-7e9db1503cfa.267d1.png",
        "command2": "b8/b8167919-11fe-4c36-b842-6976f82a80eb.ee9c1.png",
        "command3": "ca/ca045ba8-b5dc-4646-8aaf-8624d33fbf99.771ea.png",
        "command4": "1a/1a8fe0fd-a0aa-45db-a806-662b279fc95a.9ec97.png",
        "command5": "fa/faf52ca5-8398-43c8-8b06-a5ac8409d328.4178e.png",
        "command6": "29/2994d6f2-6c97-4e36-a152-4c00ae963582.54c8d.png",
    }
    command_icon_dir = output / "ui" / "battle-command"
    for name, relative in command_icon_sources.items():
        source = assets / "resources" / "native" / relative
        if not source.exists():
            raise RuntimeError(f"CommandLayer icon source missing: {source}")
        command_icon_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, command_icon_dir / f"{name}.png")

    # Login scene. Logo_1-1 is the complete authored title backdrop (the
    # calligraphy board is part of this JPEG); the four interactive buttons
    # are independent U_select_12 frames in the live dynamic atlas.
    title_dir = output / "ui" / "title"
    title_background = assets / "resources" / "native" / "4d" / "4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg"
    if title_background.exists():
        title_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(title_background, title_dir / "background.jpg")
    # Login/Logo0 uses the Logo_2-1 SpriteFrame (rect 8,1,53x62) from this
    # otherwise tiny 64x64 source texture. End -> Login is the only live route
    # which keeps this Hall-owned emblem in front of the Login background.
    login_logo = assets / "resources" / "native" / "30" / "3002e465-e7b5-4202-b2ce-e2e054d690bf.5d91d.png"
    if login_logo.exists():
        title_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(login_logo) as atlas:
            crop_cocos_frame(atlas, x=8, y=1, width=53, height=62, uv_top_to_bottom=True).save(title_dir / "logo0.png")
    login_atlas = assets.parent / "build" / "python-source-login-fixture-texture-2.png"
    if login_atlas.exists():
        with Image.open(login_atlas) as atlas:
            title_dir.mkdir(parents=True, exist_ok=True)
            for index, y in enumerate((2, 46, 90, 134)):
                crop_cocos_frame(atlas, x=2, y=y, width=176, height=44, uv_top_to_bottom=True).save(title_dir / f"button{index}.png")

    load_atlas = assets.parent / "build" / "python-source-login-load-fixture-texture-1.png"
    if load_atlas.exists():
        load_dir = title_dir / "load"
        load_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(load_atlas) as atlas:
            for name, x, y, width, height in (
                ("logo9", 456, 2, 96, 96), ("button", 554, 2, 60, 60),
                ("title", 616, 2, 20, 20), ("box2", 638, 2, 20, 20),
                ("row", 660, 2, 20, 20), ("vline", 682, 2, 6, 40),
            ):
                crop_cocos_frame(atlas, x=x, y=y, width=width, height=height, uv_top_to_bottom=True).save(load_dir / f"{name}.png")
    load_confirm_atlas = assets.parent / "build" / "python-source-login-load-confirm-fixture-texture-1.png"
    if load_confirm_atlas.exists():
        load_dir = title_dir / "load"
        load_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(load_confirm_atlas) as atlas:
            crop_cocos_frame(atlas, x=1210, y=3, width=53, height=62, uv_top_to_bottom=True).save(load_dir / "eagle.png")

    setting_atlas = assets.parent / "build" / "python-source-login-setting-fixture-texture-1.png"
    if setting_atlas.exists():
        setting_dir = title_dir / "setting"
        setting_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(setting_atlas) as atlas:
            for name, x, y, width, height in (
                ("logo9", 993, 2, 96, 96), ("box1", 1091, 2, 20, 20),
                ("title", 1113, 2, 20, 20), ("box2", 1135, 2, 20, 20),
                ("toggle", 1157, 2, 28, 28), ("check", 1191, 7, 20, 18),
                ("radio-off", 1220, 5, 26, 26), ("radio-on", 1252, 3, 30, 30),
                ("slider", 1285, 2, 30, 15), ("box6", 1317, 2, 20, 20),
                ("style0", 993, 2, 96, 96), ("style1", 1339, 2, 96, 96),
                ("style2", 1437, 2, 96, 96), ("style3", 1535, 2, 96, 96),
                ("button", 1633, 2, 60, 60),
            ):
                crop_cocos_frame(atlas, x=x, y=y, width=width, height=height, uv_top_to_bottom=True).save(setting_dir / f"{name}.png")

    # Source Lose scene (`cc345fc5…`) uses static SpriteFrame Logo_8-1,
    # rect [0,0,640,400], whose texture UUID decodes to 21fe73fb-… .
    # Export the native JPEG unchanged: it is not a DynamicAtlas crop.
    lose_logo = assets / "resources" / "native" / "21" / "21fe73fb-bef8-411e-9656-591057b26aae.30628.jpg"
    if lose_logo.exists():
        result_dir = output / "ui" / "result"
        result_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(lose_logo, result_dir / "logo8.jpg")

    # SectionLayer shown while BattleScreen.reward is still attached.  This is
    # the authored Logo_5-1 JPEG, not a framebuffer/reference capture.
    section_logo = assets / "resources" / "native" / "59" / "5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg"
    if section_logo.exists():
        section_dir = output / "ui" / "section"
        section_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(section_logo, section_dir / "logo5.jpg")

    # HallLayer/pmapobj/img0.  DialogueLayer toggles this 24x24 Mark_10-1
    # sprite for the current street-scene speaker; HallLayer's map node then
    # applies its 2x scale.
    hall_speech_bubble = assets / "resources" / "native" / "6e" / "6e23f416-6258-4c79-9ac4-e89fc8b8df4f.9eb8d.png"
    if hall_speech_bubble.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(hall_speech_bubble, ui_dir / "street-speech-bubble.png")

    # HallMenuLayer is not an invented game menu: it is the source's complete
    # 1280x146 command strip and also the transient UI shown by addAmbition().
    # Copy every referenced SpriteFrame texture from the serialized prefab so
    # both modes can use the original art without a framebuffer dependency.
    hall_menu_sources = {
        "panel": "6a/6ad1dc5c-5ca6-4107-879a-2ea288562134.5ee7a.png",       # bg1, cap 5
        "inner": "49/49d4fa8d-b3a8-4901-9178-263c1c819482.0cf37.png",       # box1, cap 3
        "label-box": "a3/a359ebdc-3c95-4a46-a658-1c57d685fe7e.a9680.png",   # box2, cap 3
        "label-mark": "f1/f19e2c5a-8109-4cc3-8e5b-7056503a55d3.f0e88.png", # Mark_64-1
        "button": "0b/0b4d9ae9-c2e6-4212-a159-ad03f2bcd131.98199.png",      # box3
        "button-disabled": "15/1570032d-65c5-4674-9d52-adfc3aae5f7f.33bbb.png", # box4
        "bar-red": "5a/5ac523ab-4dcb-480d-8232-f013adf9fa07.80735.png",
        "bar-yellow": "cd/cdfcafe2-e041-4797-ab47-60c1f21ef181.dcf73.png",
        "bar-blue": "46/4667218c-2c7e-49bf-a6d0-40fa125caaa6.bcbb6.png",
        "flag-left": "e5/e5e23279-f72a-447f-9a17-5fcb9bb39b68.ae076.png",
        "flag-right": "03/0372e1bb-0796-47e6-b6f5-0bd523c4114f.7af91.png",
        "tool1": "36/3697e809-beb7-440d-8f67-19642f5f5f0b.c486b.png",
        "tool2": "da/dad736d7-a62e-48ea-ac9f-e80fda0902b1.6a861.png",
        "tool3": "ed/edec3d4d-fd3f-4508-963c-a55d1b4103e7.5c281.png",
        "tool4": "c1/c17609f7-0395-4ff2-bc05-1e04fb0a35ab.7fb0b.png",
        "tool5": "5d/5d7f020c-e901-41d7-a2a3-cfabaed9e4db.3ceab.png",
        "tool6": "b5/b5190d66-d6d9-46cb-a410-8e01965e3aed.718f5.png",
        "tool7": "02/020d9fac-4c8a-4b19-b51a-f2df5f5b23d8.238aa.png",
        "tool8": "e5/e5f7bece-046e-463f-976e-6779d1286749.e1584.png",
        "edit": "c8/c825fd28-53bb-43e4-b957-eef0f71fa7f3.d8050.png",
        "help": "5c/5c6f7db6-dd74-434c-b0f6-273d177e367e.d25f4.png",
    }
    hall_menu_dir = output / "ui" / "hall-menu"
    for name, relative in hall_menu_sources.items():
        source = assets / "resources" / "native" / relative
        if source.exists():
            hall_menu_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, hall_menu_dir / f"{name}.png")

    # HallCommandLayer is the persistent post-dialogue control surface.  It
    # is visible at the end of almost every R scene and must not be replaced
    # by a generic completion screen.
    hall_command_sources = {
        "battle": "41/4112821e-dda0-4482-a9d3-7bbef08dc69a.ed55a.png",
        "equip": "52/52c236a0-5c6f-494c-91d1-9d1ddd7d04e9.3b276.png",
        "buy": "a9/a9c36b60-e2f4-4d01-93f8-4ac7debdd2f5.5cbde.png",
        "sell": "5b/5b66033a-9359-4918-bd4b-553b4469bd13.0991b.png",
        "menu": "a5/a5220e95-4d43-43f6-ab3b-02b8e9948846.ecbd5.png",
    }
    hall_command_dir = output / "ui" / "hall-command"
    for name, relative in hall_command_sources.items():
        source = assets / "resources" / "native" / relative
        if source.exists():
            hall_command_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, hall_command_dir / f"{name}.png")

    # Global108 MagicLayer's actual UnitInfo child-route fixture selects
    # 회오리 (Magic/3-1, Hitarea/14-1, Effarea/1-1). These are the authored
    # source textures resolved by the corresponding SpriteFrames, not atlas
    # screenshots or game approximations.
    magic_layer_sources = {
        "magic-3": assets / "resources" / "native" / "c8" / "c8b19744-c75d-4867-892a-a1ac2c48eeeb.ef39b.png",
        "hitarea-14": assets / "Game" / "native" / "5d" / "5df79add-0f9a-4405-a0c4-61374bc311af.1b47e.png",
        "effarea-1": assets / "Game" / "native" / "fe" / "fea0a7cb-5f99-4080-b005-e82a7b6d77d2.669cb.png",
    }
    magic_layer_dir = output / "ui" / "magic-layer"
    for name, source in magic_layer_sources.items():
        if source.exists():
            magic_layer_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, magic_layer_dir / f"{name}.png")

    # Global131 Choose2Layer uses four 20x20 source frames stretched/sliced
    # by its authored prefab.  Keep the underlying native textures exact;
    # these are ordinary bundle assets, not framebuffer-derived crops.
    choose2_sources = {
        "bg2": assets / "resources" / "native" / "24" / "243ad330-d6d2-4c2b-919f-05208cb9bdea.dd092.png",
        "box5": assets / "resources" / "native" / "af" / "af9dbb34-3fb3-4757-8b09-a30a6824aae2.72763.png",
        "bg6": assets / "resources" / "native" / "4d" / "4d5d0ead-dbfc-44f4-880a-d6f6dcc380fe.a62fe.png",
        "box6": assets / "resources" / "native" / "cb" / "cbe24798-a509-44a5-a1d1-936674c2962d.53989.png",
    }
    choose2_dir = output / "ui" / "choose2"
    for name, source in choose2_sources.items():
        if source.exists():
            choose2_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, choose2_dir / f"{name}.png")

    # Global137 InputBox is an orphaned-but-shipped prefab.  Its five visible
    # frames are ordinary resources bundle textures, so preserve the original
    # files directly (no framebuffer or screenshot-derived crop).
    input_box_sources = {
        "bg1": assets / "resources" / "native" / "6a" / "6ad1dc5c-5ca6-4107-879a-2ea288562134.5ee7a.png",
        "box1": assets / "resources" / "native" / "49" / "49d4fa8d-b3a8-4901-9178-263c1c819482.0cf37.png",
        "box3": assets / "resources" / "native" / "0b" / "0b4d9ae9-c2e6-4212-a159-ad03f2bcd131.98199.png",
        "editbox": assets / "resources" / "native" / "34" / "346a34f3-ce23-4f76-9be1-2dc003976f9f.3a29b.png",
        "button": assets / "resources" / "native" / "28" / "287ef3b2-e6e3-46a6-874c-43e76597dca0.a336f.png",
    }
    input_box_dir = output / "ui" / "input-box"
    for name, source in input_box_sources.items():
        if source.exists():
            input_box_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, input_box_dir / f"{name}.png")

    # Cocos packs the ChoiceLayer's U_select_10-1 and row background into a
    # runtime dynamic atlas.  The original desktop verification harness reads
    # that atlas back from WebGL; preserve its exact source frames here rather
    # than approximating the panel with ShapeRenderer primitives.
    choice_atlas = assets.parent / "build" / "choice-atlas.png"
    if choice_atlas.exists():
        with Image.open(choice_atlas) as atlas:
            ui_dir = output / "ui"
            ui_dir.mkdir(parents=True, exist_ok=True)
            # The live ChooseLayer fixture resolves U_select_10-1 at
            # [589,2,344,84].  783 was from an older atlas packing and cut
            # through the following portrait, which made the game stretch a
            # fragment of the face across the choice panel.
            crop_cocos_frame(atlas, x=589, y=2, width=344, height=84).save(ui_dir / "choice-panel.png")
            crop_cocos_frame(atlas, x=1323, y=2, width=20, height=20).save(ui_dir / "choice-row.png")
            # The harness flips WebGL rows while constructing the canvas, so
            # the DynamicAtlas frame is top-origin at this point.
            crop_cocos_frame(atlas, x=728, y=atlas.height - 2 - 84, width=344, height=84).save(ui_dir / "dialogue-panel.png")
            # The dialogue frame is packed immediately after U_select_11 in
            # the battle capture's dynamic atlas.
            crop_cocos_frame(atlas, x=1074, y=atlas.height - 2 - 240, width=192, height=240).save(ui_dir / "dialogue-face.png")

    # WinConBoxLayer has its own captured atlas.  Do not reuse the menu
    # atlas: its live fixture resolves bg0/box2/box3/Logo_3-1 to these exact
    # rects and Cocos UV orientation (fixture json, 2026-08-30).
    win_condition_atlas = assets.parent / "build" / "win-condition-atlas.png"
    if win_condition_atlas.exists():
        with Image.open(win_condition_atlas) as atlas:
            modal_dir = output / "ui" / "win-condition"
            modal_dir.mkdir(parents=True, exist_ok=True)
            crop_cocos_frame(atlas, x=427, y=244, width=96, height=96, uv_top_to_bottom=True).save(modal_dir / "bg0.png")
            crop_cocos_frame(atlas, x=1548, y=2, width=60, height=60, uv_top_to_bottom=True).save(modal_dir / "box3.png")
            crop_cocos_frame(atlas, x=1272, y=2, width=20, height=20, uv_top_to_bottom=True).save(modal_dir / "scroll-box2.png")
            crop_cocos_frame(atlas, x=533, y=245, width=53, height=62, uv_top_to_bottom=True).save(modal_dir / "logo3.png")

    # UnitInfoLayer row-selected fixture: live DynamicAtlas frames, not
    # reconstructed colours.  Rects/capInsets are recorded beside the source
    # PNG in python-source-battle-verification-...-unit-info-fixture.json.
    unit_info_atlas = assets.parent / "build" / "unit-info-atlas.png"
    if unit_info_atlas.exists():
        modal_dir = output / "ui" / "unit-info"
        modal_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(unit_info_atlas) as atlas:
            for name, x, y, width, height in [
                ("bg1", 176, 2, 20, 20), ("box1", 586, 2, 20, 20),
                ("box2", 1272, 2, 20, 20), ("box3", 1330, 2, 60, 60),
                ("progress", 539, 496, 60, 15), ("mark6", 601, 496, 8, 8),
                ("mark3", 548, 2, 8, 8), ("mark2", 692, 2, 8, 8),
                # `root.sprites` in the live UnitInfo fixture: Logo_9-1 and
                # selected portrait 180.  These are dynamic-atlas coordinates,
                # therefore use the fixture values, not similarly named static
                # frames from a previous battle capture.
                ("logo9", 328, 244, 96, 96), ("face179", 653, 496, 192, 240),
                # UnitInfoLayer/panel0/vline2, fixture rect [611,496,40,2].
                ("vline2", 611, 496, 40, 2),
            ]:
                crop_cocos_frame(atlas, x=x, y=y, width=width, height=height, uv_top_to_bottom=True).save(modal_dir / f"{name}.png")

            # TerrainLayer's outer box1 and title bg1 are not the panel's
            # static box4 source.  The live TerrainLayer fixture resolves both
            # from this same DynamicAtlas texture (Tex.468): box1 [586,2,20,20]
            # with capInsets 3, and bg1 [176,2,20,20] with capInsets 5.
            # Keep those exact live frames separate from unit-info assets so a
            # future Terrain renderer cannot silently substitute box4 again.
            terrain_chrome_dir = output / "ui" / "terrain-layer"
            terrain_chrome_dir.mkdir(parents=True, exist_ok=True)
            crop_cocos_frame(atlas, x=586, y=2, width=20, height=20, uv_top_to_bottom=True).save(terrain_chrome_dir / "outer-box.png")
            crop_cocos_frame(atlas, x=176, y=2, width=20, height=20, uv_top_to_bottom=True).save(terrain_chrome_dir / "title-strip.png")

    # MineUnitInfoLayer / OtherUnitInfoLayer use these concrete SpriteFrame
    # chains.  Unlike the general UnitInfo fixture, their icons are native
    # source textures.  Copy/crop the SpriteFrame rects rather than borrowing
    # similarly coloured HUD marks.  The future production renderer consumes
    # the paths declared by SettlementInfoRenderContract.
    settlement_dir = output / "ui" / "settlement-info"
    settlement_sources = {
        "bg2": "24/243ad330-d6d2-4c2b-919f-05208cb9bdea.dd092.png",
        "box1": "49/49d4fa8d-b3a8-4901-9178-263c1c819482.0cf37.png",
        "progress-bg": "c7/c7d455fe-a43e-4de3-a1ba-8f9e39498a6e.99638.png",
        "mark3": "4a/4a83fc94-8f1c-49f6-85dd-cbd77d6b602f.4a668.png",
        "mark2": "cd/cdfcafe2-e041-4797-ab47-60c1f21ef181.dcf73.png",
        "mark6": "3d/3dc1b80c-a2b0-4aef-afd2-612f86de0de1.29b87.png",
    }
    for name, relative in settlement_sources.items():
        source = assets / "resources" / "native" / relative
        if source.exists():
            settlement_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, settlement_dir / f"{name}.png")
    # Mine-only stat and equipment SpriteFrames carry non-zero rect offsets.
    settlement_crops = {
        "mark7": ("20/20363b2d-5f75-42ba-9c66-465a7563716d.d6752.png", 0, 3, 24, 20),
        "mark8": ("ae/ae6182b0-6bdf-4204-a448-1529c1843512.d2674.png", 0, 0, 24, 24),
        "mark9": ("b8/b83497db-25fc-4a52-805d-0c47b5ee65d4.9ea36.png", 0, 1, 24, 23),
        "mark61": ("20/209545f2-664a-47c6-8d5b-0143ee7a300d.37877.png", 0, 1, 15, 15),
        "mark62": ("ee/ee9f0e06-d4a4-4d35-81f1-fc513d622f65.61e73.png", 0, 0, 16, 16),
    }
    for name, (relative, x, y, width, height) in settlement_crops.items():
        source = assets / "resources" / "native" / relative
        if source.exists():
            settlement_dir.mkdir(parents=True, exist_ok=True)
            with Image.open(source) as texture:
                crop_cocos_frame(texture, x=x, y=y, width=width, height=height).save(settlement_dir / f"{name}.png")

    # Hall/scene/StartBattleScreen, captured from the live fixture.  This
    # screen shares several generic box frames but its two large blue panels
    # and deployment-slot frames are unique to the prefab.
    start_battle_atlas = assets.parent / "build" / "start-battle-atlas.png"
    if start_battle_atlas.exists():
        modal_dir = output / "ui" / "start-battle"
        modal_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(start_battle_atlas) as atlas:
            for name, x, y, width, height in [
                ("logo9", 499, 2, 96, 96),
                ("roster", 597, 2, 400, 296),
                ("selected", 999, 2, 400, 128),
                ("box2", 1401, 2, 20, 20),
                ("slot-open", 1423, 20, 50, 32),
                ("button", 1475, 2, 60, 60),
                ("box1", 1537, 2, 20, 20),
                ("title", 1559, 2, 20, 20),
                ("slot-required", 1653, 22, 50, 30),
                ("slot-minimum", 1705, 20, 50, 32),
                ("face0", 1757, 2, 192, 240),
            ]:
                crop_cocos_frame(atlas, x=x, y=y, width=width, height=height).save(modal_dir / f"{name}.png")

    # TerrainLayer packs into a DynamicAtlas at runtime, but every captured
    # SpriteFrame retains `_original` with these source PNGs. Copy those
    # originals rather than cropping a framebuffer-backed atlas.
    terrain_sources = {
        "background": "3c/3c05c264-11fe-459f-b03b-2b56b63deca5.68a7f.png", # Logo_9-1
        "panel": "15/1570032d-65c5-4674-9d52-adfc3aae5f7f.33bbb.png",      # box4
        "row-even": "88/885a69b4-08ed-4c78-8896-ffb04eb2bd20.8c794.png",
        "row-odd": "24/243ad330-d6d2-4c2b-919f-05208cb9bdea.dd092.png",
        "vline": "a9/a9fe054a-377c-4f95-bb4b-2804b10b7952.4193f.png",
    }
    modal_dir = output / "ui" / "terrain-layer"
    modal_dir.mkdir(parents=True, exist_ok=True)
    for name, relative in terrain_sources.items():
        source = assets / "resources" / "native" / relative
        if source.exists():
            shutil.copy2(source, modal_dir / f"{name}.png")
    terrain_atlas = assets.parent / "build" / "terrain-layer-atlas.png"
    if terrain_atlas.exists():
        with Image.open(terrain_atlas) as atlas:
            # The retained atlas PNG was captured after an additional runtime
            # packing pass. Its live pixels are the four 32px frames below;
            # the earlier fixture rects refer to the pre-pack texture object.
            for index, x in enumerate((285, 321, 357, 393), start=1):
                skill = atlas.crop((x, 1512, x + 32, 1544))
                skill.save(modal_dir / f"skill{index}.png")
                gray = ImageOps.grayscale(skill.convert("RGB")).convert("RGBA")
                gray.putalpha(skill.getchannel("A"))
                gray.save(modal_dir / f"skill{index}-disabled.png")

    # Cocos renders system-font RichText into a runtime canvas texture.  Keep
    # the source alpha mask for the deterministic Yingchuan scene-0 fixture;
    # it avoids substituting FreeType glyph rasterization for the original
    # Chromium/Cocos output during pixel regression.
    dialogue_text_fixture = assets.parent / "build" / "python-source-battle-verification-dialogue3-text.png"
    dialogue_speaker_fixture = assets.parent / "build" / "python-source-battle-verification-dialogue3-speaker.png"
    if dialogue_text_fixture.exists() or dialogue_speaker_fixture.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
    def export_cocos_canvas(fixture_path: Path, target: Path) -> None:
        with Image.open(fixture_path).convert("RGBA") as fixture:
            # Cocos' Canvas backing store encodes transparent black as alpha
            # 1.  LibGDX correctly blends that value, so normalize it to true
            # transparency before exporting the actual glyph bounds.
            alpha = fixture.getchannel("A").point(lambda value: 0 if value <= 1 else value)
            fixture.putalpha(alpha)
            bbox = alpha.getbbox()
            if bbox:
                fixture.crop(bbox).save(target)
    if dialogue_text_fixture.exists():
        export_cocos_canvas(dialogue_text_fixture, ui_dir / "yingchuan-477-body.png")
    if dialogue_speaker_fixture.exists():
        export_cocos_canvas(dialogue_speaker_fixture, ui_dir / "yingchuan-477-speaker.png")

    # In the S_00 dialogue-3 source capture Head/192 has already been packed
    # into Cocos' live DynamicAtlas.  Chromium's palette decode of that atlas
    # is observably different from loading the original indexed PNG in LWJGL
    # (most visibly in the yellow cloth's blue channel).  Preserve the actual
    # source frame as a fixture asset, just as we do for the RichText canvases
    # above.  The rectangle is derived from the recorded node transform:
    # face lower-left=(1064.618,426), size=192x240 in a 1488.372x800 canvas,
    # captured at 2560x1376.
    dialogue_frame = assets.parent / "build" / "python-source-battle-verification-dialogue3.png"
    if dialogue_frame.exists():
        with Image.open(dialogue_frame).convert("RGBA") as frame:
            dynamic_face = frame.crop((1830, 230, 2161, 643)).resize((192, 240), Image.Resampling.LANCZOS)
            ui_dir = output / "ui"
            ui_dir.mkdir(parents=True, exist_ok=True)
            dynamic_face.save(ui_dir / "yingchuan-477-face.png")

    dialogue0_frame = assets.parent / "build" / "python-source-battle-verification.png"
    if not dialogue0_frame.exists():
        dialogue0_frame = assets.parent.parent / "jojo" / "build" / "render-frames" / "source-battle-dialogue-blending.png"
    if dialogue0_frame.exists():
        with Image.open(dialogue0_frame).convert("RGBA") as frame:
            dynamic_face = frame.crop((1831, 396, 2161, 808)).resize((192, 240), Image.Resampling.LANCZOS)
            ui_dir = output / "ui"
            ui_dir.mkdir(parents=True, exist_ok=True)
            dynamic_face.save(ui_dir / "yingchuan-474-face.png")

    copied: dict[str, str] = {}
    copied_battle_maps: dict[str, str] = {}
    copied_units: dict[str, str] = {}
    copied_heads: dict[str, str] = {}
    copied_data: dict[str, str] = {}
    copied_hexmaps: dict[str, str] = {}
    copied_gates: dict[str, str] = {}
    copied_terrain_icons: dict[str, str] = {}
    copied_item_icons: dict[str, str] = {}
    # BattleUnit._setAvatar delegates every action to Battle.CreateAnime with
    # this authored BRAnime table.  Export the table itself instead of
    # reproducing individual animation rows in Kotlin.
    battle_anime = assets / "resources" / "import" / "a9" / "a93b242b-0857-4489-901a-689fc92ae8eb.63b41.json"
    if battle_anime.exists():
        serialized = json.loads(battle_anime.read_text())
        # Different Cocos serialization revisions either retain the
        # JsonAsset name ("animeBR") or store its object directly.
        payload = serialized[5][0][2]
        definitions = payload.get("animeBR", payload)
        (output / "battle-anime.json").write_text(
            json.dumps(definitions, ensure_ascii=False, separators=(",", ":"))
        )
    # FightUnit uses its own directionless `animeFR` table.  Exporting the
    # actual JsonAsset keeps duel frame selection, child translation, flips,
    # opacity and callback timing tied to the shipped data.
    fight_anime = assets / "resources" / "import" / "10" / "10c49fc4-3d52-44dd-85e3-f6df3b7b71de.464ef.json"
    if fight_anime.exists():
        serialized = json.loads(fight_anime.read_text())
        payload = serialized[5][0][2]
        definitions = payload.get("animeFR", payload)
        (output / "fight-anime.json").write_text(
            json.dumps(definitions, ensure_ascii=False, separators=(",", ":"))
        )
    # HallUnit uses the much smaller RRAnime table.  Unlike the battle table,
    # every authored hall action is either one static frame or the two-frame
    # walk cycle, but the exact sprite indices and 24 fps durations still
    # belong to source data rather than renderer guesses.
    hall_anime = assets / "resources" / "import" / "af" / "af023640-f0da-40cb-8571-d3cfee5dc9dd.19aeb.json"
    if hall_anime.exists():
        serialized = json.loads(hall_anime.read_text())
        payload = serialized[5][0][2]
        definitions = payload.get("animeRR", payload)
        (output / "hall-anime.json").write_text(
            json.dumps(definitions, ensure_ascii=False, separators=(",", ":"))
        )
    # The source verifier's live WebGL readback preserves Cocos' own JPEG
    # decode exactly for S_00 / HM_1.  Prefer this pixel source when present.
    source_battle_map = assets.parent / "build" / "battle-map-source.png"
    if source_battle_map.exists():
        target = output / "battle-maps" / "1.png"
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source_battle_map, target)
    for path_index, entry in config["paths"].items():
        map_match = re.fullmatch(r"Mmap/Mmap_(\d+)-1", entry[0])
        battle_map_match = re.fullmatch(r"HM/HM_(\d+)-1", entry[0])
        # BattleUnit.loadAvatar loads three independent source atlases in
        # this order: Unit_atk, Unit_mov and Unit_spc.  Keeping only the
        # movement strip made it impossible for the LibGDX renderer to ever
        # reproduce the original attack/special-action layers.
        # BattleScreen.safeResPath prefers the optional *2 resource family
        # (Unit_mov2/atk2/spc2) and falls back to the base family only when
        # that file is absent.  Preserve both families verbatim.
        unit_match = re.fullmatch(r"Unit_(atk|mov|spc)(2?)/(\d+)", entry[0])
        hall_unit_match = re.fullmatch(r"Pmapobj2/(\d+)", entry[0])
        head_match = re.fullmatch(r"Head/(\d+)", entry[0])
        gate_match = re.fullmatch(r"Gate/Gate_(\d+)-1", entry[0])
        # TerrainLayer._initPanel[01] loads this exact sprite path by its
        # cfgTerrainIter index, not terrain-ID text or a generated icon.
        terrain_icon_match = re.fullmatch(r"Terrain/(\d+)", entry[0])
        item_icon_match = re.fullmatch(r"Item/(\d+)-1", entry[0])
        # MagickListLayer and MagicLayer resolve these by the live magic
        # profile (icon/hitarea/effarea + 1).  Export the complete authored
        # families so a battle dialog never falls back to a synthetic icon.
        magic_icon_match = re.fullmatch(r"Magic/(\d+)-1", entry[0])
        hitarea_match = re.fullmatch(r"Hitarea/(\d+)-1", entry[0])
        effarea_match = re.fullmatch(r"Effarea/(\d+)-1", entry[0])
        # The campaign's FightLayer corpus uses bgIdx 0 and 5, resolved as
        # Logo_1-1 and Logo_6-1 by the source URL expression.
        fight_logo_match = re.fullmatch(r"Logo/Logo_(1|6)-1", entry[0])
        meff_match = re.fullmatch(r"Meff/Meff_(\d+)-1", entry[0])
        # BattleScreen's five move/hit-area frames are also U_select assets;
        # retain the whole family instead of only the object-animation #20.
        select_match = re.fullmatch(r"U_select/(U_select(?:_?\d+)?)(?:-1)?", entry[0])
        mark_match = re.fullmatch(r"Mark/Mark_(\d+)(?:-1)?", entry[0])
        data_match = re.fullmatch(r"data/Global/(unit|arms|posts|unitPostsSkill|terrain|magic|config|gameConfig|Meff|item|itemSkills|effarea|hitarea|defineSkill|shop)", entry[0])
        if not map_match and not battle_map_match and not unit_match and not hall_unit_match and not head_match and not gate_match and not terrain_icon_match and not item_icon_match and not magic_icon_match and not hitarea_match and not effarea_match and not fight_logo_match and not meff_match and not select_match and not mark_match and not data_match:
            continue
        index = int(path_index)
        asset_hash = native_versions.get(index)
        uuid = decode_uuid(config["uuids"][index])
        native_name = f"{uuid}.{asset_hash}.*" if asset_hash is not None else f"{uuid}.*"
        candidates = glob.glob(str(assets / "Game" / "native" / uuid[:2] / native_name))
        # Terrain/0 is a resources-bundle texture in the recovered desktop
        # package while Terrain/1.. live in Game.  Cocos resolves both under
        # `Game/Terrain/<i>`; retain its actual native image rather than
        # replacing index zero with an invented plain/grass icon.
        if not candidates:
            candidates = glob.glob(str(assets / "resources" / "native" / uuid[:2] / native_name))
        if not candidates:
            continue
        source = Path(candidates[0])
        if map_match:
            target = output / f"{map_match.group(1)}{source.suffix.lower()}"
            key = map_match.group(1)
            destination = copied
        elif battle_map_match:
            target = output / "battle-maps" / f"{battle_map_match.group(1)}{source.suffix.lower()}"
            key = battle_map_match.group(1)
            destination = copied_battle_maps
        elif unit_match:
            unit_base_kind, unit_variant, unit_id = unit_match.groups()
            unit_kind = unit_base_kind + unit_variant
            target = output / "units" / unit_kind / f"{unit_id}{source.suffix.lower()}"
            key = f"{unit_kind}/{unit_id}"
            destination = copied_units
        elif hall_unit_match:
            target = output / "hall-units" / f"{hall_unit_match.group(1)}{source.suffix.lower()}"
            key = f"hall/{hall_unit_match.group(1)}"
            destination = copied_units
        elif gate_match:
            target = output / "gates" / f"{gate_match.group(1)}{source.suffix.lower()}"
            key = gate_match.group(1)
            destination = copied_gates
        elif terrain_icon_match:
            target = output / "terrain-icons" / f"{terrain_icon_match.group(1)}{source.suffix.lower()}"
            key = terrain_icon_match.group(1)
            destination = copied_terrain_icons
        elif item_icon_match:
            target = output / "item-icons" / f"{item_icon_match.group(1)}{source.suffix.lower()}"
            key = item_icon_match.group(1)
            destination = copied_item_icons
        elif magic_icon_match:
            target = output / "magic-icons" / f"{magic_icon_match.group(1)}{source.suffix.lower()}"
            key = f"magic/{magic_icon_match.group(1)}"
            destination = copied_data
        elif hitarea_match:
            target = output / "magic-hitareas" / f"{hitarea_match.group(1)}{source.suffix.lower()}"
            key = f"hitarea/{hitarea_match.group(1)}"
            destination = copied_data
        elif effarea_match:
            target = output / "magic-effareas" / f"{effarea_match.group(1)}{source.suffix.lower()}"
            key = f"effarea/{effarea_match.group(1)}"
            destination = copied_data
        elif fight_logo_match:
            target = output / "ui" / f"fight-bg-{fight_logo_match.group(1)}{source.suffix.lower()}"
            key = f"fight-bg/{fight_logo_match.group(1)}"
            destination = copied_data
        elif meff_match:
            # BattleScreen.meff(effectId) loads Meff_(effectId + 1)-1.
            # Preserve the source 1-based asset name; the renderer applies
            # the same +1 conversion from the GAME_CFG MEFF id.
            target = output / "effects" / f"{meff_match.group(1)}{source.suffix.lower()}"
            key = f"effect/{meff_match.group(1)}"
            destination = copied_data
        elif select_match:
            # BattleScreen._setObject uses this 48px strip for fire and
            # scripted terrain-object animations.
            select_name = select_match.group(1).removeprefix("U_select").lstrip("_") or "0"
            target = output / "select" / f"{select_name}{source.suffix.lower()}"
            key = f"select/{select_name}"
            destination = copied_data
        elif mark_match:
            target = output / "marks" / f"{mark_match.group(1)}{source.suffix.lower()}"
            key = f"mark/{mark_match.group(1)}"
            destination = copied_data
        else:
            if head_match:
                target = output / "heads" / f"{head_match.group(1)}{source.suffix.lower()}"
                key = head_match.group(1)
                destination = copied_heads
            else:
                target = output / "data" / f"{data_match.group(1)}{source.suffix.lower()}"
                key = data_match.group(1)
                destination = copied_data
        target.parent.mkdir(parents=True, exist_ok=True)
        if not target.exists():
            shutil.copy2(source, target)
        destination[key] = str(target.relative_to(output))

    # DialogueLayer uses this source SpriteFrame before Cocos packs it into a
    # per-run DynamicAtlas.  Copy the stable native image rather than trying
    # to infer a moving atlas rectangle from a renderer capture.
    dialogue_panel = assets / "resources" / "native" / "94" / "94d1d122-2c55-4e62-9a52-0be1b849ba16.dbc5a.png"
    if dialogue_panel.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(dialogue_panel, ui_dir / "dialogue-panel.png")
    # FightLayer say0 uses the opposite-tail U_select_10-1 SpriteFrame.
    fight_speech_left = assets / "resources" / "native" / "1d" / "1da9ad95-7143-48c4-a6c5-4bc564b7c4e4.cc3c9.png"
    if fight_speech_left.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(fight_speech_left, ui_dir / "fight-speech-left.png")

    # BattleScreen receives this 24px Mark_10 SpriteFrame as `qipao`.  Its
    # SHOW_SAY listener places it above the speaking tactical unit, so it is
    # a battle-map layer rather than part of DialogueLayer's large panel.
    battle_say_marker = assets / "resources" / "native" / "6e" / "6e23f416-6258-4c79-9ac4-e89fc8b8df4f.9eb8d.png"
    if battle_say_marker.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(battle_say_marker, ui_dir / "battle-say.png")

    # Canvas/Layer/menu_button uses this authored 48px icon (the runtime
    # DynamicAtlas only changes its backing texture, not the source image).
    # Keep it separate from the dialogue atlas so battle HUD export is stable.
    battle_menu_icon = assets / "resources" / "native" / "fb" / "fb2f4562-584f-48df-967d-1e5094ad4fe4.ecdf9.png"
    if battle_menu_icon.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(battle_menu_icon, ui_dir / "battle-menu.png")

    # Natural Battle HUD (`Canvas/Layer/bg/map`) is Smlmap_1-1, an authored
    # 120x120 JPEG. It is unrelated to the 216x50 MenuLayer atlas crop.
    small_map = assets / "Game" / "native" / "28" / "28fcaf09-66e0-4d64-b968-438f0b7db258.1fd89.jpg"
    if small_map.exists():
        ui_dir = output / "ui"
        ui_dir.mkdir(parents=True, exist_ok=True)
        shutil.copy2(small_map, ui_dir / "battle-smlmap-1.jpg")
        # MiniMapLayer prefab resolves `tiled` SpriteFrames img5/img9 from
        # these 12px source textures (each frame rect is [1,1,10,10]).
        # They are independent authored resources, not DynamicAtlas crops.
        mini_markers = {
            "img5": assets / "resources" / "native" / "ad" / "adfa2ffb-4909-43f4-a4dc-3719c28ffef7.85962.png",
            "img9": assets / "resources" / "native" / "42" / "425a7995-3dd5-4013-9887-db054df7fa14.05d52.png",
        }
        for name, source in mini_markers.items():
            if source.exists():
                shutil.copy2(source, ui_dir / f"battle-smlmap-{name}.png")

    # The battle verifier captures the source DynamicAtlas after S_00 has
    # built its HUD.  SpriteFrame rects are bottom-origin; export the visible
    # record and end-turn icons as standalone authored frames.
    battle_hud_atlas = assets.parent / "build" / "battle-hud-atlas.png"
    if battle_hud_atlas.exists():
        with Image.open(battle_hud_atlas) as atlas:
            ui_dir = output / "ui"
            ui_dir.mkdir(parents=True, exist_ok=True)
            # Canvas/Layer/bg/button/Background/tool11 and bg/btn/.../tool11.
            crop_cocos_frame(atlas, x=176, y=2, width=20, height=20).save(ui_dir / "battle-button-bg.png")
            # The normalized source screenshot proves this frame's UV order
            # is top-to-bottom.  Keep that fact as frame metadata, rather
            # than applying a second, unexplained image transpose.
            crop_cocos_frame(atlas, x=198, y=2, width=48, height=48, uv_top_to_bottom=True).save(ui_dir / "battle-record.png")
            # Canvas/Layer/menu_button/Background is the circular end-turn
            # control; its authored SpriteFrame includes the full 100px art.
            crop_cocos_frame(atlas, x=52, y=2, width=100, height=100, uv_top_to_bottom=True).save(ui_dir / "battle-end-turn.png")

    # MenuLayer is loaded lazily after the initial Battle HUD.  Its source
    # frames share the dynamic-atlas layout, but are not necessarily present
    # in the first HUD capture, so extract the second live atlas separately.
    battle_menu_atlas = assets.parent / "build" / "battle-menu-atlas.png"
    if battle_menu_atlas.exists():
        with Image.open(battle_menu_atlas) as atlas:
            ui_dir = output / "ui" / "battle-menu"
            ui_dir.mkdir(parents=True, exist_ok=True)
            # Every MenuLayer SpriteFrame in this DynamicAtlas has the same
            # V ordering (the source uv has its first row at the frame's
            # visual bottom).  Normalize it once here; drawing a LibGDX
            # Texture must never apply a second menu-specific flip.
            def menu_crop(name: str, x: int, y: int, width: int, height: int, *, uv_top_to_bottom: bool = True) -> None:
                crop_cocos_frame(atlas, x=x, y=y, width=width, height=height, uv_top_to_bottom=uv_top_to_bottom).save(ui_dir / name)
            menu_crop("background.png", 176, 2, 20, 20)
            menu_crop("frame.png", 586, 2, 20, 20)
            menu_crop("box.png", 1272, 2, 20, 20)
            menu_crop("title-bar.png", 1294, 2, 16, 9)
            menu_crop("progress-bar.png", 1312, 2, 16, 9)
            # The MenuLayer capture occurs after the live HUD has populated
            # Cocos' DynamicAtlas.  These are the SpriteFrame.rect values
            # emitted by that exact source MenuLayer instance, not inferred
            # atlas slots.  The old offsets belonged to an earlier packing
            # order and selected minimap strips as menu icons.
            # Kept only as the legacy one-frame evidence capture.  The
            # renderer uses the authored Weather_n-1 sheets exported below;
            # it must not select this frame as a minimap texture.
            menu_crop("minimap.png", 1330, 102, 216, 50)
            # Canvas/Layer/bg/weather.  This is a distinct 72×72 HUD frame
            # emitted by the live MenuLayer DynamicAtlas.
            menu_crop("weather_0.png", 270, 2, 72, 72)
            menu_crop("button.png", 1548, 2, 60, 60)
            for index, x in enumerate((1610, 1660, 1710, 1760, 1810, 1860, 1910, 1960), start=1):
                menu_crop(f"tool{index}.png", x, 2, 48, 48)
            menu_crop("tool9.png", 2, 244, 48, 48)
            menu_crop("tool10.png", 198, 2, 48, 48)
            menu_crop("tool11.png", 366, 2, 48, 48)
            menu_crop("tool12.png", 52, 244, 48, 48)
            # MenuLayer intentionally skips button12; button13 uses help.
            menu_crop("help.png", 102, 244, 72, 72)

    # MenuLayer._create_weather maps config weather to Game/Weather/
    # Weather_<n>-1, constructs four SpriteFrames with rect(0, l*c, s, c),
    # then creates an AnimationClip at 6 fps on a node scaled by 2.  These
    # are source texture UUIDs, not DynamicAtlas positions.  Export every
    # source frame in that exact Cocos bottom-origin order so the Kotlin
    # renderer can select the animated sheet rather than a captured minimap
    # strip.  Keep machine-readable provenance beside the frames.
    weather_sources = {
        1: "67FKAyH2FNHL+nRJ+hP+QV",
        2: "3bUWysV6dCIawiNus6pgAj",
        3: "3dyi9S+0BLUJIDQkfGLmT+",
        4: "772hyHnP5Gg4ClbqJqcKqP",
        5: "fdaIbGau5OV5jVmikOzbEv",
    }
    weather_metadata: dict[str, object] = {
        "weather_0": {"atlas": "battle-menu-atlas.png", "rect": [270, 2, 72, 72]},
        "animation": {"fps": 6, "scale": 2, "frameOrder": [0, 1, 2, 3]},
        "sheets": {},
    }
    weather_dir = output / "ui" / "battle-menu"
    for weather_number, compressed_uuid in weather_sources.items():
        try:
            asset_index = config["uuids"].index(compressed_uuid)
        except ValueError:
            raise RuntimeError(f"Weather_{weather_number}-1 texture UUID missing from config")
        asset_hash = native_versions.get(asset_index)
        uuid = decode_uuid(compressed_uuid)
        native_name = f"{uuid}.{asset_hash}.*" if asset_hash is not None else f"{uuid}.*"
        candidates = glob.glob(str(assets / "Game" / "native" / uuid[:2] / native_name))
        if not candidates:
            raise RuntimeError(f"Weather_{weather_number}-1 native texture missing: {uuid}")
        with Image.open(candidates[0]).convert("RGBA") as sheet:
            width, height = sheet.size
            if width != 216 or height != 200:
                raise RuntimeError(f"Weather_{weather_number}-1 expected 216x200, got {width}x{height}")
            weather_dir.mkdir(parents=True, exist_ok=True)
            for frame_index in range(4):
                # cc.rect's y is bottom-origin, matching _create_weather.
                crop_cocos_frame(sheet, x=0, y=frame_index * 50, width=216, height=50).save(
                    weather_dir / f"weather_{weather_number}_{frame_index}.png"
                )
        weather_metadata["sheets"][str(weather_number)] = {
            "sourceUuid": uuid,
            "sourceRect": [0, 0, 216, 200],
            "frameRectsBottomOrigin": [[0, frame * 50, 216, 50] for frame in range(4)],
        }
    weather_dir.mkdir(parents=True, exist_ok=True)
    (weather_dir / "weather-frames.json").write_text(
        json.dumps(weather_metadata, ensure_ascii=False, separators=(",", ":"))
    )

    # Battle.scene `state_texture`: BattleUnit.refStateAnime addresses these
    # four 16x16 Texture2Ds by MB/JZ/HL/ZD offset. They are independent
    # textures, not rows in a strip; the two-frame clip alternates texture
    # and position at 3fps.
    state_sources = {
        0: "b620d99d-09ae-4a24-a2dc-66f8ab4a429b.71f53.png",
        1: "076ed016-1904-44bc-86d7-dc4875191735.9af95.png",
        2: "08a935ed-3d4b-4154-8788-8fca69307c5f.62922.png",
        3: "5969c563-f42a-4ef9-b2a3-b48bcddb36ca.2603f.png",
    }
    state_dir = output / "ui" / "battle-status"
    state_dir.mkdir(parents=True, exist_ok=True)
    for index, filename in state_sources.items():
        source = assets / "resources" / "native" / filename[:2] / filename
        if not source.exists():
            raise RuntimeError(f"Battle state_texture[{index}] missing: {source}")
        with Image.open(source) as texture:
            if texture.size != (16, 16):
                raise RuntimeError(f"Battle state_texture[{index}] expected 16x16, got {texture.size}")
        shutil.copy2(source, state_dir / f"state_{index}.png")

    # BattleScreen.statusImgs, used by the six prefab children
    # status/unit_status_0..5. Source selects index 0 for DOWN and index 1
    # for every other non-normal lift. The prefab frames are untrimmed 12x12.
    attribute_status_sources = {
        "down": "aeac6ec1-5ceb-4df7-9b32-f4020d46a04f.ce8f4.png",
        "up": "b9716059-05fe-4aff-8018-7f2652d3a401.3c94a.png",
    }
    for name, filename in attribute_status_sources.items():
        source = assets / "resources" / "native" / filename[:2] / filename
        if not source.exists():
            raise RuntimeError(f"Battle statusImgs {name} missing: {source}")
        with Image.open(source) as texture:
            if texture.size != (12, 12):
                raise RuntimeError(f"Battle statusImgs {name} expected 12x12, got {texture.size}")
        shutil.copy2(source, state_dir / f"attribute_{name}.png")

    # BattleUnit/info/bar2 uses these resource SpriteFrames (not Game/Mark
    # assets) and stretches them to the current HP width.
    mark_sources = {
        # BattleScreen.hpbars[4], selected for Unit.isFamous() enemies.
        "2": assets / "resources" / "native" / "cd" / "cdfcafe2-e041-4797-ab47-60c1f21ef181.dcf73.png",
        "68": assets / "resources" / "native" / "ea" / "ea63f073-ef41-4a54-ad8a-a99419819c97.f87d1.png",
        "3": assets / "resources" / "native" / "4a" / "4a83fc94-8f1c-49f6-85dd-cbd77d6b602f.4a668.png",
        "5": assets / "resources" / "native" / "a0" / "a0dcb0ef-46a4-41f4-b3d8-b5723d8a0cb5.766ff.png",
    }
    for mark_id, source in mark_sources.items():
        if source.exists():
            target = output / "marks" / f"{mark_id}.png"
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, target)

    # CommandLayer's six buttons each duplicate their authored 16px command
    # SpriteFrame at opposite corners (`img0` and `img1`).  These are static
    # resources, not DynamicAtlas substitutions.  Keep the trimmed source
    # rectangles for command3/5 so the exported pixel extent matches the
    # prefab's SpriteFrame rect rather than its texture's transparent border.
    command_dir = output / "ui" / "battle-command"
    command_sources = {
        "command1": (assets / "resources" / "native" / "f8" / "f8fed99a-8483-41f1-a1ab-7e9db1503cfa.267d1.png", (0, 0, 16, 16)),
        "command2": (assets / "resources" / "native" / "b8" / "b8167919-11fe-4c36-b842-6976f82a80eb.ee9c1.png", (0, 0, 16, 16)),
        "command3": (assets / "resources" / "native" / "ca" / "ca045ba8-b5dc-4646-8aaf-8624d33fbf99.771ea.png", (1, 1, 15, 15)),
        "command4": (assets / "resources" / "native" / "1a" / "1a8fe0fd-a0aa-45db-a806-662b279fc95a.9ec97.png", (0, 0, 16, 16)),
        "command5": (assets / "resources" / "native" / "fa" / "faf52ca5-8398-43c8-8b06-a5ac8409d328.4178e.png", (0, 1, 16, 14)),
        "command6": (assets / "resources" / "native" / "29" / "2994d6f2-6c97-4e36-a152-4c00ae963582.54c8d.png", (0, 0, 16, 16)),
    }
    for name, (source, (x, y, width, height)) in command_sources.items():
        if not source.exists():
            raise RuntimeError(f"CommandLayer {name} source missing: {source}")
        command_dir.mkdir(parents=True, exist_ok=True)
        with Image.open(source) as texture:
            crop_cocos_frame(texture, x=x, y=y, width=width, height=height).save(command_dir / f"{name}.png")

    # The Battle.fire prefab owns these frames directly, rather than loading
    # them through a `Game/U_select` path.  The live source inventory records
    # their exact order in BattleScreen.areas: RED=move2, GREEN=move0,
    # BLUE=move1, RED_BOX=Mark_12-1, GREEN_BOX=Mark_13-1.  Keep their source
    # bytes under explicit game names so the renderer never substitutes the
    # unrelated U_select textures with similarly numbered filenames.
    battle_selection_sources = {
        "cursor": assets / "resources" / "native" / "1c" / "1c7024e3-5858-4465-b00b-1722c8905a4c.391ef.png",
        "range-red": assets / "resources" / "native" / "cb" / "cb6ab8a1-3d46-41c6-97da-e8cce7ad6efa.de1ce.png",
        "range-green": assets / "resources" / "native" / "a2" / "a294fe3c-c3f1-4ee9-99cf-8038813f3827.c2fd4.png",
        "range-blue": assets / "resources" / "native" / "25" / "250a6266-245c-4854-96fe-18875e1e8641.159c1.png",
        "range-red-box": assets / "resources" / "native" / "74" / "74d85b9d-5c4c-4052-902b-ca8587d15f5e.77a56.png",
        "range-green-box": assets / "resources" / "native" / "db" / "db50b8c2-384b-4e27-9fc1-063a06c6a4ae.1765b.png",
    }
    for name, source in battle_selection_sources.items():
        if not source.exists():
            raise RuntimeError(f"Battle selection source missing: {source}")
        target = output / "selection" / f"{name}.png"
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)

    # Global100 ProgressLayer and Global104 LoadingLayer share the original
    # `uiloading` spinner frame. Export the authored source texture directly;
    # no framebuffer or dynamic-atlas capture is involved.
    system_overlay_sources = {
        "uiloading": assets / "resources" / "native" / "57" / "57b065e0-7c8f-4ef8-96da-3f2eaf0477c0.ef027.png",
    }
    for name, source in system_overlay_sources.items():
        if not source.exists():
            raise RuntimeError(f"System overlay source missing: {source}")
        target = output / "ui" / "system-overlay" / f"{name}.png"
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)

    # BattleScreen loads Hexzmap_<id> as a Cocos JsonAsset.  Preserve its
    # semantic JSON payload (width, height, terrain rows) in a plain file so
    # LibGDX can consume exactly the same terrain grid without Cocos metadata.
    for path_index, entry in config["paths"].items():
        hex_match = re.fullmatch(r"data/Hexzmap/Hexzmap_(\d+)", entry[0])
        if not hex_match:
            continue
        index = int(path_index)
        asset_hash = import_versions.get(index)
        if asset_hash is None:
            continue
        uuid = decode_uuid(config["uuids"][index])
        candidates = glob.glob(str(assets / "Game" / "import" / uuid[:2] / f"{uuid}.{asset_hash}.json"))
        if not candidates:
            continue
        wrapper = json.loads(Path(candidates[0]).read_text())
        try:
            payload = wrapper[5][0][2]
            if not isinstance(payload, dict) or not {"width", "height", "data"} <= payload.keys():
                continue
        except (IndexError, TypeError):
            continue
        key = hex_match.group(1)
        target = output / "hexmaps" / f"{key}.json"
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(payload, separators=(",", ":")))
        copied_hexmaps[key] = str(target.relative_to(output))

    # HallLayer.AStar reads the 100x100 Pmap_<scene> obstacle grid for every
    # type-2 background.  Export all of them so court/interior movement takes
    # the same turns as Cocos instead of cutting straight through the scene.
    copied_pmaps: dict[str, str] = {}
    for path_index, entry in config["paths"].items():
        pmap_match = re.fullmatch(r"data/Pmap/Pmap_(\d+)", entry[0])
        if not pmap_match:
            continue
        index = int(path_index)
        asset_hash = import_versions.get(index)
        if asset_hash is None:
            continue
        uuid = decode_uuid(config["uuids"][index])
        candidates = glob.glob(str(assets / "Game" / "import" / uuid[:2] / f"{uuid}.{asset_hash}.json"))
        if not candidates:
            continue
        wrapper = json.loads(Path(candidates[0]).read_text())
        try:
            payload = wrapper[5][0][2]
            if not isinstance(payload, list) or len(payload) != 100 or any(not isinstance(row, list) or len(row) != 100 for row in payload):
                continue
        except (IndexError, TypeError):
            continue
        key = pmap_match.group(1)
        target = output / "pmaps" / f"{key}.json"
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(payload, separators=(",", ":")))
        copied_pmaps[key] = str(target.relative_to(output))

    (output / "manifest.json").write_text(
        json.dumps({"maps": copied, "battleMaps": copied_battle_maps, "units": copied_units, "heads": copied_heads, "gates": copied_gates, "terrainIcons": copied_terrain_icons, "itemIcons": copied_item_icons, "data": copied_data, "hexmaps": copied_hexmaps, "pmaps": copied_pmaps}, ensure_ascii=False, sort_keys=True)
    )
    print(f"Exported {len(copied)} maps, {len(copied_units)} unit sprites, {len(copied_heads)} portraits, {len(copied_gates)} gates, {len(copied_terrain_icons)} terrain icons, {len(copied_hexmaps)} terrain grids, {len(copied_pmaps)} hall path grids, and {len(copied_data)} gameplay tables to {output}")


if __name__ == "__main__":
    main()
