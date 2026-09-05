import hashlib
import json
import sys

source = json.load(open(sys.argv[1]))
port = json.load(open(sys.argv[2]))
if source != port:
    raise SystemExit("CONFIG_FULL_PAIRWISE_MISMATCH")
inventory = source["all_enumerable_config_exports"]

def count(value):
    if isinstance(value, dict):
        children = [count(v) for v in value.values()]
        return (1 + sum(x[0] for x in children), sum(x[1] for x in children), 1 + sum(x[2] for x in children))
    if isinstance(value, list):
        children = [count(v) for v in value]
        return (1 + sum(x[0] for x in children), sum(x[1] for x in children), sum(x[2] for x in children))
    return (1, 1, 0)

raw = json.dumps(inventory, ensure_ascii=False, separators=(",", ":"))
nodes, leaves, objects = count(inventory)
print("CONFIG_FULL_PAIRWISE_OK cases=%d exports=%d nodes=%d leaves=%d objects=%d sha256=%s" % (len(source), len(inventory), nodes, leaves, objects, hashlib.sha256(raw.encode()).hexdigest()))
