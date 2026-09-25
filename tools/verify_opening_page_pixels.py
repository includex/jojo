#!/usr/bin/env python3
"""Strict isolated opening pages reached by two completed-dialogue inputs."""
import argparse
import hashlib
import json
import math
from pathlib import Path

from dialogue_text_catalog import dialogue_text
from verify_opening_panel_pixels import compare

TEXTS = [dialogue_text(f"opening_scene1_page{page}") for page in range(1, 4)]
SPEAKERS = ['181', '0', '157']


def validate(manifest, source=False):
    if (manifest.get('contract'), manifest.get('width'), manifest.get('height'), manifest.get('origin')) != (
            'natural-opening-pages-rgba8', 2560, 1376, 'bottom-left'):
        raise ValueError('unsupported page capture contract')
    rows = manifest['captures']
    if len(rows) != 3 or [r['page'] for r in rows] != [1, 2, 3]:
        raise ValueError('all three ordered pages required')
    inputs = manifest['inputs']
    if manifest['dialogueInputs'] != 2 or len(inputs) != 2:
        raise ValueError('exactly two dialogue inputs required')
    for i, row in enumerate(rows):
        if (row['text'], row['speakerId'], row['complete'], row['dialogueInputs']) != (TEXTS[i], SPEAKERS[i], True, i):
            raise ValueError('page text, speaker, completion or input count mismatch')
        if row.get('side') != ['right', 'left', 'right'][i]:
            raise ValueError('page side mismatch')
        observed = row['observedStrings']
        if not observed or observed[-1] != TEXTS[i] or any(not TEXTS[i].startswith(t) for t in observed):
            raise ValueError('invalid observed page strings')
        if not any(t and t != TEXTS[i] for t in observed):
            raise ValueError('natural partial page observation required')
        if any(len(a) >= len(b) for a, b in zip(observed, observed[1:])):
            raise ValueError('observed page strings must increase')
        if not math.isfinite(row['elapsedSeconds']):
            raise ValueError('invalid capture time')
        if source:
            if row.get('trigger') != 'EVENT_AFTER_DRAW' or row.get('content') != row['text'] or row.get('richText') != row['text']:
                raise ValueError('source rendered text mismatch')
            if row.get('remaining') != '' or row.get('handle') is not None:
                raise ValueError('source scheduler has not completed')
            if ''.join(s['string'] for s in row['segments']) != row['text']:
                raise ValueError('source label segments mismatch')
        elif row.get('naturalStreetTextIsolation') is not True:
            raise ValueError('game natural isolation required')
    for i, event in enumerate(inputs):
        before, after = rows[i:i + 2]
        if event['afterPage'] != i + 1 or event.get('completeBeforeInput') is not True or event['textBeforeInput'] != TEXTS[i]:
            raise ValueError('input occurred before natural completion')
        if not before['frame'] < event['frame'] < after['frame']:
            raise ValueError('input frame must be between completed captures')
        if not before['elapsedSeconds'] <= event['elapsedSeconds'] < after['elapsedSeconds']:
            raise ValueError('input time must be between completed captures')
        if source:
            if event.get('kind') != 'pointer' or event.get('target') != 'Panel_cancel' or event.get('dialogueAdvance') is not True:
                raise ValueError('source input route mismatch')
        elif event.get('kind') != 'InputProcessor.keyDown/keyUp(SPACE)':
            raise ValueError('game input route mismatch')
    return rows


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_rows, game_rows = validate(source, source=True), validate(game)
    samples = []
    for source_row, game_row in zip(source_rows, game_rows):
        buffers = []
        for path, row in ((source_path, source_row), (game_path, game_row)):
            raw = (path.parent / row['file']).read_bytes()
            if hashlib.sha256(raw).hexdigest() != row['sha256']:
                raise ValueError('stale or corrupt page capture')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(page=source_row['page'], text=source_row['text'], speakerId=source_row['speakerId'],
                      scope='isolated completed opening page reached through normal advance input; no timing or full-scene claim')
        samples.append(result)
    return dict(contract='natural-opening-page-pixels/v1', equal=all(s['equal'] for s in samples), samples=samples)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps(report, ensure_ascii=False))
    return 0 if report['equal'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
