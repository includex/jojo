import unittest
from verify_opening_panel_pixels import compare


class OpeningPanelPixelsTest(unittest.TestCase):
    def test_exact_color_and_alpha(self):
        source = bytes([100, 100, 100, 255])
        self.assertTrue(compare(source, source, 1, 1)["equal"])
        for pixel in ([99, 100, 100, 255], [100, 100, 100, 254]):
            result = compare(source, bytes(pixel), 1, 1)
            self.assertFalse(result["equal"])
            self.assertEqual(1, result["changedPixels"])

    def test_empty_black_or_wrong_size_are_not_evidence(self):
        for data in (b"", bytes([0, 0, 0, 255]), bytes([100, 100, 100])):
            with self.assertRaises(ValueError):
                compare(data, data, 1, 1)


if __name__ == "__main__":
    unittest.main()
