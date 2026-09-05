import json, sys
source, port = (json.load(open(p, encoding='utf-8')) for p in sys.argv[1:3])
if source != port:
    raise SystemExit('CHOICE_COMMAND_PAIRWISE_MISMATCH')
print('CHOICE_COMMAND_PAIRWISE_OK cases=%d source-factory=ChooseLayer,Choose2Layer,CommandLayer keyboard=none' % len(source))
