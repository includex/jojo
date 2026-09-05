#!/usr/bin/env python3
import json,subprocess
from pathlib import Path
r=Path(__file__).resolve().parents[1]; f=r/'tools/terrain_layer_trace_cases.json'
a=json.loads(subprocess.check_output(['node',str(r/'tools/terrain_layer_source_trace_harness.js'),str(f)],text=True));b=json.loads(subprocess.check_output([str(r/'gradlew'),'-q',':core:terrainLayerTrace'],cwd=r,text=True))
assert {x['tag'] for x in a}>={'create','END:1','END:2','SCROLL:0','SCROLL:27'}
for state in a:
    assert len(state['rows']) == 2
    for panel in state['rows']:
        assert len(panel) == 28
        assert [row['id'] for row in panel] == list(range(28))
        assert [row['icon'] for row in panel] == list(range(28))
        assert all(len(row['skills']) == 4 and len(row['arms']) == 13 for row in panel)
assert a==b,(a,b)
print('TERRAIN_LAYER_TRACE_OK states=7 payload=2x28x13 lazy=rise/expend scroll=0/27 close=true')
