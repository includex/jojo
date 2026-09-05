#!/usr/bin/env python3
import json,sys
a=json.load(open(sys.argv[1]));b=json.load(open(sys.argv[2]))
if a!=b: print('FOUNDATION_PAIRWISE_MISMATCH');print(json.dumps({'source':a,'port':b},ensure_ascii=False,indent=2));sys.exit(1)
print('FOUNDATION_PAIRWISE_OK cases='+str(len(a)))
