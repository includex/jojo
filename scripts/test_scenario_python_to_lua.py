import unittest
from pathlib import Path
from scripts.scenario_python_to_lua import convert


class ScenarioLuaConversionTest(unittest.TestCase):
    def test_numeric_flag_boolean_and_zero_index_are_preserved(self):
        lua = convert("def scene0():\n if not gvars['done']:\n  vars[0] = 1\n if gvars['ready'] and stage.test():\n  return True\n return False\n")
        self.assertIn('gvars["done"] ~= 0', lua)
        self.assertIn('gvars["ready"] ~= 0', lua)
        self.assertIn('vars[0] = 1', lua)
        self.assertIn('__truth(stage.test())', lua)
        self.assertIn('return true', lua)
        self.assertIn('return false', lua)

    def test_nested_unit_calls_and_table_keys_preserve_api_contract(self):
        lua = convert("def scene0():\n stage.createMine([{'id':513,'dir':1}])\n stage.unit(513).setAction(6, 1, False)\n")
        self.assertIn('stage.createMine({{["id"] = 513, ["dir"] = 1}})', lua)
        self.assertIn('stage.unit(513).setAction(6, 1, false)', lua)

    def test_all_committed_lua_scenarios_match_authoring_output(self):
        root=Path(__file__).resolve().parents[1]
        modules=list((root/'content/scenarios').glob('*.py'))
        self.assertEqual(16,len(modules))
        for module in modules:
            self.assertEqual(convert(module.read_text()),module.with_suffix('.lua').read_text(),module.name)

    def test_reserved_lua_method_name_uses_index_notation(self):
        self.assertIn('stage["end"]()', convert('def scene0():\n stage.end()\n'))

    def test_unsupported_syntax_fails_instead_of_changing_behavior(self):
        with self.assertRaises(ValueError):
            convert('def scene0():\n while True:\n  pass\n')


if __name__ == '__main__':
    unittest.main()
