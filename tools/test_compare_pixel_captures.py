#!/usr/bin/env python3
import importlib.util
import tempfile
import unittest
import sys
from pathlib import Path

from PIL import Image, ImageCms

MODULE_PATH = Path(__file__).with_name("compare_pixel_captures.py")
SPEC = importlib.util.spec_from_file_location("compare_pixel_captures", MODULE_PATH)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
sys.modules[SPEC.name] = MODULE
SPEC.loader.exec_module(MODULE)


class PixelCaptureComparatorTest(unittest.TestCase):
    def test_raw_comparison_is_strict(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            game = root / "game.png"
            Image.new("RGB", (2, 1), (10, 20, 30)).save(source)
            image = Image.new("RGB", (2, 1), (10, 20, 30))
            image.putpixel((1, 0), (11, 20, 30))
            image.save(game)
            report = MODULE.compare(source, game, "raw")
            self.assertEqual("fail", report["status"])
            self.assertEqual(1, report["normalized"]["changedPixels"])
            self.assertEqual("raw", report["normalization"]["mode"])

    def test_explicit_icc_conversion_records_metadata(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            game = root / "game.png"
            profile = ImageCms.ImageCmsProfile(ImageCms.createProfile("sRGB"))
            pixels = Image.new("RGB", (2, 2), (42, 84, 126))
            pixels.save(source, icc_profile=profile.tobytes())
            pixels.save(game)
            report = MODULE.compare(source, game, "source-to-srgb")
            self.assertEqual("pass", report["status"])
            self.assertTrue(report["sourceProfile"]["embedded"])
            self.assertFalse(report["gameProfile"]["embedded"])
            self.assertEqual("sRGB", report["normalization"]["target"])
            self.assertTrue(report["normalized"]["pixelEqual"])

    def test_icc_conversion_rejects_untagged_source(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            game = root / "game.png"
            Image.new("RGB", (1, 1), "black").save(source)
            Image.new("RGB", (1, 1), "black").save(game)
            with self.assertRaisesRegex(ValueError, "embedded source ICC"):
                MODULE.compare(source, game, "source-to-srgb")

    def test_dimension_mismatch_is_not_a_pixel_pass(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            game = root / "game.png"
            Image.new("RGB", (1, 1), "black").save(source)
            Image.new("RGB", (2, 1), "black").save(game)
            report = MODULE.compare(source, game, "raw")
            self.assertEqual("dimension-mismatch", report["status"])
            self.assertFalse(report["pixelEqual"])


if __name__ == "__main__":
    unittest.main()
