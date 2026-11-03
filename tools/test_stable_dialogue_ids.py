"""Regression checks for authored dialogue identity."""

import ast
import unittest

from tools.export_python_ast import collect_dialogue_texts


class StableDialogueIdsTest(unittest.TestCase):
    CATALOG = {
        'R_00.scene1.dialogue0001': '&0\nFirst',
        'R_00.scene1.dialogue0002': '&0\nSecond',
    }

    def collect(self, source, catalog=None):
        return collect_dialogue_texts(ast.parse(source), 'R_00', catalog or self.CATALOG)

    def test_comments_and_reorder_do_not_change_ids(self):
        first = "def scene1():\n stage.say('@dialogue:R_00.scene1.dialogue0001')\n stage.say('@dialogue:R_00.scene1.dialogue0002')\n"
        moved = "def scene1():\n # inserted comment\n stage.say('@dialogue:R_00.scene1.dialogue0002')\n\n stage.say('@dialogue:R_00.scene1.dialogue0001')\n"
        self.assertEqual(self.collect(first), self.collect(moved))

    def test_function_rename_and_move_keep_existing_id(self):
        source = "def later_scene():\n stage.say('@dialogue:R_00.scene1.dialogue0001')\n"
        tree = ast.parse(source)
        self.assertEqual(collect_dialogue_texts(tree, 'S_01', self.CATALOG), {'R_00.scene1.dialogue0001'})

    def test_semantic_id_is_allowed(self):
        key = 'R_01.scene1.recruit_greeting'
        self.assertEqual(self.collect(f"def scene1():\n stage.say('@dialogue:{key}')\n", {key: '&512\nGreeting'}), {key})

    def test_missing_catalog_entry(self):
        with self.assertRaisesRegex(ValueError, 'missing from catalog'):
            self.collect("def scene1():\n stage.say('@dialogue:R_00.scene1.dialogue9999')\n")

    def test_duplicate_id(self):
        with self.assertRaisesRegex(ValueError, 'duplicate scenario dialogue ID'):
            self.collect("def scene1():\n stage.say('@dialogue:R_00.scene1.dialogue0001')\n stage.say('@dialogue:R_00.scene1.dialogue0001')\n")

    def test_raw_dialogue_is_preserved(self):
        from tools.export_python_ast import encode
        source = "def scene1():\n stage.say('&512\\n있는 그대로 @dialogue:본문')\n"
        tree = ast.parse(source)
        self.assertEqual(collect_dialogue_texts(tree, 'S_90', self.CATALOG), set())
        encoded = encode(tree.body[0].body[0].value)
        self.assertEqual(encoded['fields']['args'][0]['fields']['value'], '&512\n있는 그대로 @dialogue:본문')

    def test_computed_text_is_resolved_at_runtime(self):
        self.assertEqual(self.collect("def scene1():\n stage.say(message)\n"), set())


if __name__ == '__main__':
    unittest.main()
