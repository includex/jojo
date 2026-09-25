# 자동 턴 종료 확인 후 다음 진영과 유비32의 첫 대사. 경계에서 표시 부대 전체 상태를 대조한다.
# 조작기 입력을 낀 간격은 판정하지 않는다(확인창 감지->좌표 확보->클릭은 조작기 반응 시간이다).
import json, re, sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from dialogue_text_catalog import dialogue_text
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
# 한 프레임의 기대 길이. 이보다 긴 프레임은 캡처가 멈춰 선 자리다.
FRAME=1/60
# 정체 판정 기준. 한 프레임이 이보다 길면 캡처가 멈춰 선 자리다.
STALL=0.05
DRIVER_SPANNED={'camp0_to_camp1_s'}
TEXT=dialogue_text("yingchuan_round2_followup_speaker32")
def summarize(path):
 d=json.loads(Path(path).read_text());fs=d['frames']
 c0 =next(i for i,f in enumerate(fs) if f.get('round')==2 and f.get('camp')==0)
 c1 =next(i for i,f in enumerate(fs) if i>=c0 and f.get('round')==2 and f.get('camp')==1)
 dlg=next((i for i,f in enumerate(fs) if i>=c1 and f.get('dialogueSpeakerId')=='32' and f.get('dialogueText')==TEXT), None)
 if dlg is None: raise SystemExit('speaker-32 dialogue %r not observed in %s'%(TEXT,path))
 t=lambda i:fs[i]['t']
 units={x[1]:(x[3],x[4],x[5],x[6],x[7],x[9]) for x in fs[dlg]['units'] if x[9]}
 checks={
  'camp_advances_to_1':fs[c1].get('camp')==1,
  'dialogue_speaker_32':fs[dlg].get('dialogueSpeakerId')=='32',
  'dialogue_text_exact':fs[dlg].get('dialogueText')==TEXT,
  'dialogue_after_camp1':dlg>c1,
  'no_further_camp_change':all(f.get('camp')==1 for f in fs[dlg:]),
 }
 spans={'camp0_to_camp1_s':(c0,c1),'camp1_to_dialogue_s':(c1,dlg)}
 timing={k:round(t(b)-t(a),4) for k,(a,b) in spans.items()}
 # 창 안의 정체를 함께 잰다. 판정은 아래에서 양쪽 불확실 구간을 더해 허용치로 쓴다.
 def stall(a,b):
  return round(sum(fs[i]['t']-fs[i-1]['t']-FRAME
                   for i in range(a+1,b+1) if fs[i]['t']-fs[i-1]['t']>STALL),4)
 stalls={k:stall(*v) for k,v in spans.items()}
 return {'checks':checks,'timing':timing,'stalls':stalls,
         'visibleUnits':units,'camera':fs[dlg].get('camera'),
         'frames':len(fs),'reason':d.get('reason')}
r={k:summarize(p) for k,p in zip(('source','port'),sys.argv[1:])}
r['disagreements']={k:(r['source']['checks'][k],r['port']['checks'][k])
                    for k in r['source']['checks'] if r['source']['checks'][k]!=r['port']['checks'][k]}
su,pu=r['source']['visibleUnits'],r['port']['visibleUnits']
r['visibleUnitCount']={'source':len(su),'port':len(pu)}
r['unitDifferences']={k:{'source':su.get(k),'port':pu.get(k)} for k in set(su)|set(pu) if su.get(k)!=pu.get(k)}
r['cameraDelta']=[round(p-s,4) for s,p in zip(r['source']['camera'] or [0,0], r['port']['camera'] or [0,0])]
r['timingDeltas']={k:round(r['port']['timing'][k]-r['source']['timing'][k],4) for k in r['source']['timing']}
# 허용치 = 고정 폭 + 양쪽 창의 정체 폭. DRIVER_SPANNED는 조작기 반응 시간이라 정체와
# 무관하게 이미 판정하지 않으므로 그대로 둔다.
r['stalls']={k:{'source':r['source']['stalls'][k],'port':r['port']['stalls'][k]} for k in r['timingDeltas']}
r['tolerances']={k:round(TOL+r['source']['stalls'][k]+r['port']['stalls'][k],4) for k in r['timingDeltas']}
r['timingChecks']={k:(True if k in DRIVER_SPANNED else abs(v)<=r['tolerances'][k])
                   for k,v in r['timingDeltas'].items()}
r['driverSpannedNotJudged']={k:r['timingDeltas'][k] for k in DRIVER_SPANNED if k in r['timingDeltas']}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and not r['unitDifferences']
              and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
