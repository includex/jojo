# 첫 적군 턴의 마지막 네 행동(484/485/475/476)과 2턴 진입.
# 교차 델타로 판정하고, 측정 창 안의 정체 프레임(dt>0.05)이 있으면 그 지표는 판정하지 않는다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
STALL_DT=0.05
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv484 =next(i for i,f in enumerate(fs) if u(f,484) and u(f,484)[3:5]==[10,6])
 atk484=next(i for i,f in enumerate(fs) if i>=mv484 and clip(u(f,484))=='anime25')
 hit3  =next(i for i,f in enumerate(fs) if i>=atk484 and u(f,3)[5]==154)
 cnt3  =next(i for i,f in enumerate(fs) if i>=hit3 and clip(u(f,3))=='anime25')
 hit484=next(i for i,f in enumerate(fs) if i>=cnt3 and u(f,484)[5]==49)
 atk475=next(i for i,f in enumerate(fs) if i>=hit484 and clip(u(f,475))=='anime25')
 hit211=next(i for i,f in enumerate(fs) if i>=atk475 and u(f,211)[5]==83)
 hit475=next(i for i,f in enumerate(fs) if i>=hit211 and u(f,475)[5]==50)
 atk476=next(i for i,f in enumerate(fs) if i>=hit475 and clip(u(f,476))=='anime25')
 hit210=next(i for i,f in enumerate(fs) if i>=atk476 and u(f,210)[5]==62)
 hit476=next(i for i,f in enumerate(fs) if i>=hit210 and u(f,476)[5]==21)
 t=lambda i:fs[i]['t']
 checks={
  'e484_moves_to_10_6':u(fs[mv484],484)[3:5]==[10,6],
  'e484_attack_clip_25':clip(u(fs[atk484],484))=='anime25',
  'a3_hp_155_to_154':u(fs[hit3-1],3)[5]==155 and u(fs[hit3],3)[5]==154,
  'a3_counters_25':clip(u(fs[cnt3],3))=='anime25',
  'e484_hp_97_to_49':u(fs[hit484-1],484)[5]==97 and u(fs[hit484],484)[5]==49,
  'e485_moves_only':not any(clip(u(f,485))=='anime25' for f in fs[:hit476+1]),
  'e475_attacks_in_place':u(fs[atk475],475)[3:5]==[9,15],
  'a211_hp_to_83':u(fs[hit211],211)[5]==83,
  'e475_hp_70_to_50':u(fs[hit475-1],475)[5]==70 and u(fs[hit475],475)[5]==50,
  'e476_attacks_in_place':u(fs[atk476],476)[3:5]==[10,15],
  'a210_hp_to_62':u(fs[hit210],210)[5]==62,
  'e476_hp_41_to_21':u(fs[hit476-1],476)[5]==41 and u(fs[hit476],476)[5]==21,
  'a3_survives_154':u(fs[hit476],3)[5]==154,
 }
 spans={'e484_move_to_attack_s':(mv484,atk484),'e484_attack_to_hit_s':(atk484,hit3),
        'a3_hit_to_counter_s':(hit3,cnt3),'a3_counter_to_hit_s':(cnt3,hit484),
        'e475_attack_to_hit_s':(atk475,hit211),'a211_counter_to_hit_s':(hit211,hit475),
        'e476_attack_to_hit_s':(atk476,hit210),'a210_counter_to_hit_s':(hit210,hit476)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
         'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
contaminated={k for k in r['timingDeltas']
              if r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k)}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
r['timingChecks']={k:(True if k in contaminated else abs(v)<=TOL) for k,v in r['timingDeltas'].items()}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
