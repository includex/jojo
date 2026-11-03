#!/usr/bin/env python3
"""Pack selected jojo_asset native poses into the existing 48x64 hall ABI.

Pixels are cropped at native scale and bottom-aligned; no poses are generated.
Hall's two direction textures use south (2/east flipped1) and north (0/west flipped3).
"""
from pathlib import Path
import hashlib
import json
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]


def package_hall(source: Path, destination: Path) -> dict:
    config_path = source / 'hall-sprites/selection.json'
    config = json.loads(config_path.read_text())
    files = []
    for entry in config['characters'].values():
        opened = {}
        for family, record in entry['files'].items():
            path = source / record['path']
            assert hashlib.sha256(path.read_bytes()).hexdigest() == record['sha256'], path
            opened[family] = Image.open(path).convert('RGBA')
        for back in (False, True):
            idle = 2 if back else 0
            walk = 3 if back else 1
            gesture = 4 if back else 3
            # All actions retain authored poses. Row8(action6) is the crouched/subdued pose.
            poses = [('mov',idle),('mov',idle),('mov',walk)]
            poses += [('spc',1 if back else 0)] * 17
            subdued = entry.get('subduedPose', {'family':'spc','index':gesture})
            poses[8] = (subdued['family'], subdued['index'])
            image = Image.new('RGBA',(48,64*20))
            for row,(family,index) in enumerate(poses):
                frame = opened[family].crop((0,index*50+1,48,index*50+49))
                image.paste(frame,(0,row*64+16))
            asset_id = 1 + entry['avatarId']*2 + int(back)
            relative = Path('maps/hall-units') / f'{asset_id}.png'
            out = destination / relative
            out.parent.mkdir(parents=True,exist_ok=True)
            image.save(out,optimize=True)
            assert Image.open(out).size == (48,1280)
            files.append(str(relative))
        for image in opened.values():image.close()
    return {'generatedFiles': files, 'selection': 'hall-sprites/selection.json',
            'selectionSha256':hashlib.sha256(config_path.read_bytes()).hexdigest()}


if __name__ == '__main__':
    result = package_hall(ROOT/'content/examples/linchong/assets',ROOT/'content/assets')
    print(f"Packed {len(result['generatedFiles'])} native hall atlases")
