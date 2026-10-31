import json,sys
from pathlib import Path
p=Path(__file__).parent
rows=json.loads((p/'source.json').read_text())
def save(name,items):
 out={rows[int(k)]['zh']:v for k,v in items.items()}
 (p/name).write_text(json.dumps(out,ensure_ascii=False,indent=2)+'\n')
if __name__=='__main__':save(sys.argv[1],json.load(sys.stdin))
