import json,sys
a=json.load(open(sys.argv[1])); b=json.load(open(sys.argv[2]))
if a != b:
    raise SystemExit('ITEM_EQUIP_PAIRWISE_MISMATCH')
print('ITEM_EQUIP_PAIRWISE_OK cases=%d' % len(a))
