#!/usr/bin/env python3
"""Compare first-observed natural typing milestones with the entire opening scene visible."""
import argparse
import hashlib
import json
import math
from pathlib import Path

from verify_opening_panel_pixels import compare
from verify_opening_full_pages import AFTER_MOVE as INITIAL

FULL_TEXT = '잠시만 기다려 주세요!'
LENGTHS = list(range(len(FULL_TEXT) + 1))
ASSET_UUIDS = {
    1: '40aee40e-20ae-4e90-b26c-648cfb672c93',
    316: '4c6293ae-56ee-459d-a026-fcf3e9652c62',
    363: '1593970e-62c6-4f20-83a1-16b46cf3ee13',
    364: '4619bc1c-9716-40eb-a352-b75d07c58ace',
    365: '0512fd55-9b87-4b27-850e-f71cc6908a29',
}


def require(condition, message):
    if not condition:
        raise ValueError(message)


def validate(document, source=False):
    require((document['contract'], document['width'], document['height'], document['origin']) ==
            ('natural-third-dialogue-full-prefixes-rgba8', 2560, 1376, 'bottom-left'), 'capture contract')
    require(document['isolation'] is False and document['dialogueInputs'] == 2 and
            document['clockMode'] == 'natural', 'unaltered scene, two completed-page inputs, natural clock')
    require(document['fullText'] == FULL_TEXT and document['sampleLengths'] == LENGTHS, 'all natural prefixes including empty')
    rows = document['captures']
    require([row['prefixLength'] for row in rows] == LENGTHS, 'ordered first-observed prefixes')
    completions, inputs = document['dialogueCompletions'], document['inputs']
    require(len(completions) == 2 and len(inputs) == 2, 'two completed pages and normal inputs required')
    for index, (completion, event) in enumerate(zip(completions, inputs)):
        text, speaker = [('대장님, 서둘러야 해요!', '181'), ('알아!', '0')][index]
        require((completion['page'], str(completion['speakerId']), completion['text'], completion['complete']) ==
                (index + 1, speaker, text, True), 'prior completed dialogue identity')
        require(event['afterPage'] == index + 1 and event['textBeforeInput'] == text and
                event['completeBeforeInput'] is True, 'input after complete text')
        require(completion['frame'] <= event['frame'] < rows[0]['frame'], 'prior input before third dialogue')
        if index:
            require(inputs[index-1]['frame'] < completion['frame'], 'prior page order')
        if source:
            request = document['inputRequests'][index]
            require(event['kind'] == 'pointer' and event['target'] == 'Panel_cancel' and
                    event['handler'] == 'DialogueLayer._next pass-through', 'actual source input route')
            require(str(event['speakerId']) == speaker and str(request['speakerId']) == speaker and
                    request['complete'] is True and request['textBeforeInput'] == text and
                    request['frame'] <= event['frame'] and request['elapsedSeconds'] <= event['elapsedSeconds'],
                    'observed source input speaker and request ordering')
        else:
            require(event['kind'] == 'InputProcessor.keyDown/keyUp(SPACE)', 'normal port input route')
    if source:
        require(document['bootstrap']['autoDialogueAdvance'] is False and document['bootstrap']['dialogueInputs'] == 2,
                'source auto advance disabled and two normal inputs')
        require(document['hook']['installedFrame'] < rows[0]['frame'], 'observer installed before dialogue')
        prior_layer = inputs[0]['layerIdentity']
        target_layer = rows[0]['layerIdentity']
        require(prior_layer and target_layer and inputs[1]['layerIdentity'] == prior_layer and target_layer != prior_layer,
                'third dialogue uses a fresh layer after the first two shared pages')
        require(all(row['layerIdentity'] == target_layer for row in rows + document['firstDialogueFrames']),
                'all observations belong to the third dialogue layer')
    else:
        require(document['observationPhase'] == 'post-render', 'port observation phase')
    phase_frames = document['firstDialogueFrames']
    require(len(phase_frames) == 3 and [row['frame'] for row in phase_frames] ==
            list(range(rows[0]['frame'], rows[0]['frame'] + 3)), 'three initial consecutive dialogue observations')
    for row in phase_frames:
        require(row['actor157']['logical'] == [54, 50] and row['actor157']['direction'] == 3,
                'initial logical direction already changed')
        if source:
            require(row['actor157']['animeScaleX'] in (-1, 1) and
                    row['actor157']['flipX'] is (row['actor157']['animeScaleX'] < 0), 'observed initial sprite flip')
    for index, row in enumerate(rows):
        require(row['text'] == FULL_TEXT[:index] and str(row['speakerId']) == '157', 'exact prefix and speaker identity')
        require(row['complete'] is (index == len(FULL_TEXT)), 'typing completion boundary')
        require(row['frame'] == row['firstObservedFrame'], 'capture must be first observation; no readiness wait')
        require(math.isfinite(row['elapsedSeconds']), 'finite observation time')
        require((row['width'], row['height']) == (2560, 1376), 'raw dimensions')
        require(isinstance(row['portraitReady'], bool), 'observed portrait readiness')
        if index:
            require(rows[index-1]['frame'] < row['frame'] and
                    rows[index-1]['elapsedSeconds'] <= row['elapsedSeconds'], 'monotonic prefix observations')
        if source:
            require(row['trigger'] == 'EVENT_AFTER_DRAW', 'source rendered observation')
            require(row['content'] == row['text'] and row['remaining'] == FULL_TEXT[index:], 'source content and remaining text')
            require((row['typingHandle'] is None) == row['complete'], 'source typing timer completion')
            if row['portraitReady']:
                require(row['portrait']['frameName'] == '214' and row['portrait']['active'] is True and
                        row['portrait']['textureGLReady'] is True, 'source selected portrait ready')
        else:
            require(row['observationPhase'] == 'post-render', 'port rendered observation')
            require(row['portraitId'] == 214, 'actual third-dialogue portrait selection')
        actors = row['actors']
        require(len(actors) == 4 and {actor['id'] for actor in actors} == set(INITIAL), 'four opening actors')
        for actor in actors:
            x, y, direction = INITIAL[actor['id']]
            require(actor['logical'] == [x, y] and actor['direction'] == direction and
                    actor['action'] == 0 and actor['frameRow'] == 0, 'stationary opening actor state')
            require(actor['showSpeechBubble'] is (actor['id'] == 157), 'speaker bubble')
            if source:
                require(actor['animeScaleX'] in (-1, 1) and actor['flipX'] is (actor['animeScaleX'] < 0),
                        'source flip derived from the actual sampled animation transform')
    return rows


def verify(source_path, game_path):
    source, game = [json.loads(path.read_text()) for path in (source_path, game_path)]
    source_rows, game_rows = validate(source, True), validate(game)
    phase_states = []
    for index, (source_frame, game_frame) in enumerate(zip(source['firstDialogueFrames'], game['firstDialogueFrames'])):
        asset_id = game_frame['actor157']['textureAssetId']
        source_texture = source_frame['actor157']['spriteTextureUrl']
        phase_states.append({'offset': index, 'sourceTexture': source_texture, 'gameAssetId': asset_id,
                             'equal': ASSET_UUIDS.get(asset_id, 'unknown-asset') in source_texture and source_frame['actor157']['flipX'] == game_frame['actor157']['flipX'],
                             'sourceFlipX': source_frame['actor157']['flipX'], 'gameFlipX': game_frame['actor157']['flipX'],
                             'sourceText': source_frame['text'], 'gameText': game_frame['text'],
                             'sourcePortraitReady': source_frame['portraitReady'], 'gamePortraitReady': game_frame['portraitReady']})
    samples = []
    for source_row, game_row in zip(source_rows, game_rows):
        buffers = []
        for manifest, row in ((source_path, source_row), (game_path, game_row)):
            require(Path(row['file']).name == row['file'], 'raw must be adjacent to manifest')
            raw = (manifest.parent / row['file']).read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row['sha256'], 'raw hash mismatch')
            buffers.append(raw)
        result = compare(*buffers, stage='text')
        readiness_equal = source_row['portraitReady'] == game_row['portraitReady']
        game_actors = {actor['id']: actor for actor in game_row['actors']}
        sprite_states = []
        for actor in source_row['actors']:
            port_actor = game_actors[actor['id']]
            asset_id = port_actor['textureAssetId']
            sprite_states.append({'actorId': actor['id'], 'gameAssetId': asset_id,
                                  'sourceTexture': actor['spriteTextureUrl'],
                                  'equal': ASSET_UUIDS.get(asset_id, 'unknown-asset') in actor['spriteTextureUrl'] and actor['flipX'] == port_actor['flipX'],
                                  'sourceFlipX': actor['flipX'], 'gameFlipX': port_actor['flipX']})
        result.update(contract='natural-opening-third-prefix-frame/v1', prefixLength=source_row['prefixLength'],
                      text=source_row['text'], sourcePortraitReady=source_row['portraitReady'],
                      gamePortraitReady=game_row['portraitReady'], portraitReadinessEqual=readiness_equal,
                      spriteStates=sprite_states, spriteSelectionEqual=all(state['equal'] for state in sprite_states),
                      scope='Entire first-observed prefix framebuffer, all RGBA channels without excluded regions.')
        samples.append(result)
    return {'contract': 'natural-opening-third-prefixes-comparison/v1',
            'equal': all(row['equal'] and row['portraitReadinessEqual'] and row['spriteSelectionEqual'] for row in samples) and
                     all(row['equal'] for row in phase_states),
            'emptyFrameEqual': samples[0]['equal'],
            'nonemptyFramesEqual': all(row['equal'] for row in samples[1:]),
            'samples': samples, 'initialSpritePhase': phase_states,
            'scope': 'Initial empty body and 12 first-observed natural typing prefixes in the full scene, plus actor157 sprite selection on the first three dialogue frames. Intermediate text/readiness observations are diagnostic; equal wall-clock timing, all intervening framebuffers and later dialogue are not proven.'}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    print(json.dumps({'equal': report['equal'], 'emptyFrameEqual': report['emptyFrameEqual'],
                      'nonemptyFramesEqual': report['nonemptyFramesEqual'],
                      'changedPixels': {row['prefixLength']: row['changedPixels'] for row in report['samples']}}, ensure_ascii=False))
    raise SystemExit(0 if report['equal'] else 1)
