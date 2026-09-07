#!/usr/bin/env python3
"""Add baseline KDoc comments to public Kotlin APIs in a given file list."""

import re
import sys
from pathlib import Path

DECL_CLASS_RE = re.compile(
    r"^(?P<indent>\s*)(?P<mods>(?:(?:public|private|protected|internal)\s+)*)((class|interface|object|enum\s+class|data\s+class|sealed\s+class)\s+)(?P<name>[A-Za-z_][A-Za-z0-9_]*)"
)

DECL_FUNC_RE = re.compile(
    r"^(?P<indent>\s*)(?P<mods>(?:(?:public|private|protected|internal)\s+)*)((?:(?:inline|tailrec|suspend|operator|infix|crossinline|noinline)\s+)*)fun\s+(?P<name>[A-Za-z_][A-Za-z0-9_]*)\s*(<[^>]*>)?\s*\((?P<params>[^)]*)\)\s*(?::\s*(?P<ret>[^=\{]+))?"
)

NON_PUBLIC_MODS = {"private", "protected", "internal"}


def previous_nonblank(lines, i):
    j = i - 1
    while j >= 0 and lines[j].strip() == "":
        j -= 1
    return j


def has_direct_doc(lines, i):
    j = previous_nonblank(lines, i)
    if j < 0:
        return False

    if lines[j].lstrip().startswith("/**"):
        return True

    # annotation only lines above declaration
    k = j
    while k >= 0 and lines[k].lstrip().startswith("@"):
        k -= 1
    if k >= 0 and lines[k].lstrip().startswith("/**"):
        return True

    return False


def split_params(raw):
    if not raw.strip():
        return []
    parts = []
    depth_angle = depth_paren = depth_brace = 0
    cur = []
    for ch in raw:
        if ch == "<":
            depth_angle += 1
        elif ch == ">":
            depth_angle = max(0, depth_angle - 1)
        elif ch == "(":
            depth_paren += 1
        elif ch == ")":
            depth_paren = max(0, depth_paren - 1)
        elif ch == "{":
            depth_brace += 1
        elif ch == "}":
            depth_brace = max(0, depth_brace - 1)

        if ch == "," and depth_angle == depth_paren == depth_brace == 0:
            token = "".join(cur).strip()
            if token:
                parts.append(token)
            cur = []
        else:
            cur.append(ch)

    token = "".join(cur).strip()
    if token:
        parts.append(token)
    return parts


def param_docs(raw):
    if not raw.strip():
        return ["- 입력 파라미터: 없음"]

    out = []
    for part in split_params(raw):
        if ":" in part:
            p = part.split(":", 1)[0].strip().split()[-1]
            ptype = part.split(":", 1)[1].strip()
            out.append(f"- `{p}` (`{ptype}`): 구현 기준으로 역할 및 허용 값 정의 필요")
        elif part:
            out.append(f"- `{part}`: 구현 기준으로 역할 및 허용 값 정의 필요")
    return out


def return_type_from_match(m):
    if not m.group("ret"):
        return "Unit"
    ret = m.group("ret").strip()
    if ret.startswith("{"):
        return "Unit"
    return ret.split("=")[0].strip().rstrip(" ")


def doc_for_class(name, kind):
    return [
        "/**",
        f" * {kind} `{name}`",
        " *",
        " * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.",
        " *",
        " * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.",
        " */",
        "",
    ]


def doc_for_func(name, params, ret):
    entries = ["/**", f" * 공개 메서드 `{name}`", " *", " * ### 파라미터"]
    entries.extend(param_docs(params))
    entries.extend([" *", " * ### 응답 스펙", f" * - 반환 타입: `{ret}`", " * - 반환값: 동작 결과의 도메인 값입니다.", " */", ""])
    return entries


def should_process_fun(m):
    mods = (m.group("mods") or "").split()
    return not any(mod in NON_PUBLIC_MODS for mod in mods)


def should_process_class(m):
    mods = (m.group("mods") or "").split()
    return not any(mod in NON_PUBLIC_MODS for mod in mods)


def process_file(path):
    text = Path(path).read_text(encoding="utf-8")
    lines = text.splitlines()
    out = []
    changed = False

    for i, line in enumerate(lines):
        if has_direct_doc(lines, i):
            out.append(line)
            continue

        cls = DECL_CLASS_RE.match(line)
        if cls and should_process_class(cls):
            kind = cls.group(3)
            name = cls.group("name")
            out.extend(doc_for_class(name, kind))
            out.append(line)
            changed = True
            continue

        fn = DECL_FUNC_RE.match(line)
        if fn and should_process_fun(fn):
            name = fn.group("name")
            params = fn.group("params")
            ret = return_type_from_match(fn)
            # skip constructors/getter/setter/operator overloading one-liners
            if name in {"get", "set", "contains", "invoke"} and "(" not in line:
                out.append(line)
                continue
            out.extend(doc_for_func(name, params, ret))
            out.append(line)
            changed = True
            continue

        out.append(line)

    if changed:
        Path(path).write_text("\n".join(out) + "\n", encoding="utf-8")


def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: bulk_kdoc_augment.py <file-list>")

    for line in Path(sys.argv[1]).read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line:
            continue
        process_file(line)


if __name__ == "__main__":
    main()
