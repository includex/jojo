import json, sys
src=open(sys.argv[1]).read(); game=open(sys.argv[2]).read()
if json.loads(src)!=json.loads(game): raise SystemExit('UNIT_INFO_PAIRWISE_MISMATCH')
print('UNIT_INFO_PAIRWISE_OK')
