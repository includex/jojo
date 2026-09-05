import json,sys
a=json.load(open(sys.argv[1])); b=json.load(open(sys.argv[2]))
if a!=b: raise SystemExit('AUTO_BATTLE_PAIRWISE_MISMATCH')
print('AUTO_BATTLE_PAIRWISE_OK cases=%d source-factory=TuoGuanLayer,MsgBox4' % len(a))
