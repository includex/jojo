import json,pathlib,sys
p=pathlib.Path(__file__).parent
d=json.load(open(p.parents[2]/'../jojo_mobile/sgccz-desktop/tools/ko-source-strings.json'))
start=int(sys.argv[1]); k=json.load(sys.stdin)
assert all(isinstance(s,str) and s for s in k)
out={d[start+i*3]['text']:s for i,s in enumerate(k)}
opening=p.parent/'opening'/'batch-001.json'
if opening.exists():
 for key in json.loads(opening.read_text()):out.pop(key,None)
(p/f'batch-{start:05d}.json').write_text(json.dumps(out,ensure_ascii=False,indent=2)+'\n')
allout={}
for f in p.glob('batch-*.json'):allout.update(json.loads(f.read_text()))
(p/'progress.json').write_text(json.dumps({'partition':'source_index % 3 == 0','assigned':2821,'completed':len(allout),'next_source_index':start+len(k)*3,'model':'gpt-6.1-sol','status':'in_progress'},ensure_ascii=False,indent=2)+'\n')
print('Saved',len(k),'Total',len(allout))
