#!/usr/bin/env python3
"""Check first Hall dialogue callbacks using each run's actual frame deltas."""
import argparse
import json
import math
from pathlib import Path
from verify_opening_event_timing import validate_source

from dialogue_text_catalog import dialogue_text

TEXT = dialogue_text("opening_scene1_page1")


def replay(frames):
    if not frames:
        raise ValueError('Missing dialogue timer frames')
    elapsed = 0.0
    expected = []
    previous = None
    for index, row in enumerate(frames):
        frame, delta = row['frame'], row['delta']
        if not math.isfinite(delta) or delta < 0 or (previous is not None and frame != previous + 1):
            raise ValueError('Invalid or missing dialogue timer update')
        previous = frame
        if index == 0:
            continue
        elapsed += delta
        if elapsed >= .04:
            expected.append(frame)
            elapsed = 0.0
            if len(expected) == len(TEXT):
                break
    return expected


def assess(frames, prefixes):
    if [r['text'] for r in prefixes] != [TEXT[:i] for i in range(1, len(TEXT) + 1)]:
        raise ValueError('Missing or unordered first dialogue prefixes')
    expected = replay(frames)
    actual = [r['frame'] for r in prefixes]
    return dict(matchesSourceTimerRule=expected == actual, expectedPrefixFrames=expected,
                actualPrefixFrames=actual, registrationFrame=frames[0]['frame'])


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    if source.get('contract') != 'natural-opening-event-timing-v1' or game.get('contract') != 'natural-opening-event-timing-game/v1':
        raise ValueError('Unexpected natural timing contract')
    validate_source(source)
    if (not source.get('firstDialogueComplete') or not source.get('delayBoundaryHookInstalled')
            or source.get('firstDialogueTimerRemoved') is not True):
        raise ValueError('Incomplete source dialogue capture')
    if game.get('dialogueInputs') != 0 or game.get('pixelReadback') is not False or game.get('isolation') is not False:
        raise ValueError('Modified game playback')
    removal = source.get('firstDialogueTimerRemoval', {})
    if removal.get('callbackIdentityPreserved') is not True or removal.get('registryContainsTimer') is not False:
        raise ValueError('Source timer callback identity or removal unproven')
    boundary = source['delayBoundaryEvents']
    registrations = [r for r in boundary if r['kind'] == 'DialogueLayer.glyphSchedule.register']
    if len(registrations) != 1 or registrations[0]['interval'] != .04:
        raise ValueError('Unexpected source glyph registration')
    registration = registrations[0]
    updates = [r for r in boundary if r['kind'] == 'DialogueLayer.glyphTimer.update.after']
    if (not updates or updates[0]['frame'] != registration['frame'] or
            updates[0]['elapsedBefore'] != -1 or updates[0]['elapsedAfter'] != 0 or updates[0]['callbackFired']):
        raise ValueError('Missing actual source same-frame prime')
    for index, row in enumerate(updates):
        if (row['updateIndex'] != index or (row['callbackFired'] and row['elapsedAfter'] != 0)
                or row.get('scheduledActive') is not (index < len(updates) - 1)):
            raise ValueError('Source timer reset or update sequence inconsistent')
    prefixes = source['dialoguePrefixes']
    for index, row in enumerate(prefixes):
        if (row['trigger'] != 'EVENT_AFTER_DRAW' or row['content'] != row['text'] or
                row['remaining'] != TEXT[index + 1:] or
                row['typingHandle'] != (None if index == len(TEXT) - 1 else 'active')):
            raise ValueError('Source rendered prefix state inconsistent')
    if updates[-1]['frame'] != prefixes[-1]['frame'] or removal.get('frame') != prefixes[-1]['frame']:
        raise ValueError('Source timer continued after dialogue completion')
    callbacks = [r for r in boundary if r['kind'] == 'DialogueLayer.glyphCallback.after']
    if ([r['frame'] for r in callbacks] != [r['frame'] for r in prefixes] or
            [r['content'] for r in callbacks] != [r['text'] for r in prefixes] or
            [r['frame'] for r in updates if r['callbackFired']] != [r['frame'] for r in prefixes]):
        raise ValueError('Source callback and rendered prefixes disagree')
    source_result = assess([dict(frame=r['frame'], delta=r['dt']) for r in updates], prefixes)
    all_frames = game['frames']
    start = next(i for i, r in enumerate(all_frames) if r['playback'] == 'DIALOGUE')
    rows = all_frames[start:]
    if rows[0]['dialogueText'] != '' or not rows[-1]['dialogueComplete']:
        raise ValueError('Game must observe empty through complete first dialogue')
    game_prefixes, last = [], ''
    for row in rows:
        if row['playback'] != 'DIALOGUE' or row['dialogueSpeakerId'] != '181':
            raise ValueError('First dialogue identity changed')
        if row['dialogueComplete'] != (row['dialogueText'] == TEXT):
            raise ValueError('Game completion flag inconsistent')
        if row['dialogueText'] != last:
            game_prefixes.append(dict(frame=row['frame'], text=row['dialogueText']))
            last = row['dialogueText']
    game_result = assess([dict(frame=r['frame'], delta=r['deltaSeconds']) for r in rows], game_prefixes)
    return dict(contract='natural-opening-dialogue-timer-rule/v1',
                firstDialogueTimingMatchesSourceRule=source_result['matchesSourceTimerRule'] and game_result['matchesSourceTimerRule'],
                source=source_result, game=game_result,
                scope='first Hall dialogue creation prime and 13 typing callbacks; auto advance, portrait readiness and total opening latency excluded')


if __name__ == '__main__':
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
    raise SystemExit(0 if result['firstDialogueTimingMatchesSourceRule'] else 1)
