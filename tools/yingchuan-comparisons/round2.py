import json
from pathlib import Path
source_path=Path('build/reports/yingchuan-source-round2-handoff-20260920/source-full-trace.json')
port_path=Path('verification/build/verification/yingchuan-round2-handoff-final-20260920/yingchuan-manual-trace.json')
s=json.loads(source_path.read_text());p=json.loads(port_path.read_text())
sf=next(f for f in s['frames'] if f['round']==2 and f['camp']==0)
pf=next(f for f in p['frames'] if f['round']==2 and f.get('phase')=='PLAYER_INPUT')
fields={'x':3,'y':4,'HP':5,'MP':6,'direction':7,'visible':9,'AI':12,'AIvalue':13}
su={u[1]:u for u in sf['units']};pu={u[1]:u for u in pf['units']}
diffs=[]
for uid in sorted(su.keys()&pu.keys()):
 ds={k:[su[uid][i],pu[uid][i]] for k,i in fields.items() if su[uid][i]!=pu[uid][i]}
 if ds:diffs.append({'unit':uid,'sourceVisible':su[uid][9],'portVisible':pu[uid][9],'differences':ds})
r={'source':str(source_path),'port':str(port_path),'sourceFrame':sf['f'],'sourceT':sf['t'],'sourceCamera':sf.get('camera'),'portFrame':pf['f'],'portT':pf['t'],'portCamera':pf.get('camera'),'commonUnits':len(su.keys()&pu.keys()),'sourceOnly':sorted(su.keys()-pu.keys()),'portOnly':sorted(pu.keys()-su.keys()),'comparedFields':fields,'differences':diffs}
Path('build/reports/yingchuan-round2-comparison-20260920/player-boundary.json').write_text(json.dumps(r,indent=2)+'\n')
print(json.dumps(r,indent=2))
