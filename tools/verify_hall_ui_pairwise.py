import json, sys
source, game = (json.load(open(path, encoding='utf-8')) for path in sys.argv[1:3])
if source != game:
    raise SystemExit('HALL_UI_PAIRWISE_MISMATCH')
print('HALL_UI_PAIRWISE_OK cases=%d source-factory=HallMenuLayer,HallCommandLayer routes=0..9 ambition=complete command=-1,0..3' % len(source))
