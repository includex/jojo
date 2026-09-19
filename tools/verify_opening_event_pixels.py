#!/usr/bin/env python3
"""Compare naturally completed R00 event notices, both full scene and isolated black background."""
import argparse
import hashlib
import json
import math
from pathlib import Path
from verify_opening_panel_pixels import compare

TEXT = '재능의 첫 징후'


def validate(manifest, source=False):
    if (manifest.get('contract'), manifest.get('width'), manifest.get('height'), manifest.get('origin')) != (
            'natural-opening-event-rgba8', 2560, 1376, 'bottom-left'):
        raise ValueError('invalid event capture contract')
    if manifest['eventText'] != TEXT or manifest['dialogueInputs'] != 0:
        raise ValueError('event text or input mismatch')
    rows = [manifest[key] for key in ('fullFrame', 'isolatedOverlay')]
    if not rows[0]['frame'] < rows[1]['frame']:
        raise ValueError('isolated capture must follow full capture')
    times = [r['elapsedSeconds'] for r in rows]
    if not all(math.isfinite(t) for t in times) or not times[0] < times[1]:
        raise ValueError('event times must increase')
    if rows[1].get('clearColor') != [0, 0, 0, 1]:
        raise ValueError('isolated background must be opaque black')
    for i, row in enumerate(rows):
        if row.get('complete') is not True or row['text'] != TEXT:
            raise ValueError('event not naturally complete')
        if source:
            if (row.get('content'), row.get('richText'), row.get('remaining'), row.get('typingHandle')) != (TEXT, TEXT, '', None):
                raise ValueError('source typing is incomplete')
            if row.get('closeHandle') != 'active':
                raise ValueError('source natural auto-close timer missing')
            if row.get('trigger') != 'EVENT_AFTER_DRAW':
                raise ValueError('source not captured after draw')
        elif row.get('naturalModalIsolation') is not bool(i):
            raise ValueError('game capture isolation mismatch')
    observed = manifest.get('observedStrings', [])
    if not observed or observed[-1] != TEXT or any(not TEXT.startswith(t) for t in observed):
        raise ValueError('observed event strings missing or invalid')
    if not any(t and t != TEXT for t in observed):
        raise ValueError('natural partial event text not observed')
    return rows


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_rows, game_rows = validate(source, True), validate(game)
    samples = []
    for stage, sr, gr in zip(('fullFrame', 'isolatedOverlay'), source_rows, game_rows):
        buffers = []
        for path, row in ((source_path, sr), (game_path, gr)):
            raw = (path.parent / row['file']).read_bytes()
            if hashlib.sha256(raw).hexdigest() != row['sha256']:
                raise ValueError('corrupt event capture')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(stage=stage, scope='same completed natural event text; no typing or auto-close timing equivalence claim')
        samples.append(result)
    return dict(contract='natural-opening-event-pixels/v1', equal=all(x['equal'] for x in samples), samples=samples)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    result = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(result, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps(result, ensure_ascii=False))
    return 0 if result['equal'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
