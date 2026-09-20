import json,re,sys
paths=sys.argv[1:]
results={}
for label,path in zip(('source','port'),paths):
 d=json.load(open(path));frames=d['frames'];first=next(i for i,f in enumerate(frames) if f.get('round')==3 and f.get('camp')==0)
 last=frames[-1];prev={};events=[]
 for f in frames[first:]:
  for u in f['units']:
   if u[1] not in (0,483):continue
   clip=re.sub(r'_\d+$','',u[14] or '')
   state=(u[3],u[4],u[5],u[6],u[7],u[9],u[11],clip)
   if prev.get(u[1])!=state:
    events.append({'frame':f['f'],'t':f['t'],'unit':u[1],'state':state,'clipTime':u[15],'camera':f['camera'],'growth':u[17].get('growth')})
   prev[u[1]]=state
 units={str(u[1]):{'pos':u[3:5],'hp':u[5],'mp':u[6],'dir':u[7],'visible':u[9],'acted':u[11],'growth':u[17].get('growth'),'statuses':u[17].get('statuses')} for u in last['units']}
 results[label]={'frames':len(frames),'reason':d['reason'],'terminal':{k:last.get(k) for k in ('round','camp','camera','dialogue','dialogueSpeakerId','dialogueText')},'units':units,'events':events}
if len(results)==2:
 a,b=results['source']['units'],results['port']['units']
 results['unitDifferences']={k:{'source':a.get(k),'port':b.get(k)} for k in a.keys()|b.keys() if a.get(k)!=b.get(k)}
print(json.dumps(results,ensure_ascii=False,indent=2))
