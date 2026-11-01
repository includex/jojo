import gzip
from pathlib import Path
import tempfile
import unittest
from tools.package_content_assets import package

class ContentAssetsTest(unittest.TestCase):
    def test_copy_unpack_and_remove_stale_output(self):
        with tempfile.TemporaryDirectory() as tmp:
            src, dst = Path(tmp)/'src', Path(tmp)/'dst'
            src.mkdir(); dst.mkdir()
            (src/'sample.rgba.gz').write_bytes(gzip.compress(b'framebuffer'))
            (src/'image.png').write_bytes(b'original')
            (dst/'stale.png').write_bytes(b'stale')
            self.assertEqual(2, package(src,dst))
            self.assertEqual(b'framebuffer',(dst/'sample.rgba').read_bytes())
            self.assertEqual(b'original',(dst/'image.png').read_bytes())
            self.assertFalse((dst/'stale.png').exists())

    def test_duplicate_output_is_rejected_before_mutation(self):
        with tempfile.TemporaryDirectory() as tmp:
            src, dst = Path(tmp)/'src', Path(tmp)/'dst'
            src.mkdir(); dst.mkdir()
            (src/'x.rgba').write_bytes(b'raw')
            (src/'x.rgba.gz').write_bytes(gzip.compress(b'other'))
            (dst/'keep').write_bytes(b'keep')
            with self.assertRaisesRegex(ValueError,'duplicate resource'):package(src,dst)
            self.assertTrue((dst/'keep').exists())

    def test_nested_output_is_rejected(self):
        with tempfile.TemporaryDirectory() as tmp:
            with self.assertRaisesRegex(ValueError,'separate directories'):package(Path(tmp),Path(tmp)/'out')
