#!/usr/bin/env python3
import json,sys
data=json.load(open(sys.argv[1]))
assert data, 'empty core boundary source inventory'
item=next(iter(data.values()))
assert len(item['engine']['CMDS']) == 12
assert len(item['engine']['LAYER_ANIME']) == 3
assert len(item['engine']['TouchEventType']) == 4
assert len(item['layers']) == 48
assert item['loggerMethods'] == ['constructor']
print('CORE_BOUNDARY_SOURCE_INVENTORY_OK engineValues=19 layers=48; no Kotlin runtime parity claim')
