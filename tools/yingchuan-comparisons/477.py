# 적477의 공격과 210의 반격, 그리고 정산창 순서. 교차 델타로 판정한다.
# acted->pose39은 원본 자체 편차가 크다: 기록된 이전 실행 1.450, 이번 실행 1.638 -> 0.19.
# 정산 패널 생성 프레임 비용이 실행마다 달라지는 창이므로 허용치를 그 편차에서 잡는다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 원본 캡처 도구가 패널 경계에서 Page.captureScreenshot을 찍으면 그 프레임의
# dt가 0.48초까지 뛰고, Cocos CallbackTimer는 반복 발화 때 _elapsed를 0으로 되돌려 초과분을
# 버리므로 wall이 그만큼 늘어난다. 실제로 원본 A/C 모두 이 창에 0.48초 프레임이 정확히
# 하나씩 있고 포트에는 없다. 1.6375-0.188 = 1.4495 로 기록된 1.450과 맞으며, 원본의 규칙상
# 총 길이는 .1(centerUnit yield) + 5*.2(값 틱) + .3(_over) = 1.4초다. 정체 폭은 허용치에
# 더해 판정하고, 임의로 넓히지 않는다.
STALL=0.05
SOURCE_JITTER={}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv  =next(i for i,f in enumerate(fs) if u(f,477) and u(f,477)[3:5]==[11,15])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,477))=='anime25')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,210)[5]==83)
 cnt =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,210))=='anime25')
 chit=next(i for i,f in enumerate(fs) if i>=cnt and u(f,477)[5]==77)
 act =next(i for i,f in enumerate(fs) if i>=chit and u(f,477)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,477))=='anime39')
 t=lambda i:fs[i]['t']
 checks={
  'e477_starts_12_15':u(fs[0],477)[3:5]==[12,15],
  'e477_moves_to_11_15':u(fs[mv],477)[3:5]==[11,15],
  'e477_dir3':u(fs[atk],477)[7]==3,
  'e477_attack_clip_25':clip(u(fs[atk],477))=='anime25',
  'a210_at_10_16':u(fs[hit],210)[3:5]==[10,16],
  'a210_hp_104_to_83':u(fs[hit-1],210)[5]==104 and u(fs[hit],210)[5]==83,
  'a210_hit_clip_32':clip(u(fs[hit],210))=='anime32',
  'a210_counters_25':clip(u(fs[cnt],210))=='anime25',
  'e477_hp_97_to_77':u(fs[chit-1],477)[5]==97 and u(fs[chit],477)[5]==77,
  'e477_hit_clip_32':clip(u(fs[chit],477))=='anime32',
  'e477_acted_after_counter_hit':chit<act,
  'e477_pose39_after_acted':done>act,
  'a210_never_acts_here':not u(fs[done],210)[11],
 }
 timing={'move_to_attack_s':round(t(atk)-t(mv),4),
         'attack_to_hit_s':round(t(hit)-t(atk),4),
         'hit_to_counter_s':round(t(cnt)-t(hit),4),
         'counter_to_hit_s':round(t(chit)-t(cnt),4),
         'hit_to_acted_s':round(t(act)-t(chit),4),
         'acted_to_pose39_s':round(t(done)-t(act),4)}
 # 창 안의 정체를 함께 잰다. 판정은 아래에서 양쪽 불확실 구간을 더해 허용치로 쓴다.
 def stall(a,b):
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 spans={'move_to_attack_s':(mv,atk),'attack_to_hit_s':(atk,hit),'hit_to_counter_s':(hit,cnt),
        'counter_to_hit_s':(cnt,chit),'hit_to_acted_s':(chit,act),'acted_to_pose39_s':(act,done)}
 stalls={k:stall(*v) for k,v in spans.items()}
 return {'checks':checks,'timing':timing,'stalls':stalls,'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
# 허용치 = 고정 폭 + 양쪽 창의 정체 폭.
r['stalls']={k:{'source':r['source']['stalls'][k],'port':r['port']['stalls'][k]} for k in r['timingDeltas']}
r['tolerances']={k:round(SOURCE_JITTER.get(k,TOL)+r['source']['stalls'][k]+r['port']['stalls'][k],4)
                 for k in r['timingDeltas']}
r['timingChecks']={k:abs(v)<=r['tolerances'][k] for k,v in r['timingDeltas'].items()}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
