"""Fail-closed checks for the build-time Canvas label exporter, without launching Electron."""
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
import unittest


TOOLS = Path(__file__).resolve().parent
ROOT = TOOLS.parent
SOURCE = Path(os.environ.get("JOJO_SOURCE_ROOT", ROOT.parent / "jojo_mobile/sgccz-desktop"))


@unittest.skipUnless(sys.platform == "darwin" and SOURCE.is_dir(), "requires the source macOS font environment")
class SpeakerLabelContractTest(unittest.TestCase):
    def test_changed_source_or_font_cannot_replace_existing_assets(self):
        for group in ("sourceSha256", "fontSha256"):
            with self.subTest(group=group), tempfile.TemporaryDirectory() as temp:
                root = Path(temp)
                shutil.copy(TOOLS / "export_street_speaker_labels.cjs", root)
                contract = json.loads((TOOLS / "street_speaker_label_contract.json").read_text())
                contract[group][next(iter(contract[group]))] = "0" * 64
                (root / "street_speaker_label_contract.json").write_text(json.dumps(contract))
                output = root / "output"
                output.mkdir()
                (output / "sentinel").write_text("previous assets")
                result = subprocess.run(
                    ["node", str(root / "export_street_speaker_labels.cjs"), str(SOURCE),
                     str(root / "unused-catalog.bin"), str(output)],
                    capture_output=True, text=True, timeout=5,
                )
                self.assertNotEqual(0, result.returncode)
                self.assertIn("Source label contract changed" if group == "sourceSha256" else "Unvalidated font version", result.stderr)
                self.assertEqual("previous assets", (output / "sentinel").read_text())


if __name__ == "__main__":
    unittest.main()
