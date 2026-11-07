"""Execute the generated scenario fixture to check distance waves and escape gates."""
import collections
import json
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]

class Stage:
    def __init__(self):
        self.x, self.y = 5, 11
        self.spawned = []
        self.rewards = 0
    def round(self): return 2
    def curCamp(self): return 0
    def unitStateTest(self, *args): return False
    def isInRect(self, uid, x1, y1, x2, y2): return x1 <= self.x <= x2 and y1 <= self.y <= y2
    def isInPos(self, uid, x, y): return (self.x, self.y) == (x, y)
    def createEnemy(self, units): self.spawned.extend(u['id'] for u in units)
    def reward(self): self.rewards += 1
    def __getattr__(self, name): return lambda *args: None

class EscapeTest(unittest.TestCase):
    def test_progress_waves_are_once_only_and_exit_is_distant(self):
        campaign = json.loads((ROOT/'content/examples/linchong/campaign.json').read_text())
        battle = campaign['battles'][0]
        stage = Stage()
        flags = collections.defaultdict(int)
        for event in battle['events']:
            flags['linchong.event_'+event['id'].lower()] = 1
        for key in ('e04', 'e08', 'e09'):
            flags['linchong.event_b01_'+key] = 0
        flags['linchong.b01_rescued'] = 1
        namespace = dict(stage=stage, gvars=flags, vars=collections.defaultdict(int))
        exec((ROOT/'content/scenarios/S_90.py').read_text(), namespace)
        for x, count in [(10,0),(11,2),(11,2),(18,2),(19,4),(19,4),(26,4),(27,7),(27,7)]:
            stage.x = x
            self.assertFalse(namespace['scene1']())
            self.assertEqual(count, len(stage.spawned))
        self.assertEqual(7, len(set(stage.spawned)))
        stage.x = 14  # Old exit no longer ends the battle.
        self.assertFalse(namespace['win_condition']())
        stage.x = 34
        flags['linchong.b01_rescued'] = 0
        self.assertFalse(namespace['win_condition']())
        flags['linchong.b01_rescued'] = 1
        self.assertTrue(namespace['scene1']())
        self.assertEqual(1, stage.rewards)
        grid = json.loads((ROOT/'content/assets/maps/hexmaps/58.json').read_text())
        self.assertEqual((36,24),(grid['width'],grid['height']))
        self.assertTrue(all(grid['data'][11][x] == 0 for x in range(36)))

if __name__ == '__main__': unittest.main()
