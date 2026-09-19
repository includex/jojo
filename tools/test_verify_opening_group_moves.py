import copy
import unittest

from verify_opening_group_moves import sample, verify


class OpeningGroupMovesTest(unittest.TestCase):
    def setUp(self):
        self.source = {"format": "jojo-r00-opening-source-moves/v1", "evidenceKind": "recovered-source-runtime", "groups": [], "delaySeconds": .3}
        frames = []
        for g, ids in enumerate(((181,), (181, 0), (181, 0, 157))):
            actors = [dict(id=i, origin=[0, g], destination=[0, g + 1], finalDirection=2, duration=.3,
                           segments=[dict(start=0, duration=.3, **{"from": [0, g], "to": [0, g + 1]}, direction=2)]) for i in ids]
            self.source["groups"].append(dict(actors=actors, duration=.3))
            for t in (0, .1, .2):
                frames.append(dict(time=round(g * .3 + t, 5), playback="DELAY", actors=[
                    dict(id=a["id"], x=0, y=g, visualX=0, visualY=g + t / .3, direction=2,
                         action=20, visible=True, moveElapsed=t, moveDuration=.3) for a in actors]))
        for t in (.9, 1., 1.1, 1.2):
            frames.append(dict(time=t, playback="DIALOGUE" if t == 1.2 else "DELAY", actors=[
                dict(id=i, x=0, y=3, visualX=0, visualY=3, direction=2, action=0, visible=True,
                     moveElapsed=.3, moveDuration=0) for i in (181, 0, 157)]))
        self.trace = dict(format="jojo-campaign-screen-e2e/v1", completion="checkpoint",
                          actualStopPoint=dict(module="R_00", sceneIndex=1), stopDialoguePages=1,
                          inputRecords=[dict(event="TitleScreen:new-game-click", accepted=True)], scenarioFrames=frames)

    def test_groups_and_wait(self):
        self.assertEqual(verify(self.trace, self.source)["groupFrameCounts"], [3, 3, 3])

    def test_wrong_motion_direction_commit_and_delay_fail(self):
        changes = [
            lambda t: t["scenarioFrames"][7]["actors"][1].update(visualX=1),
            lambda t: t["scenarioFrames"][7]["actors"][1].update(direction=0),
            lambda t: t["scenarioFrames"][7]["actors"][1].update(y=3),
            lambda t: t["scenarioFrames"][7]["actors"][1].update(moveDuration=.4),
            lambda t: t["scenarioFrames"][-2].update(playback="DIALOGUE"),
            lambda t: t["scenarioFrames"][9]["actors"][1].update(visualX=1),
            lambda t: t["scenarioFrames"][9]["actors"][1].update(direction=0),
            lambda t: t["scenarioFrames"][9]["actors"][1].update(action=20),
            lambda t: t["scenarioFrames"][9]["actors"][1].update(visible=False),
            lambda t: t["scenarioFrames"][7].update(playback="DIALOGUE"),
        ]
        for change in changes:
            trace = copy.deepcopy(self.trace)
            change(trace)
            with self.assertRaises(AssertionError):
                verify(trace, self.source)

    def test_source_corner_segment_is_not_replaced_with_grid_edge_interpolation(self):
        actor = dict(duration=.4, destination=[1, 2], finalDirection=2, segments=[
            dict(start=0, duration=.3, **{"from": [0, 0], "to": [1, 2]}, direction=2),
            dict(start=.3, duration=.1, **{"from": [1, 2], "to": [1, 2]}, direction=1)])
        self.assertEqual(sample(actor, .15), ([.5, 1.], 2, True))
        self.assertEqual(sample(actor, .35), ([1., 2.], 1, True))


if __name__ == "__main__":
    unittest.main()
