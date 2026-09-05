#!/usr/bin/env python3
"""Audit Kotlin declarations whose physical span exceeds a configured limit.

This is intentionally a small, read-only source audit.  It masks comments and
Kotlin string/character literals before looking for declarations, then matches
braces in the masked text.  That keeps braces in examples, URLs, and strings
from changing the reported end line without pretending to be a Kotlin parser.
"""

from __future__ import annotations

import argparse
from dataclasses import dataclass
from pathlib import Path
import re
import sys
from typing import Iterable, Sequence


DEFAULT_ROOT_NAMES = (
    "core/src/main/kotlin",
    "desktop/src/main/kotlin",
    "verification/src/main/kotlin",
    "android/src/main/kotlin",
)
DECLARATION_RE = re.compile(
    r"(?m)^[ \t]*(?:(?:public|private|protected|internal|final|open|abstract|"
    r"sealed|data|enum|annotation|inner|value|suspend|inline|tailrec|operator|"
    r"infix|override|lateinit|const|external|expect|actual)\s+)*"
    r"(?P<kind>class|interface|object|fun|typealias|val|var)\b"
)


@dataclass(frozen=True)
class Declaration:
    path: Path
    kind: str
    name: str
    start_line: int
    end_line: int

    @property
    def span(self) -> int:
        return self.end_line - self.start_line + 1


def mask_kotlin(text: str) -> str:
    """Replace comments and literals with spaces while preserving newlines."""
    out = list(text)
    i = 0
    state = "code"
    while i < len(text):
        c = text[i]
        n = text[i + 1] if i + 1 < len(text) else ""
        if state == "code":
            if c == "/" and n == "/":
                out[i] = out[i + 1] = " "
                i += 2
                state = "line_comment"
                continue
            if c == "/" and n == "*":
                out[i] = out[i + 1] = " "
                i += 2
                state = "block_comment"
                continue
            if c == '"' and text[i : i + 3] == '"""':
                out[i : i + 3] = [" "] * 3
                i += 3
                state = "triple"
                continue
            if c == '"':
                out[i] = " "
                i += 1
                state = "string"
                continue
            if c == "'":
                out[i] = " "
                i += 1
                state = "char"
                continue
            i += 1
        elif state == "line_comment":
            if c == "\n":
                state = "code"
            else:
                out[i] = " "
            i += 1
        elif state == "block_comment":
            if c == "*" and n == "/":
                out[i] = out[i + 1] = " "
                i += 2
                state = "code"
            else:
                if c != "\n":
                    out[i] = " "
                i += 1
        elif state == "triple":
            if text[i : i + 3] == '"""':
                out[i : i + 3] = [" "] * 3
                i += 3
                state = "code"
            else:
                if c != "\n":
                    out[i] = " "
                i += 1
        else:  # ordinary string or character literal
            if c == "\\":
                out[i] = " "
                if i + 1 < len(text) and text[i + 1] != "\n":
                    out[i + 1] = " "
                    i += 2
                else:
                    i += 1
            elif (state == "string" and c == '"') or (state == "char" and c == "'"):
                out[i] = " "
                i += 1
                state = "code"
            else:
                if c != "\n":
                    out[i] = " "
                i += 1
    return "".join(out)


def brace_pairs(masked: str) -> dict[int, int]:
    stack: list[int] = []
    pairs: dict[int, int] = {}
    for index, char in enumerate(masked):
        if char == "{":
            stack.append(index)
        elif char == "}" and stack:
            pairs[stack.pop()] = index
    return pairs


def _line_number(text: str, offset: int) -> int:
    return text.count("\n", 0, offset) + 1


def declarations_in_text(text: str, path: Path = Path("<string>")) -> list[Declaration]:
    masked = mask_kotlin(text)
    pairs = brace_pairs(masked)
    declarations: list[Declaration] = []
    matches = [m for m in DECLARATION_RE.finditer(masked) if masked[: m.start()].count("(") == masked[: m.start()].count(")")]
    for match_index, match in enumerate(matches):
        start = match.start()
        start_line = _line_number(text, start)
        name_match = re.search(r"\b([A-Za-z_]\w*)", masked[match.end() :])
        name = name_match.group(1) if name_match else "<anonymous>"
        # A declaration's body is the first brace after its header.  If there
        # is no body, retain the declaration's physical header span.
        next_declaration = matches[match_index + 1].start() if match_index + 1 < len(matches) else len(masked)
        brace = masked.find("{", match.end(), next_declaration)
        end = pairs.get(brace) if brace >= 0 else None
        end_line = _line_number(text, end) if end is not None else start_line
        declarations.append(Declaration(path, match.group("kind"), name, start_line, end_line))
    return declarations


def iter_kotlin_files(roots: Iterable[Path]) -> Iterable[Path]:
    for root in roots:
        if root.is_file() and root.suffix == ".kt":
            yield root
        elif root.is_dir():
            yield from sorted(root.rglob("*.kt"))


def audit_paths(roots: Sequence[Path], threshold: int = 300) -> list[Declaration]:
    findings: list[Declaration] = []
    for path in iter_kotlin_files(roots):
        findings.extend(d for d in declarations_in_text(path.read_text(encoding="utf-8"), path) if d.span > threshold)
    return sorted(findings, key=lambda d: (str(d.path), d.start_line, d.kind))


def main(argv: Sequence[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("roots", nargs="*", type=Path, help="Kotlin source roots or files")
    parser.add_argument("--source-root", action="append", type=Path, dest="source_roots", help="additional root (repeatable)")
    parser.add_argument("--threshold", type=int, default=300, help="maximum allowed physical lines (default: 300)")
    args = parser.parse_args(argv)
    base = Path(__file__).resolve().parents[1]
    if args.roots:
        roots = list(args.roots) + (args.source_roots or [])
    else:
        roots = list(args.source_roots) if args.source_roots else [base / name for name in DEFAULT_ROOT_NAMES]
    findings = audit_paths(roots, args.threshold)
    for finding in findings:
        print(f"{finding.path}:{finding.start_line}-{finding.end_line} ({finding.span} lines) {finding.kind} {finding.name}")
    print(f"KOTLIN_DECLARATION_SPAN_AUDIT findings={len(findings)} threshold={args.threshold} roots={len(roots)}", file=sys.stderr)
    return 1 if findings else 0


if __name__ == "__main__":
    raise SystemExit(main())
