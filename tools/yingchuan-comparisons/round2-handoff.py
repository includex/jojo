# 2턴 화공·증원·조작 인계. 요청 좌표가 아니라 실제 도착 좌표로 검사한다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 한 프레임이 이보다 길면 캡처가 멈춰 선 자리다.
STALL=0.05
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
 # 창 안의 정체를 함께 잰다. 판정은 아래에서 양쪽 불확실 구간을 더해 허용치로 쓴다.
 def stall(a,b):
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 stalls={k:stall(*v) for k,v in spans.items()}
 firepos=sorted(tuple(o[2:4]) for o in (fs[firei].get('mapObjects') or []))
 return {'checks':checks,'timing':timing,'stalls':stalls,
         'firePositions':firepos,'maxMapObjects':maxfire,'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['firePositionsMatch']=r['source']['firePositions']==r['port']['firePositions']
r['firePositionsOnlyInSource']=[p for p in r['source']['firePositions'] if p not in r['port']['firePositions']]
r['firePositionsOnlyInPort']=[p for p in r['port']['firePositions'] if p not in r['source']['firePositions']]
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
# 허용치 = 고정 폭 + 양쪽 창의 정체 폭.
r['stalls']={k:{'source':r['source']['stalls'][k],'port':r['port']['stalls'][k]} for k in r['timingDeltas']}
r['tolerances']={k:round(TOL+r['source']['stalls'][k]+r['port']['stalls'][k],4) for k in r['timingDeltas']}
r['timingChecks']={k:abs(v)<=r['tolerances'][k] for k,v in r['timingDeltas'].items()}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()) and r['firePositionsMatch'])
print(json.dumps(r,ensure_ascii=False,indent=2))
