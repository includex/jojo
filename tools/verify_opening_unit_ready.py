#!/usr/bin/env python3
"""Check naturally observed Hall texture readiness before initial unit presentation."""
import argparse
import json
from pathlib import Path
from verify_opening_event_timing import validate_source

ACTORS = (181, 0, 157, 182)


def source_readiness(source):
    validate_source(source)
    events = source['unitReadyEvents']
    if any('rejected' in r['kind'] for r in events):
        raise ValueError('Source asset initialization rejected')
    results = []
    for actor in ACTORS:
        def one(kind):
            values = [(i, r) for i, r in enumerate(events) if r['kind'] == kind and r.get('id') == actor]
            if len(values) != 1:
                raise ValueError(f'Missing or duplicate source {actor} {kind}')
            return values[0]
        entry_i, entry = one('HallLayer._showHallUnit.entry')
        load_i, load = one('HallUnit._loadFunitTexture.entry')
        loaded_i, loaded = one('HallUnit._loadFunitTexture.resolved')
        initialized_i, initialized = one('HallUnit.onInit.resolved')
        ready_i, ready = one('HallLayer._showHallUnit.callback')
        if not entry_i < load_i < loaded_i < initialized_i < ready_i:
            raise ValueError('Source initialization ordering is invalid')
        if loaded.get('textureCount') != 2 or loaded.get('texturesReady') != [True, True] or not initialized.get('animeReady') or not ready.get('animeReady'):
            raise ValueError('Source unit initialized without both textures and animation')
        texture_ids = [2 * load['avatar'] + 1, 2 * load['avatar'] + 2]
        last = load_i
        for texture_id in texture_ids:
            url = f'Game/Pmapobj2/{texture_id}'
            requests = [i for i, r in enumerate(events) if load_i < i < loaded_i and r['kind'] == 'HallLayer.loadByUrl.request' and r.get('url') == url]
            callbacks = [(i, r) for i, r in enumerate(events) if load_i < i < loaded_i and r['kind'] == 'HallLayer.loadByUrl.callback' and r.get('url') == url]
            if len(requests) != 1 or len(callbacks) != 1 or not last < requests[0] < callbacks[0][0] or not callbacks[0][1].get('textureReady') or callbacks[0][1].get('err') != '0':
                raise ValueError('Source texture pair was not prepared sequentially')
            last = callbacks[0][0]
        results.append({'actorId': actor, 'textureIds': texture_ids, 'entryFrame': entry['frame'], 'readyFrame': ready['frame']})
    return results


def game_readiness(game, required):
    if game.get('contract') != 'natural-opening-event-timing-game/v1' or game.get('dialogueInputs') != 0 or game.get('pixelReadback') is not False or game.get('isolation') is not False:
        raise ValueError('Unexpected or modified game capture')
    results = []
    for expected in required:
        actor = expected['actorId']
        appearances = [r for r in game['frames'] if any(a['id'] == actor and a['visible'] for a in r['actors'])]
        if not appearances:
            raise ValueError(f'Actor {actor} never appeared')
        first = appearances[0]
        loaded = first['loadedHallTextureIds']
        missing = [i for i in expected['textureIds'] if i not in loaded]
        results.append({'actorId': actor, 'firstVisibleFrame': first['frame'], 'requiredTextureIds': expected['textureIds'],
                        'missingTextureIds': missing, 'readyAtFirstVisibleFrame': not missing})
    return results


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    required = source_readiness(source)
    observed = game_readiness(game, required)
    return {'contract': 'natural-opening-hall-unit-readiness/v1',
            'allTexturePairsReadyAtFirstVisibleFrame': all(r['readyAtFirstVisibleFrame'] for r in observed),
            'source': required, 'game': observed,
            'scope': 'first appearance of 181, 0, 157, 182 with both source avatar textures loaded; exact loading latency and within-frame callback ordering are not inferred from frame snapshots'}


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
    raise SystemExit(0 if result['allTexturePairsReadyAtFirstVisibleFrame'] else 1)
