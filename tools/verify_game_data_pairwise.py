import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('GAME_DATA_PAIRWISE_MISMATCH')
print('GAME_DATA_PAIRWISE_OK cases=%d modules=Item,ItemStore,HallCfg'%len(a))
