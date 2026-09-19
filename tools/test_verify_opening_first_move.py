import unittest
from verify_opening_first_move import DURATION, EPSILON, assess, replay, source_contract, source_positions, validate_game_clock


class FirstMoveRuleTest(unittest.TestCase):
    def test_source_control_flow_cannot_be_silently_discarded(self):
        prefix = "def scene1():\n    stage.showUnit(181,40,5,2)\n"
        for line in ["return", "flag = False", "helper()"]:
            with self.assertRaises(AssertionError):
                source_contract(prefix + f"    {line}\n    stage.unit(181).move(40,15,2)\n",
                                "cc.moveTo(.04 * o, u.x, u.y)")

    def test_observed_endpoints_cannot_define_their_own_reference(self):
        reference = dict(actorId=181, pathStart=[40, 5], pathEnd=[40, 15], nodeStart=[100, 200], nodeEnd=[0, 160])
        rows = [dict(frame=1, node=[100, 200]), dict(frame=2, node=[0, 160])]
        self.assertEqual(15, source_positions(rows, reference)[-1]['y'])
        rows[-1]['node'] = [0, 150]
        with self.assertRaises(ValueError):
            source_positions(rows, reference)

    def test_nonfinite_observed_position_is_rejected(self):
        frames = [dict(frame=1, delta=0), dict(frame=2, delta=1)]
        observed = [dict(frame=1, x=40, y=float('nan')), dict(frame=2, x=40, y=15)]
        with self.assertRaises(ValueError):
            assess(frames, observed, 2)
        reference = dict(actorId=181, pathStart=[40, 5], pathEnd=[40, 15], nodeStart=[100, 200], nodeEnd=[0, 160])
        with self.assertRaises(ValueError):
            source_positions([dict(frame=1, node=[100, 200]), dict(frame=2, node=[0, float('nan')])], reference)

    def test_authoritative_duration_and_double_clock_are_required(self):
        initial = dict(hallMoveElapsedSeconds=0, hallMoveDurationSeconds=DURATION)
        actor = dict(id=181, x=40, y=5, hallMoveElapsedSeconds=0, hallMoveDurationSeconds=DURATION)
        rows = [dict(deltaSeconds=10, actors=[actor])]
        validate_game_clock(initial, rows)
        with self.assertRaises(ValueError):
            validate_game_clock(dict(initial, hallMoveDurationSeconds=.4), rows)
        actor['hallMoveElapsedSeconds'] = 1e-8
        with self.assertRaises(ValueError):
            validate_game_clock(initial, rows)

    def test_small_error_that_changes_float_projection_is_rejected(self):
        frames = [dict(frame=1, delta=0), dict(frame=2, delta=1)]
        observed = [dict(frame=1, x=40, y=5.000001), dict(frame=2, x=40, y=15)]
        self.assertFalse(assess(frames, observed, 2)['matchesActionRule'])

    def test_first_tick_discards_even_large_delta(self):
        result = replay([{'frame': 1, 'delta': 10}, {'frame': 2, 'delta': .2}, {'frame': 3, 'delta': .21}])
        self.assertEqual(5, result[0]['y'])
        self.assertEqual(3, result[-1]['frame'])
        self.assertAlmostEqual(10 - 25 * EPSILON, result[1]['y'])

    def test_zero_duration_nested_sequence_affects_completion_boundary(self):
        result = replay([{'frame': 1, 'delta': 0}, {'frame': 2, 'delta': .4}, {'frame': 3, 'delta': EPSILON * 2}])
        self.assertFalse(result[1]['complete'])
        self.assertEqual(3, result[-1]['frame'])

    def test_matching_end_frame_does_not_hide_early_motion(self):
        frames = [{'frame': 1, 'delta': .01}, {'frame': 2, 'delta': DURATION}]
        observed = [{'frame': 1, 'x': 40, 'y': 5.25}, {'frame': 2, 'x': 40, 'y': 15}]
        self.assertFalse(assess(frames, observed, 2)['matchesActionRule'])

    def test_wrong_completion_frame_fails_even_with_correct_positions(self):
        frames = [{'frame': 1, 'delta': .01}, {'frame': 2, 'delta': DURATION}]
        observed = [{'frame': 1, 'x': 40, 'y': 5}, {'frame': 2, 'x': 40, 'y': 15}]
        self.assertFalse(assess(frames, observed, 3)['matchesActionRule'])

    def test_missing_frame_or_nonfinite_delta_rejected(self):
        for frames in ([{'frame': 1, 'delta': 0}, {'frame': 3, 'delta': 1}],
                       [{'frame': 1, 'delta': float('nan')}],
                       [{'frame': 1, 'delta': -.1}]):
            with self.assertRaises(ValueError):
                replay(frames)


if __name__ == '__main__':
    unittest.main()
