import json,sys,re
from pathlib import Path
def u(f,id):return next(x for x in f['units'] if x[1]==id)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
def summarize(path):
 d=json.loads(Path(path).read_text());fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==1]
 start=next(i for i,f in enumerate(fs) if clip(u(f,146))=='anime21');fs=fs[start:]
 hit=next(i for i,f in enumerate(fs) if clip(u(f,258))=='anime32')
 acted=next(i for i,f in enumerate(fs) if u(f,258)[11])
 grown=next(i for i,f in enumerate(fs) if u(f,146)[17]['growth']['experience']==8)
 events=[];prev={}
 for f in fs[:max(acted,grown)+3]:
  for id in (146,258):
   x=u(f,id);state=(*x[3:8],x[9],x[11],clip(x),x[17]['growth']['experience'])
   if prev.get(id)!=state:events.append({'frame':f['f'],'time':f['t'],'unit':id,'state':state,'clipTime':x[15]})
   prev[id]=state
 checks={'counter_from_9_11':u(fs[0],146)[3:5]==[9,11],'target_at_8_11':u(fs[0],258)[3:5]==[8,11],
 'target55_before_hit':all(u(f,258)[5]==55 for f in fs[:hit]),'hit_target24':u(fs[hit],258)[5]==24,
 'counteractor_hp_mp_preserved':all(u(f,146)[5:7]==[105,47] for f in fs[:max(acted,grown)+1]),
 'outer_actor_waits_for_hit':hit<acted,'outer_actor_exp25':u(fs[acted],258)[17]['growth']['experience']==25,
 'counteractor_exp8':u(fs[grown],146)[17]['growth']['experience']==8}
 return {'checks':checks,'events':events,'frames':len(d['frames']),'reason':d['reason']}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])};print(json.dumps(r,ensure_ascii=False,indent=2))
