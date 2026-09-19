#!/usr/bin/env python3
"""Verify the first stage.delay(3) against actual source timer updates and render deltas."""
import argparse
import json
import math
from pathlib import Path

DURATION = .1 * 3


def fire_frame(frames):
    elapsed = 0.0
    last = None
    for index, row in enumerate(frames):
        if not math.isfinite(row['delta']) or row['delta'] < 0 or (last is not None and row['frame'] != last + 1):
            raise ValueError('Invalid or missing timer update')
        last = row['frame']
        if not index:
            continue
        elapsed += row['delta']
        if elapsed >= DURATION:
            return row['frame']
    raise ValueError('No delay threshold crossing')


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    if source.get('errors') or not source.get('delayBoundaryHookInstalled'):
        raise ValueError('Source timer hook unavailable')
    events = source['delayBoundaryEvents']
    def one(kind):
        rows = [r for r in events if r['kind'] == kind]
        if len(rows) != 1:
            raise ValueError('Expected one ' + kind)
        return rows[0]
    register = one('StageLayer.scheduleOnce.register')
    fired = one('StageLayer.scheduleOnce.fire')
    updates = [r for r in events if r['kind'] == 'StageLayer.delay.timerUpdate.after']
    if (register['delaySeconds'] != DURATION or updates[0]['frame'] != register['frame']
            or updates[0]['elapsedBefore'] != -1 or updates[0]['elapsedAfter'] != 0
            or updates[0]['callbackFired']):
        raise ValueError('Missing same-frame actual source timer priming')
    actual_fires = [r['frame'] for r in updates if r['callbackFired']]
    if actual_fires != [fired['frame']]:
        raise ValueError('Source callback evidence inconsistent')
    expected_source = fire_frame([dict(frame=r['frame'], delta=r['dt']) for r in updates])
    frames = game['frames']
    if game.get('dialogueInputs') != 0 or game.get('pixelReadback') is not False or game.get('isolation') is not False:
        raise ValueError('Modified game playback')
    start = next(i for i, r in enumerate(frames)
                 if r['playback'] == 'DELAY' and len(r['actors']) == 4
                 and all(a['moveDuration'] == 0 for a in r['actors'])
                 and .29 < r['delayRemainingSeconds'] < .31)
    expected_game = fire_frame([dict(frame=r['frame'], delta=r['deltaSeconds']) for r in frames[start:]])
    dialogue = next(r for r in frames[start:] if r['playback'] == 'DIALOGUE')
    if dialogue['dialogueSpeakerId'] != '181':
        raise ValueError('Wrong next dialogue')
    return dict(contract='natural-opening-stage-delay/v1',
                observedDelayMatchesSourceRule=expected_source == fired['frame'] and expected_game == dialogue['frame'],
                source=dict(registrationFrame=register['frame'], expectedFireFrame=expected_source, actualFireFrame=fired['frame']),
                game=dict(registrationFrame=frames[start]['frame'], expectedFireFrame=expected_game, actualFireFrame=dialogue['frame']),
                durationSeconds=DURATION,
                scope='first explicit delay after Hall movement; dialogue glyph timing and total opening latency excluded')


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
    raise SystemExit(0 if result['observedDelayMatchesSourceRule'] else 1)
