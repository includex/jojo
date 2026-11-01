#!/usr/bin/env python3
"""Adapt project-generated atlas cells to the unmodified Jojo renderer's resource ABI.

This is deterministic build-format conversion only: extract authored grid cells,
resize to renderer dimensions, and repeat the same miniature in the existing
animation atlas slots. It does not synthesize poses or change the source art.
The original generated atlases and their provenance remain in content/examples/linchong.
"""
from __future__ import annotations
import argparse
import hashlib
import json
import shutil
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
CHARACTERS = ['lin', 'lu', 'hu', 'jin', 'zhu', 'hong', 'yang', 'wang',
              'chai', 'song', 'dong', 'xue', 'luk', 'fu', 'guard', 'bandit']
SUPPORT = ['wife', 'gao_yanei', 'gao_qiu', 'servant', 'escort',
           'pursue_boss', 'smug_boss', 'bandit_boss']
SUPPORT_IDS = dict(zip(SUPPORT, [571, 572, 573, 574, 539, 552, 559, 540]))
BATTLES = ['B01', 'B02', 'EX01', 'B03', 'B04', 'B05', 'EX02', 'B06']
# Atlas ordinal, runtime source name, frame dimensions. Existing BattleSpriteTimeline ABI.
ATLAS = {0: ('atk2', 64), 1: ('mov2', 48), 2: ('spc2', 48)}


def cell(image: Image.Image, columns: int, rows: int, index: int) -> Image.Image:
    if not 0 <= index < columns * rows:
        raise ValueError(f'Cell index {index} outside {columns}x{rows}')
    col, row = index % columns, index // columns
    # Integer partition covers non-divisible generated image dimensions exactly.
    box = (col * image.width // columns, row * image.height // rows,
           (col + 1) * image.width // columns, (row + 1) * image.height // rows)
    return image.crop(box)


def frame_counts(timeline: dict) -> dict[int, int]:
    counts = {ordinal: 1 for ordinal in ATLAS}
    for frames in timeline.values():
        for frame in frames:
            sprite = frame.get('sprite', {})
            if sprite.get('t') in counts:
                ordinal = sprite['t']
                counts[ordinal] = max(counts[ordinal], int(sprite.get('idx', 0)) + 1)
    return counts


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def package(source: Path, destination: Path, manifest_path: Path) -> dict:
    source = source.resolve()
    destination = destination.resolve()
    timeline_path = ROOT / 'content/assets/maps/battle-anime.json'
    counts = frame_counts(json.loads(timeline_path.read_text()))
    token_source = source / 'tokens.png'
    portrait_source = source / 'portraits.png'
    support_source = source / 'portraits-support.png'
    manifest = {
        'format': 'jojo-existing-assets-v1',
        'description': 'Static generated miniatures packed into existing Jojo movement/attack/special atlas rows. Existing UI, movement, combat, timing and input remain unchanged.',
        'sourceHashes': {p.name: digest(p) for p in (token_source, portrait_source, support_source)},
        'animationTimelineSha256': digest(timeline_path),
        'atlasFrameCounts': {ATLAS[k][0]: v for k, v in counts.items()},
        'characters': {}, 'objects': {}, 'maps': {}, 'generatedFiles': [],
    }
    with Image.open(token_source) as opened:
        tokens = opened.convert('RGBA')
        for slot, character_id in enumerate(CHARACTERS):
            avatar_id = 601 + slot
            token = cell(tokens, 4, 4, slot)
            paths = {}
            for ordinal, (kind, size) in ATLAS.items():
                # The legacy sampler takes sourceY = index * (height + 2) + 1.
                frame = token.resize((size, size), Image.Resampling.LANCZOS)
                atlas = Image.new('RGBA', (size, (size + 2) * counts[ordinal]), (0, 0, 0, 0))
                for index in range(counts[ordinal]):
                    atlas.paste(frame, (0, index * (size + 2) + 1))
                relative = Path('maps/units') / kind / f'{avatar_id}.png'
                output = destination / relative
                output.parent.mkdir(parents=True, exist_ok=True)
                atlas.save(output, optimize=True)
                paths[kind] = str(relative)
                manifest['generatedFiles'].append(str(relative))
            manifest['characters'][character_id] = {
                'characterId': 512 + slot, 'avatarId': avatar_id, 'battleAvatar': avatar_id - 304, 'battleAvatarType': -1, 'face': 600 + slot,
                'headTextureId': 608 + slot, 'sourceTokenCell': slot,
                'movement': paths['mov2'], 'attack': paths['atk2'], 'special': paths['spc2'],
            }
    object_source = source / 'objects.png'
    if object_source.exists():
        manifest['sourceHashes'][object_source.name] = digest(object_source)
        with Image.open(object_source) as opened:
            object_atlas = opened.convert('RGBA')
            for index in range(4):
                avatar_id = 617 + index
                token = cell(object_atlas, 2, 2, index)
                paths = {}
                for ordinal, (kind, size) in ATLAS.items():
                    frame = token.resize((size, size), Image.Resampling.LANCZOS)
                    atlas = Image.new('RGBA', (size, (size + 2) * counts[ordinal]), (0, 0, 0, 0))
                    for frame_index in range(counts[ordinal]):
                        atlas.paste(frame, (0, frame_index * (size + 2) + 1))
                    relative = Path('maps/units') / kind / f'{avatar_id}.png'
                    output = destination / relative
                    output.parent.mkdir(parents=True, exist_ok=True)
                    atlas.save(output, optimize=True)
                    paths[kind] = str(relative)
                    manifest['generatedFiles'].append(str(relative))
                for object_id, character_id in ([ [('cart_a', 566), ('cart_b', 567), ('supply', 568), ('cargo1', 569)][index] ] + ([('cargo2', 570)] if index == 3 else [])):
                    manifest['objects'][object_id] = {
                        'characterId': character_id, 'avatarId': avatar_id,
                        'battleAvatar': avatar_id - 304, 'battleAvatarType': -1,
                        'sourceTokenAtlas': object_source.name, 'sourceTokenCell': index,
                        'movement': paths['mov2'], 'attack': paths['atk2'], 'special': paths['spc2'],
                    }
    for portrait_path, columns, rows, names, first_slot in (
            (portrait_source, 4, 4, CHARACTERS, 0),
            (support_source, 4, 2, SUPPORT, 16)):
        with Image.open(portrait_path) as opened:
            portraits = opened.convert('RGB')
            for index, character_id in enumerate(names):
                slot = first_slot + index
                # Existing dialogue texture ABI is 192x240. Fit the square
                # portrait without distorting it; fill the remainder from its
                # own upper-left parchment color.
                portrait = cell(portraits, columns, rows, index)
                head = Image.new('RGB', (192, 240), portrait.getpixel((0, 0)))
                head.paste(portrait.resize((192, 192), Image.Resampling.LANCZOS), (0, 24))
                relative = Path('maps/heads') / f'{608 + slot}.png'
                output = destination / relative
                output.parent.mkdir(parents=True, exist_ok=True)
                head.save(output, optimize=True)
                entry = manifest['characters'].setdefault(character_id, {
                    'characterId': SUPPORT_IDS.get(character_id, 512 + slot), 'face': 600 + slot, 'headTextureId': 608 + slot})
                entry.update({'portrait': str(relative), 'sourcePortraitAtlas': portrait_path.name,
                              'sourcePortraitCell': index})
                manifest['generatedFiles'].append(str(relative))
    for index, battle_id in enumerate(BATTLES):
        original = source / 'maps' / f'{battle_id}.png'
        relative = Path('maps/battle-maps') / f'{index + 59}.png'
        output = destination / relative
        output.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(original, output)
        manifest['maps'][battle_id] = {'mapIndex': index + 58, 'texture': str(relative),
                                      'sourceSha256': digest(original)}
        manifest['generatedFiles'].append(str(relative))
    # A nonpictured narrator still uses the native dialogue portrait contract.
    narrator_head = Path('maps/heads/632.png')
    narrator_output = destination / narrator_head
    narrator_output.parent.mkdir(parents=True, exist_ok=True)
    Image.new('RGBA', (192, 240), (0, 0, 0, 0)).save(narrator_output)
    manifest['narrator'] = {'characterId': 575, 'name': '이야기', 'face': 624, 'headTextureId': 632, 'portrait': str(narrator_head)}
    manifest['generatedFiles'].append(str(narrator_head))
    # Native completion route uses the existing scenario background loader.
    terminal_background = Path('source-map-textures/990.png')
    terminal_output = destination / terminal_background
    terminal_output.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(source / 'maps/B06.png', terminal_output)
    manifest['terminalBackground'] = {'loadBgArguments': [3, 990], 'texture': str(terminal_background)}
    manifest['generatedFiles'].append(str(terminal_background))
    manifest_path.parent.mkdir(parents=True, exist_ok=True)
    manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
    return manifest


def verify(destination: Path, manifest: dict) -> None:
    for filename in manifest['generatedFiles']:
        with Image.open(destination / filename) as image:
            image.verify()
    for entry in manifest['characters'].values():
        with Image.open(destination / entry['portrait']) as image:
            assert image.size == (192, 240)
        if 'avatarId' in entry:
            for ordinal, (kind, size) in ATLAS.items():
                with Image.open(destination / entry[{'mov2': 'movement', 'atk2': 'attack', 'spc2': 'special'}[kind]]) as image:
                    assert image.mode == 'RGBA'
                    assert image.size == (size, (size + 2) * manifest['atlasFrameCounts'][kind])
                    # All sampled rows are populated, while inset rows remain transparent.
                    for index in range(manifest['atlasFrameCounts'][kind]):
                        assert image.crop((0, index*(size+2)+1, size, index*(size+2)+1+size)).getbbox()
                        assert not image.crop((0, index*(size+2), size, index*(size+2)+1)).getbbox()
    for entry in manifest['objects'].values():
        for ordinal, (kind, size) in ATLAS.items():
            with Image.open(destination / entry[{'mov2': 'movement', 'atk2': 'attack', 'spc2': 'special'}[kind]]) as image:
                assert image.mode == 'RGBA'
                assert image.size == (size, (size + 2) * manifest['atlasFrameCounts'][kind])
                assert image.getbbox()
    assert len(manifest['maps']) == 8


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source', type=Path, default=ROOT / 'content/examples/linchong/assets')
    parser.add_argument('--output', type=Path, default=ROOT / 'content/assets')
    parser.add_argument('--manifest', type=Path, default=ROOT / 'content/examples/linchong/assets/jojo-asset-mapping.json')
    args = parser.parse_args()
    result = package(args.source, args.output, args.manifest)
    verify(args.output, result)
    print(f"Packaged and verified {len(result['generatedFiles'])} textures: {len(result['maps'])} maps, 24 portraits, {len(result['characters'])} characters and {len(result['objects'])} object mappings")
