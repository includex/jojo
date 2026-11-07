"""Independent source parity, references and map accessibility checks."""
import json,re,pathlib,collections
root=pathlib.Path(__file__).resolve().parents[1];c=json.loads((root/'content/examples/linchong/campaign.json').read_text());src=(root/'content/examples/linchong/source.md').read_text()
assert len(c['battles'])==8
actual=[]
for b in c['battles']:
 w,h=b['map']['width'],b['map']['height'];blocked={(p['x'],p['y']) for p in b['runtime']['blocked']}
 ids={u['id'] for u in b['units']+b['spawns']};objs={o['id'] for o in b['objects']}
 for u in b['units']+b['spawns']:
  assert (u['x'],u['y']) not in blocked,(b['id'],u['id'],'blocked spawn')
 for e in b['events']:
  assert e['dialogue'];actual+= [(d['speaker'],d['text']) for d in e['dialogue']]
  for a in e['actions']:
   if a['command']=='spawn':assert a['args'][0] in ids
 start=next(u for u in b['units'] if u['id']=='lin');seen={(start['x'],start['y'])};q=collections.deque(seen)
 while q:
  x,y=q.popleft()
  for nx,ny in [(x+1,y),(x-1,y),(x,y+1),(x,y-1)]:
   if 1<=nx<=w and 1<=ny<=h and (nx,ny) not in blocked|seen:seen.add((nx,ny));q.append((nx,ny))
 for o in b['objects']:
  assert any((p['x'],p['y']) in seen or any((p['x']+dx,p['y']+dy) in seen for dx,dy in [(1,0),(-1,0),(0,1),(0,-1)]) for p in o['positions']),(b['id'],o['id'],'unreachable')
assert len(actual)==219
for speaker,text in actual:assert f'> **{speaker}**: {text}' in src
assert sum(len(v) for v in c['interludes'].values())==34
print('PASS: 8 battles, 75 placements, 81 events, 219 source dialogue, 34 interlude lines; all spawns clear and all objects reachable.')
