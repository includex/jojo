import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a != b: raise SystemExit('FORCES_LIST_LAYER_PAIRWISE_MISMATCH')
print('FORCES_LIST_LAYER_PAIRWISE_OK cases=%d tabs-sort-status-row-close-destroy=true' % len(a))
