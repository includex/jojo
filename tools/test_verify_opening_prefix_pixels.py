import copy
import unittest
import json
import tempfile
from pathlib import Path
from verify_opening_prefix_pixels import index_captures, verify


class PrefixCoverageTest(unittest.TestCase):
    def setUp(self):
        full = '대장님, 서둘러야 해요!'
        self.manifest = dict(fullText=full, dialogueInputs=0, captures=[
            dict(text=full[:n], complete=n == len(full)) for n in (5, 9, len(full))])

    def test_accepts_all_naturally_observed_samples(self):
        self.assertEqual(len(index_captures(self.manifest)), 3)

    def test_missing_and_duplicate_cannot_pass(self):
        for rows in [self.manifest['captures'][:-1], self.manifest['captures'] * 2]:
            with self.assertRaises(ValueError):
                index_captures(dict(self.manifest, captures=rows))

    def test_forced_completion_and_dialogue_input_cannot_pass(self):
        bad = copy.deepcopy(self.manifest)
        bad['captures'][0]['complete'] = True
        for manifest in [bad, dict(self.manifest, dialogueInputs=1)]:
            with self.assertRaises(ValueError):
                index_captures(manifest)

    def test_provenance_guards_reject_false_isolation_and_forced_source(self):
        base = dict(self.manifest, contract='natural-first-dialogue-prefixes-rgba8',
                    width=2560, height=1376, origin='bottom-left')
        source, game = copy.deepcopy(base), copy.deepcopy(base)
        for i, row in enumerate(source['captures']):
            row.update(frame=i + 1, elapsedSeconds=i + 1, trigger='EVENT_AFTER_DRAW',
                       content=row['text'], richText=row['text'], remaining=source['fullText'][len(row['text']):],
                       handle=None if row['complete'] else 'active', segments=[{'string': row['text']}])
        for i, row in enumerate(game['captures']):
            row.update(frame=i + 1, elapsedSeconds=i + 1, naturalStreetTextIsolation=True)
        cases = []
        bad_game = copy.deepcopy(game)
        bad_game['captures'][0]['naturalStreetTextIsolation'] = False
        cases.append((source, bad_game, 'natural isolated'))
        bad_source = copy.deepcopy(source)
        bad_source['captures'][-1]['remaining'] = 'forced reveal remainder'
        cases.append((bad_source, game, 'scheduler'))
        bad_source = copy.deepcopy(source)
        bad_source['captures'][1]['frame'] = 1
        cases.append((bad_source, game, 'frames must increase'))
        with tempfile.TemporaryDirectory() as directory:
            a, b = Path(directory) / 'source.json', Path(directory) / 'game.json'
            for source_value, game_value, message in cases:
                a.write_text(json.dumps(source_value)); b.write_text(json.dumps(game_value))
                with self.assertRaisesRegex(ValueError, message):
                    verify(a, b)

    def test_all_prefixes_require_complete_ordered_coverage(self):
        full = self.manifest['fullText']
        all_rows = [dict(text=full[:n], complete=n == len(full)) for n in range(1, len(full) + 1)]
        manifest = dict(self.manifest, sampleLengths=list(range(1, len(full) + 1)), captures=all_rows)
        self.assertEqual(len(index_captures(manifest)), len(full))
        for rows in (all_rows[1:], all_rows[:4] + all_rows[5:], all_rows + [all_rows[0]], list(reversed(all_rows))):
            with self.assertRaises(ValueError):
                index_captures(dict(manifest, captures=rows))

    def test_all_gate_rejects_selected_only_and_mismatched_coverage(self):
        base = dict(self.manifest, contract='natural-first-dialogue-prefixes-rgba8',
                    width=2560, height=1376, origin='bottom-left')
        with tempfile.TemporaryDirectory() as directory:
            a, b = Path(directory) / 'source.json', Path(directory) / 'game.json'
            a.write_text(json.dumps(base)); b.write_text(json.dumps(base))
            with self.assertRaisesRegex(ValueError, 'all prefixes required'):
                verify(a, b, require_all=True)
            b.write_text(json.dumps(dict(base, sampleLengths=list(range(1, len(base['fullText']) + 1)))))
            with self.assertRaisesRegex(ValueError, 'sampleLengths'):
                verify(a, b)
