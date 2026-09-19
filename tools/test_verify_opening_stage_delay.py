import unittest
from verify_opening_stage_delay import fire_frame

class StageDelayTest(unittest.TestCase):
    def test_large_initial_delta_only_primes_then_float_tenths_finish(self):
        rows = [dict(frame=1, delta=10)] + [dict(frame=i, delta=0.10000000149011612) for i in (2, 3, 4)]
        self.assertEqual(4, fire_frame(rows))

    def test_missing_update_and_nonfinite_delta_rejected(self):
        for rows in ([dict(frame=1, delta=0), dict(frame=3, delta=1)], [dict(frame=1, delta=float('nan'))]):
            with self.assertRaises(ValueError):
                fire_frame(rows)

if __name__ == '__main__':
    unittest.main()
