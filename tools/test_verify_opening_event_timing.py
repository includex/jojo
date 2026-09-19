import unittest
from verify_opening_event_timing import replay, assess, TEXT


class EventTimerRuleTest(unittest.TestCase):
    def test_primes_drops_excess_and_starts_close_at_completion(self):
        deltas = [10.0] + [.03, .03] * len(TEXT) + [.4, .4, .3]
        rows = [{'frame': i, 'delta': delta} for i, delta in enumerate(deltas)]
        result = replay(rows)
        self.assertEqual(result['prefixFrames'], list(range(2, 17, 2)))
        self.assertEqual(result['closeFrame'], 19)

    def test_slow_frame_does_not_reveal_multiple_characters(self):
        rows = [{'frame': i, 'delta': 2.0} for i in range(10)]
        self.assertEqual(replay(rows), {'prefixFrames': list(range(1, 9)), 'closeFrame': 9})

    def test_actual_callback_frame_must_match_not_just_wall_duration(self):
        rows = [{'frame': i, 'delta': .05 if i < 9 else 1.0} for i in range(10)]
        prefixes = [{'text': TEXT[:i], 'frame': i} for i in range(1, 9)]
        self.assertTrue(assess(rows, prefixes, 9)['matchesSourceTimerRule'])
        prefixes[2]['frame'] = 2
        self.assertFalse(assess(rows, prefixes, 9)['matchesSourceTimerRule'])

    def test_invalid_delta_is_rejected(self):
        for delta in [-1.0, float('nan'), float('inf')]:
            with self.assertRaises(ValueError): replay([{'frame': 1, 'delta': delta}])

class SourceTimingEvidenceTest(unittest.TestCase):
    def test_rejects_forced_stale_or_duplicate_lifecycle_evidence(self):
        import copy
        from verify_opening_event_timing import validate_source
        names = ['InfoLayer.onCreate', 'InfoLayer.firstObserved', 'InfoLayer.autoClose',
                 'InfoLayer.complete', 'InfoLayer._next', 'InfoLayer.onDestroy',
                 'InfoLayer.gone', 'DialogueLayer.firstText']
        events = [dict(kind=name, frame=frame) for name, frame in zip(names, [0, 0, 8, 8, 10, 10, 10, 12])]
        events[1]['classHookInstalled'] = True
        events[3]['closeHandle'] = 'active'
        events[6]['destroyed'] = True
        rows = [dict(text=TEXT[:i], joined=TEXT[:i], content=TEXT[:i], remaining=TEXT[i:], frame=i,
                     typingHandle=None if i == len(TEXT) else 'active', closeHandle='active' if i == len(TEXT) else None,
                     uploaded=True, opacityChain=[255, 255]) for i in range(1, len(TEXT) + 1)]
        source = dict(target=TEXT, errors=[], bootstrap=dict(dialogueInputs=0, autoDialogueAdvance=False), events=events, prefixes=rows)
        validate_source(source)
        for mutate in [lambda d: d['bootstrap'].update(autoDialogueAdvance=True),
                       lambda d: d['events'].append(copy.deepcopy(d['events'][0])),
                       lambda d: d['events'][1].update(classHookInstalled=False),
                       lambda d: d['events'][4].update(frame=1),
                       lambda d: d['prefixes'][0].update(content=TEXT),
                       lambda d: d['prefixes'][0].update(typingHandle=None),
                       lambda d: d['prefixes'][0].update(uploaded=False),
                       lambda d: d['prefixes'][0].update(opacityChain=[254])]:
            bad = copy.deepcopy(source); mutate(bad)
            with self.assertRaises(ValueError): validate_source(bad)


if __name__ == '__main__':
    unittest.main()
