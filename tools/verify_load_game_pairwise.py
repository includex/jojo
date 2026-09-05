import json, sys
if json.load(open(sys.argv[1])) != json.load(open(sys.argv[2])):
    raise SystemExit("LOAD_GAME_PAIRWISE_MISMATCH")
print("LOAD_GAME_PAIRWISE_OK")
