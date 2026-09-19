import unittest
from verify_opening_dialogue_auto import threshold_frame


def frames(deltas):
    return [dict(frame=i, delta=d) for i, d in enumerate(deltas)]


class AutoTimerTest(unittest.TestCase):
    def test_prime_ignores_large_delta_and_float_tenths_fire_on_sixteenth(self):
        self.assertEqual(threshold_frame(frames([10] + [.10000000149011612] * 16), 1.6), 16)

    def test_next_page_glyph_starts_after_prime(self):
        self.assertEqual(threshold_frame(frames([10, .02, .02]), .04), 2)

    def test_incomplete_capture_does_not_pass(self):
        with self.assertRaises(ValueError):
            threshold_frame(frames([0, 1.59]), 1.6)

    def test_invalid_delta_or_gap_is_rejected(self):
        with self.assertRaises(ValueError):
            threshold_frame(frames([0, float('nan')]), 1.6)
        with self.assertRaises(ValueError):
            threshold_frame([dict(frame=1, delta=0), dict(frame=3, delta=2)], 1.6)

    def test_dialogue_identity_cannot_change_while_waiting(self):
        import copy
        from verify_opening_dialogue_auto import assess_game
        first = dict(speaker='181', revision=1, text='대장님, 서둘러야 해요!', complete=True, playback='DIALOGUE')
        second = dict(speaker='0', revision=2, text='', complete=False, playback='DIALOGUE')
        game = dict(contract='natural-opening-dialogue-auto-game/v1', autoCloseSetting=True,
                    persistentSettingsChanged=False, dialogueInputs=0, pixelReadback=False, isolation=False,
                    frames=[dict(first, frame=0, delta=0), dict(first, frame=1, delta=.8),
                            dict(second, frame=2, delta=.8), dict(second, frame=3, delta=.05)])
        game['frames'][-1]['text'] = '알'
        self.assertTrue(assess_game(game)['matchesSourceRule'])
        for field, value in [('revision', 9), ('text', '잘못된 본문'), ('playback', 'MODAL')]:
            broken = copy.deepcopy(game)
            broken['frames'][1][field] = value
            with self.assertRaises(ValueError):
                assess_game(broken)
        broken = copy.deepcopy(game)
        broken['frames'][-1]['revision'] = 3
        with self.assertRaises(ValueError):
            assess_game(broken)
