#!/usr/bin/env python3
"""Strict controlled-clock comparison of the first three-actor Hall movement group."""
import argparse
import hashlib
import json
from pathlib import Path
from verify_opening_first_move_pixels import STEP, STEP_BITS, DURATION, ORDINALS, f32, require
from verify_opening_panel_pixels import compare

PATHS = {0: [[40, y] for y in range(5, 16)], 157: [[54, y] for y in range(95, 84, -1)],
         181: [[40, y] for y in range(15, 26)]}
ASSETS = {0: (1, '40aee40e-20ae-4e90-b26c-648cfb672c93'),
          157: (316, '4c6293ae-56ee-459d-a026-fcf3e9652c62'),
          181: (363, '1593970e-62c6-4f20-83a1-16b46cf3ee13')}


def validate(document, source=False):
    require(document['contract'] == 'controlled-opening-first-group-rgba8', 'capture contract')
    require(document['clockMode'] == 'controlled-fixed-float32' and document['stepDelta'] == STEP and
            document['stepDeltaFloat32Bits'] == STEP_BITS, 'fixed clock')
    require(document['isolation'] is False and document['dialogueInputs'] == 0, 'isolation/input')
    require((document['width'], document['height'], document['origin']) == (2560, 1376, 'bottom-left'), 'frame dimensions/origin')
    ticks, captures = document['ticks'], document['captures']
    require([t['ordinal'] for t in ticks] == list(range(25)), 'contiguous group ticks')
    require([c['ordinal'] for c in captures] == ORDINALS, 'all seven ordered captures')
    if source:
        require(document['simultaneousPrime'] is True, 'source group prime offsets differ')
        require({int(key) for key in document['references']} == set(PATHS), 'source group identities')
        for actor_id, path in PATHS.items():
            reference = document['references'][str(actor_id)]
            require(reference['path'] == path and reference['actionDurationSeconds'] == DURATION, 'observed source path/duration')
            require(reference['registeredFrame'] <= ticks[0]['frame'], 'source registration after prime')
    else:
        require(document['commonPrimeAligned'] is True, 'port group prime offsets differ')
        timings = document['actorTimings']
        require(len(timings) == 3 and {t['id'] for t in timings} == set(PATHS), 'port prime identities')
        for timing in timings:
            require(timing['primeFrame'] == ticks[0]['frame'] and timing['primeElapsedSeconds'] == 0, 'port actual prime')
            require(timing['firstObservedMoveFrame'] <= timing['primeFrame'], 'first observed move after prime')
    for index, tick in enumerate(ticks):
        require(tick['delta'] == STEP and (index == 0 or tick['frame'] == ticks[index-1]['frame'] + 1), 'contiguous fixed-delta frames')
        actors = tick['actors']
        require(len(actors) == 3 and {a['id'] for a in actors} == set(PATHS), 'exact group actors; no fourth actor')
        if source:
            require(tick['trigger'] == 'EVENT_AFTER_DRAW' and tick['schedulerDt'] == STEP, 'source scheduler/render phase')
        for actor in actors:
            actor_id = actor['id']
            require(actor['logical'] == PATHS[actor_id][0] and actor['action'] == 20 and
                    actor['direction'] == (0 if actor_id == 157 else 2), 'active group move state')
            require(actor['elapsedSeconds'] == index * STEP, 'actor action clock/prime mismatch')
            sprite = actor['sprite']
            if source:
                require(actor['actionFirstTick'] is False and len(actor['actionTrees']) == 1, 'actual source action identity/prime')
                action = actor['actionTrees'][0]
                require(action['elapsed'] == index * STEP and action['duration'] == DURATION and action['done'] is False, 'source action timeline')
                require(ASSETS[actor_id][1] in sprite['texture']['url'] and sprite['rect'] == [0, sprite['frameRow']*64, 48, 64], 'source sprite selection')
            else:
                require(actor['moveJustStarted'] is False and actor['durationSeconds'] == DURATION, 'port action timeline')
                require(sprite['textureAssetId'] == ASSETS[actor_id][0] and sprite['flipX'] is False, 'port sprite selection')
    for capture in captures:
        tick = ticks[capture['ordinal']]
        require(all(capture[key] == tick[key] for key in ('frame', 'delta', 'actors')), 'capture/tick identity')
        require((capture['width'], capture['height']) == (2560, 1376), 'capture dimensions')
    digests = document['frameDigests']
    require([row['ordinal'] for row in digests] == list(range(25)), 'every movement frame digest required')
    for row in digests:
        require(row['frame'] == ticks[row['ordinal']]['frame'] and (row['width'], row['height']) == (2560, 1376), 'digest frame identity')
        require(len(row['sha256']) == 64 and all(c in '0123456789abcdef' for c in row['sha256']), 'SHA-256 digest format')
    for capture in captures:
        require(capture['sha256'] == digests[capture['ordinal']]['sha256'], 'raw capture and all-frame digest must agree')
    return ticks, captures


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_ticks, source_captures = validate(source, True)
    game_ticks, game_captures = validate(game)
    states = []
    for source_tick, game_tick in zip(source_ticks, game_ticks):
        port_actors = {a['id']: a for a in game_tick['actors']}
        for actor in source_tick['actors']:
            actor_id = actor['id']
            reference = source['references'][str(actor_id)]
            start, end = reference['nodeStart'], reference['nodeEnd']
            path = PATHS[actor_id]
            grid_dy = path[-1][1] - path[0][1]
            scale_x, scale_y = [(a-b)/grid_dy for a, b in zip(start, end)]
            require(scale_x != 0 and scale_y != 0, 'source isometric coordinate reference')
            dx = (actor['node'][0] - start[0]) / scale_x
            dy = (actor['node'][1] - start[1]) / scale_y
            # Invert both axes; projection onto the authored path would hide lateral drift.
            source_grid = [f32(path[0][0] + (dx-dy)/2), f32(path[0][1] + (-dx-dy)/2)]
            port_actor = port_actors[actor_id]
            game_grid = [port_actor['visualX'], port_actor['visualY']]
            states.append({'ordinal': source_tick['ordinal'], 'actorId': actor_id, 'sourceGridFloat32': source_grid,
                           'gameGridFloat32': game_grid, 'positionsEqual': source_grid == game_grid,
                           'spriteRowsEqual': actor['sprite']['frameRow'] == port_actor['sprite']['frameRow']})
    samples = []
    for source_row, game_row in zip(source_captures, game_captures):
        buffers = []
        for manifest, row in ((source_path, source_row), (game_path, game_row)):
            require(Path(row['file']).name == row['file'], 'raw file must be adjacent to manifest')
            raw = (manifest.parent / row['file']).read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw hash mismatch')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(contract='controlled-opening-first-group-frame/v1', ordinal=source_row['ordinal'],
                      scope='Entire controlled-clock first group framebuffer, all RGBA channels without excluded regions.')
        samples.append(result)
    state_equal = all(row['positionsEqual'] and row['spriteRowsEqual'] for row in states)
    hashes = [{'ordinal': a['ordinal'], 'equal': a['sha256'] == b['sha256'],
               'sourceSha256': a['sha256'], 'gameSha256': b['sha256']}
              for a, b in zip(source['frameDigests'], game['frameDigests'])]
    return {'contract': 'controlled-opening-first-group-comparison/v1',
            'equal': state_equal and all(row['equal'] for row in samples) and all(row['equal'] for row in hashes), 'stateEqual': state_equal,
            'states': states, 'samples': samples, 'frameDigests': hashes,
            'scope': 'First three-actor move under identical fixed Float32 clock. Registration subframe timing, natural frame timing, completion and subsequent groups are not proven.'}


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
                      'mismatchedFrameOrdinals': [row['ordinal'] for row in report['frameDigests'] if not row['equal']],
                      'changedPixels': {s['ordinal']: s['changedPixels'] for s in report['samples']}}))
    raise SystemExit(0 if report['equal'] else 1)
