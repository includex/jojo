import json,sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])): raise SystemExit('FIGHT_PRESENTATION_PAIRWISE_MISMATCH')
print('FIGHT_PRESENTATION_PAIRWISE_OK')
