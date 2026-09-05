import json,sys
source,game=(json.load(open(p)) for p in sys.argv[1:3])
if source != game:
    raise SystemExit('INFO_LAYER_PAIRWISE_MISMATCH')
print('INFO_LAYER_PAIRWISE_OK cases=%d source-factory=InfoLayer' % len(source))
