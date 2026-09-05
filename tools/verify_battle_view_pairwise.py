import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('BATTLE_VIEW_PAIRWISE_MISMATCH')
print('BATTLE_VIEW_PAIRWISE_OK cases=%d view/fight/dule lifecycle/actions'%len(a))
