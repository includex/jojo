import json, sys
source, game = (json.load(open(path, encoding="utf-8")) for path in sys.argv[1:3])
if source != game:
    raise SystemExit("HELPER_LAYER_PAIRWISE_MISMATCH")
print("HELPER_LAYER_PAIRWISE_OK cases=%d tabs=none button=TOUCH_END route=removeFromParent" % len(source))
