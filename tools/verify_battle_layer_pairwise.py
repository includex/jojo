import json,sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])): raise SystemExit('BATTLE_LAYER_PAIRWISE_MISMATCH')
print('BATTLE_LAYER_PAIRWISE_OK')
