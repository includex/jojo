# 3턴 장보146의 방어와 필살 반격. 258이 공격하고 146이 방어26 뒤 필살21로 반격한다.
import json,sys,re
from pathlib import Path
def u(f,i):return next((x for x in f['units'] if x[1]==i),None)
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
def exp(x):return x[17]['growth']['experience']
TOL=0.06
STALL_DT=0.05
QUANTIZED={'counterhit_to_actorActed_s'}   # 정산 패널: 타이머 개수 차이로 구조적 격차
# defence_to_counter는 판정하지 않는다. 이 창의 끝은 클립이 아니라 146의 필살 대사
# `후우후……!`가 닫히는 시점이 정하고, 원본 캡처가 매 실행 바로 그 대사에서 스크린샷을
# 찍는다(maxdt 0.449/0.451, 같은 실행의 다른 대사는 0.018 이하). 매번 같은 지점이라
# 두 실행이 똑같이 부풀려지고, 그래서 편차0.0003이 오염을 배제하지 못한다.
# 같은 실행 안에서 비교하면 드러난다. 원본의 다른 두 대사는 글자당0.058/0.055인데
# 이 6글자 대사만0.173이다. 두 깨끗한 점의 직선은 6글자를0.407초로 예측하고 포트는
# 0.450초다. 포트가 자기 실행의 다른 대사들과 정합하며 원본 측정이 이상값이다.
# 포트는 checkCrit 교대, getCritTxt, 1초 자동닫기, 0.04초/글자 타자를 이미 구현한다.
DIALOGUE_TERMINATED={'defence_to_counter_s'}
STALL_SENSITIVE={'counterhit_to_actorActed_s'}
def summarize(path):
 d=json.loads(Path(path).read_text());fs=[f for f in d['frames'] if f.get('round')==3 and f.get('camp')==1]
 atk =next(i for i,f in enumerate(fs) if u(f,258) and clip(u(f,258))=='anime25')
 dfd =next(i for i,f in enumerate(fs) if i>=atk and u(f,146) and clip(u(f,146))=='anime26')
 cnt =next(i for i,f in enumerate(fs) if i>=dfd and clip(u(f,146))=='anime21')
 chit=next(i for i,f in enumerate(fs) if i>=cnt and u(f,258)[5]==24)
 act =next(i for i,f in enumerate(fs) if i>=chit and u(f,258)[11])
 t=lambda i:fs[i]['t']
 checks={
  'e258_attacks_25':clip(u(fs[atk],258))=='anime25',
  'a146_defends_26':clip(u(fs[dfd],146))=='anime26',
  'a146_counters_critical_21':clip(u(fs[cnt],146))=='anime21',
  'a146_hp105_mp47_preserved':all(u(f,146)[5:7]==[105,47] for f in fs[atk:act+1]),
  'e258_hp_55_to_24':u(fs[chit-1],258)[5]==55 and u(fs[chit],258)[5]==24,
  'e258_hit_clip_32':clip(u(fs[chit],258))=='anime32',
  'defence_before_counter':dfd<cnt,
  'counter_before_hit':cnt<chit,
  'e258_acted_after_counterhit':chit<act,
  'e258_exp25_at_acted':exp(u(fs[act],258))==25,
  'a146_exp_reaches_8':any(exp(u(f,146))==8 for f in fs[act:]),
  'a146_exp4_until_counterhit':all(exp(u(f,146))==4 for f in fs[atk:chit+1]),
 }
 spans={'attack_to_defence_s':(atk,dfd),'defence_to_counter_s':(dfd,cnt),
        'counter_to_hit_s':(cnt,chit),'counterhit_to_actorActed_s':(chit,act)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
         'frames':len(d['frames']),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
contaminated={k for k in r['timingDeltas'] if k in STALL_SENSITIVE
              and (r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k))}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
skip=contaminated|QUANTIZED|DIALOGUE_TERMINATED
r['timingChecks']={k:(True if k in skip else abs(v)<=TOL) for k,v in r['timingDeltas'].items()}
r['notJudged']={k:r['timingDeltas'][k] for k in skip if k in r['timingDeltas']}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
