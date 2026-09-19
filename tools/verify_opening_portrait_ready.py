#!/usr/bin/env python3
"""Compare first Hall portrait presence and actual atlas regions, without inferring load duration."""
import argparse
import json
from pathlib import Path


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    if source.get('errors') or not source.get('portraitHookInstalled'):
        raise ValueError('Source portrait hook failed')
    if (source['bootstrap'].get('dialogueInputs') != 2 or source['bootstrap'].get('autoDialogueAdvance') is not False
            or game.get('dialogueInputs') != 2):
        raise ValueError('Exactly two completed-dialogue inputs required')
    if (game.get('contract') != 'natural-opening-portrait-readiness-game/v1' or
            game.get('pixelReadback') is not False or game.get('isolation') is not False):
        raise ValueError('Unsupported game capture')
    pages = source['portraitPages']
    if len(pages) != 3 or [p['pageIndex'] for p in pages] != [0, 1, 2]:
        raise ValueError('Three source pages required')
    samples = []
    for page, speaker, portrait in zip(pages, ['181', '0', '157'], [181, 1, 214]):
        source_rows = page['frames']
        active = []
        for row in source_rows:
            faces = [face for face in row['sides'] if face['isCurrentBg']]
            if (len(faces) != 1 or row['trigger'] != 'EVENT_AFTER_DRAW' or
                    row['layerIdentity'] != page['layerIdentity']):
                raise ValueError('Missing actual active source face or stable layer')
            face = faces[0]
            if str(face['speakerId']) != speaker or not face['bgActive'] or not face['faceNodeActive']:
                raise ValueError('Unexpected source active portrait')
            active.append(face)
        source_ready = next(i for i, face in enumerate(active) if face['textureGLReady'])
        if active[0]['framePresent'] or source_ready == 0 or active[source_ready]['frameName'] != str(portrait):
            raise ValueError('Source blank to ready lifecycle not established')
        events = [e for e in source['portraitEvents'] if e.get('pageIndex') == page['pageIndex']]
        def one(kind):
            found = [e for e in events if e['kind'] == kind]
            if len(found) != 1:
                raise ValueError('Missing source portrait event ' + kind)
            return found[0]
        request = one('DialogueLayer.loadByUrl.request')
        applied = one('DialogueLayer.loadByUrl.callback.afterApply')
        if (request['url'] != f'Game/Head/{portrait}' or request['cacheBefore']['cached'] is not False or
                applied['frameName'] != str(portrait) or not applied['textureGLReady'] or
                not request['frame'] <= source_rows[0]['frame'] < applied['frame'] <= source_rows[source_ready]['frame']):
            raise ValueError('Source request, callback and draw order inconsistent')
        game_rows = [r for r in game['frames'] if r['speaker'] == speaker]
        if not game_rows or game_rows[0]['text'] != '' or not game_rows[-1]['complete']:
            raise ValueError('Game initial through completed dialogue required')
        selections = []
        for row in game_rows:
            draws = [r for r in game['portraitRegionSelections'] if r['frame'] == row['frame']]
            if len(draws) != 1 or draws[0]['portraitId'] != portrait:
                raise ValueError('Missing actual returned portrait region')
            selections.append(draws[0])
        ready_index = next(i for i, r in enumerate(selections) if r['regionReturned'])
        ready = selections[ready_index]
        if ready['textureHandle'] <= 0 or ready['width'] <= 0 or ready['height'] <= 0:
            raise ValueError('Returned game region not GPU ready')
        rect = [ready['regionX'], ready['regionY'], ready['width'], ready['height']]
        if any(r['regionReturned'] or r['textureHandle'] != 0 for r in selections[:ready_index]):
            raise ValueError('Pending game portrait unexpectedly available')
        if any(not r['regionReturned'] or r['textureHandle'] != ready['textureHandle'] or
               [r['regionX'], r['regionY'], r['width'], r['height']] != rect for r in selections[ready_index:]):
            raise ValueError('Ready portrait changed before dialogue completion')
        samples.append(dict(speakerId=speaker, portraitId=portrait,
                    matches=not selections[0]['regionReturned'] and ready_index > 0 and rect == active[source_ready]['rect'],
                    source=dict(firstFrame=source_rows[0]['frame'], readyFrame=source_rows[source_ready]['frame'],
                                layerIdentity=page['layerIdentity'], atlasRect=active[source_ready]['rect']),
                    game=dict(firstFrame=game_rows[0]['frame'], firstPortraitPresent=selections[0]['regionReturned'],
                              readyFrame=game_rows[ready_index]['frame'], atlasRect=rect)))
    if pages[0]['layerIdentity'] != pages[1]['layerIdentity'] or pages[1]['layerIdentity'] == pages[2]['layerIdentity']:
        raise ValueError('Unexpected source dialogue layer lifetime')
    return dict(contract='opening-portrait-readiness/v1', matchesSourceReadinessSequence=all(s['matches'] for s in samples),
                samples=samples,
                scope='first three active faces start empty and become GPU-ready with matching atlas rectangles; exact loading duration, cached loads and full framebuffer excluded')


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
    raise SystemExit(0 if result['matchesSourceReadinessSequence'] else 1)
