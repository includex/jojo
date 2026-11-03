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
    assert {p.stem for p in (CONTENT / 'scenarios').glob('*.lua')} == modules
    python_modules = {p.stem for p in (CONTENT / 'scenarios').glob('*.py')}
    assert python_modules <= modules, 'Unexpected legacy Python module'
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
        assert {int(p.stem) for p in (CONTENT / 'assets/maps/units' / family).glob('*.png')} == set(range(601, 623))
    selection = json.loads((CONTENT / 'examples/linchong/assets/release-sprites/selection.json').read_text())
    for character_id, role, avatar in [(513, 'lu', 602), (522, 'dong', 611), (523, 'xue', 612), (528, 'assassin', 621), (529, 'assassin', 621)]:
        row = read_yaml(CONTENT / 'characters' / f'{character_id:04}.yaml')
        assert row['mapAvatar'] == avatar and row['battleAvatar'] == avatar - 304
        for family in ('atk', 'mov', 'spc'):
            source = CONTENT / 'examples/linchong/assets' / selection['characters'][role]['files'][family]['path']
            assert source.read_bytes() == (CONTENT / 'assets/maps/units' / (family + '2') / f'{avatar}.png').read_bytes()
    first_battle = (CONTENT / 'scenarios/S_90.lua').read_text()
    assert '["avatarId"] = 622' in first_battle
    for index in range(91, 98):
        assert '["avatarId"] = 622' not in (CONTENT / 'scenarios' / f'S_{index}.lua').read_text()
    for family in ('atk', 'mov', 'spc'):
        selected = selection['characters']['lin_b01']['files'][family]
        assert (CONTENT / 'examples/linchong/assets' / selected['path']).read_bytes() == (CONTENT / 'assets/maps/units' / (family + '2') / '622.png').read_bytes()
    for variant in range(991, 995):
        assert (CONTENT / 'assets/source-map-textures' / f'{variant}.png').is_file()
    opening = json.loads((CONTENT / 'examples/linchong/opening-scenes.json').read_text())
    opening_units = {row[0] for scene in opening['scenes'] for shot in scene['shots'] for key in ('units','appear') for row in shot.get(key, [])}
    avatars = {read_yaml(CONTENT / 'characters' / f'{unit:04}.yaml')['mapAvatar'] for unit in opening_units}
    for avatar in avatars:
        for direction in (1, 2):
            assert (CONTENT / 'assets/maps/hall-units' / f'{avatar * 2 + direction}.png').is_file()
    assert not list((CONTENT / 'assets/maps').glob('*.jpg'))
    assert not (CONTENT / 'resources/localization').exists()
    assert not (CONTENT / 'resources/maps').exists()
    for module in modules:
        source = (CONTENT / 'scenarios' / f'{module}.lua').read_text()
        for reference in __import__('re').findall(r'@dialogue:([^\'\"]+)', source):
            assert reference in dialogue['entries'], reference
    print(f'Linchong package OK: {len(modules)} scenarios, 64 characters, {len(ITEM_IDS)} items, 10 techniques, 8 battles; no original campaign assets.')


if __name__ == '__main__':
    validate()
