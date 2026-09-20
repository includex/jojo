# 무입력 자연 진행 구간. round3-210 캡처의 종점(camp 전환) 이후를 같은 실행 trace에서 읽는다.
# 새 실행이 아니라 이미 보존한 300초 기록의 뒷부분이다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# Tolerance comes from how much the ORIGINAL disagrees with ITSELF, measured across two real
# source runs of this segment (yingchuan-source-round3-210-20260920 and -srgb-20260920):
#   hit1_to_counter_s 0.0199  counter_to_kill_s 0.0117  kill_to_hit2_s 0.0018
#   hit2_to_lowpose_s 0.0005  lowpose_to_retreat_s 0.0515  retreat_to_hidden_s 0.0355
# An earlier version pinned two of these as "accepted residuals" at one run's values, which
# was fitting source noise; that is removed.
SOURCE_JITTER={'lowpose_to_retreat_s':0.08,'retreat_to_hidden_s':0.07}
# Differences we have located and deliberately not fixed. They are allowed to persist at the
# size recorded here and fail the gate if they GROW - they are never hidden.
OPEN={
 'lowpose_to_retreat_s':(-0.1115,
   'the original runs unitDeath case 0 -> run_script -> centerUnit before the retreat '
   '(BattleLayer.js:7118-7130, :7033-7070); the port still runs that script pass before '
   'settlement (AiPresentationCoordinator.kt:417-421, BattleScreen.kt:5126-5137). Reordering '
   'was measured to make the settlement window worse and reverses a deliberate earlier '
   'decision, so it is recorded, not patched.'),
 'retreat_to_hidden_s':(0.0940,
   'UNRESOLVED. The port holds 1.2666s, matching the retreat clip asset length 30/24=1.25s. '
   'The source reads 1.1006s and 1.1726s by wall across two runs, but its own dt over the '
   'same window sums to 1.2667s. The source wall and dt measures disagree with each other '
   'here, so which side is wrong is not yet established. Do not tune the port to the wall '
   'figure on this evidence.'),
}

def summarize(path):
 d=json.loads(Path(path).read_text())
 fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==2]
 hit1=next(i for i,f in enumerate(fs) if u(f,210) and u(f,210)[5]==20)
 cnt =next(i for i,f in enumerate(fs) if i>=hit1 and clip(u(f,210))=='anime25')
 defd=next(i for i,f in enumerate(fs) if i>=cnt and u(f,477) and clip(u(f,477))=='anime26')
 kill=next(i for i,f in enumerate(fs) if i>=cnt and u(f,479) and clip(u(f,479))=='anime25')
 hit2=next(i for i,f in enumerate(fs) if i>=kill and u(f,210)[5]==0)
 low =next(i for i,f in enumerate(fs) if i>=hit2 and clip(u(f,210))=='anime9')
 ret =next(i for i,f in enumerate(fs) if i>=low and clip(u(f,210))=='anime23')
 hid =next(i for i,f in enumerate(fs) if i>=ret and not u(f,210)[9])
 checks={
  'defender_210_at_10_16':u(fs[0],210)[3:5]==[10,16],
  'first_hit_41_to_20':u(fs[hit1-1],210)[5]==41 and u(fs[hit1],210)[5]==20,
  'first_hit_clip_32':clip(u(fs[hit1],210))=='anime32',
  'counter_clip_25':clip(u(fs[cnt],210))=='anime25',
  'attacker_477_at_11_15_hp77':u(fs[defd],477)[3:5]==[11,15] and u(fs[defd],477)[5]==77,
  'attacker_477_defends_26':clip(u(fs[defd],477))=='anime26',
  'counter_deals_no_damage':u(fs[defd],477)[5]==77,
  'killer_479_at_10_15_hp77':u(fs[kill],479)[3:5]==[10,15] and u(fs[kill],479)[5]==77,
  'killer_479_attacks_25':clip(u(fs[kill],479))=='anime25',
  'second_hit_20_to_0':u(fs[hit2],210)[5]==0 and clip(u(fs[hit2],210))=='anime32',
  'low_hp_pose_9':clip(u(fs[low],210))=='anime9',
  'retreat_23_after_low':ret>low,
  'hidden_after_retreat':hid>ret,
  'defender_never_moves':all(u(f,210)[3:5]==[10,16] for f in fs[:hid+1]),
 }
 t=lambda i:fs[i]['t']
 timing={'hit1_to_counter_s':round(t(cnt)-t(hit1),4),
         'counter_to_kill_s':round(t(kill)-t(cnt),4),
         'kill_to_hit2_s':round(t(hit2)-t(kill),4),
         'hit2_to_lowpose_s':round(t(low)-t(hit2),4),
         'lowpose_to_retreat_s':round(t(ret)-t(low),4),
         'retreat_to_hidden_s':round(t(hid)-t(ret),4)}
 return {'checks':checks,'timing':timing,'frames':len(d['frames']),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
def judge(k,v):
 if abs(v)<=SOURCE_JITTER.get(k,TOL): return True
 e=OPEN.get(k)
 # an open difference may persist at its recorded size, but not grow beyond source jitter
 return e is not None and abs(v-e[0])<=SOURCE_JITTER.get(k,TOL)
r['timingChecks']={k:judge(k,v) for k,v in r['timingDeltas'].items()}
r['tolerances']={k:SOURCE_JITTER.get(k,TOL) for k in r['timingDeltas']}
r['openDifferences']={k:{'recordedDelta':e[0],'actualDelta':r['timingDeltas'][k],'reason':e[1]}
                      for k,e in OPEN.items() if abs(r['timingDeltas'][k])>SOURCE_JITTER.get(k,TOL)}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
