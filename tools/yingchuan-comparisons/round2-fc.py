# 유비32의 필살 대사를 닫은 뒤 484 격파까지. 애니메이션 길이는 클립 시계(tuple[15])로 본다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
STALL_DT=0.05
# 정산 창은 원본이 16개 타이머로, 포트가 절대 마감으로 시간을 쓰는 양자화 차이를 갖는다.
# 값은 드러내되 판정하지 않는다. 자세한 근거는 single-player-action 절에 있다.
QUANTIZED={'acted_to_pose39_s'}
WALL_ONLY={'retreat_to_hidden_s'}   # 애니메이션 길이는 클립 시계로 따로 검사한다
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 atk =next(i for i,f in enumerate(fs) if u(f,32) and clip(u(f,32))=='anime21')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,484)[5]==0)
 act =next(i for i,f in enumerate(fs) if i>=hit and u(f,32)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,32))=='anime39')
 low =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,484))=='anime9')
 ret =next(i for i,f in enumerate(fs) if i>=low and clip(u(f,484))=='anime23')
 hid =next(i for i,f in enumerate(fs) if i>=ret and not u(f,484)[9])
 t=lambda i:fs[i]['t']
 checks={
  'a32_at_9_5':u(fs[atk],32)[3:5]==[9,5],
  'a32_critical_clip_21':clip(u(fs[atk],32))=='anime21',
  'a32_hp149_preserved':all(u(f,32)[5]==149 for f in fs[atk:hid+1]),
  'e484_at_10_6':u(fs[hit],484)[3:5]==[10,6],
  'e484_hp_7_to_0':u(fs[hit-1],484)[5]==7 and u(fs[hit],484)[5]==0,
  'e484_hit_clip_32':clip(u(fs[hit],484))=='anime32',
  'e484_lowpose_9':clip(u(fs[low],484))=='anime9',
  'e484_retreat_23_after_lowpose':ret>low,
  'e484_hidden_after_retreat':hid>ret,
  'a32_acted_after_hit':hit<act,
  'a32_pose39_after_acted':done>act,
  'e484_never_counters':not any(clip(u(f,484))=='anime25' for f in fs[atk:hid+1]),
 }
 spans={'attack_to_hit_s':(atk,hit),'hit_to_acted_s':(hit,act),
        'acted_to_pose39_s':(act,done),'retreat_to_hidden_s':(ret,hid)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 lastvis=u(fs[hid-1],484)
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
         'retreatClipTimeAtLastVisible':lastvis[15],'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
contaminated={k for k in r['timingDeltas']
              if r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k)}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
r['timingChecks']={k:(True if (k in contaminated or k in QUANTIZED or k in WALL_ONLY) else abs(v)<=TOL)
                   for k,v in r['timingDeltas'].items()}
r['notJudged']={k:r['timingDeltas'][k] for k in (QUANTIZED|WALL_ONLY) if k in r['timingDeltas']}
sc,pc=r['source']['retreatClipTimeAtLastVisible'],r['port']['retreatClipTimeAtLastVisible']
r['retreatClipTime']={'source':sc,'port':pc,'bothReachClipEnd':abs((sc or 0)-1.25)<0.03 and abs((pc or 0)-1.25)<0.03}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values())
              and r['retreatClipTime']['bothReachClipEnd'])
print(json.dumps(r,ensure_ascii=False,indent=2))
