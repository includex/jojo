import pathlib
import re
import unittest


PROJECT_ROOT = pathlib.Path(__file__).resolve().parents[1]
DOMAIN_ROOT = PROJECT_ROOT / "core/src/main/kotlin/com/jojo/game/domain"
INFRASTRUCTURE_ROOT = PROJECT_ROOT / "core/src/main/kotlin/com/jojo/game/infrastructure"
PRESENTATION_ROOT = PROJECT_ROOT / "core/src/main/kotlin/com/jojo/game/presentation"
FORBIDDEN_DOMAIN_IMPORT = re.compile(
    r"^import (?:com\.badlogic\.gdx|com\.jojo\.game\.(?:application|infrastructure|presentation))(?:\.|$)",
    re.MULTILINE,
)
PRESENTATION_STATE_ACCESS = re.compile(r"\b[A-Za-z_][A-Za-z0-9_]*\.presentation\b")


class PackageBoundaryTest(unittest.TestCase):
    def assert_no_imports(self, root: pathlib.Path, pattern: re.Pattern[str], label: str) -> None:
        violations: list[str] = []
        for source in sorted(root.rglob("*.kt")):
            text = source.read_text(encoding="utf-8")
            for match in pattern.finditer(text):
                line = text[: match.start()].count("\n") + 1
                violations.append(f"{source.relative_to(PROJECT_ROOT)}:{line}: {match.group(0)}")
        self.assertEqual([], violations, f"{label}:\n" + "\n".join(violations))

    def test_domain_does_not_depend_on_framework_or_outer_layers(self) -> None:
        self.assert_no_imports(DOMAIN_ROOT, FORBIDDEN_DOMAIN_IMPORT, "Domain dependency violations")

    def test_domain_does_not_reach_into_entity_presentation_state(self) -> None:
        violations: list[str] = []
        for source in sorted(DOMAIN_ROOT.rglob("*.kt")):
            text = source.read_text(encoding="utf-8")
            for line_number, line in enumerate(text.splitlines(), start=1):
                code = line.split("//", 1)[0]
                if PRESENTATION_STATE_ACCESS.search(code):
                    violations.append(f"{source.relative_to(PROJECT_ROOT)}:{line_number}: {code.strip()}")

        self.assertEqual([], violations, "Domain presentation-state access:\n" + "\n".join(violations))

    def test_infrastructure_does_not_depend_on_application_or_presentation(self) -> None:
        pattern = re.compile(
            r"^import com\.jojo\.game\.(?:application|presentation)(?:\.|$)",
            re.MULTILINE,
        )
        self.assert_no_imports(INFRASTRUCTURE_ROOT, pattern, "Infrastructure dependency violations")

    def test_presentation_does_not_depend_on_infrastructure(self) -> None:
        pattern = re.compile(r"^import com\.jojo\.game\.infrastructure(?:\.|$)", re.MULTILINE)
        self.assert_no_imports(PRESENTATION_ROOT, pattern, "Presentation dependency violations")


if __name__ == "__main__":
    unittest.main()
