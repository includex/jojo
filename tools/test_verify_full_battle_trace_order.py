#!/usr/bin/env python3
import unittest

from verify_full_battle_trace_order import (
    build_report, canonical_episode_frames, compact_boundary_sequence, control_sequence,
    dialogue_content_sequence, dialogue_sequence, extract_episodes, load_trace,
)


def unit(character, camp, x, y, hp, action, *, visible=1, exists=1, visual=None,
         statuses=None, status_rounds=None):
    meta = {"visual": visual or [x, y]}
    if statuses is not None:
        meta["statuses"] = statuses
    if status_rounds is not None:
        meta["statusRounds"] = status_rounds
    return [character, character, camp, x, y, hp, 20, 1, action, visible, exists, 0, 0, 0,
            f"anime{action}_1", 0, None, meta]


def trace(engine, frames, camera=True):
    rows = []
    for index, frame in enumerate(frames):
        row = {"f": index, "t": index * (0.1 if engine == "cocos-original" else 0.8),
               "round": 1, "camp": 2, "dialogue": 0,
               "dialogueRevision": 0, "dialogueIdentity": "",
               "dialogueSpeakerId": "", "dialogueText": "", "units": frame}
        if camera:
            row["camera"] = [index if index < 2 else 1, 0]
        rows.append(row)
    return {"format": "jojo-yingchuan-full-battle-trace/v1", "engine": engine,
            "config": {"toolSeed": 1000, "mathSeed": 0x12345678},
            "frames": rows, "events": [], "rng": [], "summary": {"end": True}}


class FullBattleOrderVerifierTest(unittest.TestCase):
    def test_dialogue_revision_preserves_same_tick_close_and_reopen(self):
        observed = trace("libgdx-port", [[], [], []])
        observed["frames"][0].update(dialogue=1, dialogueRevision=1, dialogueIdentity="210:a")
        observed["frames"][1].update(dialogue=1, dialogueRevision=2, dialogueIdentity="211:b")
        observed["frames"][2].update(dialogue=0, dialogueRevision=2, dialogueIdentity="")
        observed["frames"][0]["f"] = observed["frames"][1]["f"] = 7

        self.assertEqual(["open", "close", "open", "close"], dialogue_sequence(observed))

    def test_dialogue_content_normalizes_source_br_and_port_newlines(self):
        source = trace("cocos-original", [[], []])
        source["frames"][0].update(
            dialogue=1, dialogueRevision=1, dialogueIdentity="source-layer-1",
            dialogueSpeakerId="3", dialogueText="첫 줄<br/>둘째 줄",
        )
        source["frames"][1].update(
            dialogue=1, dialogueRevision=1, dialogueIdentity="source-layer-1",
            dialogueSpeakerId="33", dialogueText="첫 줄<br/>둘째 줄",
        )
        port = trace("libgdx-port", [[], []])
        port["frames"][0].update(
            dialogue=1, dialogueRevision=1, dialogueIdentity="port-call-1",
            dialogueSpeakerId="3", dialogueText="첫 줄\n둘째 줄",
        )
        port["frames"][1].update(
            dialogue=1, dialogueRevision=1, dialogueIdentity="port-call-1",
            dialogueSpeakerId="33", dialogueText="첫 줄\n둘째 줄",
        )

        self.assertEqual([("3", "첫 줄\n둘째 줄")], dialogue_content_sequence(source))
        self.assertIsNone(build_report(source, port)["dialogueContentMismatch"])

    def test_asymmetric_dialogue_identity_is_a_capability_gap_not_a_sequence_failure(self):
        source = trace("cocos-original", [[], [], []])
        source["frames"][0].update(dialogue=1)
        source["frames"][1].update(dialogue=1)
        source["frames"][2].update(dialogue=0)
        port = trace("libgdx-port", [[], [], []])
        port["frames"][0].update(dialogue=1, dialogueRevision=1, dialogueIdentity="210:a")
        port["frames"][1].update(dialogue=1, dialogueRevision=2, dialogueIdentity="211:b")
        port["frames"][2].update(dialogue=0, dialogueRevision=2, dialogueIdentity="")

        report = build_report(source, port)

        self.assertIsNone(report["dialogueSequenceMismatch"])
        self.assertIsNotNone(report["dialogueComparisonBlocked"])
        self.assertFalse(report["passed"])
        self.assertIn(
            "dialogueIdentity",
            [blocker["observation"] for blocker in report["comparisonBlockers"]],
        )

    def test_unobserved_round_boundary_camp_is_not_fabricated(self):
        observed = trace("libgdx-port", [[], []])
        observed["frames"][0].update(round=2, camp=3)
        observed["frames"][1].update(round=3, camp=0)

        self.assertEqual([[2, 3], [3, 0]], compact_boundary_sequence(observed))
        self.assertEqual(
            ["camp:2:3", "camp:3:0"],
            control_sequence(observed, include_dialogue=False),
        )

    def test_same_renderer_frame_camp_micro_samples_keep_order(self):
        observed = trace("cocos-original", [[], [], []])
        observed["frames"][0].update(f=9, round=2, camp=3)
        observed["frames"][1].update(f=9, round=3, camp=3)
        observed["frames"][2].update(f=9, round=3, camp=0)

        self.assertEqual([[2, 3], [3, 3], [3, 0]], compact_boundary_sequence(observed))

    def test_same_renderer_frame_action_micro_samples_form_an_episode(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21)],
            [unit(10, 2, 1, 1, 100, 0)],
        ]
        observed = trace("cocos-original", frames)
        for frame in observed["frames"]:
            frame["f"] = 12

        episodes = extract_episodes(observed)

        self.assertEqual(1, len(episodes))
        self.assertEqual(21, episodes[0].action)

    def test_redundant_action_observations_defer_move_end_until_tile_commit(self):
        frames = [
            [unit(210, 2, 10, 17, 100, 0, visual=[10, 17])],
            [unit(210, 2, 10, 17, 100, 20, visual=[10, 17])],
            [unit(210, 2, 10, 17, 100, 20, visual=[10, 17])],
            [unit(210, 2, 10, 17, 100, 20, visual=[10, 16.2])],
            [unit(210, 2, 10, 17, 100, 0, visual=[10, 16])],
            [unit(210, 2, 10, 16, 100, 0, visual=[10, 16])],
        ]
        observed = trace("cocos-original", frames)
        observed["frames"][1].update(f=10, observation="transition:action:210")
        observed["frames"][2]["f"] = 11
        observed["frames"][3]["f"] = 12
        observed["frames"][4].update(f=12, observation="transition:action:210")
        observed["frames"][5]["f"] = 13

        canonical = canonical_episode_frames(observed["frames"])
        episodes = extract_episodes(observed)

        self.assertEqual([0, 2, 3, 5], [raw_index for raw_index, _ in canonical])
        self.assertEqual(1, len(episodes))
        self.assertEqual({"from": [10, 17], "to": [10, 16], "direction": 1,
                          "visualInterpolation": True}, episodes[0].movement)
        self.assertEqual([2, 5], [episodes[0].start, episodes[0].end])

    def test_superseded_idle_hook_does_not_close_move_before_commit(self):
        frames = [
            [unit(478, 2, 9, 14, 100, 20, visual=[9, 13.2])],
            [unit(478, 2, 9, 14, 100, 0, visual=[7, 13])],
            [unit(478, 2, 7, 13, 100, 39, visual=[7, 13])],
            [unit(478, 2, 7, 13, 100, 39, visual=[7, 13])],
        ]
        observed = trace("cocos-original", frames)
        observed["frames"][0]["f"] = 30
        observed["frames"][1].update(f=30, observation="transition:action:478")
        observed["frames"][2].update(f=30, observation="transition:action:478")
        observed["frames"][3]["f"] = 31

        canonical = canonical_episode_frames(observed["frames"])
        episodes = extract_episodes(observed)

        self.assertEqual([0, 3], [raw_index for raw_index, _ in canonical])
        self.assertEqual(1, len(episodes))
        self.assertEqual([7, 13], episodes[0].movement["to"])

    def test_next_actor_focus_is_not_owned_by_closing_episode(self):
        idle_147 = unit(147, 0, 10, 11, 100, 0)
        focus_147 = unit(147, 0, 10, 11, 100, 39)
        frames = [
            [unit(33, 2, 13, 1, 100, 21), idle_147],
            [unit(33, 2, 13, 1, 100, 0), idle_147],
            [unit(33, 2, 13, 1, 100, 0), focus_147],
            [unit(33, 2, 13, 1, 100, 0), focus_147],
        ]
        observed = trace("cocos-original", frames)
        observed["frames"][0].update(f=40, camera=[0, 0])
        observed["frames"][1].update(f=40, camera=[0, 0], observation="transition:action:33")
        observed["frames"][2].update(f=40, camera=[100, 0], observation="transition:action:147")
        observed["frames"][3].update(f=41, camera=[100, 0])

        episodes = extract_episodes(observed)

        self.assertEqual(1, len(episodes))
        self.assertEqual({"from": [0.0, 0.0], "to": [0.0, 0.0], "changed": False}, episodes[0].camera)
        self.assertFalse(any(
            marker.startswith("camera:")
            for bucket in episodes[0].buckets for marker in bucket
        ))

    def test_action_wholly_between_regular_frames_remains_an_episode(self):
        frames = [
            [unit(210, 2, 10, 17, 100, 0)],
            [unit(210, 2, 10, 17, 100, 21)],
            [unit(210, 2, 10, 17, 100, 0)],
            [unit(210, 2, 10, 17, 100, 0)],
        ]
        observed = trace("cocos-original", frames)
        observed["frames"][1].update(f=20, observation="transition:action:210")
        observed["frames"][2].update(f=20, observation="transition:action:210")
        observed["frames"][3]["f"] = 21

        canonical = canonical_episode_frames(observed["frames"])
        episodes = extract_episodes(observed)

        self.assertEqual([0, 1, 3], [raw_index for raw_index, _ in canonical])
        self.assertEqual(1, len(episodes))
        self.assertEqual(21, episodes[0].action)

    def test_missing_dialogue_observations_block_a_would_be_pass(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21)],
            [unit(10, 2, 1, 1, 100, 0)],
        ]

        source, port = trace("cocos-original", frames), trace("libgdx-port", frames)
        for observed in (source, port):
            for frame in observed["frames"]:
                for field in ("dialogueRevision", "dialogueIdentity", "dialogueSpeakerId", "dialogueText"):
                    del frame[field]
        report = build_report(source, port)

        self.assertFalse(report["passed"])
        self.assertEqual(
            ["dialogueIdentity", "dialogueContent"],
            [blocker["observation"] for blocker in report["comparisonBlockers"]],
        )

    def test_placeholder_dialogue_content_does_not_claim_capability(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21)],
            [unit(10, 2, 1, 1, 100, 0)],
        ]
        source, port = trace("cocos-original", frames), trace("libgdx-port", frames)
        for observed in (source, port):
            for frame in observed["frames"]:
                frame.update(dialogue=1, dialogueRevision=1, dialogueIdentity="say-1")

        report = build_report(source, port)

        self.assertFalse(report["passed"])
        self.assertIn(
            "dialogueContent",
            [blocker["observation"] for blocker in report["comparisonBlockers"]],
        )

    def test_non_terminal_trace_cannot_pass(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21)],
            [unit(10, 2, 1, 1, 100, 0)],
        ]
        source, port = trace("cocos-original", frames), trace("libgdx-port", frames)
        for observed in (source, port):
            observed["summary"]["end"] = False
            for frame in observed["frames"]:
                frame.update(dialogueRevision=0, dialogueIdentity="", dialogueSpeakerId="", dialogueText="")

        report = build_report(source, port)

        self.assertFalse(report["passed"])
        self.assertIsNotNone(report["terminalMismatch"])

    def test_seed_difference_cannot_pass(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21)],
            [unit(10, 2, 1, 1, 100, 0)],
        ]
        source, port = trace("cocos-original", frames), trace("libgdx-port", frames)
        port["config"]["toolSeed"] = 999
        for observed in (source, port):
            for frame in observed["frames"]:
                frame.update(dialogueRevision=0, dialogueIdentity="", dialogueSpeakerId="", dialogueText="")

        report = build_report(source, port)

        self.assertFalse(report["passed"])
        self.assertEqual({"toolSeed": [1000, 999]}, report["configMismatch"]["seedDifferences"])

    def test_time_scale_is_ignored_for_same_callback_buckets(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0)],
        ]
        report = build_report(trace("cocos-original", frames), trace("libgdx-port", frames))
        self.assertTrue(report["passed"])
        self.assertEqual(report["commonDecisionEpisodes"], 1)

    def test_damage_before_reaction_is_an_order_mismatch(self):
        source = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0)],
        ]
        port = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0)],
        ]
        report = build_report(trace("cocos-original", source), trace("libgdx-port", port))
        self.assertFalse(report["passed"])
        self.assertTrue(report["orderMismatches"])

    def test_different_tactical_target_is_reported_not_compared_as_equal(self):
        source = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0), unit(30, 0, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0), unit(30, 0, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32), unit(30, 0, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0), unit(30, 0, 3, 1, 100, 0)],
        ]
        port = [
            source[0], source[1],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0), unit(30, 0, 3, 1, 75, 32)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0), unit(30, 0, 3, 1, 75, 0)],
        ]
        report = build_report(trace("cocos-original", source), trace("libgdx-port", port))
        self.assertEqual(report["commonDecisionEpisodes"], 0)
        self.assertTrue(report["decisionDivergences"])
        self.assertTrue(report["insufficientCommonCoverage"])

    def test_extra_tactical_episode_fails_even_when_common_coverage_exists(self):
        common = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0), unit(30, 2, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0), unit(30, 2, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32), unit(30, 2, 3, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0), unit(30, 2, 3, 1, 100, 0)],
        ]
        port = common + [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0), unit(30, 2, 3, 1, 100, 20)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0), unit(30, 2, 4, 1, 100, 20)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0), unit(30, 2, 4, 1, 100, 0)],
        ]

        report = build_report(trace("cocos-original", common), trace("libgdx-port", port))

        self.assertEqual(1, report["commonDecisionEpisodes"])
        self.assertTrue(report["decisionDivergences"])
        self.assertFalse(report["passed"])

    def test_dialogue_content_mismatch_fails_the_gate(self):
        frames = [
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 100, 21), unit(20, 0, 2, 1, 75, 32)],
            [unit(10, 2, 1, 1, 100, 0), unit(20, 0, 2, 1, 75, 0)],
        ]
        source = trace("cocos-original", frames)
        port = trace("libgdx-port", frames)
        for observed, text in ((source, "원본 대사"), (port, "다른 대사")):
            for frame in observed["frames"]:
                frame.update(
                    dialogue=1, dialogueRevision=1, dialogueIdentity="say-1",
                    dialogueSpeakerId="10", dialogueText=text,
                )

        report = build_report(source, port)

        self.assertIsNotNone(report["dialogueContentMismatch"])
        self.assertFalse(report["passed"])

    def test_overlapping_reaction_does_not_become_a_death_target(self):
        source = [
            [unit(10, 2, 1, 1, 0, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 0, 23), unit(20, 0, 2, 1, 100, 32)],
            [unit(10, 2, 1, 1, 0, 23, visible=0), unit(20, 0, 2, 1, 100, 0)],
        ]
        port = [
            [unit(10, 2, 1, 1, 0, 0), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 0, 23), unit(20, 0, 2, 1, 100, 0)],
            [unit(10, 2, 1, 1, 0, 23, visible=0), unit(20, 0, 2, 1, 100, 0)],
        ]

        report = build_report(trace("cocos-original", source), trace("libgdx-port", port))

        self.assertEqual([], report["decisionDivergences"])
        self.assertEqual([], report["orderMismatches"])

    def test_missing_source_camera_is_an_explicit_capability_gap(self):
        frames = [[unit(10, 2, 1, 1, 100, 0)]]
        report = build_report(trace("cocos-original", frames, camera=False), trace("libgdx-port", frames))
        self.assertIn("camera", [gap["observation"] for gap in report["capabilityGaps"]])

    def test_camera_focus_before_episode_is_not_an_episode_callback(self):
        observed = trace("libgdx-port", [
            [unit(1, 1, 0, 0, 100, 0)],
            [unit(1, 1, 0, 0, 100, 20)],
            [unit(1, 1, 0, 0, 100, 20)],
            [unit(1, 1, 1, 0, 100, 0)],
        ])
        for row, camera in zip(observed["frames"], [
            [0, 0], [96, 0], [96, 0], [96, 0],
        ]):
            row["camera"] = camera
        episode = extract_episodes(observed)[0]
        self.assertFalse(any(
            marker.startswith("camera:")
            for bucket in episode.buckets
            for marker in bucket
        ))

    def test_status_state_is_read_from_shared_unit_metadata(self):
        normal = [1] * 15
        rounds = [0] * 15
        poisoned = normal.copy()
        poisoned[10] = 0
        poisoned_rounds = rounds.copy()
        poisoned_rounds[10] = 3
        frames = [
            [unit(10, 2, 1, 1, 100, 0, statuses=normal, status_rounds=rounds)],
            [unit(10, 2, 1, 1, 100, 21, statuses=normal, status_rounds=rounds)],
            [unit(10, 2, 1, 1, 100, 21, statuses=poisoned, status_rounds=poisoned_rounds)],
            [unit(10, 2, 1, 1, 100, 0, statuses=poisoned, status_rounds=poisoned_rounds)],
        ]
        source = trace("cocos-original", frames)
        port = trace("libgdx-port", frames)

        report = build_report(source, port)

        self.assertTrue(report["source"]["capabilities"]["statusState"])
        self.assertTrue(report["port"]["capabilities"]["statusState"])
        self.assertTrue(report["passed"])

    def test_action_point_only_source_does_not_create_false_decision_divergence(self):
        frames = [[unit(10, 2, 1, 1, 100, 0)], [unit(10, 2, 1, 1, 100, 1)]]
        source = trace("cocos-original", frames)
        port_frames = [frames[0], [unit(10, 2, 1, 1, 100, 21)], [unit(10, 2, 1, 1, 100, 0)]]
        report = build_report(source, trace("libgdx-port", port_frames))
        self.assertIsNotNone(report["actionComparisonBlocked"])
        self.assertEqual(report["decisionDivergences"], [])

    def test_interrupted_writer_recovers_complete_frames_without_claiming_pass(self):
        import json
        import tempfile
        from pathlib import Path
        complete = trace("cocos-original", [[unit(10, 2, 1, 1, 100, 0)]])
        prefix = json.dumps({key: value for key, value in complete.items() if key != "frames"})[:-1]
        payload = prefix + ', "frames":[' + json.dumps(complete["frames"][0]) + ','
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "partial.json"
            path.write_text(payload, encoding="utf-8")
            recovered = load_trace(path)
        self.assertEqual(len(recovered["frames"]), 1)
        self.assertEqual(recovered["_partial"]["completeFrames"], 1)
        report = build_report(recovered, complete)
        self.assertFalse(report["passed"])
        self.assertTrue(report["terminalDiagnostics"]["source"]["hangCandidate"])


if __name__ == "__main__":
    unittest.main()
