import json,sys
a,b=(json.load(open(p,encoding='utf8')) for p in sys.argv[1:3])
if a!=b: raise SystemExit('END_FLOW_PAIRWISE_MISMATCH')
print('END_FLOW_PAIRWISE_OK cases=%d source-factory=WinConBoxLayer,Lose,End,SkipLayer' % len(a))
