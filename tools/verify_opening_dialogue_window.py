#!/usr/bin/env python3
"""Compare configured natural dialogue completion frames, with input evidence."""
import argparse
import hashlib
import json
import math
from pathlib import Path

from verify_opening_panel_pixels import compare


def require(condition, message):
    if not condition:
        raise ValueError(message)


def canonical(text):
    return text.replace('<br/>', '\n')


def validate(manifest, case, case_hash, source=False):
    require(manifest['contract'] == 'natural-opening-dialogue-window-rgba8', 'contract')
    require((manifest['width'], manifest['height'], manifest['origin']) ==
            (2560, 1376, 'bottom-left'), 'framebuffer contract')
    require(manifest['isolation'] is False and manifest['clockMode'] == 'natural', 'natural capture')
    require(manifest['caseSha256'] == case_hash, 'case digest')
    expected = case['expectedPages']
    require(manifest['capturePages'] == case['capturePages'], 'selected pages')
    rows = manifest['completions'] if source else manifest['dialogueCompletions']
    inputs = manifest['inputs']
    require(len(rows) == len(expected), 'completion count')
    require(len(inputs) == manifest['dialogueInputs'] == len(expected) - 1, 'input count')
    if source:
        require(manifest['bootstrap']['autoDialogueAdvance'] is False, 'source auto advance')
        require(len(manifest['inputRequests']) == len(inputs), 'source input request count')
        groups = case['sourceLayerGroups']
        require(len(groups) == len(rows), 'layer group count')
        for i, row in enumerate(rows):
            for j in range(i):
                require((row['layerIdentity'] == rows[j]['layerIdentity']) ==
                        (groups[i] == groups[j]), 'source layer lifecycle')
    for row, want in zip(rows, expected):
        ids = [a['id'] for a in row['actors']]
        require(len(ids) == len(set(ids)) and sorted(ids) == case['actorIds'], 'complete actor identities')
        require((row['page'], row['speakerId'], row['text'], row['complete']) ==
                (want['page'], want['speakerId'], want['text'], True), 'observed page identity')
        require(math.isfinite(row['elapsedSeconds']), 'finite elapsed time')
        if source:
            require(row['trigger'] == 'EVENT_AFTER_DRAW' and row['remaining'] == '' and
                    row['typingHandle'] is None, 'source completion phase')
            require(canonical(row['content']) == row['text'] and
                    canonical(row['rawText']) == row['text'] and
                    row['rawContent'] == row['rawText'], 'source raw multiline text')
            require(bool(row['layerIdentity']), 'source layer identity')
        else:
            require(row['observationPhase'] == 'post-render' and
                    row['firstObservedFrame'] == row['frame'], 'port completion phase')
    for before, after, event in zip(rows, rows[1:], inputs):
        require(before['frame'] <= event['frame'] < after['frame'] and
                before['elapsedSeconds'] <= event['elapsedSeconds'] <= after['elapsedSeconds'], 'input ordering')
        require(event['afterPage'] == before['page'] and event['completeBeforeInput'] is True and
                event['speakerId'] == before['speakerId'] and
                canonical(event['textBeforeInput']) == before['text'], 'input identity/completion')
        if source:
            require(event['kind'] == 'pointer' and event['target'] == 'Panel_cancel' and
                    event['handler'] == 'DialogueLayer._next pass-through' and
                    event['layerIdentity'] == before['layerIdentity'], 'source input handler')
            request = manifest['inputRequests'][before['page'] - 1]
            require(request['complete'] is True and request['button'] is True and
                    request['text'] == before['text'] and request['speakerId'] == before['speakerId'] and
                    request['layerIdentity'] == before['layerIdentity'], 'source input request identity')
            require(before['frame'] <= request['frame'] <= event['frame'] and
                    before['elapsedSeconds'] <= request['elapsedSeconds'] <= event['elapsedSeconds'], 'source request ordering')
        else:
            require(event['kind'] == 'InputProcessor.keyDown/keyUp(SPACE)', 'port input route')
    captures = manifest['captures']
    require([r['page'] for r in captures] == case['capturePages'], 'capture ordering')
    for row in captures:
        completion = rows[row['page'] - 1]
        for key in ('page', 'speakerId', 'text', 'complete', 'frame', 'elapsedSeconds', 'actors'):
            require(row[key] == completion[key], 'capture must match first completion: ' + key)
        require((row['width'], row['height']) == (2560, 1376), 'raw dimensions')
    return captures


def verify(case_path, source_path, game_path):
    case_bytes = case_path.read_bytes()
    case = json.loads(case_bytes)
    require(case['contract'] == 'opening-dialogue-window-case-v1', 'case contract')
    require(bool(case['expectedPages']) and bool(case['capturePages']) and bool(case['actorIds']), 'nonempty case')
    require(case['actorIds'] == sorted(set(case['actorIds'])), 'unique case actors')
    require([r['page'] for r in case['expectedPages']] == list(range(1, len(case['expectedPages']) + 1)), 'case page sequence')
    require(case['capturePages'] == sorted(set(case['capturePages'])) and
            all(1 <= p <= len(case['expectedPages']) for p in case['capturePages']), 'case capture sequence')
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    digest = hashlib.sha256(case_bytes).hexdigest()
    source_rows, game_rows = validate(source, case, digest, True), validate(game, case, digest)
    samples = []
    for sr, gr in zip(source_rows, game_rows):
        source_actors = {a['id']: a for a in sr['actors']}
        game_actors = {a['id']: a for a in gr['actors']}
        require(source_actors.keys() == game_actors.keys(), 'actor identities')
        for actor_id, actor in source_actors.items():
            for key in ('logical', 'action', 'direction', 'frameRow', 'flipX', 'showSpeechBubble'):
                require(actor[key] == game_actors[actor_id][key], f'page {sr["page"]} actor {actor_id} {key}')
        buffers = []
        for path, row in ((source_path, sr), (game_path, gr)):
            require(Path(row['file']).name == row['file'], 'adjacent raw file')
            raw = (path.parent / row['file']).read_bytes()
            require(len(raw) == 2560 * 1376 * 4 and hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw size/hash')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(contract='natural-opening-dialogue-completion-comparison/v1',
                      scope='Entire natural completed dialogue framebuffer, all RGBA channels without excluded regions.',
                      page=sr['page'], speakerId=sr['speakerId'], text=sr['text'])
        samples.append(result)
    return {'contract': 'natural-opening-dialogue-window-comparison/v1',
            'equal': all(r['equal'] for r in samples), 'samples': samples,
            'scope': 'Selected first natural completed dialogue full RGBA frames; intermediate frames are not covered.'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('case', type=Path)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.case, args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps(report, ensure_ascii=False))
    raise SystemExit(0 if report['equal'] else 1)
