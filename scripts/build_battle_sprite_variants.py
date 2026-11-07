#!/usr/bin/env python3
"""Create optional 1.3x battle atlas variants without altering the native atlases.

Every frame is cropped before premultiplied-alpha resampling and repacked with
transparent gutters. Source-frame coordinates and displayed size remain native;
the sidecar describes the UV conversion. Resampling does not create new detail.
"""
from __future__ import annotations
import argparse
import hashlib
import json
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
VARIANT = 'runtime-1_3'


def resample_frame(frame: Image.Image, size: int, method: Image.Resampling) -> Image.Image:
    # Premultiplied alpha prevents dark RGB in transparent texels bleeding into edges.
    return frame.convert('RGBa').resize((size, size), method).convert('RGBA')


def build_variant(source: Path, output: Path, scale: float = 1.3) -> dict:
    with Image.open(source) as opened:
        atlas = opened.convert('RGBA')
    native = atlas.width
    if native not in (48, 64) or atlas.height % (native + 2):
        raise ValueError(f'{source}: expected native 48/64px frames and 2px gutters')
    count = atlas.height // (native + 2)
    size = round(native * scale)
    variant = Image.new('RGBA', (size, (size + 2) * count))
    for index in range(count):
        y = index * (native + 2)
        if atlas.crop((0, y, native, y + 1)).getchannel('A').getbbox() or atlas.crop((0, y + native + 1, native, y + native + 2)).getchannel('A').getbbox():
            raise ValueError(f'{source}: frame {index} gutter is not transparent')
        frame = atlas.crop((0, y + 1, native, y + native + 1))
        variant.paste(resample_frame(frame, size, Image.Resampling.LANCZOS), (0, index * (size + 2) + 1))
    output.parent.mkdir(parents=True, exist_ok=True)
    variant.save(output, optimize=True)
    metadata = {
        'format': 'battle-sprite-resolution/v1', 'requestedScale': scale,
        'sourceSha256': hashlib.sha256(source.read_bytes()).hexdigest(),
        'sourceFrameSize': native, 'sourcePitch': native + 2, 'sourceInsetY': 1,
        'frameSize': size, 'pitch': size + 2, 'insetY': 1, 'frameCount': count,
        'resampling': 'premultiplied-alpha Lanczos', 'textureFilter': 'nearest',
        'displaySize': 'unchanged; retain native frame geometry',
    }
    output.with_suffix('.json').write_text(json.dumps(metadata, indent=2) + '\n')
    return metadata


def build_variants(destination: Path, mapping: dict) -> dict:
    images = []
    for entry in mapping['characters'].values():
        if 'sourceRelease' not in entry:
            continue
        for field in ('movement', 'attack', 'special'):
            relative = Path(entry[field])
            output = destination / 'maps/units' / VARIANT / relative.parent.name / relative.name
            build_variant(destination / relative, output)
            images.append(str(output.relative_to(destination)))
    summary = {'format': 'battle-sprite-variants/v1', 'requestedScale': 1.3,
               'note': 'Resampled native frames, not new illustration detail. Native files are preserved.',
               'generatedImages': sorted(set(images))}
    index = destination / 'maps/units' / VARIANT / 'manifest.json'
    index.write_text(json.dumps(summary, indent=2) + '\n')
    return summary


def comparison(destination: Path, mapping: dict, output: Path):
    roles = ['lin_b01', 'dong', 'lu']
    canvas = Image.new('RGB', (672, len(roles) * 242 + 32), '#706952')
    draw = ImageDraw.Draw(canvas)
    labels = ['Native + Linear', 'Native + Nearest', '1.3x Nearest', '1.3x Lanczos']
    for col, label in enumerate(labels):
        draw.text((col * 168 + 4, 8), label, fill='white')
    for row, role in enumerate(roles):
        for action in range(2):
            atlas = Image.open(destination / mapping['characters'][role][('movement', 'attack')[action]]).convert('RGBA')
            size = atlas.width
            frame = atlas.crop((0, 1, size, size + 1))
            options = [frame.resize((96, 96), Image.Resampling.BILINEAR),
                       frame.resize((96, 96), Image.Resampling.NEAREST),
                       resample_frame(frame, round(size * 1.3), Image.Resampling.NEAREST).resize((96, 96), Image.Resampling.NEAREST),
                       resample_frame(frame, round(size * 1.3), Image.Resampling.LANCZOS).resize((96, 96), Image.Resampling.NEAREST)]
            for col, image in enumerate(options):
                x = col * 168 + 36; y = 44 + row * 242 + action * 116
                canvas.paste(image, (x, y), image)
                draw.text((col * 168 + 4, y + 98), role + (' move' if action == 0 else ' attack'), fill='white')
    output.parent.mkdir(parents=True, exist_ok=True)
    canvas.save(output)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--assets', type=Path, default=ROOT / 'content/assets')
    parser.add_argument('--mapping', type=Path, default=ROOT / 'content/examples/linchong/assets/jojo-asset-mapping.json')
    parser.add_argument('--comparison', type=Path)
    args = parser.parse_args()
    mapping = json.loads(args.mapping.read_text())
    summary = build_variants(args.assets, mapping)
    if args.comparison:
        comparison(args.assets, mapping, args.comparison)
    print(f"Generated {len(summary['generatedImages'])} frame-isolated runtime variants; native atlases unchanged")
