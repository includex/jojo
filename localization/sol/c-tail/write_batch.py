import json,pathlib
p=pathlib.Path(__file__).parent
source=json.loads((p.parent/'c'/'source.json').read_text())[2570:2820]
def save(number, rows):
 lookup={x['id']:x['zh'] for x in source}
 existing={k for f in p.parent.glob('*/batch*.json') if f.parent!=p for k in json.loads(f.read_text())}
 d={lookup[i]:v for i,v in rows.items() if lookup[i] not in existing}
 (p/f'batch-{number:03}.json').write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n')
 done={k for f in p.glob('batch*.json') for k in json.loads(f.read_text())}
 covered=sum(x['zh'] in existing|done for x in source)
 (p/'progress.json').write_text(json.dumps({'assigned':250,'translated':len(done),'covered':covered,'remaining':250-covered,'model':'gpt-6.1-sol','status':'complete' if covered==250 else 'in_progress'},indent=2)+'\n')
