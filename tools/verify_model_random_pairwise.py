#!/usr/bin/env python3
import json
import sys

source = json.load(open(sys.argv[1]))
port = json.load(open(sys.argv[2]))
if source != port:
    print("MODEL_RANDOM_PAIRWISE_MISMATCH")
    print(json.dumps({"source": source, "port": port}, ensure_ascii=False, indent=2))
    sys.exit(1)
print(f"MODEL_RANDOM_PAIRWISE_OK cases={len(source)}")
