import json,sys
a,b=(json.load(open(p)) for p in sys.argv[1:3])
if a != b: raise SystemExit('WELCOME_PAIRWISE_MISMATCH')
print('WELCOME_PAIRWISE_OK cases=%d source-factory=Welcome'%len(a))
