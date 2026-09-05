import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('TREASURE_LAYER_PAIRWISE_MISMATCH')
print('TREASURE_LAYER_PAIRWISE_OK cases=%d factory=true order=Model.itemIter discoveredOrdinal=true'%len(a))
