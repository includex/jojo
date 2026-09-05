#!/usr/bin/env python3
import json,sys
a=json.load(open(sys.argv[1]));b=json.load(open(sys.argv[2]))
assert a==b,(a,b)
print('FRAMEWORK_PAIRWISE_OK cases='+str(len(a)))
