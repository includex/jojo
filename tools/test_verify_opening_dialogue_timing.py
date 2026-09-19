import unittest
from verify_opening_dialogue_timing import TEXT, assess, replay


def frames(deltas):
    return [dict(frame=i, delta=d) for i, d in enumerate(deltas)]


class DialogueTimingTest(unittest.TestCase):
    def test_first_update_primes_and_long_frames_do_not_catch_up(self):
        self.assertEqual(replay(frames([10, .1, 0, .1])), [1, 3])

    def test_overshoot_is_discarded_and_threshold_is_double(self):
        self.assertEqual(replay(frames([0, .03999999910593033, .001, .039, .001])), [2, 4])

    def test_missing_or_nonfinite_frame_fails(self):
        with self.assertRaises(ValueError):
            replay([dict(frame=0, delta=0), dict(frame=2, delta=.1)])
        with self.assertRaises(ValueError):
            replay(frames([0, float('nan')]))

    def test_early_first_glyph_is_reported_as_difference(self):
        prefixes = [dict(frame=i, text=TEXT[:i]) for i in range(1, len(TEXT) + 1)]
        result = assess(frames([.02] * 30), prefixes)
        self.assertFalse(result['matchesSourceTimerRule'])
        self.assertEqual(result['expectedPrefixFrames'][0], 2)
