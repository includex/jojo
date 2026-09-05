import json, sys
source, game = map(lambda p: json.load(open(p)), sys.argv[1:3])
if source != game:
    raise SystemExit('PROPERTY_LAYER_PAIRWISE_MISMATCH')
print('PROPERTY_LAYER_PAIRWISE_OK cases=%d' % len(source))
