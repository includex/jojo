import unittest

from verify_dialogue_window_parity import geometry_matches


class DialogueWindowGeometryTest(unittest.TestCase):
    def row(self, component="panel"):
        return {
            "component": component,
            "source": dict(x=10, y=20, w=100, h=50),
            "game": dict(x=10, y=20, w=100, h=50),
            "delta": dict(x=0, y=0, w=0, h=0),
            "relative": dict(x=0, y=0),
        }

    def test_matching_geometry_passes(self):
        for component in ("panel", "portrait", "speaker", "text"):
            self.assertTrue(geometry_matches(self.row(component), 2))

    def test_whole_window_translation_does_not_cancel_panel_error(self):
        for axis in ("x", "y"):
            row = self.row()
            row["delta"][axis] = 10
            row["game"][axis] += 10
            # Relative panel-to-itself geometry remains zero.
            self.assertFalse(geometry_matches(row, 2))

    def test_width_and_height_changes_fail_for_every_component(self):
        for component in ("panel", "portrait", "speaker", "text"):
            for axis in ("w", "h"):
                row = self.row(component)
                row["game"][axis] += 10
                row["delta"][axis] = 10
                self.assertFalse(geometry_matches(row, 2), (component, axis))

    def test_component_relative_displacement_fails(self):
        for axis in ("x", "y"):
            row = self.row("portrait")
            row["relative"][axis] = -3
            self.assertFalse(geometry_matches(row, 2))

    def test_missing_component_or_nonfinite_geometry_fails(self):
        for key in ("source", "game", "delta", "relative"):
            row = self.row("text")
            row.pop(key)
            self.assertFalse(geometry_matches(row, 2))
        for bad in (float("inf"), float("nan")):
            row = self.row()
            row["delta"]["w"] = bad
            self.assertFalse(geometry_matches(row, 2))


if __name__ == "__main__":
    unittest.main()
