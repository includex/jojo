import json,sys
a=json.load(open(sys.argv[1]));b=json.load(open(sys.argv[2]))
if a!=b: raise SystemExit('SCENARIO_RUNTIME_PAIRWISE_MISMATCH')
print('SCENARIO_RUNTIME_PAIRWISE_OK cases=%d source-factory=PyManager,RControlScript' % len(a))
