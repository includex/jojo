import copy
import unittest
from verify_opening_page_pixels import TEXTS, SPEAKERS, validate


class OpeningPageProvenanceTest(unittest.TestCase):
    def setUp(self):
        self.manifest = dict(contract='natural-opening-pages-rgba8', width=2560, height=1376,
                             origin='bottom-left', dialogueInputs=2, captures=[], inputs=[])
        for i, text in enumerate(TEXTS):
            self.manifest['captures'].append(dict(page=i + 1, text=text, speakerId=SPEAKERS[i],
                complete=True, dialogueInputs=i, frame=i * 10 + 10, elapsedSeconds=float(i * 10 + 10),
                side=['right', 'left', 'right'][i], naturalStreetTextIsolation=True, observedStrings=[text[:1], text],
                trigger='EVENT_AFTER_DRAW', content=text, richText=text,
                remaining='', handle=None, segments=[dict(string=text)]))
            if i < 2:
                self.manifest['inputs'].append(dict(afterPage=i + 1, frame=i * 10 + 11,
                    elapsedSeconds=float(i * 10 + 11), kind='InputProcessor.keyDown/keyUp(SPACE)',
                    completeBeforeInput=True, textBeforeInput=text))

    def source_manifest(self):
        manifest = copy.deepcopy(self.manifest)
        for event in manifest['inputs']:
            event.update(kind='pointer', target='Panel_cancel', dialogueAdvance=True)
        return manifest

    def test_accepts_natural_completed_pages_and_two_inputs(self):
        self.assertEqual(len(validate(self.manifest)), 3)
        self.assertEqual(len(validate(self.source_manifest(), source=True)), 3)

    def test_rejects_missing_page_wrong_speaker_and_missing_partial(self):
        for mutate in [lambda d: d['captures'].pop(),
                       lambda d: d['captures'][1].update(speakerId='181'),
                       lambda d: d['captures'][1].update(observedStrings=[TEXTS[1]])]:
            bad = copy.deepcopy(self.manifest); mutate(bad)
            with self.assertRaises(ValueError): validate(bad)

    def test_rejects_extra_early_or_uncompleted_input(self):
        for mutate in [lambda d: d.update(dialogueInputs=3),
                       lambda d: d['inputs'][0].update(frame=10),
                       lambda d: d['inputs'][0].update(completeBeforeInput=False),
                       lambda d: d['inputs'][1].update(textBeforeInput=TEXTS[1][:1])]:
            bad = copy.deepcopy(self.manifest); mutate(bad)
            with self.assertRaises(ValueError): validate(bad)

    def test_rejects_forced_source_and_unisolated_game(self):
        for field, value in [('handle', 'active'), ('remaining', 'unfinished'), ('richText', 'wrong')]:
            bad = self.source_manifest(); bad['captures'][2][field] = value
            with self.assertRaises(ValueError): validate(bad, source=True)
        bad = copy.deepcopy(self.manifest); bad['captures'][0]['naturalStreetTextIsolation'] = False
        with self.assertRaises(ValueError): validate(bad)

    def test_rejects_wrong_side_and_direct_interpreter_input(self):
        bad = copy.deepcopy(self.manifest); bad['captures'][1]['side'] = 'right'
        with self.assertRaises(ValueError): validate(bad)
        bad = copy.deepcopy(self.manifest); bad['inputs'][0]['kind'] = 'AdvanceDialogue'
        with self.assertRaises(ValueError): validate(bad)
        bad = self.source_manifest(); bad['inputs'][0]['target'] = 'script-direct-call'
        with self.assertRaises(ValueError): validate(bad, source=True)
