#!/usr/bin/env python3
"""Audit lifecycle-sensitive stage calls in every S_*.py and R_*.py script.

This is deliberately a source audit, not a runtime verifier.  It uses Python's
AST (rather than grep) to retain the enclosing function, conditional/loop
branch, source location, draw ordering, and argument expression shape for
every call to the four APIs that still have lifecycle work outstanding.

The recovered JavaScript and ``battle_stage_api_lifecycle_contracts.json`` are
the authority for routing and callback semantics.  Findings are emitted in
the report instead of being filtered out.  The report is useful even when a
route is not covered by the battle-only lifecycle contracts (notably R_*.py).
"""
from __future__ import annotations

import argparse
import ast
import hashlib
import json
import re
from collections import Counter, defaultdict
from pathlib import Path
from typing import Any, Iterable


FORMAT = "jojo-battle-stage-api-lifecycle-call-sites/v1"
TARGET_APIS = (
    "stage.loadBg",
    "stage.setUnitAttr",
    "stage.unit().setPosts",
    "stage.resumeCtrl",
)


# The Python API name is shared by the scenario families, but the original
# object receiving it is not.  Keep this table explicit so a future audit
# cannot accidentally compare HallLayer calls with BattleLayer contracts.
ROUTES: dict[str, dict[str, dict[str, Any]]] = {
    "stage.loadBg": {
        "S": {"owner": "BattleLayer", "file": "battle/BattleLayer.js", "method": "loadBg", "arity": [1]},
        "R": {"owner": "HallLayer", "file": "ui/HallLayer.js", "method": "loadBg", "arity": [2]},
    },
    "stage.setUnitAttr": {
        "S": {"owner": "BattleLayer", "file": "battle/BattleLayer.js", "method": "setUnitAttr", "arity": [3]},
        "R": {"owner": "StageLayer", "file": "ui/StageLayer.js", "method": "setUnitAttr", "arity": [3]},
    },
    "stage.unit().setPosts": {
        "S": {"owner": "BattleUnit", "file": "battle/BattleUnit.js", "method": "setPosts", "arity": [1, 2]},
        "R": None,
    },
    "stage.resumeCtrl": {
        "S": {"owner": "BattleLayer", "file": "battle/BattleLayer.js", "method": "resumeCtrl", "arity": [0]},
        "R": None,
    },
}


def expression_path(node: ast.AST) -> str | None:
    """Return the normalized API path used by the scenario bridge."""
    if isinstance(node, ast.Name):
        return node.id
    if isinstance(node, ast.Attribute):
        base = expression_path(node.value)
        return f"{base}.{node.attr}" if base else None
    if isinstance(node, ast.Call):
        base = expression_path(node.func)
        return f"{base}()" if base else None
    return None


def _unparse(node: ast.AST) -> str:
    try:
        return ast.unparse(node)
    except (AttributeError, ValueError):
        return ast.dump(node, annotate_fields=False, include_attributes=False)


def _truncate(text: str, limit: int = 180) -> str:
    return text if len(text) <= limit else text[: limit - 3] + "..."


def _literal_value(node: ast.AST) -> tuple[str, Any] | None:
    if isinstance(node, ast.Constant):
        value = node.value
        if value is None:
            return "none", None
        if isinstance(value, bool):
            return "bool", value
        if isinstance(value, int):
            return "int", value
        if isinstance(value, float):
            return "float", value
        if isinstance(value, str):
            return "str", _truncate(value)
        return type(value).__name__, _truncate(repr(value))
    return None


def argument_descriptor(node: ast.AST) -> dict[str, Any]:
    """Describe an argument without evaluating decompiled scenario code."""
    expression = _truncate(_unparse(node))
    literal = _literal_value(node)
    if literal is not None:
        value_type, value = literal
        return {
            "kind": "literal",
            "type": value_type,
            "value": value,
            "expression": expression,
            "pattern": f"{value_type}:{value}",
        }
    if isinstance(node, ast.Subscript) and isinstance(node.value, ast.Name):
        base = node.value.id
        index = node.slice
        if base == "gvars":
            index_literal = _literal_value(index)
            index_pattern = (
                f"{index_literal[0]}:{index_literal[1]}"
                if index_literal is not None else _truncate(_unparse(index))
            )
            return {
                "kind": "gvars_subscript",
                "base": base,
                "index": _truncate(_unparse(index)),
                "expression": expression,
                "pattern": f"gvars[{index_pattern}]",
            }
        return {"kind": "subscript", "base": base, "expression": expression, "pattern": f"{base}[*]"}
    if isinstance(node, ast.Name):
        return {"kind": "name", "name": node.id, "expression": expression, "pattern": f"name:{node.id}"}
    if isinstance(node, ast.Attribute):
        return {"kind": "attribute", "expression": expression, "pattern": f"attribute:{expression}"}
    if isinstance(node, ast.Call):
        return {"kind": "call", "expression": expression, "pattern": f"call:{expression}"}
    if isinstance(node, (ast.List, ast.Tuple, ast.Set)):
        return {"kind": "container", "container": type(node).__name__.lower(), "expression": expression, "pattern": type(node).__name__.lower()}
    if isinstance(node, ast.Dict):
        return {"kind": "container", "container": "dict", "expression": expression, "pattern": "dict"}
    return {"kind": "expression", "expression": expression, "pattern": f"expr:{expression}"}


def _branch_name(kind: str, line: int, suffix: str) -> str:
    return f"{kind}@{line}:{suffix}"


class LifecycleCallVisitor(ast.NodeVisitor):
    """Collect target calls and draw sites with lexical branch context."""

    def __init__(self, module: str, family: str) -> None:
        self.module = module
        self.family = family
        self.function_stack: list[str] = []
        self.branch_stack: list[str] = []
        self.calls: list[dict[str, Any]] = []
        self.draw_sites: list[dict[str, Any]] = []

    @property
    def function(self) -> str:
        return ".".join(self.function_stack) if self.function_stack else "<module>"

    def _visit_function_body(self, node: ast.FunctionDef | ast.AsyncFunctionDef, name: str) -> None:
        self.function_stack.append(name)
        # A nested function is invoked independently of the branch in which it
        # is defined.  Reset branch context to avoid claiming a false draw
        # proof for closures.
        previous_branches = self.branch_stack
        self.branch_stack = []
        for statement in node.body:
            self.visit(statement)
        self.branch_stack = previous_branches
        self.function_stack.pop()

    def visit_FunctionDef(self, node: ast.FunctionDef) -> None:
        self._visit_function_body(node, node.name)

    def visit_AsyncFunctionDef(self, node: ast.AsyncFunctionDef) -> None:
        self._visit_function_body(node, node.name)

    def visit_Lambda(self, node: ast.Lambda) -> None:
        self.function_stack.append("<lambda>")
        previous_branches = self.branch_stack
        self.branch_stack = []
        self.visit(node.body)
        self.branch_stack = previous_branches
        self.function_stack.pop()

    def _visit_branch(self, statements: Iterable[ast.stmt], label: str) -> None:
        self.branch_stack.append(label)
        for statement in statements:
            self.visit(statement)
        self.branch_stack.pop()

    def visit_If(self, node: ast.If) -> None:
        self.visit(node.test)
        self._visit_branch(node.body, _branch_name("if", node.lineno, "then"))
        if node.orelse:
            self._visit_branch(node.orelse, _branch_name("if", node.lineno, "else"))

    def visit_For(self, node: ast.For) -> None:
        self.visit(node.target)
        self.visit(node.iter)
        self._visit_branch(node.body, _branch_name("for", node.lineno, "body"))
        if node.orelse:
            self._visit_branch(node.orelse, _branch_name("for", node.lineno, "else"))

    def visit_AsyncFor(self, node: ast.AsyncFor) -> None:
        self.visit(node.target)
        self.visit(node.iter)
        self._visit_branch(node.body, _branch_name("for", node.lineno, "body"))
        if node.orelse:
            self._visit_branch(node.orelse, _branch_name("for", node.lineno, "else"))

    def visit_While(self, node: ast.While) -> None:
        self.visit(node.test)
        self._visit_branch(node.body, _branch_name("while", node.lineno, "body"))
        if node.orelse:
            self._visit_branch(node.orelse, _branch_name("while", node.lineno, "else"))

    def visit_Try(self, node: ast.Try) -> None:
        self._visit_branch(node.body, _branch_name("try", node.lineno, "body"))
        for handler in node.handlers:
            type_name = _unparse(handler.type) if handler.type is not None else "*"
            self._visit_branch(handler.body, _branch_name("try", handler.lineno, f"except:{type_name}"))
        if node.orelse:
            self._visit_branch(node.orelse, _branch_name("try", node.lineno, "else"))
        if node.finalbody:
            self._visit_branch(node.finalbody, _branch_name("try", node.lineno, "finally"))

    def visit_With(self, node: ast.With) -> None:
        for item in node.items:
            self.visit(item.context_expr)
            if item.optional_vars:
                self.visit(item.optional_vars)
        self._visit_branch(node.body, _branch_name("with", node.lineno, "body"))

    def visit_AsyncWith(self, node: ast.AsyncWith) -> None:
        for item in node.items:
            self.visit(item.context_expr)
            if item.optional_vars:
                self.visit(item.optional_vars)
        self._visit_branch(node.body, _branch_name("with", node.lineno, "body"))

    def visit_Call(self, node: ast.Call) -> None:
        path = expression_path(node.func)
        function = self.function
        branch_path = list(self.branch_stack)
        if path == "stage.draw":
            self.draw_sites.append({
                "module": self.module,
                "family": self.family,
                "function": function,
                "functionPath": function,
                "line": node.lineno,
                "branchPath": branch_path,
            })
        if path in TARGET_APIS:
            receiver_args: list[dict[str, Any]] = []
            if path == "stage.unit().setPosts" and isinstance(node.func, ast.Attribute):
                receiver = node.func.value
                if isinstance(receiver, ast.Call):
                    receiver_args = [argument_descriptor(arg) for arg in receiver.args]
            args = [argument_descriptor(arg) for arg in node.args]
            self.calls.append({
                "api": path,
                "module": self.module,
                "family": self.family,
                "function": function,
                "functionPath": function,
                "line": node.lineno,
                "column": node.col_offset,
                "endLine": getattr(node, "end_lineno", node.lineno),
                "branchPath": branch_path,
                "branch": "/".join(branch_path) if branch_path else "<root>",
                "argumentCount": len(node.args),
                "arguments": args,
                "argumentExpressions": [_truncate(_unparse(arg)) for arg in node.args],
                "staticArgumentPattern": "|".join(arg["pattern"] for arg in args) or "<none>",
                "receiverArguments": receiver_args,
                "receiverPattern": "|".join(arg["pattern"] for arg in receiver_args) or None,
            })
        self.generic_visit(node)


def inventory(source_dir: Path) -> tuple[list[Path], list[dict[str, Any]], list[dict[str, Any]]]:
    scripts = sorted(
        [path for path in source_dir.glob("S_*.py") if path.stem[2:].isdigit()]
        + [path for path in source_dir.glob("R_*.py") if path.stem[2:].isdigit()],
        key=lambda path: (path.name[0], int(path.stem[2:])),
    )
    calls: list[dict[str, Any]] = []
    draws: list[dict[str, Any]] = []
    for path in scripts:
        module = path.stem
        visitor = LifecycleCallVisitor(module, module[0])
        tree = ast.parse(path.read_text(encoding="utf-8"), filename=str(path))
        visitor.visit(tree)
        calls.extend(visitor.calls)
        draws.extend(visitor.draw_sites)
    return scripts, calls, draws


def _balanced_body(text: str, brace: int) -> str:
    depth = 0
    quote: str | None = None
    escaped = False
    index = brace
    while index < len(text):
        char = text[index]
        if quote:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == quote:
                quote = None
        elif char in "'\"`":
            quote = char
        elif char == "{":
            depth += 1
        elif char == "}":
            depth -= 1
            if depth == 0:
                return text[brace + 1:index]
        index += 1
    raise ValueError("unbalanced recovered JavaScript method")


def source_methods(text: str) -> dict[str, dict[str, Any]]:
    result: dict[str, dict[str, Any]] = {}
    pattern = re.compile(r"\.prototype\.([A-Za-z_$][\w$]*)\s*=\s*function\s*\([^)]*\)\s*\{")
    for match in pattern.finditer(text):
        body = _balanced_body(text, match.end() - 1)
        line = text.count("\n", 0, match.start()) + 1
        result[match.group(1)] = {
            "line": line,
            "body": body,
            "bodySha256": hashlib.sha256(body.encode("utf-8")).hexdigest(),
            "signals": source_signals(body),
        }
    return result


def source_signals(body: str) -> list[str]:
    checks = {
        "pause": r"\.pause\s*\(",
        "resume": r"\.resume(?:\s*\(|\.bind)",
        "callback": r"function\s*\(",
        "action": r"\.setAction2?\s*\(",
        "move": r"\.move2?\s*\(",
        "runAction": r"\.runAction\s*\(",
        "tween": r"cc\.tween\s*\(",
        "layer": r"\.addLayer\s*\(",
        "assetLoad": r"(?:loadRes|loadAvatar|_setObject)\s*\(",
        "schedule": r"\.schedule(?:Once)?\s*\(",
    }
    return [name for name, pattern in checks.items() if re.search(pattern, body)]


def _load_json(path: Path | None) -> dict[str, Any]:
    if path is None or not path.exists():
        return {}
    return json.loads(path.read_text(encoding="utf-8"))


def _contract_for(api: str, contracts: dict[str, Any]) -> dict[str, Any] | None:
    return next((item for item in contracts.get("contracts", []) if item.get("api") == api), None)


def _source_ref(
    route: dict[str, Any] | None,
    source_root: Path,
    contract: dict[str, Any] | None,
) -> dict[str, Any] | None:
    if route is None:
        return None
    source_path = source_root / route["file"]
    result: dict[str, Any] = {
        "owner": route["owner"],
        "file": route["file"],
        "method": route["method"],
        "path": str(source_path),
        "exists": source_path.exists(),
    }
    if not source_path.exists():
        return result
    methods = source_methods(source_path.read_text(encoding="utf-8"))
    method = methods.get(route["method"])
    if method is None:
        result["methodFound"] = False
        return result
    result.update({
        "methodFound": True,
        "line": method["line"],
        "bodySha256": method["bodySha256"],
        "signals": method["signals"],
    })
    contract_functions = (contract or {}).get("sourceFunctions", [])
    matching_contract = next(
        (
            item for item in contract_functions
            if item.get("file") == route["file"] and item.get("method") == route["method"]
        ),
        None,
    )
    result["contractSourceFunction"] = matching_contract
    result["contractHashMatches"] = (
        None if matching_contract is None else matching_contract.get("bodySha256") == method["bodySha256"]
    )
    return result


def _same_or_ancestor(draw_branch: list[str], call_branch: list[str]) -> bool:
    return len(draw_branch) <= len(call_branch) and draw_branch == call_branch[: len(draw_branch)]


def annotate_draw(call: dict[str, Any], draws: list[dict[str, Any]]) -> None:
    function_draws = [
        draw for draw in draws
        if draw["module"] == call["module"] and draw["functionPath"] == call["functionPath"]
    ]
    preceding = [draw for draw in function_draws if draw["line"] < call["line"]]
    following = [draw for draw in function_draws if draw["line"] > call["line"]]
    same_branch_preceding = [
        draw for draw in preceding
        if _same_or_ancestor(draw["branchPath"], call["branchPath"])
    ]
    same_branch_following = [
        draw for draw in following
        if _same_or_ancestor(draw["branchPath"], call["branchPath"])
    ]
    if same_branch_preceding:
        phase = "after"
        proof = "preceding_draw_on_same_control_path"
    elif same_branch_following:
        phase = "before"
        proof = "following_draw_on_same_control_path"
    elif function_draws:
        phase = "unknown"
        proof = "draw_exists_but_branch_path_is_not_proven"
    else:
        phase = "no_draw_in_function"
        proof = "no_stage_draw_in_enclosing_function"
    call["drawPhase"] = phase
    call["drawProof"] = proof
    call["drawEvidence"] = {
        "functionDrawLines": [draw["line"] for draw in function_draws],
        "precedingDrawLines": [draw["line"] for draw in preceding],
        "followingDrawLines": [draw["line"] for draw in following],
        "sameBranchPrecedingDrawLines": [draw["line"] for draw in same_branch_preceding],
        "sameBranchFollowingDrawLines": [draw["line"] for draw in same_branch_following],
    }
    module_draws = [draw for draw in draws if draw["module"] == call["module"]]
    module_preceding = [draw for draw in module_draws if draw["line"] < call["line"]]
    module_following = [draw for draw in module_draws if draw["line"] > call["line"]]
    if not module_draws:
        module_phase = "no_draw_in_module"
        module_proof = "no_stage_draw_in_module"
    elif module_preceding and module_following:
        module_phase = "unknown"
        module_proof = "module_has_draws_on_both_lexical_sides"
    elif module_preceding:
        module_phase = "after"
        module_proof = "lexically_after_module_draw"
    else:
        module_phase = "before"
        module_proof = "lexically_before_module_draw"
    call["moduleDrawPhase"] = module_phase
    call["moduleDrawProof"] = module_proof
    call["moduleDrawEvidence"] = {
        "drawLines": [draw["line"] for draw in module_draws],
        "precedingDrawLines": [draw["line"] for draw in module_preceding],
        "followingDrawLines": [draw["line"] for draw in module_following],
    }


def _int_argument(call: dict[str, Any], index: int) -> int | None:
    if index >= len(call["arguments"]):
        return None
    argument = call["arguments"][index]
    if argument.get("kind") == "literal" and argument.get("type") == "int":
        return int(argument["value"])
    return None


def callback_assessment(call: dict[str, Any], contract: dict[str, Any] | None) -> dict[str, Any]:
    api = call["api"]
    if api == "stage.resumeCtrl":
        return {
            "possible": False,
            "requirement": "none",
            "reason": "original_BattleLayer_resumeCtrl_has_no_callback_or_pause",
            "sourceContract": False,
        }
    if api == "stage.loadBg":
        return {
            "possible": True,
            "requirement": "required_for_script_continuation",
            "reason": "original_loadBg_pauses_and_resumes_only_from_success_callback",
            "sourceContract": contract is not None and call["family"] == "S",
            "failureMode": "source_callback_failure_can_leave_script_paused",
        }
    if api == "stage.setUnitAttr":
        attr_index = _int_argument(call, 1)
        if attr_index in (27, 28):
            return {
                "possible": True,
                "requirement": "conditional_live_unit_avatar_path",
                "reason": "static_attr_is_S_AVATAR_or_POSTS",
                "attrIndex": attr_index,
                "sourceContract": contract is not None,
            }
        if attr_index is not None:
            return {
                "possible": False,
                "requirement": "not_triggered_by_static_attr",
                "reason": "static_attr_does_not_enter_original_avatar_reload_branch",
                "attrIndex": attr_index,
                "sourceContract": contract is not None,
            }
        return {
            "possible": True,
            "requirement": "conditional_dynamic_attr",
            "reason": "attribute_index_is_not_static",
            "sourceContract": contract is not None,
        }
    if api == "stage.unit().setPosts":
        flag = _int_argument(call, 1)
        # The Python call has one argument; recovered JS defaults its second
        # argument to 19, which includes bit 16.  Whether testAvatar detects a
        # changed attached node remains runtime-dependent.
        if len(call["arguments"]) < 2:
            flag = 19
        blocking = flag is None or bool(flag & 16)
        return {
            "possible": blocking,
            "requirement": "conditional_avatar_reload" if blocking else "nonblocking_reload",
            "reason": "original_setPosts_pauses_only_on_flag_16_and_avatar_change",
            "effectiveFlag": flag,
            "sourceContract": contract is not None,
            "failureMode": "unexpected_avatar_load_rejection_can_leave_script_paused" if blocking else None,
        }
    raise AssertionError(api)


def _counter(rows: Iterable[dict[str, Any]], key: str) -> dict[str, int]:
    return dict(sorted(Counter(str(row[key]) for row in rows).items()))


def _counter_pattern(rows: Iterable[dict[str, Any]], key: str) -> dict[str, int]:
    return dict(sorted(Counter(str(row.get(key) or "<none>") for row in rows).items()))


def _group_counts(rows: list[dict[str, Any]], key: str) -> dict[str, dict[str, int]]:
    grouped: dict[str, Counter[str]] = defaultdict(Counter)
    for row in rows:
        grouped[str(row[key])][row["api"]] += 1
    return {group: dict(sorted(counter.items())) for group, counter in sorted(grouped.items())}


def _prior_audit_summary(
    api_audit_path: Path | None,
    usage_path: Path | None,
) -> dict[str, Any]:
    result: dict[str, Any] = {}
    if api_audit_path and api_audit_path.exists():
        data = _load_json(api_audit_path)
        result["battleStageApiAudit"] = {
            "path": str(api_audit_path),
            "format": data.get("format"),
            "counts": {
                row.get("api"): row.get("callCount")
                for row in data.get("apis", [])
                if row.get("api") in TARGET_APIS
            },
        }
    if usage_path and usage_path.exists():
        data = _load_json(usage_path)
        stage_rows = {
            row.get("call"): row
            for row in data.get("injectedApiCalls", {}).get("stage", [])
            if row.get("call") in TARGET_APIS
        }
        result["pythonApiUsage"] = {
            "path": str(usage_path),
            "format": data.get("format"),
            "counts": {api: row.get("count") for api, row in stage_rows.items()},
            "sourceFiles": {api: row.get("sourceFiles", []) for api, row in stage_rows.items()},
            "note": "This prior inventory includes train.py; this report intentionally scans S_*.py/R_*.py only.",
        }
    return result


def audit(
    source_dir: Path,
    source_root: Path,
    contracts_path: Path | None = None,
    api_audit_path: Path | None = None,
    usage_path: Path | None = None,
) -> dict[str, Any]:
    scripts, calls, draws = inventory(source_dir)
    contracts = _load_json(contracts_path)
    contracts_by_api = {item.get("api"): item for item in contracts.get("contracts", [])}
    for call in calls:
        annotate_draw(call, draws)
        call["callback"] = callback_assessment(call, contracts_by_api.get(call["api"]))
        route = ROUTES[call["api"]].get(call["family"])
        expected_arity = route.get("arity", []) if route else []
        findings: list[str] = []
        if route is None:
            findings.append("no_original_route_for_scenario_family")
        elif call["argumentCount"] not in expected_arity:
            findings.append("argument_arity_differs_from_original_route")
        if call["family"] == "R" and call["api"] == "stage.loadBg":
            findings.append("R_route_uses_HallLayer_not_battle_lifecycle_contract")
        if call["drawPhase"] == "unknown":
            findings.append("draw_order_not_proven_through_branch_analysis")
        call["findings"] = findings

    api_rows: list[dict[str, Any]] = []
    for api in TARGET_APIS:
        api_calls = [call for call in calls if call["api"] == api]
        family_counts = Counter(call["family"] for call in api_calls)
        module_counts = Counter(call["module"] for call in api_calls)
        function_counts = Counter(f"{call['module']}::{call['functionPath']}" for call in api_calls)
        branch_counts = Counter(call["branch"] for call in api_calls)
        draw_counts = Counter(call["drawPhase"] for call in api_calls)
        module_draw_counts = Counter(call["moduleDrawPhase"] for call in api_calls)
        callback_counts = Counter(call["callback"]["requirement"] for call in api_calls)
        pattern_counts = Counter(call["staticArgumentPattern"] for call in api_calls)
        receiver_counts = Counter(call["receiverPattern"] or "<none>" for call in api_calls)
        families = sorted({call["family"] for call in api_calls})
        contract = contracts_by_api.get(api)
        route_refs = {
            family: _source_ref(ROUTES[api].get(family), source_root, contract)
            for family in ("S", "R")
            if family in families
        }
        findings = Counter(finding for call in api_calls for finding in call["findings"])
        if api_calls and contract is None:
            findings["lifecycle_contract_not_present"] += 1
        for family, ref in route_refs.items():
            if ref and ref.get("methodFound") is False:
                findings["original_route_method_not_found"] += family_counts[family]
            if ref and ref.get("contractHashMatches") is False:
                findings["original_source_hash_differs_from_contract"] += family_counts[family]
        if api_calls and any(call["callback"]["possible"] for call in api_calls):
            # This is an explicit review flag, not a gate suppression.  It is
            # useful when all current static calls happen to avoid the branch.
            findings["callback_can_be_required_at_runtime"] += sum(
                call["callback"]["possible"] for call in api_calls
            )
        api_rows.append({
            "api": api,
            "callCount": len(api_calls),
            "callCountByFamily": dict(sorted(family_counts.items())),
            "callCountsByModule": dict(sorted(module_counts.items())),
            "callCountsByFunction": dict(sorted(function_counts.items())),
            "callCountsByBranch": dict(sorted(branch_counts.items())),
            "callCountsByDrawPhase": dict(sorted(draw_counts.items())),
            "callCountsByModuleDrawPhase": dict(sorted(module_draw_counts.items())),
            "callCountsByCallbackRequirement": dict(sorted(callback_counts.items())),
            "staticArgumentPatterns": dict(sorted(pattern_counts.items())),
            "receiverPatterns": dict(sorted(receiver_counts.items())),
            "routeByFamily": route_refs,
            "contract": contract,
            "findings": dict(sorted(findings.items())),
            "calls": api_calls,
        })

    calls_by_family = Counter(call["family"] for call in calls)
    scripts_by_family = Counter(path.stem[0] for path in scripts)
    prior = _prior_audit_summary(api_audit_path, usage_path)
    prior_discrepancies: list[dict[str, Any]] = []
    usage_counts = prior.get("pythonApiUsage", {}).get("counts", {})
    current_counts = {row["api"]: row["callCount"] for row in api_rows}
    for api, previous in usage_counts.items():
        current = current_counts.get(api, 0)
        if previous != current:
            prior_discrepancies.append({
                "api": api,
                "priorCount": previous,
                "currentSRCount": current,
                "reason": "prior_inventory_includes_non_SR_sources_or_scope_differs",
            })
    findings: Counter[str] = Counter()
    for row in api_rows:
        findings.update(row["findings"])
    return {
        "format": FORMAT,
        "sourceDirectory": str(source_dir),
        "sourceRoot": str(source_root),
        "contracts": str(contracts_path) if contracts_path else None,
        "scripts": len(scripts),
        "scriptsByFamily": dict(sorted(scripts_by_family.items())),
        "drawSites": len(draws),
        "callSites": len(calls),
        "callSitesByFamily": dict(sorted(calls_by_family.items())),
        "apis": api_rows,
        "priorAudit": prior,
        "priorAuditDiscrepancies": prior_discrepancies,
        "summary": {
            "apiCount": len(TARGET_APIS),
            "callCounts": current_counts,
            "findingCountByName": dict(sorted(findings.items())),
            "apisWithFindings": sum(bool(row["findings"]) for row in api_rows),
            "callbackPossibleCallSites": sum(
                call["callback"]["possible"] for call in calls
            ),
            "drawPhaseCounts": dict(sorted(Counter(call["drawPhase"] for call in calls).items())),
            "moduleDrawPhaseCounts": dict(sorted(Counter(call["moduleDrawPhase"] for call in calls).items())),
        },
    }


def markdown(report: dict[str, Any]) -> str:
    summary = report["summary"]
    lines = [
        "# Battle stage lifecycle call-site audit",
        "",
        f"AST-scanned **{report['scripts']}** scripts ({report['scriptsByFamily'].get('S', 0)} S + {report['scriptsByFamily'].get('R', 0)} R), "
        f"**{report['callSites']}** target call sites, and **{report['drawSites']}** `stage.draw()` sites.",
        "",
        "The report intentionally includes only `S_*.py` and `R_*.py`; prior inventories that include `train.py` are reconciled below.",
        "",
        "## API summary",
        "",
        "| API | Calls S/R | Draw phase (function / module lexical) | Callback possibility | Static argument patterns | Findings |",
        "|---|---:|---|---:|---:|---|",
    ]
    for row in report["apis"]:
        phase = ", ".join(f"{key}={value}" for key, value in row["callCountsByDrawPhase"].items()) or "—"
        module_phase = ", ".join(f"{key}={value}" for key, value in row["callCountsByModuleDrawPhase"].items()) or "—"
        findings = ", ".join(f"{key}={value}" for key, value in row["findings"].items()) or "—"
        lines.append(
            f"| `{row['api']}` | {row['callCount']} ({row['callCountByFamily'].get('S', 0)}/{row['callCountByFamily'].get('R', 0)}) | "
            f"fn: {phase}; module: {module_phase} | {sum(1 for call in row['calls'] if call['callback']['possible'])} | "
            f"{len(row['staticArgumentPatterns'])} | {findings} |"
        )
    lines.extend(["", "## Route and lifecycle authority", ""])
    for row in report["apis"]:
        lines.append(f"### `{row['api']}`")
        for family, route in row["routeByFamily"].items():
            if route is None:
                lines.append(f"- {family}: no original route found")
                continue
            signals = ", ".join(route.get("signals", [])) or "none"
            hash_status = route.get("contractHashMatches")
            contract_status = (
                "contract hash matches" if hash_status is True
                else "CONTRACT HASH DIFFERS" if hash_status is False
                else "no contract hash for this route"
            )
            lines.append(
                f"- {family}: `{route['owner']}.{route['method']}` ({route['file']}:{route.get('line', '?')}); "
                f"signals={signals}; {contract_status}"
            )
        callback_counts = ", ".join(
            f"{key}={value}" for key, value in row["callCountsByCallbackRequirement"].items()
        ) or "none"
        lines.append(f"- callback requirements: {callback_counts}")
        top_modules = sorted(row["callCountsByModule"].items(), key=lambda item: (-item[1], item[0]))[:8]
        if top_modules:
            lines.append("- top modules: " + ", ".join(f"`{module}`={count}" for module, count in top_modules))
        patterns = sorted(row["staticArgumentPatterns"].items(), key=lambda item: (-item[1], item[0]))[:12]
        if patterns:
            lines.append("- static argument patterns: " + ", ".join(f"`{pattern}`={count}" for pattern, count in patterns))
    lines.extend(["", "## Findings", ""])
    if summary["findingCountByName"]:
        for finding, count in summary["findingCountByName"].items():
            lines.append(f"- `{finding}`: {count}")
    else:
        lines.append("- none")
    if report["priorAuditDiscrepancies"]:
        lines.extend(["", "## Prior-audit reconciliation", ""])
        for item in report["priorAuditDiscrepancies"]:
            lines.append(
                f"- `{item['api']}`: prior={item['priorCount']}, current S/R={item['currentSRCount']} — {item['reason']}"
            )
    lines.extend([
        "",
        "Full per-call evidence (module/function/branch/line/draw proof/arguments/callback assessment) is in the machine JSON.",
        "",
    ])
    return "\n".join(lines)


def main(argv: list[str] | None = None) -> int:
    root = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-dir", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/decompiled-python")
    parser.add_argument("--source-root", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules")
    parser.add_argument("--contracts", type=Path, default=root / "tools/battle_stage_api_lifecycle_contracts.json")
    parser.add_argument("--api-audit", type=Path, default=root / "tools/battle_stage_api_audit.json")
    parser.add_argument("--usage", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/recovered-js/porting/python-api-usage.json")
    parser.add_argument("--json", type=Path, default=root / "tools/battle_stage_api_lifecycle_call_sites.json")
    parser.add_argument("--markdown", type=Path, default=root / "tools/battle_stage_api_lifecycle_call_sites.md")
    args = parser.parse_args(argv)
    report = audit(
        args.source_dir.resolve(), args.source_root.resolve(), args.contracts.resolve(),
        args.api_audit.resolve(), args.usage.resolve(),
    )
    args.json.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    args.markdown.write_text(markdown(report), encoding="utf-8")
    print(
        f"BATTLE_STAGE_API_LIFECYCLE_CALL_SITES scripts={report['scripts']} "
        f"calls={report['callSites']} findings={sum(report['summary']['findingCountByName'].values())}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
