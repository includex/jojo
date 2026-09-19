#!/usr/bin/env python3
"""Compare the untouched first naturally completed opening frame, all RGBA pixels."""
import argparse
import hashlib
import json
from pathlib import Path

from verify_opening_panel_pixels import compare

TEXT = "대장님, 서둘러야 해요!"
EXPECTED = {0: (40, 35, 2), 157: (54, 65, 0), 181: (40, 45, 0), 182: (40, 25, 2)}


def require(condition, message):
    if not condition:
        raise ValueError(message)


def verify(source_path, game_path):
    source = json.loads(source_path.read_text())
    game = json.loads(game_path.read_text())
    require(source['contract'] == 'natural-opening-full-scene-rgba8-v1', 'source contract')
    require(game['contract'] == 'natural-opening-full-scene-game/v1', 'game contract')
    require(source['evidenceKind'] == 'actual-source-full-framebuffer-no-isolation', 'source isolation')
    require(source['trigger'] == 'EVENT_AFTER_DRAW', 'source capture trigger')
    require(source['bootstrap']['dialogueInputs'] == 0 and not source['bootstrap']['autoDialogueAdvance'], 'source input')
    require(game['isolation'] is False and game['dialogueInputs'] == 0, 'game isolation/input')
    for document in (source, game):
        require((document['width'], document['height'], document['origin']) == (2560, 1376, 'bottom-left'), 'frame dimensions/origin')
    dialogue = source['dialogue']
    require(dialogue['speakerId'] == 181 and dialogue['text'] == dialogue['content'] == TEXT, 'source dialogue')
    require(dialogue['remaining'] == '' and dialogue['typingHandle'] is None, 'source completion')
    require(str(game['speakerId']) == '181' and game['text'] == TEXT and game['complete'] is True, 'game completion')
    require(game['backgroundId'] == 71 and 'c6b7d3e4-8590-4fb6-85a5-7967e64abc3e' in source['background']['frame']['texture']['url'], 'background')
    for document in (source, game):
        actors = document['actors']
        require(len(actors) == 4 and {a['id'] for a in actors} == set(EXPECTED), 'actor identities')
        for actor in actors:
            x, y, direction = EXPECTED[actor['id']]
            actual = actor['logical'] if document is source else [actor['visualX'], actor['visualY']]
            require(actual == [x, y] and actor['direction'] == direction and actor['action'] == 0, 'actor state')
            require(actor['node']['active'] if document is source else actor['visible'] and actor['moveDuration'] == 0, 'actor visibility/motion')
    buffers = []
    for manifest, metadata in ((source_path, source['raw']), (game_path, game)):
        require(Path(metadata['file']).name == metadata['file'], 'raw file must be adjacent to manifest')
        raw = (manifest.parent / metadata['file']).read_bytes()
        require(hashlib.sha256(raw).hexdigest() == metadata['sha256'], 'raw hash mismatch')
        buffers.append(raw)
    report = compare(*buffers, stage='text')
    report['contract'] = 'natural-opening-full-scene-comparison/v1'
    report['scope'] = 'Entire first naturally completed R_00 dialogue framebuffer; no masks or excluded channels. Later frames and audio are outside this check.'
    report['sourceFrame'] = source['frame']
    report['gameFrame'] = game['frame']
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('source', type=Path)
    parser.add_argument('game', type=Path)
    parser.add_argument('--report', type=Path)
    args = parser.parse_args()
    report = verify(args.source, args.game)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + '\n')
    print(json.dumps(report))
    return 0 if report['equal'] else 1


if __name__ == '__main__':
    raise SystemExit(main())
