import json, sys
source, port = map(lambda p: json.load(open(p)), sys.argv[1:3])
if source != port:
    raise SystemExit('PROPERTY_LAYER_PAIRWISE_MISMATCH')
print('PROPERTY_LAYER_PAIRWISE_OK cases=%d' % len(source))
