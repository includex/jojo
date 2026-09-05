import json,sys
a,b=(json.load(open(p)) for p in sys.argv[1:3])
if a!=b: raise SystemExit('SEND_GIFTS_PAIRWISE_MISMATCH')
print('SEND_GIFTS_PAIRWISE_OK cases=%d source-factory=SendGiftsLayer'%len(a))
