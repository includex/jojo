#!/usr/bin/env python3
"""Package selected native atlases or authored sheets for Jojo's atlas ABI.

Selected release atlases are copied unchanged. The legacy sheet path performs
alpha-gutter extraction, explicit source pivots and scale, nearest-neighbor
reduction, and transparent row padding. Neither path synthesizes poses.
"""
from __future__ import annotations
import argparse
import hashlib
import json
import shutil
import statistics
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
SHEET_LAYOUT = {0: (0, 12), 1: (12, 11), 2: (23, 5)}


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


def load_release_selection(source: Path) -> dict | None:
    selection_path = source / 'release-sprites/selection.json'
    if not selection_path.is_file():
        return None
    selection = json.loads(selection_path.read_text(encoding='utf-8'))
    characters = selection.get('characters')
    if not isinstance(characters, dict) or set(characters) != set(CHARACTERS + ['assassin', 'lin_b01']):
        raise ValueError(f'{selection_path}: selection must contain base characters and assassin')
    for character_id, entry in characters.items():
        if not isinstance(entry, dict) or not all(entry.get(key) for key in ('setId', 'name', 'reason', 'sourceTitle')):
            raise ValueError(f'{selection_path}: incomplete selection for {character_id}')
        files = entry.get('files')
        if not isinstance(files, dict) or set(files) != {'atk', 'mov', 'spc'}:
            raise ValueError(f'{selection_path}: {character_id} requires atk, mov, spc files')
        for kind, selected in files.items():
            if not isinstance(selected, dict) or not isinstance(selected.get('path'), str) or not isinstance(selected.get('sha256'), str):
                raise ValueError(f'{selection_path}: invalid {character_id}/{kind} file')
            relative = Path(selected['path'])
            if relative.is_absolute() or '..' in relative.parts or relative.parts[:1] != ('release-sprites',):
                raise ValueError(f'{selection_path}: invalid source path {relative}')
            path = source / relative
            if not path.is_file() or digest(path) != selected['sha256']:
                raise ValueError(f'{selection_path}: missing or hash mismatch for {relative}')
    return selection


def pack_atlas(frames: list[Image.Image], size: int) -> Image.Image:
    atlas = Image.new('RGBA', (size, (size + 2) * len(frames)), (0, 0, 0, 0))
    for index, frame in enumerate(frames):
        # The complete source cell receives the same transform for every pose.
        # Never fit individual alpha bounds: that makes animation jitter.
        reduced = frame.resize((size, size), Image.Resampling.NEAREST)
        atlas.paste(reduced, (0, index * (size + 2) + 1))
    return atlas


def sheet_frames(path: Path) -> list[Image.Image]:
    with Image.open(path) as opened:
        sheet = opened.convert('RGBA')
    if sheet.width < 4 * 64 or sheet.height < 7 * 64:
        raise ValueError(f'{path}: too small for a 4x7 sprite sheet: {sheet.size}')
    # Generated PNGs can contain near-invisible alpha noise connecting poses
    # across otherwise clear gutters. Remove only alpha < 8/255 before finding
    # gutters; this does not change visible authored pixels at native size.
    alpha = sheet.getchannel('A').point(lambda value: value if value >= 8 else 0)
    sheet.putalpha(alpha)

    def cuts(projection: list[int], count: int) -> list[int]:
        length = len(projection)
        result = [0]
        for i in range(1, count):
            expected = i * length // count
            radius = int(length / count * .39)
            candidates = range(max(result[-1] + 1, expected - radius), min(length, expected + radius + 1))
            result.append(min(candidates, key=lambda x: (projection[x], abs(x - expected))))
        return result + [length]

    # AI-authored sheets often have uneven row heights even when the intended
    # layout is 4x7. Split on transparent gutters near grid lines, preserving
    # every alpha pixel. Cuts are chosen per row for the four columns.
    row_projection = [sum(alpha.crop((0, y, sheet.width, y + 1)).getdata()) // 255 for y in range(sheet.height)]
    row_cuts = cuts(row_projection, 7)
    assigned = []
    centers_by_col = [[] for _ in range(4)]
    for row in range(7):
        top, bottom = row_cuts[row:row + 2]
        band = sheet.crop((0, top, sheet.width, bottom))
        width, height = band.size
        values = band.getchannel('A').tobytes()
        visited = bytearray(width * height)
        masks = [bytearray(width * height) for _ in range(4)]
        centers = [(col + .5) * width / 4 for col in range(4)]
        for seed in range(len(values)):
            if not values[seed] or visited[seed]:
                continue
            visited[seed] = 1
            stack = [seed]
            component = []
            sum_x = 0
            min_x, max_x = width, 0
            while stack:
                index = stack.pop()
                component.append(index)
                x, y = index % width, index // width
                sum_x += x
                min_x, max_x = min(min_x, x), max(max_x, x)
                for ny in range(max(0, y - 1), min(height, y + 2)):
                    for nx in range(max(0, x - 1), min(width, x + 2)):
                        neighbor = ny * width + nx
                        if values[neighbor] and not visited[neighbor]:
                            visited[neighbor] = 1
                            stack.append(neighbor)
            centroid = sum_x / len(component)
            span = max_x - min_x + 1
            if len(component) > 1000 and span > width / 4 * 1.4:
                raise ValueError(
                    f'{path}: row {row} has a connected pose/effect spanning x={min_x}..{max_x} '
                    f'({span}px across a {width / 4:.0f}px column pitch); regenerate with transparent gutters'
                )
            col = min(range(4), key=lambda c: abs(centroid - centers[c]))
            if len(component) > 1000:
                centers_by_col[col].append((len(component), centroid))
            for index in component:
                masks[col][index] = values[index]
        for col in range(4):
            mask = Image.frombytes('L', (width, height), bytes(masks[col]))
            isolated = band.copy()
            isolated.putalpha(mask)
            assigned.append((isolated, mask.getbbox(), col))
    aligned_centers = []
    for col in range(4):
        candidates = sorted(centers_by_col[col], reverse=True)[:7]
        aligned_centers.append(statistics.median(value for _, value in candidates) if candidates else (col + .5) * sheet.width / 4)
    # Hu's generated sheet is wider than the requested 4:7 canvas, with
    # intentionally inset columns. These four authored grid centers are shared
    # by every pose in each column; no individual frame is fit or recentered.
    if path.stem == 'hu' and sheet.size == (1185, 1327):
        aligned_centers = [240, 470, 715, 970]
    extents = [0.0, 0.0, 0.0]
    heights = [0, 0, 0]
    for index, (isolated, bounds, col) in enumerate(assigned):
        category = 0 if index < 12 else 1 if index < 23 else 2
        if bounds:
            heights[category] = max(heights[category], bounds[3] - bounds[1])
            extents[category] = max(extents[category], aligned_centers[col] - bounds[0], bounds[2] - aligned_centers[col])
    frames = []
    for index, (isolated, bounds, col) in enumerate(assigned):
        category = 0 if index < 12 else 1 if index < 23 else 2
        side = max(heights[category], int(extents[category] * 2 + 1))
        left = round(aligned_centers[col] - side / 2)
        foot = bounds[3] if bounds else isolated.height
        frame = isolated.crop((left, foot - side, left + side, foot))
        if frame.getchannel('A').getbbox() is None:
            raise ValueError(f'{path}: missing pose after alpha isolation')
        frames.append(frame)
    if any(not frame.getchannel('A').getbbox() for frame in frames):
        raise ValueError(f'{path}: one or more of the 28 authored frames are empty')
    for index, frame in enumerate(frames):
        alpha = frame.getchannel('A')
        coverage = sum(value > 0 for value in alpha.getdata()) / (frame.width * frame.height)
        if not 0.02 <= coverage <= 0.9:
            raise ValueError(f'{path}: frame {index} has suspicious alpha coverage {coverage:.1%}')
    return frames


def framed_frames(path: Path, mirrors: list[int]) -> list[Image.Image]:
    """Apply reviewed source-pixel pivots and one scale to all three atlases.

    Coordinates refer to sheet_frames' isolated square cells, before mirroring.
    Native pivots are centered on x=24/32; attack feet at y=56 compensate
    for the renderer's centered 64px canvas around its 48px movement canvas.
    """
    frames = sheet_frames(path)
    metadata_path = path.parent / 'framing.json'
    metadata = json.loads(metadata_path.read_text())['characters'][path.stem]
    if len(metadata.get('frames', [])) != 28 or metadata.get('sourcePixelScale', 0) <= 0:
        raise ValueError(f'{metadata_path}: {path.stem} needs 28 explicit pivots and a positive scale')
    if metadata.get('sourceSpriteSha256') and metadata['sourceSpriteSha256'] != digest(path):
        raise ValueError(f'{metadata_path}: {path.stem} changed since framing review')
    result = []
    for index, frame in enumerate(frames):
        scale = metadata['frames'][index].get('sourcePixelScale', metadata['sourcePixelScale'])
        size = 64 if index < 12 else 48
        pivot = metadata['frames'][index]['pivot']
        target = metadata['frames'][index]['target']
        reduced = frame.resize((round(frame.width * scale), round(frame.height * scale)), Image.Resampling.NEAREST)
        native = Image.new('RGBA', (size, size))
        origin = (round(target[0] - pivot[0] * scale), round(target[1] - pivot[1] * scale))
        bounds = reduced.getchannel('A').getbbox()
        if bounds and (bounds[0] + origin[0] < 0 or bounds[1] + origin[1] < 0 or bounds[2] + origin[0] > size or bounds[3] + origin[1] > size):
            raise ValueError(f'{path}: framing clips authored alpha in frame {index}; review framing.json')
        native.paste(reduced, origin)
        if index in mirrors:
            native = native.transpose(Image.Transpose.FLIP_LEFT_RIGHT)
        result.append(native)
    return result


def load_adjustments(sheet_dir: Path) -> dict[str, list[int]]:
    path = sheet_dir / 'frame-adjustments.json'
    if not path.exists():
        return {}
    data = json.loads(path.read_text())
    if not isinstance(data, dict) or any(name not in CHARACTERS for name in data):
        raise ValueError(f'{path}: expected character-to-frame-index mapping')
    for name, indices in data.items():
        if not isinstance(indices, list) or len(set(indices)) != len(indices) or any(type(i) is not int or not 0 <= i < 28 for i in indices):
            raise ValueError(f'{path}: invalid mirror frame list for {name}')
    return data


def adjusted_frames(path: Path, mirrors: list[int]) -> list[Image.Image]:
    frames = sheet_frames(path)
    for index in mirrors:
        frames[index] = frames[index].transpose(Image.Transpose.FLIP_LEFT_RIGHT)
    return frames


def package(source: Path, destination: Path, manifest_path: Path, sprite_dir: str = 'sprites-v3') -> dict:
    source = source.resolve()
    destination = destination.resolve()
    timeline_path = ROOT / 'content/assets/maps/battle-anime.json'
    counts = frame_counts(json.loads(timeline_path.read_text()))
    token_source = source / 'tokens.png'
    portrait_source = source / 'portraits.png'
    support_source = source / 'portraits-support.png'
    release = load_release_selection(source)
    sheet_dir = source / sprite_dir
    if release is None:
        if Path(sprite_dir).is_absolute() or '..' in Path(sprite_dir).parts:
            raise ValueError('sprite_dir must be a relative directory within source')
        # A partial directory must fail rather than ship static token frames.
        if sheet_dir.exists():
            missing = [name for name in CHARACTERS if not (sheet_dir / f'{name}.png').is_file()]
            if missing:
                raise FileNotFoundError(f'Incomplete {sprite_dir}: missing {", ".join(missing)}')
        else:
            raise FileNotFoundError(f'Required 28-frame sprite sheets missing: {sheet_dir}')
    if counts != {0: 12, 1: 11, 2: 5}:
        raise ValueError(f'Runtime atlas frame contract changed: {counts}')
    adjustments = {} if release else load_adjustments(sheet_dir)
    manifest = {
        'format': 'jojo-existing-assets-v2',
        'authoredSpriteDirectory': 'release-sprites' if release else sprite_dir,
        'description': ('Selected native character atlases copied unchanged into original Jojo atlas slots. Runtime timeline and engine remain unchanged.'
                        if release else 'Authored 28-frame character sheets packed into original Jojo atlas slots. Runtime timeline and engine remain unchanged.'),
        'sourceHashes': {p.name: digest(p) for p in (token_source, portrait_source, support_source)},
        'animationTimelineSha256': digest(timeline_path),
        'atlasFrameCounts': {ATLAS[k][0]: v for k, v in counts.items()},
        'characters': {}, 'objects': {}, 'maps': {}, 'generatedFiles': [],
    }
    if release:
        manifest['sourceHashes']['release-sprites/selection.json'] = digest(source / 'release-sprites/selection.json')
    else:
        manifest['frameAdjustments'] = adjustments
        manifest['framingSha256'] = digest(sheet_dir / 'framing.json')
        manifest['sourceHashes'][f'{sprite_dir}/framing.json'] = digest(sheet_dir / 'framing.json')
        if (sheet_dir / 'frame-adjustments.json').exists():
            manifest['sourceHashes'][f'{sprite_dir}/frame-adjustments.json'] = digest(sheet_dir / 'frame-adjustments.json')
    for slot, character_id in enumerate(CHARACTERS):
            avatar_id = 601 + slot
            paths = {}
            if release:
                selected = release['characters'][character_id]
            else:
                sheet_path = sheet_dir / f'{character_id}.png'
                frames = framed_frames(sheet_path, adjustments.get(character_id, []))
                manifest['sourceHashes'][f'{sprite_dir}/{character_id}.png'] = digest(sheet_path)
            for ordinal, (kind, size) in ATLAS.items():
                relative = Path('maps/units') / kind / f'{avatar_id}.png'
                output = destination / relative
                output.parent.mkdir(parents=True, exist_ok=True)
                if release:
                    source_file = selected['files'][('atk', 'mov', 'spc')[ordinal]]
                    shutil.copyfile(source / source_file['path'], output)
                    manifest['sourceHashes'][source_file['path']] = source_file['sha256']
                else:
                    start, count = SHEET_LAYOUT[ordinal]
                    atlas = pack_atlas(frames[start:start + count], size)
                    atlas.save(output, optimize=True)
                paths[kind] = str(relative)
                manifest['generatedFiles'].append(str(relative))
            entry = {
                'characterId': 512 + slot, 'avatarId': avatar_id, 'battleAvatar': avatar_id - 304, 'battleAvatarType': -1, 'face': 600 + slot,
                'headTextureId': 608 + slot,
                'movement': paths['mov2'], 'attack': paths['atk2'], 'special': paths['spc2'],
            }
            if release:
                entry['sourceRelease'] = selected
            else:
                entry['sourceSpriteSheet'] = f'{sprite_dir}/{character_id}.png'
                entry['sourceFrameLayout'] = {'attack': [0, 12], 'movement': [12, 11], 'special': [23, 5]}
            manifest['characters'][character_id] = entry
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
    if release:
        # Battle-specific atlas roles reuse their character's dialogue portrait.
        for role, base, character_id, avatar_id in [('assassin', 'guard', 528, 621), ('lin_b01', 'lin', 512, 622)]:
            selected = release['characters'][role]
            entry = dict(manifest['characters'][base])
            entry.update(characterId=character_id, avatarId=avatar_id, battleAvatar=avatar_id - 304, sourceRelease=selected)
            for kind, field in [('atk', 'attack'), ('mov', 'movement'), ('spc', 'special')]:
                selected_file = selected['files'][kind]
                relative = Path('maps/units') / (kind + '2') / f'{avatar_id}.png'
                shutil.copyfile(source / selected_file['path'], destination / relative)
                entry[field] = str(relative)
                manifest['sourceHashes'][selected_file['path']] = selected_file['sha256']
                manifest['generatedFiles'].append(str(relative))
            manifest['characters'][role] = entry
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


def verify(destination: Path, manifest: dict, source: Path) -> None:
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
                        y = index * (size + 2)
                        frame = image.crop((0, y + 1, size, y + 1 + size))
                        assert frame.getchannel('A').getbbox(), f'{entry["avatarId"]}/{kind}/{index}: empty'
                        assert not image.crop((0, y, size, y + 1)).getbbox()
                        assert not image.crop((0, y + 1 + size, size, y + 2 + size)).getbbox()
            if 'sourceRelease' in entry:
                for ordinal, (kind, size) in ATLAS.items():
                    selected = entry['sourceRelease']['files'][('atk', 'mov', 'spc')[ordinal]]
                    source_path = source / selected['path']
                    atlas_path = destination / entry[{'mov2': 'movement', 'atk2': 'attack', 'spc2': 'special'}[kind]]
                    if digest(source_path) != selected['sha256'] or source_path.read_bytes() != atlas_path.read_bytes():
                        raise AssertionError(f'{atlas_path}: differs from selected native atlas {source_path}')
                continue
            source_path = source / entry['sourceSpriteSheet']
            authored = framed_frames(source_path, manifest['frameAdjustments'].get(source_path.stem, []))
            hashes = [hashlib.sha256(frame.tobytes()).hexdigest() for frame in authored]
            if len(set(hashes)) != 28:
                raise AssertionError(f'{source_path}: expected 28 distinct authored frames')
            for start in (0, 4, 8):
                if len(set(hashes[start:start + 4])) != 4:
                    raise AssertionError(f'{source_path}: duplicated attack phase in {start}..{start + 3}')
            for ordinal, (kind, size) in ATLAS.items():
                start, count = SHEET_LAYOUT[ordinal]
                atlas_path = destination / entry[{'mov2': 'movement', 'atk2': 'attack', 'spc2': 'special'}[kind]]
                with Image.open(atlas_path) as image:
                    for i in range(count):
                        actual = image.crop((0, i * (size + 2) + 1, size, i * (size + 2) + 1 + size))
                        expected = authored[start + i].resize((size, size), Image.Resampling.NEAREST)
                        if actual.tobytes() != expected.tobytes():
                            raise AssertionError(f'{atlas_path}: slot {i} differs from authored cell {start + i}')
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
    parser.add_argument('--sprite-dir', default='sprites-v3', help='Legacy authored-sheet directory inside --source; ignored when release-sprites/selection.json exists')
    parser.add_argument('--output', type=Path, default=ROOT / 'content/assets')
    parser.add_argument('--manifest', type=Path, default=ROOT / 'content/examples/linchong/assets/jojo-asset-mapping.json')
    args = parser.parse_args()
    result = package(args.source, args.output, args.manifest, args.sprite_dir)
    verify(args.output, result, args.source)
    print(f"Packaged and verified {len(result['generatedFiles'])} textures: {len(result['maps'])} maps, 24 portraits, {len(result['characters'])} characters and {len(result['objects'])} object mappings")
