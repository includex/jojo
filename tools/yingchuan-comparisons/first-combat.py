# 첫 일반 교전(210 -> 476)의 양쪽 대조. 한쪽씩의 검사만으로는 타이밍 차이를 놓치므로
# 교차 델타도 함께 판정한다. 허용치는 원본이 스스로 어긋나는 폭에서 온다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 원본 두 실행의 acted->pose39: 1.429(기록) vs 1.453(재실행) -> 0.024. 정산 패널 생성
# 프레임 비용이 실행마다 달라 이 창만 여유를 둔다.
JITTER={'acted_to_pose39_s':0.10}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv  =next(i for i,f in enumerate(fs) if u(f,210) and u(f,210)[3:5]==[10,16])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,210))=='anime25')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,476)[5]==70)
 cnt =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,476))=='anime25')
 chit=next(i for i,f in enumerate(fs) if i>=cnt and u(f,210)[5]==104)
 act =next(i for i,f in enumerate(fs) if i>=chit and u(f,210)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,210))=='anime39')
 t=lambda i:fs[i]['t']
 checks={
  'attacker_starts_10_17':u(fs[0],210)[3:5]==[10,17],
  'attacker_moves_to_10_16':u(fs[mv],210)[3:5]==[10,16],
  'attacker_hp119_before_counter':all(u(f,210)[5]==119 for f in fs[:chit]),
  'attack_clip_25':clip(u(fs[atk],210))=='anime25',
  'target_at_10_15':u(fs[hit],476)[3:5]==[10,15],
  'target_hp_97_to_70':u(fs[hit-1],476)[5]==97 and u(fs[hit],476)[5]==70,
  'target_hit_clip_32':clip(u(fs[hit],476))=='anime32',
  'target_counters_25':clip(u(fs[cnt],476))=='anime25',
  'attacker_hp_119_to_104':u(fs[chit],210)[5]==104,
  'attacker_hit_clip_32':clip(u(fs[chit],210))=='anime32',
  'acted_after_counter_hit':chit<act,
  'attacker_exp_0_to_9':u(fs[act],210)[17]['growth']['experience']==9,
  'completed_pose_after_acted':done>act,
  'target_survives_hp70':u(fs[done],476)[5]==70,
 }
 timing={'move_to_attack_s':round(t(atk)-t(mv),4),
         'attack_to_hit_s':round(t(hit)-t(atk),4),
         'hit_to_counter_s':round(t(cnt)-t(hit),4),
         'counter_to_counterhit_s':round(t(chit)-t(cnt),4),
         'counterhit_to_acted_s':round(t(act)-t(chit),4),
         'acted_to_pose39_s':round(t(done)-t(act),4)}
 return {'checks':checks,'timing':timing,'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
r['tolerances']={k:JITTER.get(k,TOL) for k in r['timingDeltas']}
r['timingChecks']={k:abs(v)<=JITTER.get(k,TOL) for k,v in r['timingDeltas'].items()}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
