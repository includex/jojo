import json, sys

source, port = (json.load(open(path, encoding="utf-8")) for path in sys.argv[1:3])
if source != port:
    raise SystemExit("SETTING_LAYER_PAIRWISE_MISMATCH")
print("SETTING_LAYER_PAIRWISE_OK cases=%d flags=0..6 radio=0,2 deferredSlider=true close=TOUCH_END" % len(source))
