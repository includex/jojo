# 2턴 화공·증원·조작 인계. 요청 좌표가 아니라 실제 도착 좌표로 검사한다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
STALL_DT=0.05
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 vis258=next(i for i,f in enumerate(fs) if u(f,258) and u(f,258)[9])
 arr258=next(i for i,f in enumerate(fs) if i>=vis258 and u(f,258)[3:5]==[9,7])
 arr259=next(i for i,f in enumerate(fs) if i>=vis258 and u(f,259) and u(f,259)[3:5]==[10,7])
 arr0  =next(i for i,f in enumerate(fs) if u(f,0) and u(f,0)[3:5]==[10,5])
 arr32 =next(i for i,f in enumerate(fs) if u(f,32) and u(f,32)[3:5]==[9,5])
 fires=[len(f.get('mapObjects') or []) for f in fs]
 maxfire=max(fires); firei=fires.index(maxfire)
 t=lambda i:fs[i]['t']
 checks={
  'r258_hidden_at_start':not u(fs[0],258)[9],
  'r259_hidden_at_start':not u(fs[0],259)[9],
  'r258_arrives_9_7':u(fs[arr258],258)[3:5]==[9,7],
  'r259_arrives_10_7':u(fs[arr259],259)[3:5]==[10,7],
  'r258_visible_on_arrival':u(fs[arr258],258)[9]==1,
  'r259_visible_on_arrival':u(fs[arr259],259)[9]==1,
  'u0_lands_10_5':u(fs[arr0],0)[3:5]==[10,5],
  'u32_lands_9_5':u(fs[arr32],32)[3:5]==[9,5],
  'fire_count_27':maxfire==27,
  'r259_moves_to_10_6_later':any(u(f,259) and u(f,259)[3:5]==[10,6] for f in fs[arr259:]),
 }
 spans={'r258_to_r259_arrival_s':(arr258,arr259)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 firepos=sorted(tuple(o[2:4]) for o in (fs[firei].get('mapObjects') or []))
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
         'firePositions':firepos,'maxMapObjects':maxfire,'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['firePositionsMatch']=r['source']['firePositions']==r['port']['firePositions']
r['firePositionsOnlyInSource']=[p for p in r['source']['firePositions'] if p not in r['port']['firePositions']]
r['firePositionsOnlyInPort']=[p for p in r['port']['firePositions'] if p not in r['source']['firePositions']]
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
contaminated={k for k in r['timingDeltas']
              if r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k)}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
r['timingChecks']={k:(True if k in contaminated else abs(v)<=TOL) for k,v in r['timingDeltas'].items()}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()) and r['firePositionsMatch'])
print(json.dumps(r,ensure_ascii=False,indent=2))
