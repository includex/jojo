import unittest
import tempfile
import json
import hashlib
from pathlib import Path
from PIL import Image
from verify_portrait_atlas_crop import compare_crop, verify_packing, verify


class AtlasCropTest(unittest.TestCase):
    def setUp(self):
        self.native = bytes(range(24))
        self.crop = bytearray(5 * 4 * 4)
        for y in range(4):
            for x in range(5):
                nx, ny = min(2, max(0, x - 1)), min(1, max(0, y - 1))
                self.crop[(y * 5 + x) * 4:(y * 5 + x + 1) * 4] = self.native[(ny * 3 + nx) * 4:(ny * 3 + nx + 1) * 4]

    def test_accepts_native_and_extruded_strips(self):
        self.assertTrue(compare_crop(self.crop, self.native, 3, 2)['equal'])

    def test_detects_interior_and_each_edge_corruption(self):
        for x, y in [(2, 1), (0, 1), (4, 1), (1, 0), (1, 3)]:
            crop = self.crop.copy(); crop[(y * 5 + x) * 4] ^= 1
            self.assertFalse(compare_crop(crop, self.native, 3, 2)['equal'])

    def test_rejects_truncated_buffer(self):
        with self.assertRaises(ValueError): compare_crop(self.crop[:-1], self.native, 3, 2)

    def test_packing_replays_shelf_wrap_and_texture_reuse(self):
        rows = []
        for i, (texture_id, width, height, x, y) in enumerate([
                ('a', 1900, 20, 2, 2), ('b', 200, 40, 2, 24), ('a', 1900, 20, 2, 2)]):
            rows.append(dict(order=i, sourceTexture=dict(id=texture_id, width=width, height=height),
                frameRect=[0, 0, width, height], result=dict(atlasId='atlas', atlasWidth=2048, atlasHeight=2048, x=x, y=y)))
        self.assertTrue(verify_packing(rows)['equal'])
        rows[1]['result']['x'] += 1
        with self.assertRaises(ValueError): verify_packing(rows)

    def test_crop_must_belong_to_recorded_atlas(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            Image.frombytes('RGBA', (3, 2), self.native).save(root / 'native.png')
            (root / 'crop.rgba').write_bytes(self.crop)
            atlas = dict(atlasId='atlas', atlasWidth=2048, atlasHeight=2048,
                frameRect=[2, 2, 3, 2], cropRect=[1, 1, 5, 4], cropIncludesBorder=1,
                origin='bottom-left texture coordinates', minFilter=9729, magFilter=9729,
                wrapS=33071, wrapT=33071, file='crop.rgba', byteLength=len(self.crop),
                sha256=hashlib.sha256(self.crop).hexdigest())
            manifest = dict(contract='natural-opening-pages-rgba8', captures=[dict(page=3, faceFrame='face', faceAtlas=atlas)],
                atlasPacking=[dict(order=0, frameName='face', frameRect=[0, 0, 3, 2],
                    sourceTexture=dict(id='native', width=3, height=2),
                    result=dict(atlasId='atlas', atlasWidth=2048, atlasHeight=2048, x=2, y=2))])
            path = root / 'manifest.json'; path.write_text(json.dumps(manifest))
            self.assertTrue(verify(path, root / 'native.png')['equal'])
            for field, bad in [('atlasId', 'other-atlas'), ('atlasWidth', 1024)]:
                original = atlas[field]; atlas[field] = bad
                path.write_text(json.dumps(manifest))
                with self.assertRaises(ValueError): verify(path, root / 'native.png')
                atlas[field] = original
