#!/usr/bin/env python3

import importlib.util
import json
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest


TOOLS = Path(__file__).parent


def load(name):
    path = TOOLS / f"{name}.py"
    spec = importlib.util.spec_from_file_location(name, path)
    module = importlib.util.module_from_spec(spec)
    assert spec.loader is not None
    spec.loader.exec_module(module)
    return module


DIRECTION = load("verify_hall_walk_direction")
FRAME = load("verify_hall_walk_frame_trace")


def row(x=0, y=0):
    return {
        "timestamp": 0,
        "nodePath": "walk-1",
        "assetFrameId": "actor;flipX=true",
        "flipX": True,
        "text": f"grid={x},{y};dir=1;action=20",
    }


class HallWalkVerifierTest(unittest.TestCase):
    def test_direction_audit_rejects_empty_stream(self):
        self.assertEqual([], DIRECTION.audit([], "source"))
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / "source.jsonl"
            game = Path(directory) / "game.jsonl"
            source.write_text("")
            game.write_text("")
            result = subprocess.run(
                [sys.executable, TOOLS / "verify_hall_walk_direction.py", source, game],
                capture_output=True, text=True,
            )
        self.assertEqual(1, result.returncode)
        self.assertIn("source draw stream is empty", result.stdout)

    def test_direction_comparison_rejects_equal_length_different_draws(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / "source.jsonl"
            game = Path(directory) / "game.jsonl"
            source.write_text(json.dumps(row(1)) + "\n")
            game.write_text(json.dumps(row(2)) + "\n")
            result = subprocess.run(
                [sys.executable, TOOLS / "verify_hall_walk_direction.py", source, game],
                capture_output=True, text=True,
            )
        self.assertEqual(1, result.returncode)
        self.assertIn("draw[0] differs", result.stdout)

    def test_frame_sampler_exposes_no_samples_for_empty_evidence(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / "source.jsonl"
            game = Path(directory) / "game.jsonl"
            source.write_text("")
            game.write_text("")
            result = subprocess.run(
                [sys.executable, TOOLS / "verify_hall_walk_frame_trace.py", source, game],
                capture_output=True, text=True,
            )
        self.assertEqual(1, result.returncode)
        self.assertIn("source frame sample stream is empty", result.stdout)

    def test_frame_report_identifies_reconstructed_checkpoint_evidence(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / "source.jsonl"
            game = Path(directory) / "game.jsonl"
            report = Path(directory) / "report.json"
            encoded = json.dumps(row()) + "\n"
            source.write_text(encoded)
            game.write_text(encoded)
            result = subprocess.run([
                sys.executable, TOOLS / "verify_hall_walk_frame_trace.py",
                source, game, "--output", report,
            ], capture_output=True, text=True)
            evidence = json.loads(report.read_text())
        self.assertEqual(0, result.returncode)
        self.assertEqual("reconstructed-checkpoints", evidence["evidenceKind"])

    def test_source_generator_refuses_a_synthetic_game_output(self):
        with tempfile.TemporaryDirectory() as directory:
            snapshot = Path(directory) / "snapshot.json"
            anime = {
                name: [
                    {"frame": 5, "sprite": {"t": 1, "idx": row}}
                    for row in rows
                ]
                for name, rows in {
                    "anime20_0": [2, 3],
                    "anime20_2": [0, 1],
                    "anime20_3": [4, 5],
                }.items()
            }
            snapshot.write_text(json.dumps(anime))
            result = subprocess.run([
                sys.executable, TOOLS / "hall_walk_render_events.py",
                "--source-snapshot", snapshot,
                "--original", Path(directory) / "source.jsonl",
                "--game", Path(directory) / "game.jsonl",
            ], capture_output=True, text=True)
        self.assertEqual(2, result.returncode)
        self.assertIn("unrecognized arguments: --game", result.stderr)


if __name__ == "__main__":
    unittest.main()
