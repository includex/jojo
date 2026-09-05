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
            port = root / "port.png"
            Image.new("RGB", (2, 1), (10, 20, 30)).save(source)
            image = Image.new("RGB", (2, 1), (10, 20, 30))
            image.putpixel((1, 0), (11, 20, 30))
            image.save(port)
            report = MODULE.compare(source, port, "raw")
            self.assertEqual("fail", report["status"])
            self.assertEqual(1, report["normalized"]["changedPixels"])
            self.assertEqual("raw", report["normalization"]["mode"])

    def test_explicit_icc_conversion_records_metadata(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            port = root / "port.png"
            profile = ImageCms.ImageCmsProfile(ImageCms.createProfile("sRGB"))
            pixels = Image.new("RGB", (2, 2), (42, 84, 126))
            pixels.save(source, icc_profile=profile.tobytes())
            pixels.save(port)
            report = MODULE.compare(source, port, "source-to-srgb")
            self.assertEqual("pass", report["status"])
            self.assertTrue(report["sourceProfile"]["embedded"])
            self.assertFalse(report["portProfile"]["embedded"])
            self.assertEqual("sRGB", report["normalization"]["target"])
            self.assertTrue(report["normalized"]["pixelEqual"])

    def test_icc_conversion_rejects_untagged_source(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            port = root / "port.png"
            Image.new("RGB", (1, 1), "black").save(source)
            Image.new("RGB", (1, 1), "black").save(port)
            with self.assertRaisesRegex(ValueError, "embedded source ICC"):
                MODULE.compare(source, port, "source-to-srgb")

    def test_dimension_mismatch_is_not_a_pixel_pass(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            source = root / "source.png"
            port = root / "port.png"
            Image.new("RGB", (1, 1), "black").save(source)
            Image.new("RGB", (2, 1), "black").save(port)
            report = MODULE.compare(source, port, "raw")
            self.assertEqual("dimension-mismatch", report["status"])
            self.assertFalse(report["pixelEqual"])


if __name__ == "__main__":
    unittest.main()
