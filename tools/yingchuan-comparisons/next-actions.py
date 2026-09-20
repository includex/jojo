# 후속 아군 교전(211 -> 475)과 234의 특수공격(-> 476). 한쪽씩의 검사에 더해 교차 델타도 본다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 정산 패널 생성 프레임 비용이 실행마다 달라 그 창만 여유를 둔다(기록1.439 대 재실행1.417).
JITTER={'acted_to_pose39_s':0.10}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv  =next(i for i,f in enumerate(fs) if u(f,211) and u(f,211)[3:5]==[9,16])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,211))=='anime25')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,475)[5]==70)
 cnt =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,475))=='anime25')
 chit=next(i for i,f in enumerate(fs) if i>=cnt and u(f,211)[5]==104)
 act =next(i for i,f in enumerate(fs) if i>=chit and u(f,211)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,211))=='anime39')
 smv =next(i for i,f in enumerate(fs) if i>=done and u(f,234) and u(f,234)[3:5]==[10,17])
 satk=next(i for i,f in enumerate(fs) if i>=smv and clip(u(f,234))=='anime48')
 shit=next(i for i,f in enumerate(fs) if i>=satk and u(f,476)[5]==41)
 t=lambda i:fs[i]['t']
 checks={
  'a211_starts_9_18':u(fs[0],211)[3:5]==[9,18],
  'a211_moves_to_9_16':u(fs[mv],211)[3:5]==[9,16],
  'a211_attack_clip_25':clip(u(fs[atk],211))=='anime25',
  'e475_at_9_15':u(fs[hit],475)[3:5]==[9,15],
  'e475_hp_97_to_70':u(fs[hit-1],475)[5]==97 and u(fs[hit],475)[5]==70,
  'e475_hit_clip_32':clip(u(fs[hit],475))=='anime32',
  'e475_counters_25':clip(u(fs[cnt],475))=='anime25',
  'a211_hp_119_to_104':u(fs[chit],211)[5]==104,
  'a211_exp_0_to_9':u(fs[act],211)[17]['growth']['experience']==9,
  'a211_acted_after_counter':chit<act,
  'a211_pose39_after_acted':done>act,
  # 234는 첫 프레임에 HP96이고 약0.6초의 스크립트 피해로 16이 된다. 시작값이 아니라
  # 이동 시점의 값으로 본다.
  'a234_at_12_17_hp16_before_move':u(fs[smv-1],234)[3:5]==[12,17] and u(fs[smv-1],234)[5]==16,
  'a234_moves_to_10_17':u(fs[smv],234)[3:5]==[10,17],
  'a234_special_clip_48':clip(u(fs[satk],234))=='anime48',
  'a234_mp_unchanged_11':u(fs[0],234)[6]==11 and u(fs[shit],234)[6]==11,
  'e476_hp_70_to_41':u(fs[shit-1],476)[5]==70 and u(fs[shit],476)[5]==41,
  'e476_special_hit_clip_32':clip(u(fs[shit],476))=='anime32',
 }
 timing={'a211_move_to_attack_s':round(t(atk)-t(mv),4),
         'a211_attack_to_hit_s':round(t(hit)-t(atk),4),
         'a211_hit_to_counter_s':round(t(cnt)-t(hit),4),
         'a211_counter_to_hit_s':round(t(chit)-t(cnt),4),
         'a211_hit_to_acted_s':round(t(act)-t(chit),4),
         'acted_to_pose39_s':round(t(done)-t(act),4),
         'a234_move_to_special_s':round(t(satk)-t(smv),4),
         'a234_special_to_hit_s':round(t(shit)-t(satk),4)}
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
