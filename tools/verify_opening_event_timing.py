#!/usr/bin/env python3
"""Validate natural InfoLayer callbacks against each capture's actual scheduler deltas."""
import argparse
import json
import math
from pathlib import Path

TEXT = '재능의 첫 징후'


def replay(frames):
    """Cocos repeating timer primes once, drops excess; close timer starts at completion."""
    if not frames:
        raise ValueError('Missing timer frames')
    prefix_frames = []
    elapsed = 0.0
    closing = False
    previous_frame = None
    close_frame = None
    for index, row in enumerate(frames):
        frame, delta = row['frame'], row['delta']
        if not math.isfinite(delta) or delta < 0 or (previous_frame is not None and frame <= previous_frame):
            raise ValueError('Invalid timer frame or delta')
        previous_frame = frame
        if index == 0:  # Initial timer update sets elapsed=0 and ignores that update's dt.
            continue
        elapsed += delta
        if closing:
            if elapsed >= 1.0:
                close_frame = frame
                break
        elif elapsed >= .04:
            prefix_frames.append(frame)
            elapsed = 0.0
            closing = len(prefix_frames) == len(TEXT)
    return {'prefixFrames': prefix_frames, 'closeFrame': close_frame}


def assess(frames, prefixes, close_frame):
    if [row['text'] for row in prefixes] != [TEXT[:i] for i in range(1, len(TEXT) + 1)]:
        raise ValueError('Missing, skipped, or unordered visible prefixes')
    prediction = replay(frames)
    actual = {'prefixFrames': [row['frame'] for row in prefixes], 'closeFrame': close_frame}
    complete_frame = prefixes[-1]['frame']
    closing_frames = [r for r in frames if complete_frame < r['frame'] <= close_frame]
    return {'matchesSourceTimerRule': prediction == actual, 'expected': prediction, 'actual': actual,
            'completionToCloseSeconds': sum(r['delta'] for r in closing_frames),
            'closeFrameDeltaSeconds': closing_frames[-1]['delta'] if closing_frames else None}


def validate_source(source):
    if source.get('target') != TEXT or source.get('errors'):
        raise ValueError('Source timing target or hook error')
    bootstrap = source['bootstrap']
    if bootstrap.get('dialogueInputs') != 0 or bootstrap.get('autoDialogueAdvance') is not False:
        raise ValueError('Source timing must not inject dialogue input')
    names = ['InfoLayer.onCreate', 'InfoLayer.firstObserved', 'InfoLayer.autoClose',
             'InfoLayer.complete', 'InfoLayer._next', 'InfoLayer.onDestroy',
             'InfoLayer.gone', 'DialogueLayer.firstText']
    events = {}
    for name in names:
        matches = [row for row in source['events'] if row['kind'] == name]
        if len(matches) != 1:
            raise ValueError('Missing or duplicate source lifecycle event: ' + name)
        events[name] = matches[0]
    if events['InfoLayer.firstObserved'].get('classHookInstalled') is not True:
        raise ValueError('Source prototype hook was not installed')
    order = [events[name]['frame'] for name in names]
    if order != sorted(order) or not order[1] < order[2] or not order[3] < order[4]:
        raise ValueError('Source lifecycle order invalid')
    prefixes = source['prefixes']
    if [r['text'] for r in prefixes] != [TEXT[:i] for i in range(1, len(TEXT) + 1)]:
        raise ValueError('Source did not naturally observe all prefixes')
    for row in prefixes:
        text = row['text']
        done = text == TEXT
        if (row.get('joined'), row.get('content'), row.get('remaining'), row.get('typingHandle'), row.get('closeHandle')) != (
                text, text, TEXT[len(text):], None if done else 'active', 'active' if done else None):
            raise ValueError('Source prefix state inconsistent')
        if row.get('uploaded') is not True or not row.get('opacityChain') or any(v != 255 for v in row['opacityChain']):
            raise ValueError('Source prefix not visibly rendered')
    if events['InfoLayer.autoClose']['frame'] != prefixes[-1]['frame'] or events['InfoLayer.complete']['frame'] != prefixes[-1]['frame']:
        raise ValueError('Source close timer did not begin at text completion')
    if events['InfoLayer.complete'].get('closeHandle') != 'active' or events['InfoLayer.gone'].get('destroyed') is not True:
        raise ValueError('Source completion or destruction evidence missing')
    return events


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    if source['contract'] != 'natural-opening-event-timing-v1' or game['contract'] != 'natural-opening-event-timing-game/v1':
        raise ValueError('Unexpected timing capture contract')
    if source['bootstrap']['dialogueInputs'] != 0 or game['dialogueInputs'] != 0 or game['pixelReadback'] or game['isolation']:
        raise ValueError('Timing capture must use unmodified playback')
    events = validate_source(source)
    start = events['InfoLayer.onCreate']['frame']
    source_frames = []
    for row in source['frames']:
        if row['frame'] < start:
            continue
        if row['directorPaused']:
            raise ValueError('Paused source scheduler not supported by this capture')
        source_frames.append({'frame': row['frame'], 'delta': row['dt'] * row['timeScale']})
    source_result = assess(source_frames, source['prefixes'], events['InfoLayer._next']['frame'])
    game_frames = game['frames']
    if not game_frames or game_frames[0]['modalKind'] != 'EVENT' or game_frames[0]['text'] != '':
        raise ValueError('Game must observe initial empty EVENT')
    prefixes = []
    close_frame = None
    last = ''
    for row in game_frames:
        if row['modalKind'] == 'EVENT':
            if row['complete'] is not (row['text'] == TEXT):
                raise ValueError('Game reveal completion flag inconsistent')
            if row['text'] != last:
                prefixes.append({'text': row['text'], 'frame': row['frame']})
                last = row['text']
        elif close_frame is None:
            close_frame = row['frame']
    if close_frame is None:
        raise ValueError('Game never closed the event')
    normalized = [{'frame': r['frame'], 'delta': r['deltaSeconds']} for r in game_frames]
    game_result = assess(normalized, prefixes, close_frame)
    source_dialogue_frame = events['DialogueLayer.firstText']['frame']
    game_dialogue_frames = [r for r in game_frames if r['playback'] == 'DIALOGUE' and r['dialogueText']]
    if not game_dialogue_frames or game_dialogue_frames[0]['dialogueSpeakerId'] != '181':
        raise ValueError('First soldier dialogue not observed')
    game_dialogue_frame = game_dialogue_frames[0]['frame']
    subsequent = {
        'verified': False,
        'sourceCloseToFirstDialogueSeconds': sum(r['delta'] for r in source_frames if events['InfoLayer._next']['frame'] < r['frame'] <= source_dialogue_frame),
        'gameCloseToFirstDialogueSeconds': sum(r['delta'] for r in normalized if close_frame < r['frame'] <= game_dialogue_frame),
    }
    return {'contract': 'natural-opening-event-timer-rule/v1',
            'eventTimingMatchesSourceRule': source_result['matchesSourceTimerRule'] and game_result['matchesSourceTimerRule'],
            'source': source_result, 'game': game_result, 'subsequentDialogueObservation': subsequent,
            'scope': 'first EVENT empty, typing callbacks and automatic close using each run delta sequence; later movement/dialogue timing not asserted'}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    result = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(result, indent=2) + '\n')
    print(json.dumps(result))
    return 0 if result['eventTimingMatchesSourceRule'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
