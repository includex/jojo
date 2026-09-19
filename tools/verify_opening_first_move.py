#!/usr/bin/env python3
"""Check the first natural Hall move against Cocos action ticks using each run's deltas."""
import argparse
import ast
import hashlib
import json
import math
import struct
from pathlib import Path
from verify_opening_event_timing import validate_source

EPSILON = 1.192092896e-7  # CCActionInterval: nested sequence of two zero-duration CallFuncs.
DURATION = .4 + EPSILON


def source_contract(source, hall_source):
    scene = next(n for n in ast.parse(source).body if isinstance(n, ast.FunctionDef) and n.name == "scene1")
    calls = []
    allowed = {"model.initLocalVar", "stage.clsUnit", "stage.setMenuVisible", "model.setAmbition",
               "stage.loadBg", "stage.setEventName", "stage.draw", "stage.effectSound", "stage.showUnit"}
    for statement in scene.body:
        assert isinstance(statement, ast.Expr) and isinstance(statement.value, ast.Call), "unsupported opening control flow"
        call = statement.value
        name = ast.unparse(call.func)
        calls.append(call)
        if name == "stage.unit(181).move":
            break
        assert name in allowed, f"unsupported opening call: {name}"
    show_index = next(i for i, c in enumerate(calls) if ast.unparse(c.func) == "stage.showUnit")
    show, move = calls[show_index:show_index + 2]
    assert [ast.literal_eval(a) for a in show.args] == [181, 40, 5, 2], "opening spawn contract changed"
    assert ast.unparse(move.func) == "stage.unit(181).move"
    assert [ast.literal_eval(a) for a in move.args] == [40, 15, 2], "opening move contract changed"
    # Pin the reviewed _move2 timing statement; report the full source hashes.
    assert "cc.moveTo(.04 * o, u.x, u.y)" in hall_source, "re-review recovered movement timing"
    return .4


def replay(frames):
    elapsed = 0.0
    samples = []
    previous = None
    for index, row in enumerate(frames):
        frame, delta = row['frame'], row['delta']
        if not math.isfinite(delta) or delta < 0 or (previous is not None and frame != previous + 1):
            raise ValueError('Non-contiguous frames or invalid delta')
        previous = frame
        if index:
            elapsed += delta
        progress = min(1.0, max(0.0, (elapsed - EPSILON) / .4))
        samples.append({'frame': frame, 'y': 5 + 10 * progress, 'complete': elapsed >= DURATION})
        if elapsed >= DURATION:
            return samples
    raise ValueError('Capture ends before predicted move completion')


def assess(frames, observed, actual_completion):
    expected = replay(frames)
    by_frame = {r['frame']: r for r in observed}
    errors = []
    float_positions_match = True
    f32 = lambda value: struct.unpack("f", struct.pack("f", value))[0]
    for row in expected:
        actual = by_frame.get(row['frame'])
        if actual is None:
            raise ValueError('Missing position sample')
        if not all(math.isfinite(actual[axis]) for axis in ('x', 'y')):
            raise ValueError('Non-finite observed position')
        errors.append(max(abs(actual['x'] - 40), abs(actual['y'] - row['y'])))
        float_positions_match &= f32(actual['x']) == f32(40) and f32(actual['y']) == f32(row['y'])
    return {'matchesActionRule': expected[-1]['frame'] == actual_completion and float_positions_match,
            'float32ProjectedPositionsMatch': float_positions_match,
            'expectedCompletionFrame': expected[-1]['frame'], 'actualCompletionFrame': actual_completion,
            'firstTickFrame': expected[0]['frame'], 'sampleCount': len(expected),
            'maxGridCoordinateError': max(errors), 'comparison': 'exact Float32 projected grid coordinates'}


def source_positions(rows, reference):
    if reference['actorId'] != 181 or reference['pathStart'] != [40, 5] or reference['pathEnd'] != [40, 15]:
        raise ValueError('Unexpected source turnPos reference')
    node_start, node_end = reference['nodeStart'], reference['nodeEnd']
    if any(len(point) != 2 or not all(math.isfinite(v) for v in point)
           for point in [node_start, node_end, *(r['node'] for r in rows)]):
        raise ValueError('Invalid source coordinate')
    if any(abs(a - b) > 1e-9 for actual, expected in ((rows[0]['node'], node_start), (rows[-1]['node'], node_end)) for a, b in zip(actual, expected)):
        raise ValueError('Source observed endpoint differs from turnPos')
    if node_start == node_end:
        raise ValueError('Source did not move')
    step_x, step_y = [(a - b) / 10 for a, b in zip(node_start, node_end)]
    observed = []
    for row in rows:
        dx = (row['node'][0] - node_start[0]) / step_x
        dy = (row['node'][1] - node_start[1]) / step_y
        observed.append({'frame': row['frame'], 'x': 40 + (dx - dy) / 2, 'y': 5 + (-dx - dy) / 2})
    return observed


def validate_game_clock(creation, moving_rows):
    if creation.get('hallMoveElapsedSeconds') != 0 or creation.get('hallMoveDurationSeconds') != DURATION:
        raise ValueError('Missing or incorrect authoritative Hall action duration')
    elapsed = 0.0
    for index, row in enumerate(moving_rows):
        if index:
            elapsed += row['deltaSeconds']
        actor = next(a for a in row['actors'] if a['id'] == 181)
        if (actor['x'], actor['y']) == (40, 15):
            break  # Next move may already have replaced the completed action clock.
        actual = actor.get('hallMoveElapsedSeconds')
        if actual is None or not math.isfinite(actual) or abs(actual - elapsed) > 1e-12:
            raise ValueError('Hall action clock differs from actual frame delta accumulation')
        if actor.get('hallMoveDurationSeconds') != DURATION:
            raise ValueError('Hall action duration changed before arrival')


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_root = Path(source['sourceRoot'])
    script = (source_root / 'decompiled-python/R_00.py').read_text()
    hall = (source_root / 'recovered-js/modules/game-data/HallUnit.js').read_text()
    source_contract(script, hall)
    validate_source(source)
    if game.get('contract') != 'natural-opening-event-timing-game/v1' or game.get('dialogueInputs') != 0 or game.get('pixelReadback') is not False or game.get('isolation') is not False:
        raise ValueError('Unexpected or modified game capture')
    rows = source['firstMoveFrames']
    if not rows or any(r['actorId'] != 181 or r['clockPhase'] != 'after-update-committed' for r in rows):
        raise ValueError('Missing natural first soldier move')
    if rows[0]['logical'] != [40, 5] or rows[-1]['logical'] != [40, 15] or not rows[-1]['complete']:
        raise ValueError('Wrong source move endpoints')
    moves = [r for r in source['flowEvents'] if r['kind'] == 'HallUnit._move2']
    if moves[0]['actorId'] != 181 or [(p['x'], p['y']) for p in moves[0]['arguments'][0]] != [(40, y) for y in range(5, 16)]:
        raise ValueError('Unexpected source first move path')
    if any(r['logical'] != [40, 5] or r['action'] != 20 or r['direction'] != 2 or r['complete'] for r in rows[:-1]):
        raise ValueError('Source move state changed before completion')
    if (rows[-1]['action'], rows[-1]['direction']) != (0, 2):
        raise ValueError('Source completion state missing')
    start = rows[0]['frame']
    source_frames = [{'frame': r['frame'], 'delta': r['dt'] * r['timeScale']} for r in source['frames'] if r['frame'] >= start]
    # Derive affine grid displacement from the actual source turnPos endpoints.
    observed = source_positions(rows, source['firstMoveReference'])
    source_result = assess(source_frames, observed, rows[-1]['frame'])
    frames = game['frames']
    first = next(i for i, r in enumerate(frames) if any(a['id'] == 181 and a['moveDuration'] > 0 for a in r['actors']))
    creation = frames[first]
    initial = next(a for a in creation['actors'] if a['id'] == 181)
    if (initial['x'], initial['y'], initial['visualX'], initial['visualY']) != (40, 5, 40, 5):
        raise ValueError('Unexpected game move initial position')
    validate_game_clock(initial, frames[first + 1:])
    observed_game = []
    complete = None
    for row in frames[first + 1:]:
        a = next(a for a in row['actors'] if a['id'] == 181)
        if not a['visible'] or a['direction'] != 2 or a['action'] != 20:
            raise ValueError('Game first move hidden or wrong direction/action')
        if (a['x'], a['y']) not in ((40, 5), (40, 15)):
            raise ValueError('Game logical position changed before arrival')
        observed_game.append({'frame': row['frame'], 'x': a['visualX'], 'y': a['visualY']})
        if (a['x'], a['y']) == (40, 15):
            complete = row['frame']
            break
    if complete is None:
        raise ValueError('Game first move never completed')
    normalized = [{'frame': r['frame'], 'delta': r['deltaSeconds']} for r in frames[first + 1:]]
    game_result = assess(normalized, observed_game, complete)
    resumed = next(r['frame'] for r in frames[first + 1:] if any(a['id'] == 0 for a in r['actors']))
    game_result['scriptResumeFrame'] = resumed
    game_result['matchesActionRule'] &= resumed == complete
    source_resume = next(r['frame'] for r in source['flowEvents'] if r['kind'] == 'StageLayer.resume' and r['frame'] > moves[0]['frame'])
    source_result['scriptResumeFrame'] = source_resume
    source_result['matchesActionRule'] &= source_resume == rows[-1]['frame']
    return {'contract': 'natural-first-hall-move-rule/v1',
            'observedFirstMoveMatchesSourceRule': source_result['matchesActionRule'] and game_result['matchesActionRule'],
            'source': source_result, 'game': game_result,
            'sourceHashes': {'R_00': hashlib.sha256(script.encode()).hexdigest(), 'HallUnit': hashlib.sha256(hall.encode()).hexdigest()},
            'scope': 'observed deltas only: first 181 move initialization, exact Float32 projected grid positions, completion and script resume; no framebuffer or later async setup parity claim',
            'authoritativeHallClockVerified': True}


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
    raise SystemExit(0 if result['observedFirstMoveMatchesSourceRule'] else 1)
