#!/usr/bin/env python3
"""Compare original background decode/upload and the port's actual cached texture."""
import argparse
import hashlib
import json
from pathlib import Path


def raw(root, file, size, digest):
    data = (root / file).read_bytes()
    if len(data) != size or hashlib.sha256(data).hexdigest() != digest:
        raise ValueError(f'Invalid raw payload: {file}')
    return data


def difference(left, right):
    if len(left) != len(right) or len(left) % 4:
        raise ValueError('RGBA dimensions differ')
    changed = sum(left[i:i + 4] != right[i:i + 4] for i in range(0, len(left), 4))
    errors = [abs(a - b) for a, b in zip(left, right)]
    return {'equal': changed == 0, 'changedPixels': changed,
            'absoluteRgbaError': sum(errors), 'maxChannelError': max(errors, default=0)}


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    if source['contract'] != 'natural-opening-event-rgba8' or game['contract'] != 'opening-background-texture/v1':
        raise ValueError('Unexpected capture contract')
    bg = source['backgroundTexture']
    width, height = game['width'], game['height']
    if bg['texture']['width'] != width or bg['texture']['height'] != height or bg['frameRect'] != [0, 0, width, height] or bg['rotated']:
        raise ValueError('Texture geometry differs')
    if bg['resourcePath'] != f"Mmap/Mmap_{game['backgroundId']}-1":
        raise ValueError('Background identity differs')
    if bg['origin'] != 'bottom-left texture coordinates' or bg['canvasOrigin'] != 'top-left browser image coordinates':
        raise ValueError('Unexpected source row convention')
    if game['origin'] != 'top-left image rows; GPU texture y=0 is uploaded first row':
        raise ValueError('Unexpected game row convention')
    jpeg = (Path(source['sourceRoot']) / bg['texture']['url']).read_bytes()
    jpeg_sha = hashlib.sha256(jpeg).hexdigest()
    if jpeg_sha != game.get('jpegSha256', game['assetSha256']):
        raise ValueError('Native JPEGs differ')
    size = width * height * 4
    src_gpu = raw(source_path.parent, bg['file'], size, bg['sha256'])
    src_cpu = raw(source_path.parent, bg['canvasFile'], size, bg['canvasSha256'])
    port = {key: raw(game_path.parent, game[key]['file'], size, game[key]['sha256']) for key in ('cpu', 'gpu')}
    comparisons = {'sourceCanvasToGpu': difference(src_cpu, src_gpu),
                   'gameCpuToGpu': difference(port['cpu'], port['gpu']),
                   'sourceToGameGpu': difference(src_gpu, port['gpu'])}
    return {'contract': 'opening-background-pixels/v1', 'width': width, 'height': height,
            'jpegSha256': jpeg_sha, 'equal': all(row['equal'] for row in comparisons.values()),
            'scope': 'native background texture decode and upload; not full-frame or timing equivalence',
            'comparisons': comparisons}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report))
    return 0 if report['equal'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
