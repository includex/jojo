import pathlib
import re
import unittest


PROJECT_ROOT = pathlib.Path(__file__).resolve().parents[1]
DOMAIN_ROOT = PROJECT_ROOT / "core/src/main/kotlin/com/jojo/game/domain"
FORBIDDEN_DOMAIN_IMPORT = re.compile(
    r"^import (?:com\.badlogic\.gdx|com\.jojo\.game\.(?:application|infrastructure|presentation))(?:\.|$)",
    re.MULTILINE,
)


class PackageBoundaryTest(unittest.TestCase):
    def test_domain_does_not_depend_on_framework_or_outer_layers(self) -> None:
        violations: list[str] = []
        for source in sorted(DOMAIN_ROOT.rglob("*.kt")):
            for match in FORBIDDEN_DOMAIN_IMPORT.finditer(source.read_text(encoding="utf-8")):
                line = source.read_text(encoding="utf-8")[: match.start()].count("\n") + 1
                violations.append(f"{source.relative_to(PROJECT_ROOT)}:{line}: {match.group(0)}")

        self.assertEqual([], violations, "Domain dependency violations:\n" + "\n".join(violations))


if __name__ == "__main__":
    unittest.main()
