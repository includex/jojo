#!/usr/bin/env python3
"""Compare independently observed natural first-dialogue strings, requiring every selected sample."""
import argparse
import hashlib
import json
import math
from pathlib import Path

from verify_opening_panel_pixels import compare


def index_captures(manifest):
    full = manifest['fullText']
    expected = [full[:5], full[:9], full]
    if len(set(expected)) != 3:
        raise ValueError('expected three distinct first-dialogue sample strings')
    if manifest['dialogueInputs'] != 0:
        raise ValueError('dialogue input invalidates natural-prefix capture')
    rows = manifest['captures']
    indexed = {row['text']: row for row in rows}
    if len(indexed) != len(rows) or set(indexed) != set(expected):
        raise ValueError('missing, duplicate or unexpected visible prefix')
    for text, row in indexed.items():
        if row['complete'] != (text == full):
            raise ValueError('completion state does not match observed prefix')
    return indexed


def verify(source_path, game_path):
    source, game = [json.loads(path.read_text()) for path in (source_path, game_path)]
    for key in ('fullText', 'width', 'height', 'origin'):
        if source[key] != game[key]:
            raise ValueError(f'manifest mismatch: {key}')
    if (source['width'], source['height'], source['origin']) != (2560, 1376, 'bottom-left'):
        raise ValueError('unexpected framebuffer dimensions or origin')
    for manifest in (source, game):
        if manifest.get('contract') != 'natural-first-dialogue-prefixes-rgba8':
            raise ValueError('unsupported natural-prefix capture contract')
    source_rows, game_rows = index_captures(source), index_captures(game)
    for rows in (source['captures'], game['captures']):
        if any(a['frame'] >= b['frame'] for a, b in zip(rows, rows[1:])):
            raise ValueError('capture frames must increase in sample order')
        times = [r.get('performanceMs', r.get('elapsedSeconds')) for r in rows]
        if any(t is None or not math.isfinite(t) for t in times) or any(a >= b for a, b in zip(times, times[1:])):
            raise ValueError('capture times must increase in sample order')
    for row in game_rows.values():
        if row.get('naturalStreetTextIsolation') is not True:
            raise ValueError('game did not observe natural isolated rendering')
    for text, row in source_rows.items():
        if row.get('trigger') != 'EVENT_AFTER_DRAW' or row.get('content') != text or row.get('richText') != text:
            raise ValueError('source did not render the observed natural prefix')
        if row.get('remaining') != source['fullText'][len(text):] or row.get('handle') != (None if row['complete'] else 'active'):
            raise ValueError('source scheduler did not naturally consume the prefix')
        if ''.join(s['string'] for s in row['segments']) != text:
            raise ValueError('source label segments do not match the observed prefix')
    samples = []
    for text, source_row in source_rows.items():
        game_row = game_rows[text]
        buffers = []
        for manifest_path, row in ((source_path, source_row), (game_path, game_row)):
            raw = (manifest_path.parent / row['file']).read_bytes()
            if hashlib.sha256(raw).hexdigest() != row['sha256']:
                raise ValueError(f'stale or corrupt capture: {row["file"]}')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(text=text, scope='same observed string in natural playback; no timing-equivalence claim')
        samples.append(result)
    return {'contract': 'natural-opening-prefix-pixels/v1', 'equal': all(s['equal'] for s in samples),
            'sampleCount': len(samples), 'samples': samples}


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
