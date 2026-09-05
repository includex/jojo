#!/usr/bin/env python3
import json,sys
fixture=json.load(open(sys.argv[1])); trace=json.load(open(sys.argv[2]))
expected=[c['name'] for c in fixture['cases']]
assert list(trace)==expected
assert len(expected)==15 and len({c['kind'] for c in fixture['cases']})==15
print('BATTLE_CONTROL_SOURCE_FIXTURE_INVENTORY_OK cases=15; overridden source branches are not Kotlin runtime parity evidence')
