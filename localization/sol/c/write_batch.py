import json
from pathlib import Path
P=Path(__file__).parent

def save(n,start,translations):
    rows=json.loads((P/'source.json').read_text())
    assert len(translations)==len(rows[start:start+len(translations)])
    opening=json.loads((P.parent/'opening'/'batch-001.json').read_text())
    mapping={r['zh']:ko for r,ko in zip(rows[start:],translations) if r['zh'] not in opening}
    (P/f'batch-{n:03}.json').write_text(json.dumps(mapping,ensure_ascii=False,indent=2)+'\n')
    done={}
    for f in P.glob('batch-*.json'): done.update(json.loads(f.read_text()))
    (P/'progress.json').write_text(json.dumps({'assigned':len(rows),'translated':len(done),'remaining':len(rows)-len(done),'method':'Sol direct translation from Chinese originals','next_row':start+len(translations)},ensure_ascii=False,indent=2)+'\n')
    print('saved',n,len(translations),'total',len(done))
