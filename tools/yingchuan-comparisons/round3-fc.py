# 3턴 조조의 실제 이동과 필살 공격. 확정 시점(경험치가 행동확정과 함께 오는지)이 핵심이다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
def exp(x):return x[17]['growth']['experience']
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 한 프레임이 이보다 길면 캡처가 멈춰 선 자리다.
STALL=0.05
DRIVER_SPANNED={'move_to_attack_s'}   # 이동 완료 감지 -> 명령창 -> 공격 -> 필살 대사 닫기
QUANTIZED={'acted_to_pose39_s'}       # MINE+OTHER 두 패널: 타이머 개수 차이로 구조적 격차
WALL_ONLY={'retreat_to_hidden_s'}     # 애니메이션 길이는 클립 시계로 본다
# 정체 프레임이 창을 부풀리는 것은 **타이머가 구동하는 구간**뿐이다. Cocos 타이머는
# interval을 넘긴 첫 프레임에 발화하고 _elapsed를 0으로 되돌려 초과분을 버리므로 정체마다
# 늘어난다. 반면 **클립이 구동하는 구간**은 정체 동안 클립도 dt만큼 함께 진행하므로 wall이
# 부풀지 않는다. 실제로 이 구간의 attack_to_hit은 원본 창에 0.1초 정체가 있는데도
# 원본0.9401 대 포트0.9319로 델타 -0.008이다. 따라서 정체를 이유로 판정에서 빼는 것은
# 타이머 구동 구간에 한정한다. 그러지 않으면 캡처가 촘촘한 모드에서 판정력이 사라진다.
STALL_SENSITIVE={'acted_to_pose39_s'}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==0]
 mv  =next(i for i,f in enumerate(fs) if u(f,0) and u(f,0)[3:5]==[10,5])
 atk =next(i for i,f in enumerate(fs) if i>=mv and clip(u(f,0))=='anime21')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,483)[5]==0)
 act =next(i for i,f in enumerate(fs) if i>=hit and u(f,0)[11])
 done=next(i for i,f in enumerate(fs) if i>=act and clip(u(f,0))=='anime39')
 low =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,483))=='anime9')
 ret =next(i for i,f in enumerate(fs) if i>=low and clip(u(f,483))=='anime23')
 hid =next(i for i,f in enumerate(fs) if i>=ret and not u(f,483)[9])
 t=lambda i:fs[i]['t']
 checks={
  'u0_starts_11_5':u(fs[0],0)[3:5]==[11,5],
  'u0_moves_to_10_5':u(fs[mv],0)[3:5]==[10,5],
  'u0_critical_clip_21':clip(u(fs[atk],0))=='anime21',
  'u0_hp123_preserved':all(u(f,0)[5]==123 for f in fs[mv:hid+1]),
  'e483_at_9_6':u(fs[hit],483)[3:5]==[9,6],
  'e483_hp_19_to_0':u(fs[hit-1],483)[5]==19 and u(fs[hit],483)[5]==0,
  'e483_hit_clip_32':clip(u(fs[hit],483))=='anime32',
  'e483_lowpose_then_retreat':ret>low,
  'e483_hidden_after_retreat':hid>ret,
  # 확정 시점: 공격 시작에는 아직 반영되지 않고 행동확정에서 함께 온다
  'exp6_until_hit':all(exp(u(f,0))==6 for f in fs[atk:hit+1]),
  'exp30_at_acted':exp(u(fs[act],0))==30,
  'target_hp_zero_before_acted':hit<act,
  'u0_pose39_after_acted':done>act,
 }
 spans={'move_to_attack_s':(mv,atk),'attack_to_hit_s':(atk,hit),'hit_to_acted_s':(hit,act),
        'acted_to_pose39_s':(act,done),'retreat_to_hidden_s':(ret,hid)}
 # 창 안의 정체를 함께 잰다. STALL_SENSITIVE로 표시한, 타이머가 구동하는 창만 이 폭을
 # 허용치에 더한다(위 주석 참고). 클립이 구동하는 창까지 더하면 판정력이 사라진다.
 def stall(a,b):
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 stalls={k:stall(*v) for k,v in spans.items()}
 return {'checks':checks,'timing':timing,'stalls':stalls,
         'retreatClipTimeAtLastVisible':u(fs[hid-1],483)[15],'frames':len(d['frames']),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
r['stalls']={k:{'source':r['source']['stalls'][k],'port':r['port']['stalls'][k]} for k in r['timingDeltas']}
def tol(k):
 return TOL+r['stalls'][k]['source']+r['stalls'][k]['port'] if k in STALL_SENSITIVE else TOL
skip=DRIVER_SPANNED|QUANTIZED|WALL_ONLY
r['timingChecks']={k:(True if k in skip else abs(v)<=tol(k)) for k,v in r['timingDeltas'].items()}
r['tolerances']={k:('not judged' if k in skip else round(tol(k),4)) for k in r['timingDeltas']}
r['notJudged']={k:r['timingDeltas'][k] for k in skip if k in r['timingDeltas']}
sc,pc=r['source']['retreatClipTimeAtLastVisible'],r['port']['retreatClipTimeAtLastVisible']
r['retreatClipTime']={'source':sc,'port':pc,'bothReachClipEnd':abs((sc or 0)-1.25)<0.03 and abs((pc or 0)-1.25)<0.03}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values())
              and r['retreatClipTime']['bothReachClipEnd'])
print(json.dumps(r,ensure_ascii=False,indent=2))
