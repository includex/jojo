import json,sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])):
    raise SystemExit('ENEMY_TURN_PAIRWISE_MISMATCH')
print('ENEMY_TURN_PAIRWISE_OK')
