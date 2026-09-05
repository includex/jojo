import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('PROGRESSION_LAYER_PAIRWISE_MISMATCH')
print('PROGRESSION_LAYER_PAIRWISE_OK cases=%d factories=Achievements,SignIn,Raffle,Reset,Register'%len(a))
