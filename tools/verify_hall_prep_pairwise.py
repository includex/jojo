import json,sys
a,b=(json.load(open(x,encoding='utf8')) for x in sys.argv[1:3])
if a!=b: raise SystemExit('HALL_PREP_PAIRWISE_MISMATCH')
print('HALL_PREP_PAIRWISE_OK cases=%d source-factory=HallLayer,BattleInitLayer,StartBattleScreen,BattleSortLayer' % len(a))
