#!/usr/bin/env python3
"""Vendor the explicitly selected, native release atlases without altering pixels."""
import argparse
import csv
import hashlib
import json
import shutil
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'content/examples/linchong/assets'
SELECTION = ASSETS / 'release-sprites/selection.json'
FAMILIES = {'atk': ('공격이미지', 64, 12), 'mov': ('이동이미지', 48, 11), 'spc': ('특수이미지', 48, 5)}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source-root', type=Path, default=ROOT.parent / 'jojo_asset')
    args = parser.parse_args()
    catalog = args.source_root / 'release_character_descriptions.csv'
    with catalog.open(encoding='utf-8-sig', newline='') as stream:
        rows = {row['캐릭터ID']: row for row in csv.DictReader(stream)}
    selection = json.loads(SELECTION.read_text())
    pending = []
    for key, entry in selection['characters'].items():
        row = rows[entry['setId']]
        entry['sourceTitle'] = row['원본게시글제목']
        entry['description'] = row['간략설명']
        entry['files'] = {}
        for family, (column, size, count) in FAMILIES.items():
            source = (args.source_root / row[column]).resolve()
            assert source.is_relative_to((args.source_root / 'release').resolve()), source
            with Image.open(source) as im:
                assert im.mode == 'RGBA' and im.size == (size, (size + 2) * count), source
                alpha = im.getchannel('A')
                for index in range(count):
                    y = index * (size + 2)
                    assert alpha.crop((0,y+1,size,y+1+size)).getbbox(), (source,index)
                    assert not alpha.crop((0,y,size,y+1)).getbbox(), (source,index)
                    assert not alpha.crop((0,y+size+1,size,y+size+2)).getbbox(), (source,index)
            relative = Path('release-sprites') / family / source.name
            entry['files'][family] = {'path': str(relative), 'sha256': hashlib.sha256(source.read_bytes()).hexdigest()}
            pending.append((source, ASSETS / relative))
    for source, destination in pending:
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, destination)
    selection['sourceCatalog'] = catalog.name
    selection['sourceCatalogSha256'] = hashlib.sha256(catalog.read_bytes()).hexdigest()
    SELECTION.write_text(json.dumps(selection, ensure_ascii=False, indent=2) + '\n')
    print(f'Imported {len(pending)} native atlases for {len(selection["characters"])} roles; pixels and frame order preserved.')


if __name__ == '__main__':
    main()
