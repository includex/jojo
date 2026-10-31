import json, pathlib
p=pathlib.Path(__file__).parent
assigned=json.loads((p/'assigned.json').read_text())
def save(number, rows):
    lookup={x['index']:x['text'] for x in assigned}
    opening=json.loads((p.parent/'opening'/'batch-001.json').read_text())
    result={lookup[i]:text for i,text in rows.items() if lookup[i] not in opening}
    (p/f'batch-{number:03}.json').write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
    done={k for f in p.glob('batch-*.json') for k in json.loads(f.read_text())}
    (p/'progress.json').write_text(json.dumps({'assigned':len(assigned),'translated':len(done),'remaining':len(assigned)-len(done),'model':'gpt-6.1-sol','status':'in_progress'},indent=2)+'\n')
