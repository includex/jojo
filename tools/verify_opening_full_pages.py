#!/usr/bin/env python3
"""Strict complete opening frames reached through two normal completed-dialogue inputs."""
import argparse
import hashlib
import json
import math
from pathlib import Path

from verify_opening_panel_pixels import compare

TEXTS = ['대장님, 서둘러야 해요!', '알아!', '잠시만 기다려 주세요!']
SPEAKERS = ['181', '0', '157']
INITIAL = {0: (40, 35, 2), 157: (54, 65, 0), 181: (40, 45, 0), 182: (40, 25, 2)}
AFTER_MOVE = {0: (40, 50, 2), 157: (54, 50, 3), 181: (40, 60, 2), 182: (40, 40, 2)}


def require(condition, message):
    if not condition:
        raise ValueError(message)


def validate(manifest, source=False):
    require((manifest['contract'], manifest['width'], manifest['height'], manifest['origin']) ==
            ('natural-opening-full-pages-rgba8', 2560, 1376, 'bottom-left'), 'capture contract')
    require(manifest['isolation'] is False and manifest['dialogueInputs'] == 2, 'isolation/input count')
    rows, inputs = manifest['captures'], manifest['inputs']
    require(len(rows) == 3 and len(inputs) == 2, 'three pages and two inputs required')
    if source:
        require(manifest['evidenceKind'] == 'actual-source-full-framebuffers-no-isolation', 'source evidence')
        require(manifest['bootstrap']['autoDialogueAdvance'] is False, 'source auto advance')
        endpoints = manifest['completionEndpoints']
        require(len(endpoints) == 3 and len(manifest['actualInputs']) == 2, 'source observed endpoints/inputs')
    for index, row in enumerate(rows):
        require((row['page'], row['text'], str(row['speakerId']), row['complete'], row['dialogueInputs']) ==
                (index + 1, TEXTS[index], SPEAKERS[index], True, index), 'page identity/completion')
        require(math.isfinite(row['elapsedSeconds']), 'capture time')
        expected_side = ['right', 'left', 'right'][index] if source else [1, 0, 1][index]
        require(row['side'] == expected_side, 'dialogue side')
        observed = row['observedStrings']
        require(observed and observed[-1] == row['text'] and any(t and t != row['text'] for t in observed), 'natural partial observation')
        require(all(row['text'].startswith(t) for t in observed) and all(len(a) < len(b) for a, b in zip(observed, observed[1:])), 'prefix sequence')
        if source:
            require(row['trigger'] == 'EVENT_AFTER_DRAW' and row['content'] == row['text'] and
                    row['remaining'] == '' and row['typingHandle'] is None, 'source natural completion')
            endpoint = endpoints[index]
            require(endpoint['frame'] == row['frame'] and endpoint['text'] == row['text'] and
                    endpoint['remaining'] == '' and endpoint['typingHandle'] is None, 'capture must be first complete frame')
        expected = INITIAL if index < 2 else AFTER_MOVE
        actors = row['actors']
        require(len(actors) == 4 and {a['id'] for a in actors} == set(expected), 'actor identities')
        for actor in actors:
            x, y, direction = expected[actor['id']]
            coordinates = actor['logical'] if source else [actor['visualX'], actor['visualY']]
            require(coordinates == [x, y] and actor['direction'] == direction and actor['action'] == 0, 'actor state')
            if not source:
                require(actor['visible'] and actor['moveDuration'] == 0, 'port actor visibility/motion')
            else:
                require(actor['bubbleActive'] == (str(actor['id']) == SPEAKERS[index]), 'source speech bubble')
    if source:
        identities = [row['layerIdentity'] for row in rows]
        require(all(identities) and identities[0] == identities[1] and identities[1] != identities[2], 'source dialogue layer lifecycle')
    else:
        revisions = [row['revision'] for row in rows]
        require(all(a < b for a, b in zip(revisions, revisions[1:])), 'port dialogue revisions')
    for index, event in enumerate(inputs):
        before, after = rows[index:index+2]
        require(event['afterPage'] == index + 1 and event['completeBeforeInput'] is True and event['textBeforeInput'] == TEXTS[index], 'input completion')
        require(before['frame'] < event['frame'] < after['frame'] and
                before['elapsedSeconds'] <= event['elapsedSeconds'] < after['elapsedSeconds'], 'input ordering')
        if source:
            require(event['kind'] == 'pointer' and event['target'] == 'Panel_cancel', 'source input route')
            actual = manifest['actualInputs'][index]
            require(actual['afterPage'] == index + 1 and actual['textBeforeInput'] == before['text'] and
                    actual['completeBeforeInput'] is True and actual['handler'] == 'DialogueLayer._next pass-through', 'actual source input')
            require(before['frame'] < actual['handledFrame'] < after['frame'] and
                    before['elapsedSeconds'] <= actual['handledElapsedSeconds'] < after['elapsedSeconds'], 'actual source input ordering')
        else:
            require(event['kind'] == 'InputProcessor.keyDown/keyUp(SPACE)', 'port input route')
    return rows


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_rows, game_rows = validate(source, source=True), validate(game)
    samples = []
    for source_row, game_row in zip(source_rows, game_rows):
        buffers = []
        for manifest, row in ((source_path, source_row), (game_path, game_row)):
            require(Path(row['file']).name == row['file'], 'raw file must be adjacent to manifest')
            raw = (manifest.parent / row['file']).read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw hash mismatch')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(contract='natural-opening-full-page-comparison/v1', page=source_row['page'],
                      text=source_row['text'], speakerId=source_row['speakerId'],
                      scope='Entire natural completed dialogue framebuffer, all RGBA channels without excluded regions.')
        samples.append(result)
    return {'contract': 'natural-opening-full-pages-comparison/v1', 'equal': all(s['equal'] for s in samples),
            'samples': samples, 'scope': 'First three natural completed full frames after normal input; moving frames and later scenes are not covered.'}


if __name__ == '__main__':
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
    raise SystemExit(0 if report['equal'] else 1)
