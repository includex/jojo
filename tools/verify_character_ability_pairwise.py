import json,sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])): raise SystemExit('CHARACTER_ABILITY_PAIRWISE_MISMATCH')
print('CHARACTER_ABILITY_PAIRWISE_OK')
