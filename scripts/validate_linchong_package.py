#!/usr/bin/env python3
"""Reject accidental reintroduction of another game's production content."""
import json
from pathlib import Path

from build_linchong_mod_content import ITEM_IDS, SKILL_IDS, read_yaml

ROOT = Path(__file__).resolve().parents[1]
CONTENT = ROOT / 'content'


def validate():
    expected = {
        'characters': set(range(512, 576)),
        'equipment': set(ITEM_IDS.values()),
        'skills/active': set(SKILL_IDS.values()),
        'shop': set(range(58, 66)),
        'skills/assignments': set(),
        'skills/equipment': set(),
    }
    for category, ids in expected.items():
        actual = {read_yaml(p)['id'] for p in (CONTENT / category).glob('*.yaml')}
        assert actual == ids, f'{category}: unexpected/missing IDs {actual ^ ids}'
    modules = {f'{prefix}_{i}' for prefix in ('R', 'S') for i in range(90, 98)}
    assert {p.stem for p in (CONTENT / 'scenarios').glob('*.py')} == modules
    manifest = json.loads((CONTENT / 'scenarios/manifest.json').read_text())
    assert set(manifest) == modules
    dialogue = json.loads((CONTENT / 'scenarios/dialogue-text.json').read_text())
    assert all(key.split('.')[0] in modules for key in dialogue['entries'])
    assert not dialogue['aliases'] and not dialogue.get('legacyKeys')
    routes = read_yaml(CONTENT / 'game.yaml')
    assert routes['startModule'] == 'R_90' and routes['protagonistId'] == 512
    assert {k.strip() for k in routes if k.startswith('  R_')} == {f'R_{i}' for i in range(90, 98)}
    for category, ids in {
        'heads': set(range(608, 633)), 'battle-maps': set(range(59, 67)),
        'hexmaps': set(range(58, 66)),
    }.items():
        actual = {int(p.stem) for p in (CONTENT / 'assets/maps' / category).iterdir() if p.is_file()}
        assert actual == ids, f'{category}: unexpected/missing assets {actual ^ ids}'
    for family in ('atk2', 'mov2', 'spc2'):
        assert {int(p.stem) for p in (CONTENT / 'assets/maps/units' / family).glob('*.png')} == set(range(601, 621))
    assert not list((CONTENT / 'assets/maps').glob('*.jpg'))
    assert not (CONTENT / 'resources/localization').exists()
    assert not (CONTENT / 'resources/maps').exists()
    for module in modules:
        source = (CONTENT / 'scenarios' / f'{module}.py').read_text()
        for reference in __import__('re').findall(r'@dialogue:([^\'\"]+)', source):
            assert reference in dialogue['entries'], reference
    print(f'Linchong package OK: {len(modules)} scenarios, 64 characters, 28 items, 10 techniques, 8 battles; no original campaign assets.')


if __name__ == '__main__':
    validate()
