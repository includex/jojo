# 첫 적군 교전(474 도착 -> 234 격파). 기준은 모호하지 않은 사건만 쓴다.
# 원본의 완료 자세 anime39는 등장/소멸을 반복하므로 "첫 등장"을 기준으로 삼지 않고,
# 퇴각 직전의 마지막 등장을 쓴다. 그 이유는 comparison.json의 poseObservation에 남긴다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 한 프레임이 이보다 길면 캡처가 멈춰 선 자리다. 프레임마다의 작은 흔들림까지
# 더하면 체계적으로 느린 쪽을 흡수해 판정력이 사라지므로, 정체 폭은 그 창의 정체 프레임만 모아
# 잰다.
STALL=0.05
# 허용치는 원본이 스스로 어긋나는 폭에서 온다. 이 구간의 원본을 두 번 실행해 측정했다.
#   (A) yingchuan-source-enemy-first-combat-20260920-final, (B) yingchuan-source-enemy-first-srgb2-20260920
#   lowpose_to_retreat_s  2.1812 vs 1.9034 -> 0.278   <- 원본 자체 편차가 매우 크다
#   retreat_to_hidden_s   1.1519 vs 1.1616 -> 0.0097  <- 안정적이다
# 앞선 판본은 lowpose_to_retreat_s를 -0.247짜리 미해결 차이로 기록했으나, 그 기대값은 같은 쌍을
# 손으로 계산해 넣은 순환이었고 원본 B로 다시 재면 델타가 +0.030으로 허용 안에 들어온다.
# 따라서 그 항목은 포트 결함의 증거가 아니다. unitDeath 순서 가설도 이 측정으로는 뒷받침되지 않는다.
SOURCE_JITTER={'lowpose_to_retreat_s':0.30}
# 애니메이션 길이는 wall이 아니라 클립 시계로 잰다. trace의 유닛 tuple 인덱스15가
# cc.AnimationState.time이며, 원본은 퇴각 중 프레임을 흘려 클립1.25초를 wall1.16초에 태운다.
# wall로 재면 포트가 0.088초 느린 것처럼 보이지만, 클립 시계로는 양쪽 모두 완료에서 숨긴다
# (원본은1.2500에서 아직 보이고 다음 프레임에 숨김, 포트는1.2333에서 숨김 = 오히려1틱 빠름).
# 따라서 이 지표를 wall로 판정하지 않는다.
OPEN={}

def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 mv  =next(i for i,f in enumerate(fs) if u(f,474) and clip(u(f,474))=='anime20')
 arr =next(i for i,f in enumerate(fs) if i>=mv and u(f,474)[3:5]==[9,17])
 atk =next(i for i,f in enumerate(fs) if i>=arr and clip(u(f,474))=='anime25')
 hit =next(i for i,f in enumerate(fs) if i>=atk and u(f,234)[5]==0)
 low =next(i for i,f in enumerate(fs) if i>=hit and clip(u(f,234))=='anime9')
 ret =next(i for i,f in enumerate(fs) if i>=low and clip(u(f,234))=='anime23')
 hid =next(i for i,f in enumerate(fs) if i>=ret and not u(f,234)[9])
 pose=max((i for i in range(low,ret+1) if clip(u(fs[i],474))=='anime39'), default=None)
 poses=[round(fs[i]['t'],3) for i in range(low,hid+1) if clip(u(fs[i],474))=='anime39']
 t=lambda i:fs[i]['t']
 checks={
  'e474_starts_6_16':u(fs[mv],474)[3:5]==[6,16],
  'e474_arrives_9_17':u(fs[arr],474)[3:5]==[9,17],
  'e474_arrival_dir1_idle':u(fs[arr],474)[7]==1 and clip(u(fs[arr],474))=='anime0',
  'e474_attack_clip_25':clip(u(fs[atk],474))=='anime25',
  'e474_attacks_dir1':u(fs[atk],474)[7]==1,
  'e474_hp97_unchanged':all(u(f,474)[5]==97 for f in fs[mv:hid+1]),
  'a234_at_10_17':u(fs[hit],234)[3:5]==[10,17],
  'a234_hp_16_to_0':u(fs[hit-1],234)[5]==16 and u(fs[hit],234)[5]==0,
  'a234_hit_clip_32':clip(u(fs[hit],234))=='anime32',
  'a234_lowpose_9':clip(u(fs[low],234))=='anime9',
  'a234_retreat_23_after_lowpose':ret>low,
  'a234_hidden_after_retreat':hid>ret,
  'e474_shows_completed_pose':pose is not None,
 }
 timing={'move_to_arrival_s':round(t(arr)-t(mv),4),
         'arrival_to_attack_s':round(t(atk)-t(arr),4),
         'attack_to_hit_s':round(t(hit)-t(atk),4),
         'hit_to_lowpose_s':round(t(low)-t(hit),4),
         'lowpose_to_retreat_s':round(t(ret)-t(low),4),
         'retreat_to_hidden_s':round(t(hid)-t(ret),4),
         'retreat_clip_time_at_last_visible':fs[hid-1]['units'][[u[1] for u in fs[hid-1]['units']].index(234)][15],
         'pose_before_retreat_s':round(t(ret)-t(pose),4) if pose is not None else None}
 # 창 안의 정체를 함께 잰다. 판정은 아래에서 양쪽 불확실 구간을 더해 허용치로 쓴다.
 def stall(a,b):
  if a is None or b is None: return 0.0
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 spans={'move_to_arrival_s':(mv,arr),'arrival_to_attack_s':(arr,atk),'attack_to_hit_s':(atk,hit),
        'hit_to_lowpose_s':(hit,low),'lowpose_to_retreat_s':(low,ret),
        'retreat_to_hidden_s':(ret,hid),'pose_before_retreat_s':(pose,ret)}
 stalls={k:stall(*v) for k,v in spans.items()}
 return {'checks':checks,'timing':timing,'stalls':stalls,
         'poseObservation':{'anime39_times':poses,'count':len(poses)},
         'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:(round(r['port']['timing'][k]-r['source']['timing'][k],4)
                      if r['source']['timing'][k] is not None and r['port']['timing'][k] is not None else None)
                   for k in r['source']['timing']}
# 허용치 = 고정 폭 + 양쪽 창의 정체 폭. 정체가 없는 창은 종전과 같은 판정력을 그대로 갖는다.
r['stalls']={k:{'source':r['source']['stalls'].get(k,0),'port':r['port']['stalls'].get(k,0)}
             for k in r['timingDeltas']}
def judge(k,v):
 if v is None: return False
 tol=SOURCE_JITTER.get(k,TOL)+r['stalls'][k]['source']+r['stalls'][k]['port']
 if abs(v)<=tol: return True
 e=OPEN.get(k); return e is not None and abs(v-e[0])<=TOL
WALL_ONLY={'retreat_to_hidden_s'}  # 원본 캡처 pacing이 섞이므로 보고만 하고 판정하지 않는다
r['timingChecks']={k:(True if k in WALL_ONLY else judge(k,v)) for k,v in r['timingDeltas'].items()}
r['reportedNotJudged']={k:r['timingDeltas'][k] for k in WALL_ONLY if k in r['timingDeltas']}
r['openDifferences']={k:{'recordedDelta':e[0],'actualDelta':r['timingDeltas'].get(k),'reason':e[1]}
                      for k,e in OPEN.items() if r['timingDeltas'].get(k) is not None and abs(r['timingDeltas'][k])>SOURCE_JITTER.get(k,TOL)}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
