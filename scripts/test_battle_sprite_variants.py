"""Frame boundary and source-preservation checks for optional runtime variants."""
import hashlib
import json
from pathlib import Path
import tempfile
import unittest
from PIL import Image
from build_battle_sprite_variants import build_variant


class BattleSpriteVariantTest(unittest.TestCase):
    def test_resampling_keeps_frames_isolated_and_native_bytes_unchanged(self):
        for size in (48, 64):
            with self.subTest(size=size), tempfile.TemporaryDirectory() as directory:
                source = Path(directory) / 'native.png'
                output = Path(directory) / 'runtime.png'
                atlas = Image.new('RGBA', (size, (size + 2) * 2))
                atlas.paste((255, 0, 0, 255), (0, 1, size, size + 1))
                atlas.paste((0, 0, 255, 255), (0, size + 3, size, size * 2 + 3))
                atlas.save(source)
                before = source.read_bytes()
                metadata = build_variant(source, output)
                self.assertEqual(source.read_bytes(), before)
                self.assertEqual(metadata['sourceSha256'], hashlib.sha256(before).hexdigest())
                self.assertEqual(json.loads(output.with_suffix('.json').read_text()), metadata)
                variant = Image.open(output).convert('RGBA')
                target = round(size * 1.3)
                self.assertEqual(variant.size, (target, (target + 2) * 2))
                for index, expected in enumerate(((255, 0, 0, 255), (0, 0, 255, 255))):
                    y = index * (target + 2)
                    self.assertIsNone(variant.crop((0, y, target, y + 1)).getchannel('A').getbbox())
                    self.assertIsNone(variant.crop((0, y + target + 1, target, y + target + 2)).getchannel('A').getbbox())
                    frame = variant.crop((0, y + 1, target, y + target + 1))
                    self.assertEqual(set(frame.getdata()), {expected})

    def test_invalid_gutter_fails_before_overwriting_any_native_file(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / 'native.png'
            Image.new('RGBA', (48, 50), 'red').save(source)
            with self.assertRaisesRegex(ValueError, 'gutter'):
                build_variant(source, Path(directory) / 'runtime.png')


if __name__ == '__main__':
    unittest.main()
