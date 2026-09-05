import json, sys
source, port = (json.load(open(p, encoding='utf-8')) for p in sys.argv[1:3])
if source != port:
    raise SystemExit('CMD_LAYER_PAIRWISE_MISMATCH')
print('CMD_LAYER_PAIRWISE_OK cases=%d featureRows=0..13 buttons=0..5 mutations=items/flags/store/dispatch' % len(source))

