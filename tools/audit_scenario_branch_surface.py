#!/usr/bin/env python3
"""Inventory source-level scenario paths that one deterministic replay misses.

This is deliberately a static coverage *inventory*, not a claim that the
listed paths are executable under every campaign state.  It reads the restored
Python R_/S_ scripts and records choice, RNG and script-host calls by source
location, so branch expansion can be selected from evidence.
"""

from __future__ import annotations

import ast
import json
import sys
from collections import Counter
from pathlib import Path
from typing import Any


def dotted_name(node: ast.AST) -> str | None:
    if isinstance(node, ast.Name):
        return node.id
    if isinstance(node, ast.Attribute):
        base = dotted_name(node.value)
        return f"{base}.{node.attr}" if base else node.attr
    return None


def choice_option_count(call: ast.Call) -> int | None:
    if not call.args or not isinstance(call.args[0], ast.Constant) or not isinstance(call.args[0].value, str):
        return None
    return len(call.args[0].value.split("\n"))


class SurfaceVisitor(ast.NodeVisitor):
    def __init__(self, module: str) -> None:
        self.module = module
        self.function = "<module>"
        self.choices: list[dict[str, Any]] = []
        self.random: list[dict[str, Any]] = []
        self.host_calls: Counter[str] = Counter()
        self.guards: list[str] = []

    def visit_FunctionDef(self, node: ast.FunctionDef) -> None:
        previous = self.function
        self.function = node.name
        self.generic_visit(node)
        self.function = previous

    def visit_Call(self, node: ast.Call) -> None:
        name = dotted_name(node.func)
        if name == "stage.choice":
            self.choices.append({
                "module": self.module,
                "function": self.function,
                "line": node.lineno,
                "options": choice_option_count(node),
                "guards": list(self.guards),
            })
        elif name and name.endswith(".random"):
            self.random.append({"module": self.module, "function": self.function, "line": node.lineno, "call": name, "guards": list(self.guards)})
        elif name and (name.startswith("stage.") or name.startswith("model.")):
            self.host_calls[f"{self.module}:{self.function}:{name}"] += 1
        self.generic_visit(node)

    def visit_If(self, node: ast.If) -> None:
        condition = ast.unparse(node.test)
        # Calls embedded in a predicate (notably model.random()) are part of
        # the branch surface too; preserve their enclosing guard context.
        self.visit(node.test)
        self.guards.append(condition)
        for statement in node.body:
            self.visit(statement)
        self.guards.pop()
        if node.orelse:
            self.guards.append(f"not ({condition})")
            for statement in node.orelse:
                self.visit(statement)
            self.guards.pop()


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: audit_scenario_branch_surface.py SOURCE_DIR OUTPUT_JSON")
    source_dir = Path(sys.argv[1]).resolve()
    output_path = Path(sys.argv[2]).resolve()
    scripts = sorted(path for path in source_dir.glob("[RS]_*.py") if path.stem[2:].isdigit())
    if not scripts:
        raise SystemExit(f"no R_/S_ scripts found in {source_dir}")
    choices: list[dict[str, Any]] = []
    random: list[dict[str, Any]] = []
    host_calls: Counter[str] = Counter()
    for path in scripts:
        visitor = SurfaceVisitor(path.stem)
        visitor.visit(ast.parse(path.read_text(encoding="utf-8"), filename=str(path), feature_version=(3, 9)))
        choices.extend(visitor.choices)
        random.extend(visitor.random)
        host_calls.update(visitor.host_calls)
    guard_expressions = [guard for entry in [*choices, *random] for guard in entry["guards"]]
    guard_primitives = Counter()
    for expression in guard_expressions:
        for primitive in (
            "vars",
            "gvars",
            "pvars",
            "stage.round",
            "stage.curCamp",
            "stage.maxRound",
            "stage.totalUnit",
            "stage.totalRectUnit",
            "stage.unitStateTest",
            "stage.isNear",
            "stage.isInPos",
            "stage.isInRect",
            "stage.unitClickTest",
            "stage.winTest",
            "stage.loseTest",
        ):
            if primitive in expression:
                guard_primitives[primitive] += 1
    payload = {
        "format": "jojo-scenario-branch-surface/v1",
        "source": str(source_dir),
        "scripts": len(scripts),
        "choices": choices,
        "randomCalls": random,
        "hostCalls": [{"site": site, "count": count} for site, count in sorted(host_calls.items())],
        "summary": {
            "choiceSites": len(choices),
            "choiceOptionPaths": sum(choice["options"] or 0 for choice in choices),
            "randomSites": len(random),
            "hostCallSites": len(host_calls),
            "scriptsWithChoices": len({choice["module"] for choice in choices}),
            "scriptsWithRandom": len({call["module"] for call in random}),
            "choiceSitesByFunction": dict(sorted(Counter(choice["function"] for choice in choices).items())),
            "randomSitesByFunction": dict(sorted(Counter(call["function"] for call in random).items())),
            "guardPrimitiveOccurrences": dict(sorted(guard_primitives.items())),
        },
    }
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    summary = payload["summary"]
    print(
        "SCENARIO_BRANCH_SURFACE_OK "
        f"scripts={payload['scripts']} choices={summary['choiceSites']} "
        f"choicePaths={summary['choiceOptionPaths']} random={summary['randomSites']} "
        f"hostCalls={summary['hostCallSites']}"
    )


if __name__ == "__main__":
    main()
