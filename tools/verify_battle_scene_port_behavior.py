import json,sys
a=json.load(open(sys.argv[1])); b=json.load(open(sys.argv[2]))
behavior=('resources','layers','log','modelSaves','callbacks')
projected={name:{key:value[key] for key in behavior} for name,value in a.items()}
if projected!=b: raise SystemExit('BATTLE_SCENE_PORT_BEHAVIOR_MISMATCH')
print('BATTLE_SCENE_PORT_BEHAVIOR_OK cases=%d production=BattleScenePort' % len(a))
