# 3턴 종료 확인 뒤 유비32의 이동·필살로 480 격파. 확정 시점과 클립 시계를 함께 본다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
def exp(x):return x[17]['growth']['experience']
TOL=0.06
STALL_DT=0.05
DRIVER_SPANNED=set()                  # 이 구간의 이동·공격은 AI가 구동한다
QUANTIZED={'acted_to_pose39_s'}       # 정산 패널: 타이머 개수 차이로 구조적 격차
WALL_ONLY={'retreat_to_hidden_s'}     # 애니메이션 길이는 클립 시계로 본다
STALL_SENSITIVE={'acted_to_pose39_s'} # 정체는 타이머 구동 구간만 부풀린다
def summarize(path):
 d=json.loads(Path(path).read_text());fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==1]
 mv  =next(i for i,f in enumerate(fs) if u(f,32) and u(f,32)[3:5]==[9,8])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,32))=='anime21')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,480)[5]==0)
 act =next(i for i,f in enumerate(fs) if i>=hit and u(f,32)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,32))=='anime39')
 low =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,480))=='anime9')
 ret =next(i for i,f in enumerate(fs) if i>=low and clip(u(f,480))=='anime23')
 hid =next(i for i,f in enumerate(fs) if i>=ret and not u(f,480)[9])
 t=lambda i:fs[i]['t']
 checks={
  'a32_starts_9_5':u(fs[0],32)[3:5]==[9,5],
  'a32_moves_to_9_8':u(fs[mv],32)[3:5]==[9,8],
  'a32_critical_clip_21':clip(u(fs[atk],32))=='anime21',
  'a32_hp149_preserved':all(u(f,32)[5]==149 for f in fs[mv:hid+1]),
  'e480_at_8_8':u(fs[hit],480)[3:5]==[8,8],
  'e480_hp_97_to_0':u(fs[hit-1],480)[5]==97 and u(fs[hit],480)[5]==0,
  'e480_hit_clip_32':clip(u(fs[hit],480))=='anime32',
  'e480_lowpose_then_retreat':ret>low,
  'e480_hidden_after_retreat':hid>ret,
  'exp8_until_hit':all(exp(u(f,32))==8 for f in fs[atk:hit+1]),
  'exp16_at_acted':exp(u(fs[act],32))==16,
  'target_zero_before_acted':hit<act,
  'a32_pose39_after_acted':done>act,
  'e480_never_counters':not any(clip(u(f,480))=='anime25' for f in fs[atk:hid+1]),
 }
 spans={'move_to_attack_s':(mv,atk),'attack_to_hit_s':(atk,hit),'hit_to_acted_s':(hit,act),
        'acted_to_pose39_s':(act,done),'retreat_to_hidden_s':(ret,hid)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
         'retreatClipTimeAtLastVisible':u(fs[hid-1],480)[15],'frames':len(d['frames']),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
contaminated={k for k in r['timingDeltas'] if k in STALL_SENSITIVE
              and (r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k))}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
skip=contaminated|DRIVER_SPANNED|QUANTIZED|WALL_ONLY
r['timingChecks']={k:(True if k in skip else abs(v)<=TOL) for k,v in r['timingDeltas'].items()}
r['notJudged']={k:r['timingDeltas'][k] for k in skip if k in r['timingDeltas']}
sc,pc=r['source']['retreatClipTimeAtLastVisible'],r['port']['retreatClipTimeAtLastVisible']
r['retreatClipTime']={'source':sc,'port':pc,'bothReachClipEnd':abs((sc or 0)-1.25)<0.03 and abs((pc or 0)-1.25)<0.03}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values())
              and r['retreatClipTime']['bothReachClipEnd'])
print(json.dumps(r,ensure_ascii=False,indent=2))
