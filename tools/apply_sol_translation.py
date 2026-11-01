#!/usr/bin/env python3
"""Apply reviewed Sol batches to the port, preserving source line addresses.

The immutable baseline makes repeated applications safe as batches arrive.
No encrypted assets or original Chinese texts are overwritten.
"""
from __future__ import annotations

import argparse
import ast
from collections import defaultdict
import io
import hashlib
import json
import re
from pathlib import Path
import tokenize
import zipfile
from export_map_assets import decode_uuid

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / 'localization/baseline'
SIBLING = ROOT.parent / 'jojo_mobile/sgccz-desktop'
SCENARIOS = ROOT / 'content/scenarios'


def read(path):
    return json.loads(path.read_text(encoding='utf-8'))


def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


def restore_dialogue_references(text, module, legacy_keys):
    """Keep authored IDs when replaying translated baseline Python sources."""
    lines = text.splitlines(keepends=True)
    edits = []
    tree = ast.parse(text, filename=module)
    for function in (node for node in tree.body if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef))):
        for node in ast.walk(function):
            if not isinstance(node, ast.Call) or ast.unparse(node.func) != 'stage.say':
                continue
            old_key = f'{module}:{function.name}:{node.lineno}'
            key = legacy_keys.get(old_key)
            if key is None:
                raise ValueError(f'No stable dialogue ID for {old_key}')
            arg = node.args[0]
            if not isinstance(arg, ast.Constant) or not isinstance(arg.value, str) or arg.lineno != arg.end_lineno:
                raise ValueError(f'Expected single-line dialogue literal at {old_key}')
            edits.append((arg.lineno, arg.col_offset, arg.end_col_offset, repr('@dialogue:' + key).encode()))
    for line, start, end, replacement in sorted(edits, reverse=True):
        data = lines[line - 1].encode('utf-8')
        lines[line - 1] = (data[:start] + replacement + data[end:]).decode('utf-8')
    return ''.join(lines)


def load_batches():
    merged = {}
    for path in sorted((ROOT / 'localization/sol').rglob('batch*.json')):
        for original, translated in read(path).items():
            if original in merged and merged[original] != translated:
                raise ValueError(f'Conflicting translations: {original!r} in {path}')
            merged[original] = translated
    return merged


def decode_table(path):
    key = 'ccz65Sha08GeZ1Fu'
    data = bytes(((b >> (ord(key[i % len(key)]) % 8)) | (b << (8 - ord(key[i % len(key)]) % 8))) & 255 for i, b in enumerate(path.read_bytes())).decode('utf-8')
    assert data[:32].lower() == hashlib.md5((key + data[32:]).encode()).hexdigest(), path
    return json.loads(data[32:])


def table_overrides(translations):
    """Use original table positions to distinguish mistranslated homonyms."""
    config = read(SIBLING / 'assets/Game/config.54cec.json')
    tables = {}
    for index, entry in config['paths'].items():
        if not entry[0].startswith('data/Global/'):
            continue
        uuid = decode_uuid(config['uuids'][int(index)])
        originals = list((SIBLING / 'localization-originals/assets/Game/native' / uuid[:2]).glob(uuid + '*.bin'))
        current = list((SIBLING / 'assets/Game/native' / uuid[:2]).glob(uuid + '*.bin'))
        if not originals or not current:
            continue
        patches = {}

        def walk(original, old, path=''):
            if isinstance(original, str) and original.strip() in translations:
                patches[path] = {'before': old, 'after': translations[original.strip()]}
            elif isinstance(original, list):
                assert isinstance(old, list) and len(original) == len(old)
                for i, (a, b) in enumerate(zip(original, old)):
                    walk(a, b, path + '/' + str(i))
            elif isinstance(original, dict):
                assert isinstance(old, dict) and original.keys() == old.keys()
                for key, value in original.items():
                    walk(value, old[key], path + '/' + key.replace('~', '~0').replace('/', '~1'))
            elif not isinstance(original, str):
                assert original == old, f'Gameplay table changed at {entry[0]}{path}'

        walk(decode_table(originals[0]), decode_table(current[0]))
        tables[entry[0].split('/')[-1] + '.bin'] = patches
    return tables


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--apply', action='store_true', help='Write catalog and restored Python sources')
    args = parser.parse_args()
    cache = read(BASE / 'translations.json')
    baseline_catalog = read(BASE / 'dialogue-text.json')
    current_catalog = read(SCENARIOS / 'dialogue-text.json')
    legacy_keys = current_catalog['legacyKeys']
    catalog = {**current_catalog, 'entries': dict(current_catalog['entries']), 'aliases': dict(current_catalog['aliases'])}
    manifest = read(SCENARIOS / 'manifest.json')
    origins = {item['text']: set(item['sources']) for item in read(SIBLING / 'tools/ko-source-strings.json')}
    translations = load_batches()
    reviews_path = ROOT / 'localization/context-reviews.json'
    reviews = {(row['module'], row['old']): row['reviewed'] for row in read(reviews_path)} if reviews_path.exists() else {}
    reverse = defaultdict(list)
    for original, old in cache.items():
        reverse[old].append(original)
    unresolved = set()
    changed = set()

    def replace(old, module=''):
        # An old mistranslation can collapse a Chinese numeral or identifier to
        # ASCII. Never apply that reverse guess to program keys or numeric text.
        if not re.search(r'[\u3400-\u9fff\uac00-\ud7a3]', old) and not re.match(r'^&\d+\n', old):
            return old
        if (module, old) in reviews:
            new = reviews[(module, old)]
            if new != old:
                changed.add((module, old))
            return new
        candidates = reverse.get(old, [])
        if not candidates:
            return old
        if len(candidates) > 1 and module in manifest:
            source = manifest[module]['source']
            local = [key for key in candidates if source in origins.get(key, set())]
            if local:
                candidates = local
        values = {translations.get(key, cache[key]) for key in candidates}
        if len(values) != 1:
            unresolved.add((module, old, tuple(candidates)))
            return old
        new = next(iter(values))
        if new != old:
            changed.add((module, old))
        return new

    for old_key, old in baseline_catalog['entries'].items():
        catalog['entries'][legacy_keys[old_key]] = replace(old, old_key.split(':')[0])
    # Aliases identify individual pages of a multi-speaker Chinese original.
    pages = {
        'opening_scene1_page1': ('R_00:scene1:247', 1),
        'opening_scene1_page2': ('R_00:scene1:247', 3),
        'opening_scene1_page3': ('R_00:scene1:254', 1),
    }
    for alias, (key, line) in pages.items():
        catalog['aliases'][alias] = catalog['entries'][legacy_keys[key]].split('\n')[line]
    catalog['aliases']['yingchuan_round2_followup_speaker32'] = replace(
        catalog['aliases']['yingchuan_round2_followup_speaker32'])

    archive_path = BASE / 'scenarios.zip'
    if not archive_path.exists():
        with zipfile.ZipFile(archive_path, 'w', zipfile.ZIP_DEFLATED) as archive:
            for path in sorted((SIBLING / 'decompiled-python').glob('*.py')):
                archive.write(path, path.name)
    sources = {}
    with zipfile.ZipFile(archive_path) as archive:
        for name in archive.namelist():
            text = archive.read(name).decode('utf-8')
            lines = text.splitlines(keepends=True)
            offsets = [0]
            for line in lines:
                offsets.append(offsets[-1] + len(line))
            edits = []
            for token in tokenize.generate_tokens(io.StringIO(text).readline):
                if token.type != tokenize.STRING:
                    continue
                try:
                    old = ast.literal_eval(token.string)
                except (ValueError, SyntaxError):
                    continue
                if not isinstance(old, str):
                    continue
                new = replace(old, Path(name).stem)
                if new != old:
                    start = offsets[token.start[0] - 1] + token.start[1]
                    end = offsets[token.end[0] - 1] + token.end[1]
                    # Leave physical source lines unchanged: dialogue keys use them.
                    replacement = repr(new) + '\n' * (token.end[0] - token.start[0])
                    edits.append((start, end, replacement))
            for start, end, replacement in reversed(edits):
                text = text[:start] + replacement + text[end:]
            new_tree = ast.parse(text, filename=name)
            old_tree = ast.parse(archive.read(name).decode('utf-8'), filename=name)
            for tree in (old_tree, new_tree):
                for node in ast.walk(tree):
                    if isinstance(node, ast.Constant) and isinstance(node.value, str):
                        node.value = '<localized text>'
            if ast.dump(old_tree, include_attributes=True) != ast.dump(new_tree, include_attributes=True):
                # String widths change end_col_offset; statement start lines must not.
                old_shape = ast.dump(old_tree)
                new_shape = ast.dump(new_tree)
                assert old_shape == new_shape, f'Non-text program change in {name}'
                assert [n.lineno for n in ast.walk(old_tree) if hasattr(n, 'lineno')] == [n.lineno for n in ast.walk(new_tree) if hasattr(n, 'lineno')], name
            sources[name] = restore_dialogue_references(text, Path(name).stem, legacy_keys)

    text_overrides = dict(translations)
    for old in reverse:
        new = replace(old)
        if new != old:
            text_overrides[old] = new
    # Decode and validate all original table structures before mutating any
    # runtime files, so a validation failure leaves the current game intact.
    tables = table_overrides(translations)
    report = {
        'translated_originals': len(translations),
        'reference_originals': len(cache),
        'catalog_entries_changed': sum(catalog['entries'][legacy_keys[k]] != v for k, v in baseline_catalog['entries'].items()),
        'changed_text_contexts': len(changed),
        'ambiguous_contexts': [{'module': m, 'old': o, 'originals': list(c)} for m, o, c in sorted(unresolved) if m],
        'ambiguous_reverse_values': [{'old': o, 'originals': list(c)} for m, o, c in sorted(unresolved) if not m],
        'applied': args.apply,
        'table_text_paths': sum(map(len, tables.values())),
    }
    if args.apply:
        write(SCENARIOS / 'dialogue-text.json', catalog)
        write(ROOT / 'core/src/main/resources/localization/ko-reviewed.json', text_overrides)
        write(ROOT / 'core/src/main/resources/localization/table-reviewed.json', tables)
        for name, text in sources.items():
            (SCENARIOS / name).write_text(text, encoding='utf-8')
    write(ROOT / 'localization/progress.json', report)
    print(json.dumps({k: v for k, v in report.items() if k not in ('ambiguous_contexts', 'ambiguous_reverse_values')}, ensure_ascii=False))
    print(f'Unresolved scenario contexts: {len(report["ambiguous_contexts"])}; ambiguous global reverse values: {len(report["ambiguous_reverse_values"])} (original table positions used)')


if __name__ == '__main__':
    main()
