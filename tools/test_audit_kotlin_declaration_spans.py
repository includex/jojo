import tempfile
import unittest
from pathlib import Path

from audit_kotlin_declaration_spans import audit_paths, declarations_in_text, mask_kotlin


class KotlinDeclarationSpanTest(unittest.TestCase):
    def test_comments_and_literals_do_not_contribute_braces(self) -> None:
        source = '''class Sample {
    // } this is not the end
    val url = "https://example.test/{not-a-brace}"
    val block = """text { still a string }"""
    fun method() {
        println("}")
    }
}
'''
        declarations = declarations_in_text(source, Path("Sample.kt"))
        self.assertEqual([(d.kind, d.name, d.span) for d in declarations], [
            ("class", "Sample", 8),
            ("val", "url", 1),
            ("val", "block", 1),
            ("fun", "method", 3),
        ])

    def test_multiline_declaration_and_nested_declaration_are_reported(self) -> None:
        source = "class Outer(\n    val x: Int\n) {\n" + "\n".join("    // filler" for _ in range(4)) + "\n    fun inner() {\n" + "\n".join("        Unit" for _ in range(3)) + "\n    }\n}\n"
        declarations = declarations_in_text(source)
        self.assertEqual((declarations[0].name, declarations[0].start_line, declarations[0].end_line), ("Outer", 1, 13))
        self.assertEqual((declarations[1].name, declarations[1].start_line, declarations[1].end_line), ("inner", 8, 12))

    def test_audit_supports_file_and_directory_roots(self) -> None:
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            source = root / "Long.kt"
            source.write_text("class Long {\n" + "\n".join("    val x = 1" for _ in range(5)) + "\n}\n", encoding="utf-8")
            self.assertEqual(audit_paths([source], threshold=3)[0].name, "Long")
            self.assertEqual(audit_paths([root], threshold=20), [])

    def test_mask_preserves_line_count(self) -> None:
        source = '/* { */\n"""{\n}"""\n'
        self.assertEqual(mask_kotlin(source).count("\n"), source.count("\n"))


if __name__ == "__main__":
    unittest.main()
