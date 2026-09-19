#!/usr/bin/env python3
"""Check GPU atlas interior and four extruded edge strips against the native portrait PNG."""
import argparse
import hashlib
import json
from pathlib import Path
from PIL import Image


def compare_crop(crop, native, width, height):
    stride = width + 2
    if len(native) != width * height * 4 or len(crop) != stride * (height + 2) * 4:
        raise ValueError('unexpected atlas crop/native byte count')
    # Texture coordinates retain upload row order here; framebuffer screenshots have a separate origin contract.
    expected, observed = bytearray(), bytearray()
    for y in range(height):
        expected.extend(native[y * width * 4:(y + 1) * width * 4])
        observed.extend(crop[((y + 1) * stride + 1) * 4:((y + 1) * stride + 1 + width) * 4])
    interior = sum(a != b for a, b in zip(expected, observed))
    edges = {}
    for name, points in {
        'left': [(0, y + 1, 0, y) for y in range(height)],
        'right': [(width + 1, y + 1, width - 1, y) for y in range(height)],
        'firstRow': [(x + 1, 0, x, 0) for x in range(width)],
        'lastRow': [(x + 1, height + 1, x, height - 1) for x in range(width)],
    }.items():
        edges[name] = sum(crop[(cy * stride + cx) * 4 + c] != native[(ny * width + nx) * 4 + c]
                          for cx, cy, nx, ny in points for c in range(4))
    return dict(equal=interior == 0 and not any(edges.values()), interiorChangedBytes=interior,
                edgeChangedBytes=edges, scope='native texels and four extruded strips only; corner padding and rendering equivalence not checked')


def verify_packing(insertions):
    """Replay the original two-pixel shelf allocator, including texture reuse."""
    atlases = {}
    for order, row in enumerate(insertions):
        if row['order'] != order:
            raise ValueError('packing insertion order mismatch')
        result, texture = row['result'], row['sourceTexture']
        atlas = atlases.setdefault(result['atlasId'], dict(x=2, y=2, nextY=2, textures={}))
        size = (result['atlasWidth'], result['atlasHeight'])
        if size != (2048, 2048):
            raise ValueError('unexpected dynamic atlas dimensions')
        if texture['id'] not in atlas['textures']:
            width, height = texture['width'], texture['height']
            if atlas['x'] + width + 2 > size[0]:
                atlas['x'], atlas['y'] = 2, atlas['nextY']
            atlas['nextY'] = max(atlas['nextY'], atlas['y'] + height + 2)
            if atlas['nextY'] > size[1]:
                raise ValueError('recorded insertion overflows atlas')
            atlas['textures'][texture['id']] = (atlas['x'], atlas['y'])
            atlas['x'] += width + 2
        x, y = atlas['textures'][texture['id']]
        frame_x, frame_y = row['frameRect'][:2]
        if (result['x'], result['y']) != (x + frame_x, y + frame_y):
            raise ValueError('recorded atlas placement differs from allocator')
    if not insertions:
        raise ValueError('packing trace missing')
    return dict(insertions=len(insertions), atlasCount=len(atlases), equal=True)


def verify(manifest_path, native_path, page=3):
    manifest = json.loads(manifest_path.read_text())
    if manifest.get('contract') != 'natural-opening-pages-rgba8':
        raise ValueError('unexpected capture contract')
    rows = [r for r in manifest['captures'] if r['page'] == page]
    if len(rows) != 1:
        raise ValueError('expected exactly one selected page')
    atlas = rows[0]['faceAtlas']
    x, y, width, height = atlas['frameRect']
    if atlas['cropIncludesBorder'] != 1 or atlas['cropRect'] != [x - 1, y - 1, width + 2, height + 2]:
        raise ValueError('crop does not include exactly one border pixel')
    if atlas['origin'] != 'bottom-left texture coordinates':
        raise ValueError('unexpected texture origin contract')
    if (atlas['minFilter'], atlas['magFilter'], atlas['wrapS'], atlas['wrapT']) != (9729, 9729, 33071, 33071):
        raise ValueError('unexpected atlas sampler')
    crop = (manifest_path.parent / atlas['file']).read_bytes()
    if len(crop) != atlas['byteLength'] or hashlib.sha256(crop).hexdigest() != atlas['sha256']:
        raise ValueError('stale or corrupt GPU atlas readback')
    with Image.open(native_path) as image:
        if image.size != (width, height):
            raise ValueError('native portrait size differs from frame')
        native = image.convert('RGBA').tobytes()
    result = compare_crop(crop, native, width, height)
    result['packing'] = verify_packing(manifest['atlasPacking'])
    placements = [r for r in manifest['atlasPacking'] if r['frameName'] == rows[0]['faceFrame']]
    if not placements or not any(
            (r['result']['atlasId'], r['result']['atlasWidth'], r['result']['atlasHeight'],
             r['result']['x'], r['result']['y'], *r['frameRect'][2:]) ==
            (atlas['atlasId'], atlas['atlasWidth'], atlas['atlasHeight'], x, y, width, height) for r in placements):
        raise ValueError('selected face placement missing from packing trace')
    result.update(contract='portrait-atlas-native-texels/v1', page=page, faceFrame=rows[0]['faceFrame'],
                  atlas=atlas, nativeFile=str(native_path), nativeFileSha256=hashlib.sha256(native_path.read_bytes()).hexdigest(),
                  nativeRgbaSha256=hashlib.sha256(native).hexdigest())
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('manifest', type=Path)
    parser.add_argument('native', type=Path)
    parser.add_argument('--page', type=int, default=3)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    result = verify(args.manifest, args.native, args.page)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(result, indent=2) + '\n')
    print(json.dumps(result))
    return 0 if result['equal'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
