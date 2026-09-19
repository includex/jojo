#!/usr/bin/env python3
"""Strict controlled-clock comparison of the final four-actor Hall movement group."""
import argparse
import hashlib
import json
from pathlib import Path
from verify_opening_first_move_pixels import STEP, STEP_BITS, f32, require
from verify_opening_panel_pixels import compare

DURATIONS = {0: .88, 182: .88, 181: .8 + 1.192092896e-7, 157: .8 + 1.192092896e-7}
COMPLETE = {actor_id: next(i for i in range(100) if i * STEP >= duration)
            for actor_id, duration in DURATIONS.items()}
LAST_COMPLETE = max(COMPLETE.values())
ORDINALS = [1, 12, 13, 24, 25, 36, 37, 45, 48, 49, 50, 53, 54]
PATHS = {0: [[40, y] for y in range(15, 25)] + [[41, y] for y in range(24, 36)] + [[40, 35]],
         182: [[40, y] for y in range(5, 15)] + [[41, y] for y in range(14, 26)] + [[40, 25]],
         157: [[54, y] for y in range(85, 64, -1)], 181: [[40, y] for y in range(25, 46)]}
BASE_ASSETS = {0: 1, 157: 315, 181: 363, 182: 365}
ASSET_UUIDS = {1: '40aee40e-20ae-4e90-b26c-648cfb672c93', 2: '23af2582-5cb4-4725-99b4-4f777af821a9',
               316: '4c6293ae-56ee-459d-a026-fcf3e9652c62', 363: '1593970e-62c6-4f20-83a1-16b46cf3ee13',
               365: '0512fd55-9b87-4b27-850e-f71cc6908a29', 366: '0915534a-5fd1-4b4c-8d83-8c89eaf69a6f'}


def validate(document, source=False):
    require(document['contract'] == 'controlled-opening-final-group-rgba8', 'capture contract')
    require(document['clockMode'] == 'controlled-fixed-float32' and document['stepDelta'] == STEP and
            document['stepDeltaFloat32Bits'] == STEP_BITS, 'fixed clock')
    require(document['isolation'] is False and document['dialogueInputs'] == 0, 'isolation/input')
    require((document['width'], document['height'], document['origin']) == (2560, 1376, 'bottom-left'), 'frame dimensions/origin')
    ticks, captures = document['ticks'], document['captures']
    require([t['ordinal'] for t in ticks] == list(range(LAST_COMPLETE + 2)), 'contiguous group ticks')
    require([c['ordinal'] for c in captures] == ORDINALS, 'all thirteen ordered captures')
    if source:
        require(document['simultaneousPrime'] is True, 'source group prime offsets differ')
        require({int(key) for key in document['references']} == set(PATHS), 'source group identities')
        for actor_id, path in PATHS.items():
            reference = document['references'][str(actor_id)]
            require(reference['path'] == path and reference['actionDurationSeconds'] == DURATIONS[actor_id], 'observed source path/duration')
            require(reference['registeredFrame'] <= ticks[0]['frame'], 'source registration after prime')
        events = document['completionEvents']
        require(len(events) == 8, 'one before/after completion callback per actor')
        for actor_id, ordinal in COMPLETE.items():
            for phase in ('before', 'after'):
                matching = [event for event in events if event['id'] == actor_id and
                            event['kind'] == 'HallUnit._move2.callback.' + phase]
                require(len(matching) == 1, 'duplicate or missing source callback')
                event = matching[0]
                require(event['ordinal'] == ordinal and event['frame'] == ticks[ordinal]['frame'] and
                        event['logical'] == PATHS[actor_id][-1] and event['action'] == (20 if phase == 'before' else 0), 'source callback completion boundary')
        stages = document['stageEvents']
        require([event['kind'] for event in stages] == ['StageLayer.resume', 'StageLayer.delay'], 'source group resume and delay')
        require(all(event['ordinal'] == LAST_COMPLETE and event['frame'] == ticks[LAST_COMPLETE]['frame'] for event in stages), 'source delay must follow final actor completion')
        require(stages[-1]['arguments'] == [3], 'source delay argument')
    else:
        require(document['commonPrimeAligned'] is True, 'port group prime offsets differ')
        timings = document['actorTimings']
        require(len(timings) == 4 and {t['id'] for t in timings} == set(PATHS), 'port prime identities')
        for timing in timings:
            require(timing['primeFrame'] == ticks[0]['frame'] and timing['primeElapsedSeconds'] == 0, 'port actual prime')
            require(timing['firstObservedMoveFrame'] <= timing['primeFrame'], 'first observed move after prime')
            require(timing['path'] == PATHS[timing['id']], 'port observed movement path')
        require(document['completionObserved'] is True and document['completionObservedFrame'] == ticks[LAST_COMPLETE]['frame'], 'port group completion observation')
    for index, tick in enumerate(ticks):
        require(tick['delta'] == STEP and (index == 0 or tick['frame'] == ticks[index-1]['frame'] + 1), 'contiguous fixed-delta frames')
        actors = tick['actors']
        require(len(actors) == 4 and {a['id'] for a in actors} == set(PATHS), 'exact four group actors')
        if source:
            require(tick['trigger'] == 'EVENT_AFTER_DRAW' and tick['schedulerDt'] == STEP, 'source scheduler/render phase')
        for actor in actors:
            actor_id = actor['id']
            complete = index >= COMPLETE[actor_id]
            duration = DURATIONS[actor_id]
            require(actor['logical'] == PATHS[actor_id][-1 if complete else 0] and actor['action'] == (0 if complete else 20) and
                    actor['direction'] in range(4), 'active group move state')
            if not complete:
                require(actor['elapsedSeconds'] == index * STEP, 'actor action clock/prime mismatch')
            if complete:
                require(actor['direction'] == (0 if actor_id == 157 else 2), 'final scripted direction')
            sprite = actor['sprite']
            asset_id = BASE_ASSETS[actor_id] + (1 if actor['direction'] in (0, 3) else 0)
            if source:
                require(actor['actionFirstTick'] is False and len(actor['actionTrees']) == 1, 'actual source action identity/prime')
                action = actor['actionTrees'][0]
                require(action['elapsed'] == min(index, COMPLETE[actor_id]) * STEP and action['duration'] == duration and action['done'] is complete, 'source action timeline')
                require(ASSET_UUIDS.get(asset_id, 'unreviewed-asset') in sprite['texture']['url'] and sprite['rect'] == [0, sprite['frameRow']*64, 48, 64], 'source sprite selection')
            else:
                require(actor['moveJustStarted'] is False and actor['durationSeconds'] == duration, 'port action timeline')
                require(actor['moveDuration'] == (0 if complete else f32(duration)), 'port completion state')
                require(actor['elapsedSeconds'] == min(index * STEP, duration), 'port clamped action clock')
                require(sprite['textureAssetId'] == asset_id and sprite['flipX'] == (actor['direction'] in (1, 3)), 'port sprite selection')
    for capture in captures:
        tick = ticks[capture['ordinal']]
        require(all(capture[key] == tick[key] for key in ('frame', 'delta', 'actors')), 'capture/tick identity')
        require((capture['width'], capture['height']) == (2560, 1376), 'capture dimensions')
    digests = document['frameDigests']
    require([row['ordinal'] for row in digests] == list(range(LAST_COMPLETE + 2)), 'every movement frame digest required')
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
            source_vertices = actor['sprite']['assembler']['vDatas'][0]
            source_flip = source_vertices[5] < source_vertices[0]
            states.append({'ordinal': source_tick['ordinal'], 'actorId': actor_id, 'sourceGridFloat32': source_grid,
                           'gameGridFloat32': game_grid, 'positionsEqual': source_grid == game_grid,
                           'spriteRowsEqual': actor['sprite']['frameRow'] == port_actor['sprite']['frameRow'],
                           'directionsEqual': actor['direction'] == port_actor['direction'],
                           'flipsEqual': source_flip == port_actor['sprite']['flipX']})
    samples = []
    for source_row, game_row in zip(source_captures, game_captures):
        buffers = []
        for manifest, row in ((source_path, source_row), (game_path, game_row)):
            require(Path(row['file']).name == row['file'], 'raw file must be adjacent to manifest')
            raw = (manifest.parent / row['file']).read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw hash mismatch')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        result.update(contract='controlled-opening-final-group-frame/v1', ordinal=source_row['ordinal'],
                      scope='Entire controlled-clock final group framebuffer, all RGBA channels without excluded regions.')
        samples.append(result)
    state_equal = all(row['positionsEqual'] and row['spriteRowsEqual'] and row['directionsEqual'] and row['flipsEqual'] for row in states)
    hashes = [{'ordinal': a['ordinal'], 'equal': a['sha256'] == b['sha256'],
               'sourceSha256': a['sha256'], 'gameSha256': b['sha256']}
              for a, b in zip(source['frameDigests'], game['frameDigests'])]
    return {'contract': 'controlled-opening-final-group-comparison/v1',
            'equal': state_equal and all(row['equal'] for row in samples) and all(row['equal'] for row in hashes), 'stateEqual': state_equal,
            'states': states, 'samples': samples, 'frameDigests': hashes,
            'scope': 'Final pre-dialogue four-actor move, completion and first idle frame under identical fixed Float32 clock. Registration subframe timing, natural frame timing and later dialogue are not proven.'}


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
