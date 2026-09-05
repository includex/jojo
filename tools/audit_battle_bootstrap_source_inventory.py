#!/usr/bin/env python3
import json,sys
data=json.load(open(sys.argv[1])); item=next(iter(data.values()))
count=sum(len(v) for v in item['constants'].values())
assert count == 127 and len(item['registry']) == 22
print('BATTLE_BOOTSTRAP_SOURCE_INVENTORY_OK constantValues=127 registry=22; inventory is not runtime parity')
