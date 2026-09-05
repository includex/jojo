import json,sys
a,b=(json.load(open(x,encoding='utf8')) for x in sys.argv[1:3])
if a!=b: raise SystemExit('SYSTEM_UI_PAIRWISE_MISMATCH')
print('SYSTEM_UI_PAIRWISE_OK cases=%d source-factory=MsgBox,MsgBox2,MsgBox3,MsgBox4,ToastLayer,ProgressLayer,LoadingLayer'%len(a))
