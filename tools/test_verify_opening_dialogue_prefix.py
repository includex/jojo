import copy
import unittest

from verify_opening_dialogue_prefix import source_pages, verify


class OpeningDialoguePrefixTest(unittest.TestCase):
    def setUp(self):
        self.expected = source_pages("""def scene1():
    stage.delay(3)
    stage.say('&181\\n대장님, 서둘러야 해요!\\n&0\\n알아!')
    stage.say('&157\\n잠시만 기다려 주세요!')
""")
        self.trace = {
            "format": "jojo-campaign-screen-e2e/v1", "completion": "checkpoint",
            "actualStopPoint": {"module": "R_00", "sceneIndex": 1},
            "stopDialoguePages": 3,
            "inputRecords": [{"event": "TitleScreen:new-game-click", "accepted": True}],
            "dialoguePages": [dict(page, module="R_00", sceneIndex=1, revision=i)
                              for i, page in enumerate(self.expected)],
        }

    def test_source_speaker_boundary_and_matching_prefix(self):
        self.assertEqual([p["speakerId"] for p in self.expected], ["181", "0", "157"])
        self.assertEqual(verify(self.trace, self.expected)["verifiedPages"], 3)

    def test_rejects_wrong_speaker_text_missing_page_and_duplicate_revision(self):
        mutations = [
            lambda t: t["dialoguePages"][0].update(speakerId="0"),
            lambda t: t["dialoguePages"][1].update(text="wrong"),
            lambda t: t["dialoguePages"].pop(),
            lambda t: t["dialoguePages"][1].update(revision=0),
            lambda t: t.update(inputRecords=[]),
        ]
        for mutate in mutations:
            trace = copy.deepcopy(self.trace)
            mutate(trace)
            with self.assertRaises(AssertionError):
                verify(trace, self.expected)

    def test_does_not_guess_through_source_branch(self):
        with self.assertRaises(AssertionError):
            source_pages("def scene1():\n    if flag:\n        stage.say('&0\\nhello')\n")

    def test_does_not_skip_unknown_calls_or_scene_jumps(self):
        for call in ["stage.jumpScene(2)", "helperDialogue()"]:
            with self.assertRaises(AssertionError):
                source_pages(f"def scene1():\n    {call}\n")


if __name__ == "__main__":
    unittest.main()
