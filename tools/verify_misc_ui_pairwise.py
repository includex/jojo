import json,sys
a,b=(json.load(open(x,encoding='utf8')) for x in sys.argv[1:3])
if a!=b: raise SystemExit('MISC_UI_PAIRWISE_MISMATCH')
print('MISC_UI_PAIRWISE_OK cases=%d source-factory=NoticeInfoLayer,HelpLayer,InputBox,SelectListLayer,ListLayer'%len(a))
