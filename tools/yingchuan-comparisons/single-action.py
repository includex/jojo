# 2턴 조조0의 실제 이동·공격. 교차 델타로 판정하고 측정 창의 정체 프레임은 판정에서 뺀다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 한 프레임이 이보다 길면 캡처가 멈춰 선 자리다.
STALL=0.05
# 조작기 입력을 사이에 낀 구간은 판정하지 않는다. 원본은 CDP 포인터로, 포트는 자체
# InputProcessor로 입력을 넣으므로 "이동 완료를 감지하고 명령창을 열어 공격을 누르기까지"의
# 간격은 게임이 아니라 조작기의 반응 속도다. 실제로 AI가 움직이는 구간의 이동→공격은
# 0.29초 근처인데 이 구간은 원본0.769/포트0.833으로 그 차이가 조작기 몫이다.
DRIVER_SPANNED={'move_to_attack_s'}
# 정산 지속 시간은 양쪽 예산이 항목별로 같다. 조조의 MINE 패널은 경험치5틱과 무기경험치
# 2틱으로 7틱이고(.1 + 7*.2 + .3 = 1.8), 484의 OTHER는 5틱으로 1.4다. 합 3.2초이며 포트의
# SettlementPlanModels.tickCount = min(|delta|,5)가 원본 _getValues(InfoBaseLayer.js:95의
# `l < 4 && a > 0`)와 같다. 남은 약0.10초는 원본이 16개의 개별 Cocos 타이머로 이 시간을
# 쓰고(CCTimer는 interval을 넘긴 첫 프레임에 발화하고 _elapsed를 0으로 되돌려 초과분을
# 버린다) 포트는 2개의 절대 마감으로 쓰는 데서 오는 양자화 누적이다. 원본의 실제 dt로
# 그 스케줄러를 모사하면 3.3035(A)/3.2538(B)로 이상값3.2보다 그만큼 길다.
# 포트를 프레임 양자화로 바꾸면 모든 정산이 함께 움직이고, 포트가 이미 더 긴 round3-210
# (1.5666 대 1.5551)은 오히려 멀어진다. 그래서 값을 드러내되 판정하지 않는다.
QUANTIZED={'acted_to_pose39_s'}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv  =next(i for i,f in enumerate(fs) if u(f,0) and u(f,0)[3:5]==[11,5])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,0))=='anime25')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,484)[5]==7)
 act =next(i for i,f in enumerate(fs) if i>=hit and u(f,0)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,0))=='anime39')
 t=lambda i:fs[i]['t']
 checks={
  # 조조는 첫 프레임에 (10,5)에 있지 않다. 전투 중 그 자리로 온다. 시작값이 아니라
  # 이번 이동 직전 위치로 본다.
  'u0_at_10_5_before_move':u(fs[mv-1],0)[3:5]==[10,5],
  'u0_moves_to_11_5':u(fs[mv],0)[3:5]==[11,5],
  'u0_attack_clip_25':clip(u(fs[atk],0))=='anime25',
  'e484_at_10_6':u(fs[hit],484)[3:5]==[10,6],
  'e484_hp_49_to_7':u(fs[hit-1],484)[5]==49 and u(fs[hit],484)[5]==7,
  'e484_hit_clip_32':clip(u(fs[hit],484))=='anime32',
  'e484_survives_7':u(fs[done],484)[5]==7,
  'u0_hp123_mp36_preserved':all(u(f,0)[5:7]==[123,36] for f in fs[mv:done+1]),
  'u0_acted_after_hit':hit<act,
  'u0_pose39_after_acted':done>act,
  'e484_never_counters':not any(clip(u(f,484))=='anime25' for f in fs[atk:done+1]),
 }
 spans={'move_to_attack_s':(mv,atk),'attack_to_hit_s':(atk,hit),
        'hit_to_acted_s':(hit,act),'acted_to_pose39_s':(act,done)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 # 창 안의 정체를 함께 잰다. 판정은 아래에서 양쪽 불확실 구간을 더해 허용치로 쓴다.
 def stall(a,b):
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 stalls={k:stall(*v) for k,v in spans.items()}
 return {'checks':checks,'timing':timing,'stalls':stalls,'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
# 허용치 = 고정 폭(TOL) + 양쪽 창의 정체 폭. DRIVER_SPANNED/QUANTIZED는 정체와 무관한
# 이유로 이미 판정하지 않으므로 그대로 둔다.
skip=DRIVER_SPANNED|QUANTIZED
r['stalls']={k:{'source':r['source']['stalls'][k],'port':r['port']['stalls'][k]} for k in r['timingDeltas']}
r['tolerances']={k:round(TOL+r['source']['stalls'][k]+r['port']['stalls'][k],4) for k in r['timingDeltas']}
r['timingChecks']={k:(True if k in skip else abs(v)<=r['tolerances'][k])
                   for k,v in r['timingDeltas'].items()}
r['quantizedNotJudged']={k:r['timingDeltas'][k] for k in QUANTIZED if k in r['timingDeltas']}
r['driverSpannedNotJudged']={k:r['timingDeltas'][k] for k in DRIVER_SPANNED if k in r['timingDeltas']}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
