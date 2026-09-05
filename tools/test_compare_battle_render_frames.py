#!/usr/bin/env python3
import importlib.util
import sys
import unittest
from pathlib import Path

from PIL import Image, ImageDraw, ImageStat

MODULE_PATH = Path(__file__).with_name("compare_battle_render_frames.py")
SPEC = importlib.util.spec_from_file_location("compare_battle_render_frames", MODULE_PATH)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
sys.modules[SPEC.name] = MODULE
SPEC.loader.exec_module(MODULE)


class BattleFrameStructuralComparatorTest(unittest.TestCase):
    def test_broad_gpu_colour_shift_has_no_structural_delta(self):
        source = Image.new("RGB", (256, 128), (50, 120, 70))
        port = Image.new("RGB", source.size, (44, 121, 53))
        self.assertLess(ImageStat.Stat(MODULE.structural_delta(source, port)).mean[0], 1.1)

    def test_different_actor_pose_remains_visible_after_sampler_filter(self):
        source = Image.new("RGB", (256, 128), (50, 120, 70))
        port = Image.new("RGB", source.size, (44, 121, 53))
        ImageDraw.Draw(source).rectangle((80, 32, 120, 96), fill=(230, 60, 20))
        ImageDraw.Draw(port).ellipse((80, 32, 120, 96), fill=(230, 60, 20))
        delta = MODULE.structural_delta(source, port)
        changed_ratio = sum(value > 8 for value in delta.getdata()) / (256 * 128)
        self.assertGreater(changed_ratio, 0.003)


if __name__ == "__main__":
    unittest.main()
