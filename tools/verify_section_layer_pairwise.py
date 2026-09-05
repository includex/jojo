import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('SECTION_LAYER_PAIRWISE_MISMATCH')
print('SECTION_LAYER_PAIRWISE_OK cases=%d touch-auto-skip-progression=true'%len(a))
