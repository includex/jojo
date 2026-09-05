import json,sys
a,b=(json.load(open(x,encoding='utf8')) for x in sys.argv[1:3])
if a!=b: raise SystemExit('EDIT_MUTATION_PAIRWISE_MISMATCH')
print('EDIT_MUTATION_PAIRWISE_OK cases=%d source-factory=EditLayer,EditLayer2,EditLayer3,EditLayer4,SAvatarEditLayer' % len(a))
