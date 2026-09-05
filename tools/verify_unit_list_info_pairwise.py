import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a != b: raise SystemExit('UNIT_LIST_INFO_PAIRWISE_MISMATCH')
print('UNIT_LIST_INFO_PAIRWISE_OK cases=%d source-factory-list-info-animation=true' % len(a))
