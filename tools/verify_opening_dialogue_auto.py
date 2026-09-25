#!/usr/bin/env python3
"""Verify natural first dialogue auto-close and next-page glyph initialization."""
import argparse
import json
import math
from pathlib import Path

from dialogue_text_catalog import dialogue_text

FIRST_TEXT = dialogue_text("opening_scene1_page1")
SECOND_TEXT = dialogue_text("opening_scene1_page2")


def threshold_frame(rows, seconds):
    elapsed = 0.0
    previous = None
    for index, row in enumerate(rows):
        frame, delta = row['frame'], row['delta']
        if not math.isfinite(delta) or delta < 0 or (previous is not None and frame != previous + 1):
            raise ValueError('Invalid or missing timer update')
        previous = frame
        if index == 0:
            continue
        elapsed += delta
        if elapsed >= seconds:
            return frame
    raise ValueError('Capture ended before timer threshold')


def assess_game(game):
    if (game.get('contract') != 'natural-opening-dialogue-auto-game/v1' or
            game.get('autoCloseSetting') is not True or game.get('persistentSettingsChanged') is not False or
            game.get('dialogueInputs') != 0 or game.get('pixelReadback') is not False or game.get('isolation') is not False):
        raise ValueError('Unsupported or modified game capture')
    frames = game['frames']
    complete = next(i for i, row in enumerate(frames) if row['speaker'] == '181' and row['complete'])
    if frames[complete]['text'] != FIRST_TEXT:
        raise ValueError('Wrong first dialogue')
    next_page = next(i for i in range(complete + 1, len(frames)) if frames[i]['speaker'] == '0')
    first_revision = frames[complete]['revision']
    next_revision = frames[next_page]['revision']
    if (next_revision == first_revision or frames[next_page]['text'] != '' or
            any(r['speaker'] != '181' or not r['complete'] or r['playback'] != 'DIALOGUE' or
                r['revision'] != first_revision or r['text'] != FIRST_TEXT
                for r in frames[complete:next_page])):
        raise ValueError('Unexpected dialogue transition')
    first_glyph = next(r for r in frames[next_page:] if r['text'])
    if first_glyph['text'] != SECOND_TEXT[:1] or first_glyph['speaker'] != '0':
        raise ValueError('Wrong second dialogue glyph')
    if any(r['speaker'] != '0' or r['playback'] != 'DIALOGUE' or r['revision'] != next_revision
           or r['complete'] or r['text'] not in ('', SECOND_TEXT[:1]) for r in frames[next_page:]):
        raise ValueError('Second dialogue identity changed')
    expected_close = threshold_frame(frames[complete:], 1.6)
    expected_glyph = threshold_frame(frames[next_page:], .04)
    return dict(matchesSourceRule=expected_close == frames[next_page]['frame'] and expected_glyph == first_glyph['frame'],
                completionFrame=frames[complete]['frame'], expectedAdvanceFrame=expected_close,
                actualAdvanceFrame=frames[next_page]['frame'], expectedNextGlyphFrame=expected_glyph,
                actualNextGlyphFrame=first_glyph['frame'])


def assess_source(source):
    from verify_opening_event_timing import validate_source
    if source.get('contract') != 'natural-opening-dialogue-auto-timing-v1':
        raise ValueError('Unexpected source contract')
    validate_source(source)
    settings = [a for a in source['bootstrap']['actions'] if a.get('kind') == 'FreshProfileSetting']
    if len(settings) != 1 or settings[0] != dict(kind='FreshProfileSetting', key='GAME_SETTING', value=8, temporary=True):
        raise ValueError('Source auto-close setting provenance missing')
    def one(rows, kind):
        selected = [r for r in rows if r['kind'] == kind]
        if len(selected) != 1:
            raise ValueError('Missing or duplicate ' + kind)
        return selected[0]
    events = source['autoCloseEvents']
    register = one(events, 'DialogueLayer.autoClose.register')
    callback = one(events, 'DialogueLayer.autoClose.callback.after')
    complete = one(source['events'], 'DialogueLayer.firstComplete')
    first_glyph = one(source['events'], 'DialogueLayer.secondSpeakerFirstText')
    if (register['delay'] != 1.6 or register['callbackIdentityPreserved'] is not True or
            register['frame'] != complete['frame'] or callback['speakerId'] != 0 or
            callback['content'] != '' or callback['remaining'] != SECOND_TEXT or first_glyph['text'] != SECOND_TEXT[:1]):
        raise ValueError('Source dialogue registration or transition inconsistent')
    updates = [r for r in events if r['kind'] == 'DialogueLayer.autoClose.timer.after']
    if (not updates or updates[0]['frame'] != register['frame'] or updates[0]['elapsedBefore'] != -1 or
            updates[0]['elapsedAfter'] != 0 or updates[0]['callbackFired'] or
            updates[-1]['scheduledActive'] is not False or
            [r['frame'] for r in updates if r['callbackFired']] != [callback['frame']]):
        raise ValueError('Source auto timer prime or removal unproven')
    for i, row in enumerate(updates):
        if row['updateIndex'] != i or row['scheduledActive'] is not (i < len(updates) - 1):
            raise ValueError('Source auto timer update sequence inconsistent')
    glyph_register = one([r for r in source['delayBoundaryEvents'] if r.get('scheduleId') == 1], 'DialogueLayer.glyphSchedule.register')
    glyph_updates = [r for r in source['delayBoundaryEvents'] if r.get('scheduleId') == 1 and r['kind'] == 'DialogueLayer.glyphTimer.update.after']
    if (glyph_register['frame'] != callback['frame'] or glyph_register['interval'] != .04 or
            not glyph_updates or glyph_updates[0]['frame'] != callback['frame'] or
            glyph_updates[0]['elapsedBefore'] != -1 or glyph_updates[0]['elapsedAfter'] != 0 or
            glyph_updates[0]['callbackFired'] or
            [r['frame'] for r in glyph_updates if r['callbackFired']] != [first_glyph['frame']]):
        raise ValueError('Source next page same-frame prime unproven')
    expected_close = threshold_frame([dict(frame=r['frame'], delta=r['dt']) for r in updates], 1.6)
    expected_glyph = threshold_frame([dict(frame=r['frame'], delta=r['dt']) for r in glyph_updates], .04)
    return dict(matchesSourceRule=expected_close == callback['frame'] and expected_glyph == first_glyph['frame'],
                completionFrame=complete['frame'], expectedAdvanceFrame=expected_close,
                actualAdvanceFrame=callback['frame'], expectedNextGlyphFrame=expected_glyph,
                actualNextGlyphFrame=first_glyph['frame'])


def verify(source_path, game_path):
    source, game = [json.loads(p.read_text()) for p in (source_path, game_path)]
    source_result, game_result = assess_source(source), assess_game(game)
    return dict(contract='natural-opening-dialogue-auto-rule/v1',
                autoAdvanceMatchesSourceRule=source_result['matchesSourceRule'] and game_result['matchesSourceRule'],
                source=source_result, game=game_result,
                scope='auto-enabled first Hall dialogue natural completion through next speaker first glyph; total latency and flag overrides excluded')


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
    raise SystemExit(0 if result['autoAdvanceMatchesSourceRule'] else 1)
