import unittest
from verify_opening_panel_pixels import compare


class OpeningPanelPixelsTest(unittest.TestCase):
    def test_exact_color_and_alpha(self):
        source = bytes([100, 100, 100, 255])
        for stage in ("panel", "portrait", "speaker"):
            self.assertTrue(compare(source, source, 1, 1, stage=stage)["equal"])
        for pixel in ([99, 100, 100, 255], [100, 100, 100, 254]):
            result = compare(source, bytes(pixel), 1, 1)
            self.assertFalse(result["equal"])
            self.assertEqual(1, result["changedPixels"])

    def test_difference_bounds_include_alpha_and_use_exclusive_edges(self):
        source = bytes([100, 100, 100, 255]) * 6
        game = bytearray(source)
        game[4 * 5 + 3] = 254
        result = compare(source, game, 3, 2, stage="speaker")
        self.assertEqual([2, 1, 3, 2], result["changedBoundsBottomLeft"])
        self.assertEqual(1, result["absoluteRgbaError"])
        self.assertEqual(1, result["squaredRgbaError"])
        self.assertIsNone(compare(source, source, 3, 2)["changedBoundsBottomLeft"])

    def test_empty_black_or_wrong_size_are_not_evidence(self):
        for data in (b"", bytes([0, 0, 0, 255]), bytes([100, 100, 100])):
            with self.assertRaises(ValueError):
                compare(data, data, 1, 1)


if __name__ == "__main__":
    unittest.main()
