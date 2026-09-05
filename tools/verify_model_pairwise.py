import json,sys
a=json.load(open(sys.argv[1]));b=json.load(open(sys.argv[2]))
if a!=b: raise SystemExit('MODEL_PAIRWISE_MISMATCH')
print('MODEL_'+sys.argv[3].upper()+'_PAIRWISE_OK cases=%d'%len(a))
