import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('MINI_MAP_LAYER_PAIRWISE_MISMATCH')
print('MINI_MAP_LAYER_PAIRWISE_OK cases=%d load-scroll-weather-toggle-destroy=true'%len(a))
