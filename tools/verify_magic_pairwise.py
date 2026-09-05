import json,sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])): raise SystemExit('MAGIC_PAIRWISE_MISMATCH')
print('MAGIC_PAIRWISE_OK')
