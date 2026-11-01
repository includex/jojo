#!/usr/bin/env python3
"""Reject missing semantic bindings used by production battle rules before packaging."""
import json
from pathlib import Path
import re
import sys


def verify(source: Path, compiled: Path) -> int:
    required = {'SkillEffects': set(), 'MagicEffects': set()}
    for path in source.rglob('*.kt'):
        text = path.read_text(encoding='utf-8')
        for registry, key in re.findall(r'(SkillEffects|MagicEffects)\.id\("([A-Za-z0-9]+)"\)', text):
            required[registry].add(key)
        if path.stem in required:
            required[path.stem].update(re.findall(r'(?<![A-Za-z0-9_.])id\("([A-Za-z0-9]+)"\)', text))
            for group in re.findall(r'setOf\((.*?)\)\.mapTo\([^\n]*::id\)', text, re.S):
                required[path.stem].update(re.findall(r'"([A-Za-z0-9]+)"', group))
    count = 0
    for registry, keys in required.items():
        path = compiled / 'data' / (registry[0].lower()+registry[1:]+'.json')
        if not path.is_file():
            raise ValueError(f'missing compiled bindings: {path}')
        bindings = json.loads(path.read_text(encoding='utf-8'))
        missing = keys - bindings.keys()
        if missing:
            raise ValueError(f'{path}: missing effects used by core: {", ".join(sorted(missing))}')
        count += len(keys)
    return count


if __name__ == '__main__':
    if len(sys.argv) != 3:
        raise SystemExit('usage: verify_content_effect_bindings.py CORE_SOURCE COMPILED_CONTENT')
    print(f'Validated {verify(Path(sys.argv[1]), Path(sys.argv[2]))} semantic effect references')
