import json,re,sys
from pathlib import Path
def unit(f,id):return next(u for u in f['units'] if u[1]==id)
def clip(u):return re.sub(r'_\d+$','',u[14] or '')
def summarize(path):
 d=json.loads(Path(path).read_text()); fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==1]
 attack=next(i for i,f in enumerate(fs) if clip(unit(f,32))=='anime21')
 hit=next(i for i,f in enumerate(fs) if clip(unit(f,480))=='anime32')
 retreat=next(i for i,f in enumerate(fs) if clip(unit(f,480))=='anime23')
 hidden=next(i for i,f in enumerate(fs) if not unit(f,480)[9])
 commit=next(i for i,f in enumerate(fs) if i>=attack and unit(f,32)[11] and unit(f,32)[17]['growth']['experience']==16)
 events=[];prev={}
 for f in fs[:hidden+1]:
  for id in (32,480):
   u=unit(f,id);state=(u[3],u[4],u[5],u[6],u[7],u[9],u[11],clip(u),u[17]['growth']['experience'])
   if prev.get(id)!=state:events.append({'frame':f['f'],'time':f['t'],'unit':id,'state':state,'clipTime':u[15],'visual':u[17].get('visual')})
   prev[id]=state
 def row(u):return {'pos':u[3:5],'hp':u[5],'mp':u[6],'direction':u[7],'visible':u[9],'acted':u[11],'growth':u[17]['growth'],'statuses':u[17].get('statuses')}
 checks={'start_9_5':unit(fs[0],32)[3:5]==[9,5],'attack_from_9_8':unit(fs[attack],32)[3:5]==[9,8],
 'target_8_8':unit(fs[hit],480)[3:5]==[8,8], 'hp97_until_hit':all(unit(f,480)[5]==97 for f in fs[:hit]),
 'hit_hp0':unit(fs[hit],480)[5]==0,'exp8_unacted_through_hit':all(unit(f,32)[17]['growth']['experience']==8 and not unit(f,32)[11] for f in fs[:hit+1]),
 'attack_hit_commit_retreat_hidden_order':attack<hit<commit<retreat<hidden,'actor_vitals':all(unit(f,32)[5:7]==[149,44] for f in fs[:hidden+1])}
 return {'checks':checks,'totalFrames':len(d['frames']),'events':events,'cameraAtHidden':fs[hidden]['camera'],'unitsAtHidden':{str(u[1]):row(u) for u in fs[hidden]['units']},'hiddenFrame':fs[hidden]['f']}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
if 'port' in r:
 a,b=r['source']['unitsAtHidden'],r['port']['unitsAtHidden'];r['unitDifferencesAtHidden']={k:{'source':a.get(k),'port':b.get(k)} for k in a.keys()|b.keys() if a.get(k)!=b.get(k)}
print(json.dumps(r,ensure_ascii=False,indent=2))
