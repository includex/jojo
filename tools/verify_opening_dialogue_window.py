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


def validate_prefixes(manifest, case, source=False):
    targets = case.get('capturePrefixes', [])
    if not targets:
        require(not manifest.get('prefixCaptures') and not manifest.get('prefixObservations'), 'unexpected prefix evidence')
        return []
    require(manifest['capturePrefixes'] == targets, 'prefix target contract')
    pairs = [(r['page'], r['length']) for r in targets]
    require(pairs == sorted(set(pairs)), 'unique ordered prefix targets')
    for page, length in pairs:
        require(type(page) is int and type(length) is int and 1 <= page <= len(case['expectedPages']), 'prefix target types/page')
        require(0 <= length < len(case['expectedPages'][page - 1]['text']), 'prefix target must precede completion')
    observations = manifest['prefixObservations']
    captures = manifest['prefixCaptures']
    require([(r['page'], r['length']) for r in captures] == pairs, 'selected prefix capture sequence')
    rows = manifest['completions'] if source else manifest['dialogueCompletions']
    expected_sequence = [(page, length) for page in sorted({p for p, _ in pairs})
                         for length in range(len(case['expectedPages'][page - 1]['text']) + 1)]
    require([(r['page'], r['length']) for r in observations] == expected_sequence, 'complete prefix observation sequence')
    by_pair = {(r['page'], r['length']): r for r in observations}
    previous = None
    for row in observations:
        page, length = row['page'], row['length']
        expected = case['expectedPages'][page - 1]
        full = expected['text']
        require(row['text'] == full[:length] and row['speakerId'] == expected['speakerId'], 'observed prefix identity')
        require(row['complete'] == (length == len(full)) and row['dialogueInputs'] == page - 1, 'prefix completion/input count')
        require(row['frame'] == row['firstObservedFrame'] and math.isfinite(row['elapsedSeconds']), 'first prefix observation')
        if previous:
            require(previous['frame'] < row['frame'] and previous['elapsedSeconds'] <= row['elapsedSeconds'], 'prefix ordering')
        previous = row
        ids = [a['id'] for a in row['actors']]
        require(len(ids) == len(set(ids)) and sorted(ids) == case['actorIds'], 'prefix actor identities')
        if page > 1:
            event = manifest['inputs'][page - 2]
            # A source pointer handler and the following AFTER_DRAW can share
            # the Director's frame index; elapsed observation time orders them.
            require(event['frame'] <= row['frame'] and event['elapsedSeconds'] <= row['elapsedSeconds'], 'prefix after normal input')
        completion = rows[page - 1]
        if source:
            require(row['rawContent'] == row['rawText'] and canonical(row['rawText']) == row['text'], 'raw prefix text')
            require(canonical(row['remaining']) == full[length:] and row['typingActive'] == (length < len(full)), 'source remaining/typing')
            require(row['layerIdentity'] == completion['layerIdentity'] and row['trigger'] == 'EVENT_AFTER_DRAW', 'prefix source lifecycle/phase')
        else:
            require(row['revision'] == completion['revision'] and row['observationPhase'] == 'post-render', 'prefix port lifecycle/phase')
            require(row['fullText'] == full, 'port full dialogue identity')
        if length == len(full):
            require(row['frame'] == completion['frame'], 'prefix completion first frame')
    for capture in captures:
        observed = by_pair[(capture['page'], capture['length'])]
        for key, value in observed.items():
            require(capture.get(key) == value, 'capture differs from first prefix observation: ' + key)
        require((capture['width'], capture['height']) == (2560, 1376), 'prefix raw dimensions')
    return captures


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
    source_prefixes = validate_prefixes(source, case, True)
    game_prefixes = validate_prefixes(game, case)
    samples = []
    for sr, gr in list(zip(source_rows, game_rows)) + list(zip(source_prefixes, game_prefixes)):
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
        if 'length' in sr:
            result.update(contract='natural-opening-dialogue-prefix-comparison/v1', length=sr['length'],
                          scope='Entire first observed natural prefix framebuffer, all RGBA channels without excluded regions.')
        samples.append(result)
    return {'contract': 'natural-opening-dialogue-window-comparison/v1',
            'equal': all(r['equal'] for r in samples), 'samples': samples,
            'scope': 'Selected first natural completed/prefix full RGBA frames; unselected frames are not covered.'}


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
