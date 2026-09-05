import json
from pathlib import Path
import tempfile
import unittest

from compare_battle_camp_boundaries import camp_boundaries, canonical_state, compare


def unit(unit_id=10, *, camp=0, x=1, y=2, hp=100, mp=20, direction=3,
         visible=1, acted=0, ai=2, ai_value=7, statuses=None, status_rounds=None,
         growth=None):
    details = {
        "statuses": statuses if statuses is not None else [1] * 15,
        "statusRounds": status_rounds if status_rounds is not None else [0] * 15,
    }
    if growth is not None:
        details["growth"] = growth
    return [-1, unit_id, camp, x, y, hp, mp, direction, 0, visible, 1,
            acted, ai, ai_value, "anime0_0", 0, None, details]


def boundary(round_number, camp, units, end=False):
    return canonical_state({"round": round_number, "camp": camp, "end": end, "units": units})


class BattleCampBoundaryComparatorTest(unittest.TestCase):
    def test_profiles_keep_direction_out_of_tactical_state(self):
        report = compare([boundary(1, 0, [unit(direction=3)])],
                         [boundary(1, 0, [unit(direction=0)])])
        self.assertEqual(0, report["profiles"]["tactical"]["mismatchCount"])
        self.assertEqual(1, report["profiles"]["direction"]["mismatchCount"])
        self.assertEqual(["direction"], report["firstMismatch"]["profiles"])
        direction_first = report["profiles"]["direction"]["firstMismatch"]
        self.assertEqual({"id": 10, "source": 3, "game": 0}, direction_first["units"][0])
        self.assertEqual(0, direction_first["omittedUnits"])

    def test_each_non_tactical_profile_is_counted_independently(self):
        source = [boundary(1, 0, [unit(acted=0, ai=2, ai_value=7)])]
        statuses = [1] * 15
        statuses[10] = 0
        game = [boundary(1, 0, [unit(acted=1, ai=3, ai_value=8,
                                            statuses=statuses)], end=True)]
        report = compare(source, game)
        self.assertEqual(0, report["profiles"]["tactical"]["mismatchCount"])
        for name in ("turnBookkeeping", "aiConfig", "statusRepresentation"):
            self.assertEqual(1, report["profiles"][name]["mismatchCount"])
        self.assertEqual(1, report["endMismatchCount"])
        self.assertEqual(1, report["stateMismatchCount"])
        self.assertEqual(1, report["mismatchCount"])

    def test_first_mismatch_detail_is_capped(self):
        source = [boundary(1, 0, [unit(index, hp=100) for index in range(20)])]
        game = [boundary(1, 0, [unit(index, hp=90) for index in range(20)])]
        report = compare(source, game, max_details=3)
        self.assertEqual(20, report["profiles"]["tactical"]["unitMismatchCount"])
        self.assertEqual(3, len(report["firstMismatch"]["units"]))
        self.assertEqual(17, report["firstMismatch"]["omittedUnits"])
        self.assertEqual(1, len(report["mismatches"]))

    def test_missing_unit_is_only_a_tactical_mismatch(self):
        report = compare([boundary(1, 0, [unit()])], [boundary(1, 0, [])])
        self.assertEqual(1, report["profiles"]["tactical"]["unitMismatchCount"])
        for name in ("direction", "turnBookkeeping", "aiConfig", "statusRepresentation"):
            self.assertEqual(0, report["profiles"][name]["unitMismatchCount"])

    def test_growth_requires_values_on_both_sides(self):
        growth = {"level": 2, "abilities": [10, 20, 30, 40, 50],
                  "posts": 3, "arm": 4, "experience": 99}
        report = compare([boundary(1, 0, [unit(growth=growth)])],
                         [boundary(1, 0, [unit()])])
        self.assertEqual(0, report["profiles"]["growth"]["mismatchCount"])

    def test_growth_difference_is_reported_when_both_profiles_are_complete(self):
        source_growth = {"level": 2, "abilities": [10, 20, 30, 40, 50],
                         "posts": 3, "arm": 4, "experience": 99}
        game_growth = {**source_growth, "level": 3}
        report = compare([boundary(1, 0, [unit(growth=source_growth)])],
                         [boundary(1, 0, [unit(growth=game_growth)])])
        self.assertEqual(1, report["profiles"]["growth"]["mismatchCount"])
        self.assertEqual(1, report["profiles"]["growth"]["unitMismatchCount"])
        first = report["profiles"]["growth"]["firstMismatch"]
        self.assertEqual(2, first["units"][0]["source"]["level"])
        self.assertEqual(3, first["units"][0]["game"]["level"])

    def test_profile_first_mismatch_has_its_own_later_evidence(self):
        source = [
            boundary(1, 0, [unit(acted=0)]),
            boundary(1, 1, [unit(hp=100)]),
        ]
        game = [
            boundary(1, 0, [unit(acted=1)]),
            boundary(1, 1, [unit(hp=90)]),
        ]
        report = compare(source, game, max_details=1)
        self.assertEqual("turnBookkeeping", report["firstMismatch"]["profiles"][0])
        tactical_first = report["profiles"]["tactical"]["firstMismatch"]
        self.assertEqual((1, 1), (tactical_first["round"], tactical_first["camp"]))
        self.assertEqual(100, tactical_first["units"][0]["source"]["hp"])
        self.assertEqual(90, tactical_first["units"][0]["game"]["hp"])
        self.assertFalse(tactical_first["sourceEnd"])
        self.assertFalse(tactical_first["gameEnd"])

    def test_bounded_lookahead_resynchronizes_an_extra_interval(self):
        source = [boundary(1, 0, []), boundary(1, 1, []), boundary(1, 2, [])]
        game = [boundary(1, 0, []), boundary(1, -1, []),
                boundary(1, 1, []), boundary(1, 2, [])]
        report = compare(iter(source), iter(game), lookahead=2)
        self.assertEqual(3, report["commonBoundaries"])
        self.assertEqual(1, report["extraInGameCount"])
        self.assertEqual([[1, -1]], report["extraInGame"])
        self.assertEqual("extraInGame", report["firstMismatch"]["kind"])

    def test_camp_boundaries_yields_only_last_frame_in_each_interval(self):
        frames = [
            {"round": 1, "camp": 0, "units": [unit(hp=100)]},
            {"round": 1, "camp": 0, "units": [unit(hp=90)]},
            {"round": 1, "camp": 1, "units": [unit(hp=80)]},
        ]
        with tempfile.TemporaryDirectory() as root:
            path = Path(root) / "trace.json"
            path.write_text(json.dumps({"frames": frames}), encoding="utf-8")
            stream = camp_boundaries(path)
            self.assertNotIsInstance(stream, list)
            states = list(stream)
        self.assertEqual([90, 80], [state["units"][0]["tactical"]["hp"] for state in states])


if __name__ == "__main__":
    unittest.main()
