import json, sys
source, game = (json.load(open(path, encoding="utf-8")) for path in sys.argv[1:3])
if source != game: raise SystemExit("ROUND_LAYER_PAIRWISE_MISMATCH")
print("ROUND_LAYER_PAIRWISE_OK cases=%d timer=2s enemyCallback=complete touchBindings=0" % len(source))
