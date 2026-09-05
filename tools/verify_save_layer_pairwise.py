import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('SAVE_LAYER_PAIRWISE_MISMATCH')
print('SAVE_LAYER_PAIRWISE_OK cases=%d' % len(a))
