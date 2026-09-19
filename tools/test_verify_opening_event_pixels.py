import copy
import unittest
from verify_opening_event_pixels import TEXT, validate, validate_prefixes


class OpeningEventProvenanceTest(unittest.TestCase):
    def setUp(self):
        self.manifest = dict(contract='natural-opening-event-rgba8', width=2560, height=1376,
            origin='bottom-left', eventText=TEXT, dialogueInputs=0, observedStrings=[TEXT[:1], TEXT])
        for i, name in enumerate(['fullFrame', 'isolatedOverlay']):
            self.manifest[name] = dict(frame=i + 1, elapsedSeconds=float(i + 1), text=TEXT, complete=True,
                naturalModalIsolation=bool(i), content=TEXT, richText=TEXT, remaining='', typingHandle=None,
                closeHandle='active', trigger='EVENT_AFTER_DRAW', clearColor=[0, 0, 0, 1])

    def test_accepts_natural_event_and_live_closing_timer(self):
        validate(self.manifest); validate(self.manifest, source=True)

    def test_rejects_inputs_incomplete_text_and_wrong_isolation(self):
        for mutate in [lambda d: d.update(dialogueInputs=1),
                lambda d: d['fullFrame'].update(complete=False),
                lambda d: d['isolatedOverlay'].update(naturalModalIsolation=False),
                lambda d: d['isolatedOverlay'].update(clearColor=[.7, .7, .7, 1]),
                lambda d: d.update(observedStrings=[TEXT])]:
            bad = copy.deepcopy(self.manifest); mutate(bad)
            with self.assertRaises(ValueError): validate(bad)

    def test_rejects_forced_source_and_stale_isolated_metadata(self):
        for field, value in [('typingHandle', 'active'), ('closeHandle', None), ('content', TEXT[:1]),
                             ('remaining', 'pending'), ('trigger', 'before-draw')]:
            bad = copy.deepcopy(self.manifest); bad['isolatedOverlay'][field] = value
            with self.assertRaises(ValueError): validate(bad, source=True)

    def test_rejects_reversed_or_nonfinite_capture_order(self):
        for field, value in [('frame', 1), ('elapsedSeconds', float('nan')), ('elapsedSeconds', .5)]:
            bad = copy.deepcopy(self.manifest); bad['isolatedOverlay'][field] = value
            with self.assertRaises(ValueError): validate(bad)


class OpeningEventPrefixesTest(unittest.TestCase):
    def test_requires_every_string_in_observed_order(self):
        rows = [dict(text=TEXT[:i], frame=i, elapsedSeconds=i / 10, width=2560, height=1376,
                     trigger='EVENT_AFTER_DRAW', joined=TEXT[:i], content=TEXT[:i], remaining=TEXT[i:],
                     typingHandle=None if i == len(TEXT) else 'active', effectiveOpacity=255, uploaded=True) for i in range(1, len(TEXT) + 1)]
        manifest = dict(prefixes=rows, fullFrame=dict(frame=10, elapsedSeconds=1.0))
        validate_prefixes(manifest, True)
        for row in rows:
            row.update(complete=row['text'] == TEXT, naturalModalIsolation=False)
        validate_prefixes(manifest)
        for field, value in [('complete', True), ('naturalModalIsolation', True)]:
            bad = copy.deepcopy(manifest); bad['prefixes'][0][field] = value
            with self.assertRaises(ValueError): validate_prefixes(bad)
        for mutate in [lambda d: d['prefixes'].pop(1),
                       lambda d: d['prefixes'][1].update(frame=1),
                       lambda d: d['prefixes'][1].update(elapsedSeconds=float('nan')),
                       lambda d: d['prefixes'][1].update(trigger='before-draw'),
                       lambda d: d['fullFrame'].update(frame=2)]:
            bad = copy.deepcopy(manifest); mutate(bad)
            with self.assertRaises(ValueError): validate_prefixes(bad, True)
