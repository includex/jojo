import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b:raise SystemExit('MAP_INFO_LAYER_PAIRWISE_MISMATCH')
print('MAP_INFO_LAYER_PAIRWISE_OK cases=%d typing=0.04 autoClose=1,5 skip=true'%len(a))
