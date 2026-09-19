import hashlib
import json
from pathlib import Path
import tempfile
import unittest

from verify_opening_background_pixels import verify


class BackgroundPixelsTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        self.jpeg = b'fixture-native-asset'
        (self.root / 'native.jpg').write_bytes(self.jpeg)
        self.rgba = b'\x01\x02\x03\xff\x04\x05\x06\xff'
        def payload(name):
            (self.root / name).write_bytes(self.rgba)
            return {'file': name, 'sha256': hashlib.sha256(self.rgba).hexdigest(), 'byteLength': 8}
        gpu = payload('source.rgba')
        self.source = {'contract': 'natural-opening-event-rgba8', 'sourceRoot': str(self.root), 'backgroundTexture': {
            **gpu, 'canvasFile': 'canvas.rgba', 'canvasSha256': gpu['sha256'], 'canvasByteLength': 8,
            'texture': {'width': 1, 'height': 2, 'url': 'native.jpg'}, 'frameRect': [0, 0, 1, 2], 'rotated': False,
            'resourcePath': 'Mmap/Mmap_71-1', 'origin': 'bottom-left texture coordinates',
            'canvasOrigin': 'top-left browser image coordinates'}}
        payload('canvas.rgba')
        self.game = {'contract': 'opening-background-texture/v1', 'backgroundId': 71, 'width': 1, 'height': 2,
                     'origin': 'top-left image rows; GPU texture y=0 is uploaded first row',
                     'assetSha256': hashlib.sha256(self.jpeg).hexdigest(), 'cpu': payload('cpu.rgba'), 'gpu': payload('gpu.rgba')}

    def check(self):
        source, game = self.root / 'source.json', self.root / 'game.json'
        source.write_text(json.dumps(self.source)); game.write_text(json.dumps(self.game))
        return verify(source, game)

    def test_all_four_observations_equal(self):
        self.assertTrue(self.check()['equal'])

    def test_upload_difference_is_separate_from_source_decode(self):
        data = bytes([9]) + self.rgba[1:]
        (self.root / 'gpu.rgba').write_bytes(data)
        self.game['gpu']['sha256'] = hashlib.sha256(data).hexdigest()
        result = self.check()
        self.assertFalse(result['equal'])
        self.assertTrue(result['comparisons']['sourceCanvasToGpu']['equal'])
        self.assertEqual(result['comparisons']['gameCpuToGpu']['changedPixels'], 1)

    def test_raw_integrity_is_required(self):
        (self.root / 'gpu.rgba').write_bytes(self.rgba[:-1])
        with self.assertRaises(ValueError): self.check()

    def test_identity_and_row_convention_are_required(self):
        for key, value in [('backgroundId', 72), ('origin', 'flipped'), ('assetSha256', 'wrong')]:
            old = self.game[key]
            self.game[key] = value
            with self.assertRaises(ValueError): self.check()
            self.game[key] = old


if __name__ == '__main__':
    unittest.main()
