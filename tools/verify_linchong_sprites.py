#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Build a self-contained visual review of packed Lin Chong sprite animations."""
from __future__ import annotations

import argparse
import base64
import json
import statistics
import sys
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))
from scripts.package_linchong_assets import sheet_frames
KINDS = ('atk2', 'mov2', 'spc2')
SIZES = (64, 48, 48)
COUNTS = (12, 11, 5)


def data_url(path: Path) -> str:
    return 'data:image/png;base64,' + base64.b64encode(path.read_bytes()).decode('ascii')


def frame_metrics(path: Path, size: int, count: int) -> tuple[float, float]:
    with Image.open(path) as opened:
        image = opened.convert('RGBA')
        spans = []
        for index in range(count):
            y = index * (size + 2) + 1
            box = image.crop((0, y, size, y + size)).getchannel('A').getbbox()
            if box:
                spans.append((box[2] - box[0], box[3] - box[1]))
        if not spans:
            raise ValueError(f'{path}: no visible sprite frames')
    return tuple(round(statistics.median(v[i] for v in spans) / size, 3) for i in (0, 1))


def edge_contacts(path: Path) -> list[int]:
    result = []
    for index, frame in enumerate(sheet_frames(path)):
        box = frame.getchannel('A').getbbox()
        if box[0] == 0 or box[1] == 0 or box[2] == frame.width or box[3] == frame.height:
            result.append(index)
    return result


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--assets', type=Path, default=ROOT / 'content/assets')
    parser.add_argument('--output', type=Path, default=ROOT / 'docs/linchong-sprite-review.html')
    parser.add_argument('--contact-output', type=Path, default=ROOT / 'verification/build/linchong-native-contact.png')
    args = parser.parse_args()
    manifest = json.loads((ROOT / 'content/examples/linchong/assets/jojo-asset-mapping.json').read_text())
    if manifest.get('format') != 'jojo-existing-assets-v2':
        raise ValueError('Package v2 sheets before building the sprite review')
    timeline = json.loads((args.assets / 'maps/battle-anime.json').read_text())
    images = {}
    metrics = {}
    edges = {}
    for name, entry in manifest['characters'].items():
        if 'avatarId' not in entry:
            continue
        avatar = str(entry['avatarId'])
        images[avatar] = [data_url(args.assets / 'maps/units' / kind / f'{avatar}.png') for kind in KINDS]
        metrics[name] = [frame_metrics(args.assets / 'maps/units' / kind / f'{avatar}.png', size, count)
                         for kind, size, count in zip(KINDS, SIZES, COUNTS)]
        edges[name] = edge_contacts(ROOT / 'content/examples/linchong/assets' / entry['sourceSpriteSheet'])
    payload = json.dumps({'timeline': timeline, 'images': images, 'metrics': metrics, 'edges': edges,
                          'characters': {k: str(v['avatarId'])
                          for k, v in manifest['characters'].items() if 'avatarId' in v}}, ensure_ascii=False)
    page = r'''<!doctype html><html lang="ko"><meta charset="utf-8"><title>임충 전투 스프라이트 검토</title>
<style>
body{margin:0;background:#161719;color:#ece6d7;font:15px system-ui,sans-serif}header{padding:20px 28px;background:#242525;border-bottom:1px solid #555}
main{padding:20px 28px;max-width:1450px;margin:auto}h1{margin:0 0 6px;font-size:24px}p{margin:8px 0 18px;color:#c5bda8}
select{background:#353633;color:#fff;border:1px solid #777;padding:7px;margin:0 12px 16px 0}.pair{display:flex;gap:24px;flex-wrap:wrap}
.panel{background:#282925;border:1px solid #5a574c;padding:16px}.panel h2{margin:0 0 10px;font-size:17px}canvas{image-rendering:pixelated;background:#34332f}
#contact{display:grid;grid-template-columns:repeat(7,minmax(100px,1fr));gap:10px}.cell{background:#282925;padding:5px;text-align:center;border:1px solid #514e44}
.cell canvas{width:96px;height:96px;max-width:100%}.cell small{display:block;color:#c8bd9e}code{color:#dec888}#metrics{font-variant-numeric:tabular-nums}
</style><header><h1>임충 전투 스프라이트 검토</h1><span>제작 시트 → 원본 규격 아틀라스. 이동·공격 프레임 슬롯을 원본 battle-anime.json의 24틱 시간으로 재생합니다.</span></header>
<main><section class="panel"><h2>검증 결과 · 2026-11-01</h2><p>16종 × 28동작(448프레임)을 기존 48개 아틀라스에 적용했습니다. 공격 192프레임의 중복 여부, 투명 여백, 방향별 슬롯, 원본 타임라인 유지 여부를 검사했습니다. 공격·이동·특수 셀을 실제 게임과 같은 배율로 검토하며, 대기와 공격 사이의 전환도 함께 표시합니다.</p><p><strong>대기 → 공격 → 대기 전환에서 몸 크기와 발 위치를 비교하세요.</strong> 사용자가 제공한 조조전 화면을 참고하여 작은 머리·성인 체형·절제된 색의 3차 시트를 제작했습니다. 이전 변환기의 아틀라스별 자동 확대와 발 기준점 오류도 수정했습니다. 게임 렌더러와 같은 픽셀 배율로 표시하며 64px 공격 셀을 48px 이동 셀과 같은 크기로 늘리지 않습니다. 생성 작화의 자세·의복 세부 차이는 남을 수 있으며, 파일 규격 통과만으로 자연스러운 애니메이션을 보장하지 않습니다. 참고 화면은 작화 기준이며, 게임에 포함되는 원본 캐릭터 데이터는 없습니다.</p><p>이미지는 built-in image_gen으로 생성했습니다. <a href="../content/examples/linchong/assets/sprites-v3/">생성 시트·프롬프트·수정 이력</a> · <a href="linchong-campaign.html">임충전 가이드</a></p></section><br><label>인물 <select id="character"></select></label><label>동작 <select id="action"></select></label>
<p><a href="assets/jojo-style-reference.png">사용자가 제시한 작화 기준 보기</a></p><div class="pair"><section class="panel"><h2>신규 스프라이트</h2><canvas id="new" width="256" height="256"></canvas></section><section class="panel"><h2>작화 참고 화면 · 게임에 포함되지 않음</h2><img src="assets/jojo-style-reference.png" alt="사용자가 제공한 작화 참고 화면" style="width:256px;max-width:100%"></section></div>
<p id="status"></p><p>이 페이지는 프레임 슬롯과 크기 검토용입니다. 실제 게임 렌더링은 검증용 런타임 캡처를 확인하세요.</p><p id="metrics"></p><h2>원본 규격 28프레임</h2><div id="contact"></div></main>
<script>const DATA=__PAYLOAD__;const sizes=[64,48,48],counts=[12,11,5];let loaded={},start=performance.now();
for(const [avatar,urls] of Object.entries(DATA.images)){loaded[avatar]=urls.map(src=>{const image=new Image();image.src=src;return image})}
const ch=document.querySelector('#character'),act=document.querySelector('#action');
for(const [name,id] of Object.entries(DATA.characters)){const o=new Option(name+' ('+id+')',name);ch.add(o)}
for(const key of ['transition_2','transition_0','transition_3','anime0_0','anime0_2','anime0_3','anime6_0','anime6_2','anime6_3']){act.add(new Option(key,key))}act.value='transition_2';
function draw(canvas,avatar,type,index){const ctx=canvas.getContext('2d');ctx.clearRect(0,0,256,256);ctx.imageSmoothingEnabled=false;
const im=loaded[avatar]?.[type];if(!im?.complete||!im.naturalWidth)return;const size=sizes[type],y=index*(size+2)+1;
const scale=4,origin=(256-size*scale)/2;ctx.strokeStyle='#696354';ctx.beginPath();ctx.moveTo(0,224);ctx.lineTo(256,224);ctx.stroke();ctx.drawImage(im,0,y,size,size,origin,origin,size*scale,size*scale)}
function phase(){const direction=act.value.split('_')[1];const idleIndex=direction==='0'?7:direction==='3'?8:6;const frames=act.value.startsWith('transition_')?[{frame:18,sprite:{t:1,idx:idleIndex}},...DATA.timeline['anime6_'+direction],{frame:18,sprite:{t:1,idx:idleIndex}}]:DATA.timeline[act.value]||[],total=frames.reduce((n,f)=>n+f.frame,0);if(!total)return {type:1,index:6,tick:0,total:0};
let tick=Math.floor((performance.now()-start)*24/1000)%total;for(const f of frames){if(tick<f.frame){const s=f.sprite||{};return {type:s.t,index:s.idx,tick,total}}tick-=f.frame}
return {type:1,index:6,tick,total}}
function update(){const p=phase();if(p.type!=null&&p.index!=null){draw(document.querySelector('#new'),DATA.characters[ch.value],p.type,p.index)}
document.querySelector('#status').textContent=act.value+' · 아틀라스 '+p.type+' · 슬롯 '+p.index+' · '+p.total+'틱 ('+(p.total/24).toFixed(3)+'초)';requestAnimationFrame(update)}
function contact(){const box=document.querySelector('#contact');box.replaceChildren();const id=DATA.characters[ch.value];
for(let n=0;n<28;n++){const type=n<12?0:n<23?1:2,idx=n-(type===0?0:type===1?12:23);const div=document.createElement('div');div.className='cell';const canvas=document.createElement('canvas');canvas.width=canvas.height=96;
const label=document.createElement('small');label.textContent=n+' · '+['공격','이동','특수'][type]+' '+idx;div.append(canvas,label);box.append(div);const im=loaded[id][type];const paint=()=>{const ctx=canvas.getContext('2d');ctx.imageSmoothingEnabled=false;const s=sizes[type];const scale=1.5,origin=(96-s*scale)/2;ctx.drawImage(im,0,idx*(s+2)+1,s,s,origin,origin,s*scale,s*scale)};if(im.complete&&im.naturalWidth)paint();else im.addEventListener('load',paint,{once:true})}
const m=DATA.metrics[ch.value];
document.querySelector('#metrics').textContent='중앙값 가시 폭×높이(프레임 대비): 공격 '+m[0].join(' × ')+', 이동 '+m[1].join(' × ')+', 특수 '+m[2].join(' × ')+'. 원본 셀 경계 접촉: '+(DATA.edges[ch.value].join(', ')||'없음')}
ch.onchange=contact;act.onchange=()=>start=performance.now();contact();requestAnimationFrame(update);</script></html>'''
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(page.replace('__PAYLOAD__', payload), encoding='utf-8')
    print(f'Wrote {args.output} ({args.output.stat().st_size:,} bytes)')
    names = [name for name, entry in manifest['characters'].items() if 'avatarId' in entry]
    contact = Image.new('RGB', (len(names) * 76, 28 * 76 + 24), '#232323')
    draw = ImageDraw.Draw(contact)
    for column, name in enumerate(names):
        draw.text((column * 76 + 4, 5), name, fill='white')
        avatar = manifest['characters'][name]['avatarId']
        for n in range(28):
            atlas_type = 0 if n < 12 else 1 if n < 23 else 2
            slot = n - (0 if atlas_type == 0 else 12 if atlas_type == 1 else 23)
            size = SIZES[atlas_type]
            with Image.open(args.assets / 'maps/units' / KINDS[atlas_type] / f'{avatar}.png') as opened:
                atlas = opened.convert('RGBA')
                frame = atlas.crop((0, slot * (size + 2) + 1, size, slot * (size + 2) + 1 + size))
            contact.paste(frame, (column * 76 + (76 - size) // 2, 24 + n * 76 + (76 - size) // 2), frame)
            draw.text((column * 76 + 1, 24 + n * 76 + 1), str(n), fill='#aaaaaa')
    args.contact_output.parent.mkdir(parents=True, exist_ok=True)
    contact.save(args.contact_output)
    print(f'Wrote {args.contact_output} ({contact.width}x{contact.height})')


if __name__ == '__main__':
    main()
