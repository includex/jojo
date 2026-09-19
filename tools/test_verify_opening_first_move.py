import copy
import unittest

from verify_opening_first_move import source_contract, verify


class OpeningFirstMoveTest(unittest.TestCase):
    def setUp(self):
        def actor(elapsed, completed=False):
            return dict(id=181, x=40, y=15 if completed else 5,
                        visualX=40., visualY=5 + 25 * elapsed,
                        direction=2, action=20, visible=True,
                        moveElapsed=0 if completed else elapsed, moveDuration=.4)
        self.trace = dict(
            format="jojo-campaign-screen-e2e/v1", completion="checkpoint",
            actualStopPoint=dict(module="R_00", sceneIndex=1),
            inputRecords=[dict(event="TitleScreen:new-game-click", accepted=True)],
            scenarioFrames=[dict(time=t, actors=[actor(t)]) for t in (0, .1, .2, .3)] +
                           [dict(time=.4, actors=[actor(.4, True), dict(id=0, visible=True), dict(id=157, visible=True)])],
        )

    def test_source_motion_and_commit_boundary(self):
        self.assertEqual(verify(self.trace)["observedMovingFrames"], 4)

    def test_rejects_frozen_reversed_early_committed_or_early_resumed_motion(self):
        changes = [
            lambda t: t["scenarioFrames"][2]["actors"][0].update(visualY=7.5),
            lambda t: t["scenarioFrames"][2]["actors"][0].update(direction=0),
            lambda t: t["scenarioFrames"][2]["actors"][0].update(y=15),
            lambda t: t["scenarioFrames"][-1]["actors"][0].update(y=5),
            lambda t: t["scenarioFrames"][-1].update(time=.39),
            lambda t: t["scenarioFrames"][-1]["actors"][1].update(visible=False),
            lambda t: t.update(scenarioFrames=[]),
        ]
        for change in changes:
            trace = copy.deepcopy(self.trace)
            change(trace)
            with self.assertRaises(AssertionError):
                verify(trace)

    def test_source_control_flow_cannot_be_silently_discarded(self):
        prefix = "def scene1():\n    stage.showUnit(181,40,5,2)\n"
        for line in ["return", "flag = False", "helper()"]:
            with self.assertRaises(AssertionError):
                source_contract(prefix + f"    {line}\n    stage.unit(181).move(40,15,2)\n",
                                "cc.moveTo(.04 * o, u.x, u.y)")


if __name__ == "__main__":
    unittest.main()
