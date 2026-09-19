#!/usr/bin/env python3
"""Compare controlled-clock first Hall movement state and seven complete RGBA frames."""
import argparse
import hashlib
import json
import math
import struct
from pathlib import Path
from verify_opening_panel_pixels import compare

ORDINALS = [1, 6, 12, 13, 18, 19, 24]
STEP = struct.unpack('f', struct.pack('f', 1 / 60))[0]
STEP_BITS = struct.unpack('I', struct.pack('f', STEP))[0]
DURATION = .4 + 1.192092896e-7


def require(condition, message):
    if not condition:
        raise ValueError(message)


def f32(value):
    return struct.unpack('f', struct.pack('f', value))[0]


def validate(document, source=False):
    require(document['contract'] == 'controlled-opening-first-move-rgba8', 'capture contract')
    require(document['clockMode'] == 'controlled-fixed-float32' and document['stepDelta'] == STEP and
            document['stepDeltaFloat32Bits'] == STEP_BITS, 'controlled clock must match exactly')
    require(document['isolation'] is False and document['dialogueInputs'] == 0, 'no isolation or dialogue input')
    require((document['width'], document['height'], document['origin']) == (2560, 1376, 'bottom-left'), 'frame dimensions/origin')
    ticks, captures = document['ticks'], document['captures']
    require([t['ordinal'] for t in ticks] == list(range(25)), 'contiguous move ticks including prime required')
    require([c['ordinal'] for c in captures] == ORDINALS, 'all seven ordered captures required')
    for index, tick in enumerate(ticks):
        require(tick['delta'] == STEP and tick['elapsedSeconds'] == index * STEP, 'actual action clock or prime mismatch')
        require(index == 0 or tick['frame'] == ticks[index-1]['frame'] + 1, 'contiguous render frames')
        actor = tick['actor']
        require(actor['id'] == 181 and actor['logical'] == [40, 5] and actor['action'] == 20 and actor['direction'] == 2, 'first active move state')
        if source:
            require(tick['trigger'] == 'EVENT_AFTER_DRAW' and tick['schedulerDt'] == STEP and tick['actionFirstTick'] is False, 'source clock phase')
            action = tick['actionTrees'][0]
            require(action['elapsed'] == tick['elapsedSeconds'] and action['duration'] == DURATION and action['done'] is False, 'actual source sequence state')
        else:
            require(actor['moveJustStarted'] is False, 'port action must have primed')
            require(all(math.isfinite(actor[key]) for key in ('visualX', 'visualY')), 'finite port coordinates')
    for capture in captures:
        tick = ticks[capture['ordinal']]
        require(all(capture[key] == tick[key] for key in ('frame', 'delta', 'elapsedSeconds', 'actor', 'sprite')), 'capture must belong to recorded tick')
        require((capture['width'], capture['height']) == (2560, 1376), 'capture dimensions')
    if source:
        reference = document['reference']
        require(reference['actorId'] == 181 and reference['pathStart'] == [40, 5] and reference['pathEnd'] == [40, 15], 'source move endpoints')
        require(reference['path'] == [[40, y] for y in range(5, 16)], 'source move path')
    return ticks, captures


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_ticks, source_captures = validate(source, True)
    game_ticks, game_captures = validate(game)
    start, end = source['reference']['nodeStart'], source['reference']['nodeEnd']
    step_x, step_y = [(a-b)/10 for a, b in zip(start, end)]
    require(step_x != 0 and step_y != 0, 'source coordinate reference')
    states = []
    for source_tick, game_tick in zip(source_ticks, game_ticks):
        node = source_tick['actor']['node']
        dx, dy = (node[0]-start[0])/step_x, (node[1]-start[1])/step_y
        source_grid = [f32(40+(dx-dy)/2), f32(5+(-dx-dy)/2)]
        port_grid = [game_tick['actor']['visualX'], game_tick['actor']['visualY']]
        states.append({'ordinal': source_tick['ordinal'], 'sourceGridFloat32': source_grid, 'gameGridFloat32': port_grid,
                       'positionsEqual': source_grid == port_grid,
                       'spriteRowsEqual': source_tick['sprite']['frameRow'] == game_tick['sprite']['frameRow']})
    samples = []
    for source_row, game_row in zip(source_captures, game_captures):
        buffers = []
        for manifest, row in ((source_path, source_row), (game_path, game_row)):
            require(Path(row['file']).name == row['file'], 'raw file must be adjacent to manifest')
            raw = (manifest.parent / row['file']).read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw hash mismatch')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(contract='controlled-opening-first-move-frame/v1', ordinal=source_row['ordinal'],
                      scope='Entire RGBA frame under identical fixed Float32 clock; no excluded regions or channels.')
        samples.append(result)
    state_equal = all(row['positionsEqual'] and row['spriteRowsEqual'] for row in states)
    return {'contract': 'controlled-opening-first-move-comparison/v1',
            'equal': state_equal and all(row['equal'] for row in samples), 'stateEqual': state_equal,
            'states': states, 'samples': samples,
            'scope': 'First move under controlled clock, ticks0..24 and seven full frames. Actor is offscreen at1/6 and partly visible at12/13/18/19/24. Natural frame timing, completion and later moves are not covered.'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps({'equal': report['equal'], 'stateEqual': report['stateEqual'],
                      'changedPixels': {s['ordinal']: s['changedPixels'] for s in report['samples']}}))
    raise SystemExit(0 if report['equal'] else 1)
