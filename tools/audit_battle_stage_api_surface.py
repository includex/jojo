#!/usr/bin/env python3
"""Audit every S_*.py stage API against the game and recovered source.

The command writes machine-readable JSON plus a review-friendly Markdown
report.  It exits non-zero when an authored call has no runtime handler, its
source implementation cannot be resolved, or a source pause/callback contract
has no battle suspension in the game handler.  ``--allow-findings`` is only
for refreshing the reports while known findings are being implemented.
"""
from __future__ import annotations

import argparse
import ast
import json
import re
from collections import Counter, defaultdict, deque
from pathlib import Path
from typing import Any


FORMAT = "jojo-battle-stage-api-audit/v1"


def expression_path(node: ast.AST) -> str | None:
    if isinstance(node, ast.Name):
        return node.id
    if isinstance(node, ast.Attribute):
        base = expression_path(node.value)
        return f"{base}.{node.attr}" if base else None
    if isinstance(node, ast.Call):
        base = expression_path(node.func)
        return f"{base}()" if base else None
    return None


class CallVisitor(ast.NodeVisitor):
    def __init__(self, module: str) -> None:
        self.module = module
        self.function = "<module>"
        self.calls: list[dict[str, Any]] = []

    def visit_FunctionDef(self, node: ast.FunctionDef) -> None:
        previous = self.function
        self.function = node.name
        self.generic_visit(node)
        self.function = previous

    def visit_Call(self, node: ast.Call) -> None:
        path = expression_path(node.func)
        if path and (path.startswith("stage.") or path.startswith("stage.unit().") or path.startswith("stage.head().")):
            self.calls.append({
                "api": path, "module": self.module, "function": self.function,
                "line": node.lineno, "argumentCount": len(node.args),
            })
        self.generic_visit(node)


def inventory_calls(source_dir: Path) -> tuple[list[Path], list[dict[str, Any]]]:
    scripts = sorted(path for path in source_dir.glob("S_*.py") if path.stem[2:].isdigit())
    calls: list[dict[str, Any]] = []
    for path in scripts:
        visitor = CallVisitor(path.stem)
        visitor.visit(ast.parse(path.read_text(encoding="utf-8"), filename=str(path)))
        calls.extend(visitor.calls)
    return scripts, calls


def _lifecycle_module() -> Any:
    """Load the independent AST lifecycle auditor used as call-site authority.

    The two tools intentionally remain separate reports, but the surface gate
    must use the same call-site facts (argument shape and draw ordering).  A
    local import keeps this script usable both as ``python tools/foo.py`` and
    as a module from the tool tests.
    """
    try:
        from audit_battle_stage_api_lifecycle_call_sites import (  # type: ignore
            annotate_draw, callback_assessment, inventory,
        )
    except ImportError:  # pragma: no cover - package-style imports
        from .audit_battle_stage_api_lifecycle_call_sites import (  # type: ignore
            annotate_draw, callback_assessment, inventory,
        )
    return annotate_draw, callback_assessment, inventory


def _load_lifecycle_call_evidence(
    source_dir: Path,
    lifecycle_path: Path | None,
) -> tuple[dict[tuple[str, str, int], deque[dict[str, Any]]], dict[str, Any]]:
    """Return fresh AST evidence plus optional checked-in JSON evidence.

    ``battle_stage_api_lifecycle_call_sites.json`` is a useful persisted
    review artifact, but it is never trusted on its own: the lifecycle AST is
    parsed again for every surface audit.  When both exist, the matching
    fields are compared and the disagreement is retained in the report.
    """
    annotate_draw, callback_assessment, inventory = _lifecycle_module()
    _, lifecycle_calls, draws = inventory(source_dir)
    contracts: dict[str, Any] = {}
    contract_path = lifecycle_path.with_name("battle_stage_api_lifecycle_contracts.json") if lifecycle_path else None
    if contract_path and contract_path.exists():
        contract_data = json.loads(contract_path.read_text(encoding="utf-8"))
        contracts = {
            item.get("api"): item
            for item in contract_data.get("contracts", [])
            if item.get("api")
        }

    by_site: dict[tuple[str, str, int], deque[dict[str, Any]]] = defaultdict(deque)
    for call in lifecycle_calls:
        if call.get("api") not in {"stage.loadBg", "stage.setUnitAttr"}:
            continue
        annotate_draw(call, draws)
        call["callback"] = callback_assessment(call, contracts.get(call.get("api")))
        by_site[(str(call["api"]), str(call["module"]), int(call["line"]))].append(call)

    persisted: dict[str, Any] = {}
    if lifecycle_path and lifecycle_path.exists():
        persisted = json.loads(lifecycle_path.read_text(encoding="utf-8"))
    persisted_by_site: dict[tuple[str, str, int], deque[dict[str, Any]]] = defaultdict(deque)
    for row in persisted.get("apis", []):
        for call in row.get("calls", []):
            key = (str(call.get("api")), str(call.get("module")), int(call.get("line", -1)))
            persisted_by_site[key].append(call)
    for key, values in by_site.items():
        for call in values:
            old = persisted_by_site.get(key)
            persisted_call = old.popleft() if old else None
            if persisted_call is not None:
                call["lifecycleJsonEvidence"] = {
                    "drawPhase": persisted_call.get("drawPhase"),
                    "drawProof": persisted_call.get("drawProof"),
                    "moduleDrawPhase": persisted_call.get("moduleDrawPhase"),
                    "callback": persisted_call.get("callback"),
                }
                call["lifecycleJsonMatchesAst"] = all(
                    persisted_call.get(field) == call.get(field)
                    for field in ("drawPhase", "drawProof", "moduleDrawPhase")
                ) and persisted_call.get("callback") == call.get("callback")
    return by_site, {
        "path": str(lifecycle_path) if lifecycle_path else None,
        "exists": bool(lifecycle_path and lifecycle_path.exists()),
        "format": persisted.get("format"),
        "astCallSites": sum(len(values) for values in by_site.values()),
        "jsonCallSites": sum(
            len(call.get("calls", []))
            for call in persisted.get("apis", [])
            if call.get("api") in {"stage.loadBg", "stage.setUnitAttr"}
        ),
    }


def _attach_lifecycle_evidence(
    calls: list[dict[str, Any]],
    by_site: dict[tuple[str, str, int], deque[dict[str, Any]]],
) -> None:
    """Attach one fresh lifecycle AST record to each surface target call."""
    for call in calls:
        if call.get("api") not in {"stage.loadBg", "stage.setUnitAttr"}:
            continue
        key = (str(call["api"]), str(call["module"]), int(call["line"]))
        candidates = by_site.get(key)
        lifecycle_call = candidates.popleft() if candidates else None
        if lifecycle_call is None:
            call["lifecycleEvidence"] = {
                "available": False,
                "reason": "surface_call_has_no_matching_lifecycle_ast_site",
            }
            continue
        call["lifecycleEvidence"] = {
            "available": True,
            "drawPhase": lifecycle_call.get("drawPhase"),
            "drawProof": lifecycle_call.get("drawProof"),
            "moduleDrawPhase": lifecycle_call.get("moduleDrawPhase"),
            "moduleDrawProof": lifecycle_call.get("moduleDrawProof"),
            "arguments": lifecycle_call.get("arguments", []),
            "argumentExpressions": lifecycle_call.get("argumentExpressions", []),
            "callback": lifecycle_call.get("callback", {}),
            "lifecycleJsonEvidence": lifecycle_call.get("lifecycleJsonEvidence"),
            "lifecycleJsonMatchesAst": lifecycle_call.get("lifecycleJsonMatchesAst"),
        }


def runtime_handlers(runtime_text: str) -> dict[str, str]:
    if "private fun invokeCall" in runtime_text:
        start = runtime_text.index("private fun invokeCall")
        end = runtime_text.find("private fun stageVariableValue", start)
        section = runtime_text[start:end] if end != -1 else runtime_text[start:]
    else:
        section = runtime_text
    pattern = re.compile(r'^[ \t]+((?:"[^"]+"\s*,?\s*)+)\s*->', re.MULTILINE)
    matches = list(pattern.finditer(section))
    result: dict[str, str] = {}
    for index, match in enumerate(matches):
        body_end = matches[index + 1].start() if index + 1 < len(matches) else len(section)
        body = section[match.end():body_end]
        for name in re.findall(r'"([^"]+)"', match.group(1)):
            result[name] = body
    return result


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


def source_methods(text: str) -> dict[str, str]:
    result: dict[str, str] = {}
    for match in re.finditer(r"\.prototype\.([A-Za-z_$][\w$]*)\s*=\s*function\s*\([^)]*\)\s*\{", text):
        result[match.group(1)] = _balanced_body(text, match.end() - 1)
    return result


def expanded_source_body(name: str, methods: dict[str, str], seen: set[str] | None = None) -> str | None:
    body = methods.get(name)
    if body is None:
        return None
    seen = set() if seen is None else seen
    if name in seen:
        return body
    seen.add(name)
    additions = []
    for called in re.findall(r"this\.([A-Za-z_$][\w$]*)\s*\(", body):
        if called in methods and called not in seen:
            additions.append(expanded_source_body(called, methods, seen) or "")
    return body + "\n" + "\n".join(additions)


def source_signals(body: str) -> list[str]:
    checks = {
        "pause": r"\.pause\s*\(", "resume": r"\.resume(?:\s*\(|\.bind)",
        "callback": r"function\s*\(", "action": r"\.setAction2?\s*\(",
        "move": r"\.move2?\s*\(", "runAction": r"\.runAction\s*\(",
        "tween": r"cc\.tween\s*\(", "layer": r"\.addLayer\s*\(",
        "assetLoad": r"(?:loadRes|loadAvatar|_setObject)\s*\(",
        "schedule": r"\.schedule(?:Once)?\s*\(",
    }
    return [name for name, pattern in checks.items() if re.search(pattern, body)]


def source_has_barrier(signals: list[str]) -> bool:
    values = set(signals)
    return "pause" in values and bool(values & {"resume", "callback", "action", "move", "runAction", "tween", "layer", "assetLoad", "schedule"})


def game_battle_barrier_evidence(body: str) -> list[dict[str, Any]]:
    """Find battle suspension edges, including dedicated resource barriers.

    A handler may use the generic ``suspendFor`` or a purpose-specific helper
    such as ``suspendForBattleBackgroundLoad``.  Matching the helper family is
    intentionally structural; adding a new source-backed barrier does not
    require updating an API allowlist.  R-only guards remain excluded because
    they do not suspend the battle script.
    """
    barrier = re.compile(
        r"\b(?:suspendFor[A-Za-z_$][\w$]*|suspendFor)\s*(?:\(|[\s\),]|;|$)|"
        r"(?:state\s*=|onSetState\s*\()\s*(?:PlaybackState\.)?(?:DIALOGUE|CHOICE|MODAL|DELAY)|"
        r"\b(?:startSay|startTalk|startChoice)\s*\("
    )
    evidence: list[dict[str, Any]] = []
    for line_number, line in enumerate(body.splitlines(), 1):
        match = barrier.search(line)
        if not match or 'moduleName.startsWith("R_")' in line:
            continue
        evidence.append({
            "line": line_number,
            "expression": match.group(0).strip(),
            "text": line.strip(),
        })
    return evidence


def game_has_battle_barrier(body: str) -> bool:
    """Compatibility wrapper used by callers and existing tests."""
    return bool(game_battle_barrier_evidence(body))


def _call_lifecycle_evidence(call: dict[str, Any]) -> dict[str, Any]:
    evidence = call.get("lifecycleEvidence")
    return evidence if isinstance(evidence, dict) else {}


def _source_requires_live_unit(source_body: str | None) -> bool:
    """Infer a live-unit guard from the recovered method body.

    The source contract says the asynchronous path follows a lookup and then
    ``_loadAvatar``.  Looking for both operations makes this reusable for a
    future method with the same shape and avoids an API-specific exemption.
    """
    if not source_body:
        return False
    return bool(
        re.search(r"(?:this\.)?unit\s*\(", source_body)
        and re.search(r"_loadAvatar\s*\(", source_body)
    )


def _source_barrier_call_assessment(
    call: dict[str, Any],
    source_body: str | None,
) -> dict[str, Any]:
    """Assess whether this concrete call can reach a source pause edge.

    The lifecycle AST auditor supplies the condition extracted from static
    arguments.  For a branch that additionally requires a live BattleUnit,
    ``stage.draw`` ordering proves that a pre-draw S call runs before the
    first live node exists.  Unknown evidence stays conservative and remains
    a required barrier call site.
    """
    evidence = _call_lifecycle_evidence(call)
    callback = evidence.get("callback") if isinstance(evidence.get("callback"), dict) else {}
    possible = callback.get("possible")
    if possible is None:
        possible = True
    result: dict[str, Any] = {
        "possibleFromSourceCondition": bool(possible),
        "requiredForGameBarrier": bool(possible),
        "reason": callback.get("reason", "lifecycle_call_site_evidence_unavailable"),
        "drawPhase": evidence.get("drawPhase"),
        "drawProof": evidence.get("drawProof"),
        "liveUnitGuardInSource": _source_requires_live_unit(source_body),
    }
    if not possible:
        result["requiredForGameBarrier"] = False
        result["classification"] = "source_condition_not_reachable_from_static_arguments"
        return result
    # This is a proof from the call-site lifecycle, not a blanket S API
    # exemption: only a same-function/same-branch following draw proves the
    # pre-draw state.  Any other shape remains conservative.
    if (
        result["liveUnitGuardInSource"]
        and (call.get("family") or str(call.get("module", ""))[:1]) == "S"
        and evidence.get("drawPhase") == "before"
        and evidence.get("drawProof") == "following_draw_on_same_control_path"
    ):
        result["requiredForGameBarrier"] = False
        result["classification"] = "live_unit_absent_before_first_stage_draw"
    elif possible:
        result["classification"] = "conditional_source_barrier_may_be_reached"
    return result


def game_mutates(body: str) -> bool:
    return bool(re.search(r"\bstage\.(?!request)[A-Za-z_$][\w$]*\s*\(", body))


def audit(
    source_dir: Path,
    runtime_path: Path,
    source_root: Path,
    lifecycle_path: Path | None = None,
) -> dict[str, Any]:
    scripts, calls = inventory_calls(source_dir)
    lifecycle_by_site, lifecycle_metadata = _load_lifecycle_call_evidence(
        source_dir, lifecycle_path,
    )
    _attach_lifecycle_evidence(calls, lifecycle_by_site)
    if runtime_path.name == "ScenarioInterpreter.kt" and runtime_path.is_file():
        dispatch_files = [
            "ScenarioCallCoordinator.kt",
            "ScenarioStageCallDispatcher.kt",
            "ScenarioTacticalActionDispatcher.kt",
            "ScenarioUnitActionDispatcher.kt",
            "ScenarioFightDispatcher.kt",
            "ScenarioInterpreter.kt",
        ]
        combined_text = "\n".join(
            (runtime_path.parent / name).read_text(encoding="utf-8")
            for name in dispatch_files
            if (runtime_path.parent / name).exists()
        )
        handlers = runtime_handlers(combined_text)
    else:
        handlers = runtime_handlers(runtime_path.read_text(encoding="utf-8"))
    battle_methods = source_methods((source_root / "battle/BattleLayer.js").read_text(encoding="utf-8"))
    unit_methods = source_methods((source_root / "battle/BattleUnit.js").read_text(encoding="utf-8"))
    stage_methods = source_methods((source_root / "ui/StageLayer.js").read_text(encoding="utf-8"))
    sites: dict[str, list[dict[str, Any]]] = defaultdict(list)
    for call in calls:
        sites[call["api"]].append(call)
    rows = []
    for api in sorted(sites):
        runtime_body = handlers.get(api)
        method_name = api.rsplit(".", 1)[-1]
        if "unit()." in api:
            owner, methods = "BattleUnit", unit_methods
        else:
            owner = "BattleScreen" if method_name in battle_methods else "StageLayer"
            methods = battle_methods if method_name in battle_methods else stage_methods
        source_body = expanded_source_body(method_name, methods)
        source_noop = source_body is not None and not re.sub(r"\s+", "", source_body)
        signals = source_signals(source_body) if source_body is not None else []
        barrier = source_has_barrier(signals)
        site_assessments = [
            _source_barrier_call_assessment(site, source_body)
            for site in sites[api]
            if site.get("lifecycleEvidence", {}).get("available") is not False
        ]
        # Missing call-site evidence is intentionally conservative.  It must
        # never turn a source callback into an accidental non-blocking result.
        missing_lifecycle_evidence = sum(
            site.get("lifecycleEvidence", {}).get("available") is False
            for site in sites[api]
        )
        required_site_assessments = [
            assessment for assessment in site_assessments
            if assessment["requiredForGameBarrier"]
        ]
        if missing_lifecycle_evidence and barrier:
            required_site_assessments.extend(
                {"classification": "missing_lifecycle_call_site_evidence", "requiredForGameBarrier": True}
                for _ in range(missing_lifecycle_evidence)
            )
        findings = []
        blocking_findings = []
        if runtime_body is None:
            finding = "unknown-handler-source-noop" if source_noop else "unknown-handler"
            findings.append(finding)
            if not source_noop:
                blocking_findings.append(finding)
        if source_body is None:
            findings.append("original-implementation-not-found")
            blocking_findings.append("original-implementation-not-found")
        eager_mutation = bool(runtime_body and game_mutates(runtime_body))
        runtime_barrier_evidence = game_battle_barrier_evidence(runtime_body or "")
        if barrier and runtime_body is not None and not runtime_barrier_evidence and required_site_assessments:
            finding = (
                "eager-mutation-without-source-callback-barrier"
                if eager_mutation else "source-barrier-not-preserved"
            )
            findings.append(finding)
            blocking_findings.append(finding)
        module_counts = Counter(site["module"] for site in sites[api])
        rows.append({
            "api": api, "callCount": len(sites[api]),
            "modules": sorted({site["module"] for site in sites[api]}),
            "callCountsByModule": dict(sorted(module_counts.items())),
            "exampleSites": sites[api][:20], "runtimeHandler": runtime_body is not None,
            "runtimeBattleBarrier": bool(runtime_barrier_evidence),
            "runtimeBattleBarrierEvidence": runtime_barrier_evidence,
            "runtimeEagerMutation": eager_mutation,
            "sourceOwner": owner, "sourceMethod": method_name,
            "sourceImplementation": source_body is not None, "sourceNoop": source_noop,
            "sourceSignals": signals, "sourceCallbackBarrier": barrier,
            "sourceBarrierCallSiteAssessment": {
                "callSitesWithEvidence": len(site_assessments),
                "missingLifecycleEvidence": missing_lifecycle_evidence,
                "possibleCallSites": sum(
                    assessment["possibleFromSourceCondition"] for assessment in site_assessments
                ),
                "requiredCallSites": len(required_site_assessments),
                "classifications": dict(sorted(Counter(
                    assessment.get("classification", "unclassified")
                    for assessment in site_assessments
                ).items())),
            },
            "findings": findings, "blockingFindings": blocking_findings,
        })
    finding_counts = Counter(finding for row in rows for finding in row["findings"])
    blocking_counts = Counter(finding for row in rows for finding in row["blockingFindings"])
    return {
        "format": FORMAT, "sourceDirectory": str(source_dir), "runtime": str(runtime_path),
        "sourceRoot": str(source_root), "scripts": len(scripts), "callSites": len(calls),
        "lifecycleEvidence": lifecycle_metadata,
        "apis": rows, "summary": {
            "apiCount": len(rows), "handledApis": sum(row["runtimeHandler"] for row in rows),
            "sourceResolvedApis": sum(row["sourceImplementation"] for row in rows),
            "sourceBarrierApis": sum(row["sourceCallbackBarrier"] for row in rows),
            "findingApis": sum(bool(row["findings"]) for row in rows),
            "findings": dict(sorted(finding_counts.items())),
            "blockingFindingApis": sum(bool(row["blockingFindings"]) for row in rows),
            "blockingFindings": dict(sorted(blocking_counts.items())),
        },
    }


def markdown(report: dict[str, Any]) -> str:
    summary = report["summary"]
    lines = [
        "# Battle stage API surface audit", "",
        f"Scanned {report['scripts']} S_*.py scripts, {report['callSites']} call sites, and {summary['apiCount']} APIs.", "",
        f"Findings: **{summary['findingApis']} APIs** ({json.dumps(summary['findings'], sort_keys=True)}).", "",
        f"Blocking gate findings: **{summary['blockingFindingApis']} APIs** ({json.dumps(summary['blockingFindings'], sort_keys=True)}).", "",
        "| API | Calls | Runtime | Original | Source barrier | Game battle barrier | Eager mutation | Findings |", "|---|---:|---|---|---|---|---|---|",
    ]
    for row in report["apis"]:
        lines.append(
            f"| `{row['api']}` | {row['callCount']} | {'yes' if row['runtimeHandler'] else 'NO'} | "
            f"{row['sourceOwner'] + '.' + row['sourceMethod'] if row['sourceImplementation'] else 'NOT FOUND'} | "
            f"{'yes' if row['sourceCallbackBarrier'] else 'no'} | {'yes' if row['runtimeBattleBarrier'] else 'no'} | "
            f"{'yes' if row['runtimeEagerMutation'] else 'no'} | {', '.join(row['findings']) or '—'} |"
        )
    lines.extend(["", "## Finding details", ""])
    for row in report["apis"]:
        if row["findings"]:
            lines.append(f"- `{row['api']}` ({row['callCount']} calls): {', '.join(row['findings'])}; source signals: {', '.join(row['sourceSignals']) or 'none'}")
    return "\n".join(lines) + "\n"


def main(argv: list[str] | None = None) -> int:
    root = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-dir", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/decompiled-python")
    parser.add_argument("--runtime", type=Path, default=root / "core/src/main/kotlin/com/jojo/game/ScenarioInterpreter.kt")
    parser.add_argument("--source-root", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules")
    parser.add_argument(
        "--lifecycle", type=Path,
        default=root / "tools/battle_stage_api_lifecycle_call_sites.json",
        help="persisted lifecycle report to compare with fresh AST evidence",
    )
    parser.add_argument("--json", type=Path, default=root / "tools/battle_stage_api_audit.json")
    parser.add_argument("--markdown", type=Path, default=root / "tools/battle_stage_api_audit.md")
    parser.add_argument("--allow-findings", action="store_true")
    args = parser.parse_args(argv)
    report = audit(
        args.source_dir.resolve(), args.runtime.resolve(), args.source_root.resolve(),
        args.lifecycle.resolve() if args.lifecycle else None,
    )
    args.json.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    args.markdown.write_text(markdown(report), encoding="utf-8")
    print(f"BATTLE_STAGE_API_AUDIT scripts={report['scripts']} calls={report['callSites']} apis={report['summary']['apiCount']} findings={report['summary']['findingApis']} blocking={report['summary']['blockingFindingApis']}")
    return 0 if args.allow_findings or not report["summary"]["blockingFindingApis"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
