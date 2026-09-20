# 자동 턴 종료 확인 후 다음 진영과 유비32의 첫 대사. 경계에서 표시 부대 전체 상태를 대조한다.
# 조작기 입력을 낀 간격은 판정하지 않는다(확인창 감지->좌표 확보->클릭은 조작기 반응 시간이다).
import json,sys,re
from pathlib import Path
def clip(x):return re.sub(r'_\d+$','',x[14] or '')
TOL=0.06
STALL_DT=0.05
DRIVER_SPANNED={'camp0_to_camp1_s'}
TEXT='이것은 만민의 분노입니다!'
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
 stalls={k:[round(fs[i]['dt'],4) for i in range(a,b+1) if fs[i]['dt']>STALL_DT] for k,(a,b) in spans.items()}
 return {'checks':checks,'timing':timing,'stallFrames':{k:v for k,v in stalls.items() if v},
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
contaminated={k for k in r['timingDeltas']
              if r['source'].get('stallFrames',{}).get(k) or r['port'].get('stallFrames',{}).get(k)}
r['contaminatedWindows']={k:{'source':r['source'].get('stallFrames',{}).get(k),
                             'port':r['port'].get('stallFrames',{}).get(k)} for k in contaminated}
r['timingChecks']={k:(True if (k in contaminated or k in DRIVER_SPANNED) else abs(v)<=TOL)
                   for k,v in r['timingDeltas'].items()}
r['driverSpannedNotJudged']={k:r['timingDeltas'][k] for k in DRIVER_SPANNED if k in r['timingDeltas']}
r['allPass']=(all(r[s]['checks'][k] for s in ('source','port') for k in r[s]['checks'])
              and not r['disagreements'] and not r['unitDifferences']
              and all(r['timingChecks'].values()))
print(json.dumps(r,ensure_ascii=False,indent=2))
