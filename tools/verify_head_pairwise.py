import json,sys
if json.load(open(sys.argv[1]))!=json.load(open(sys.argv[2])):raise SystemExit('HEAD_PAIRWISE_MISMATCH')
print('HEAD_PAIRWISE_OK cases=3')
