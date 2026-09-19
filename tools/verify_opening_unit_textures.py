#!/usr/bin/env python3
"""Compare actual source and port GPU texels for opening actors 181 and 182."""
import argparse
import hashlib
import json
from pathlib import Path


def verify(source_path, game_path):
    source = json.loads(source_path.read_text())
    game = json.loads(game_path.read_text())
    if source['contract'] != 'natural-opening-full-scene-rgba8-v1' or game['contract'] != 'opening-unit-gpu-textures/v1':
        raise ValueError('unexpected GPU capture contract')
    samples = []
    for actor_id in (181, 182):
        gpu = next(a['gpu'] for a in source['actors'] if a['id'] == actor_id)
        port = next(t for t in game['textures'] if t['actorId'] == actor_id)
        if gpu['textureCrop']['rect'] != [0, 0, 48, 1280] or (port['width'], port['height']) != (48, 1280):
            raise ValueError('complete unit texture required')
        if gpu['textureCrop']['origin'] != game['origin'] or game['origin'] != 'bottom-left texture coordinates':
            raise ValueError('texture origin mismatch')
        buffers = []
        for manifest, row in ((source_path, gpu['textureCrop']), (game_path, port)):
            raw = (manifest.parent / row['file']).read_bytes()
            if len(raw) != 48 * 1280 * 4 or hashlib.sha256(raw).hexdigest() != row['sha256']:
                raise ValueError('invalid or stale GPU texture capture')
            buffers.append(raw)
        changed = sum(buffers[0][i:i+4] != buffers[1][i:i+4] for i in range(0, len(buffers[0]), 4))
        samples.append({'actorId': actor_id, 'changedPixels': changed, 'equal': changed == 0,
                        'sourceSha256': gpu['textureCrop']['sha256'], 'gameSha256': port['sha256']})
    return {'contract': 'opening-unit-gpu-texture-comparison/v1', 'equal': all(s['equal'] for s in samples),
            'samples': samples, 'scope': 'Actual complete GPU textures only; vertex, sampling and framebuffer results require separate verification.'}


if __name__ == '__main__':
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
    raise SystemExit(0 if report['equal'] else 1)
