import json
from pathlib import Path
import tempfile
import unittest
from tools.verify_content_effect_bindings import verify

class EffectBindingTest(unittest.TestCase):
    def test_direct_and_group_bindings_are_required(self):
        with tempfile.TemporaryDirectory() as tmp:
            root=Path(tmp); source=root/'src'; output=root/'out'
            source.mkdir(); (output/'data').mkdir(parents=True)
            (source/'Rule.kt').write_text('SkillEffects.id("lifeSteal")')
            (source/'MagicEffects.kt').write_text('setOf("typeFire", "typeWater").mapTo(linkedSetOf(), ::id)')
            (output/'data/skillEffects.json').write_text('{"lifeSteal": 1}')
            magic=output/'data/magicEffects.json'
            magic.write_text('{"typeFire": 0}')
            with self.assertRaisesRegex(ValueError, 'typeWater'):verify(source,output)
            magic.write_text(json.dumps({'typeFire':0,'typeWater':1}))
            self.assertEqual(3,verify(source,output))
