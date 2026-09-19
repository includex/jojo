import json
import tempfile
import unittest
from pathlib import Path
from verify_opening_portrait_ready import verify


def captures():
    source = dict(errors=[], portraitHookInstalled=True, bootstrap=dict(dialogueInputs=2, autoDialogueAdvance=False),
                  portraitPages=[], portraitEvents=[])
    game = dict(contract='natural-opening-portrait-readiness-game/v1', pixelReadback=False, isolation=False,
                dialogueInputs=2, frames=[], portraitRegionSelections=[])
    for page, (speaker, portrait) in enumerate([('181', 181), ('0', 1), ('157', 214)]):
        start = page * 10
        layer = 'first' if page < 2 else 'second'
        rows = []
        for offset in [0, 1]:
            face = dict(isCurrentBg=True, speakerId=int(speaker), bgActive=True, faceNodeActive=True,
                        textureGLReady=bool(offset), framePresent=bool(offset), frameName=str(portrait) if offset else None,
                        rect=[395, 2, 192, 240] if offset else None)
            rows.append(dict(frame=start + offset, trigger='EVENT_AFTER_DRAW', layerIdentity=layer, sides=[face]))
            game['frames'].append(dict(frame=start + offset, speaker=speaker, text='' if not offset else 'text', complete=bool(offset)))
            game['portraitRegionSelections'].append(dict(frame=start + offset, portraitId=portrait, regionReturned=bool(offset),
                textureHandle=4 if offset else 0, regionX=395 if offset else 0, regionY=2 if offset else 0,
                width=192 if offset else 0, height=240 if offset else 0))
        source['portraitPages'].append(dict(pageIndex=page, layerIdentity=layer, frames=rows))
        source['portraitEvents'] += [dict(kind='DialogueLayer.loadByUrl.request', pageIndex=page, frame=start,
                                         url=f'Game/Head/{portrait}', cacheBefore=dict(cached=False)),
                                    dict(kind='DialogueLayer.loadByUrl.callback.afterApply', pageIndex=page,
                                         frame=start + 1, frameName=str(portrait), textureGLReady=True)]
    return source, game


class PortraitReadinessTest(unittest.TestCase):
    def verify_pair(self, source, game):
        with tempfile.TemporaryDirectory() as d:
            s, g = Path(d) / 'source.json', Path(d) / 'game.json'
            s.write_text(json.dumps(source)); g.write_text(json.dumps(game))
            return verify(s, g)

    def test_blank_then_ready_matches(self):
        self.assertTrue(self.verify_pair(*captures())['matchesSourceReadinessSequence'])

    def test_synchronous_first_frame_is_difference(self):
        s, g = captures()
        g['portraitRegionSelections'][0].update(g['portraitRegionSelections'][1], frame=0)
        self.assertFalse(self.verify_pair(s, g)['matchesSourceReadinessSequence'])

    def test_missing_or_non_gpu_selection_is_rejected(self):
        s, g = captures()
        del g['portraitRegionSelections'][0]
        with self.assertRaises(ValueError): self.verify_pair(s, g)
        s, g = captures()
        g['portraitRegionSelections'][1]['textureHandle'] = 0
        with self.assertRaises(ValueError): self.verify_pair(s, g)

    def test_stale_source_layer_header_is_rejected(self):
        s, g = captures()
        s['portraitPages'][2]['layerIdentity'] = 'first'
        with self.assertRaises(ValueError): self.verify_pair(s, g)
