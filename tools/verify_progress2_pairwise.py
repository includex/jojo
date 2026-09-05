import json,sys
a,b=(json.load(open(p)) for p in sys.argv[1:3])
if a!=b:raise SystemExit('PROGRESS2_PAIRWISE_MISMATCH')
print('PROGRESS2_PAIRWISE_OK cases=%d source-factory=ProgressLayer2'%len(a))
