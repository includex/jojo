import json,sys
a,b=(json.load(open(x)) for x in sys.argv[1:3])
if a!=b: raise SystemExit('SHOP_REWARD_PAIRWISE_MISMATCH')
print('SHOP_REWARD_PAIRWISE_OK cases=%d factories=RewardLayer,BuyLayer,SellLayer'%len(a))
