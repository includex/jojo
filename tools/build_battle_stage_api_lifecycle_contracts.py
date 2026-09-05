#!/usr/bin/env python3
"""Generate source-grounded lifecycle contracts for flagged battle stage APIs."""
from __future__ import annotations

import argparse
import hashlib
import json
import re
from pathlib import Path
from typing import Any

from audit_battle_stage_api_surface import source_methods


FORMAT = "jojo-battle-stage-api-lifecycle-contracts/v1"


CONTRACTS: list[dict[str, Any]] = [
    {
        "api": "stage.info",
        "functions": [("ui/StageLayer.js", "info"), ("ui/StageLayer.js", "_info5"), ("ui/StageLayer.js", "info2")],
        "eagerMutation": ["Reads INFO_CTRL and deletes that global before deciding whether presentation is needed."],
        "pause": "Only when !_skip and INFO_CTRL is zero; calls _script.pause() before _info5.",
        "resume": "_info5 waits for each InfoLayer callback, then its final callback calls _script.resume(). Remaining chunks are skipped if _skip becomes true, but the final callback still runs.",
        "assetFailure": "No asset load is initiated by these functions. If InfoLayer never invokes func, its Promise remains pending and resume is unreachable.",
        "branches": ["_skip: no pause or presentation", "INFO_CTRL != 0: delegates to model.info without pausing", "normal: sequential <=~100-character InfoLayer chunks under one pause"],
        "anchors": {"info": [r"delGvars", r"_script\.pause", r"_info5"], "_info5": [r"new Promise", r"info2", r"r\(\)"], "info2": [r"InfoLayer", r"func: e"]},
    },
    {
        "api": "stage.loadBg",
        "functions": [("battle/BattleLayer.js", "loadBg"), ("battle/BattleLayer.js", "_loadBg")],
        "eagerMutation": ["If t < 0 or JUMP_OFFSET != 0, clears JUMP_OFFSET and rewrites the map index before pausing.", "After MiniMapLayer callback, writes BG_INDEX before JSON/texture loads complete."],
        "pause": "Unconditional at entry, before _loadBg.",
        "resume": "Only the success tail of _loadBg invokes the supplied callback; loadBg's callback calls resume(). Success requires MiniMapLayer callback, map JSON, map texture, and (flag 1) Promise.all unit avatar initialization.",
        "assetFailure": "JSON err returns break with status 1 and texture err returns break with status 2 before the callback, so the script remains paused. A missing MiniMapLayer callback or rejected avatar Promise also prevents resume; there is no finally fallback.",
        "branches": ["jump-offset normalization occurs before pause", "flag 1 refreshes all unit avatars before callback"],
        "anchors": {"loadBg": [r"setGVars", r"this\.pause", r"e\.resume"], "_loadBg": [r"MiniMapLayer", r"if \(\(s = _\.sent\(\)\)\.err\)", r"if \(\(u = _\.sent\(\)\)\.err\)", r"Promise\.all", r"e && e\(\)"]},
    },
    {
        "api": "stage.setRectUnitHide",
        "functions": [("battle/BattleLayer.js", "setRectUnitHide"), ("battle/BattleLayer.js", "ctrlUnitHide"), ("battle/BattleLayer.js", "unitHide")],
        "eagerMutation": ["No unit mutation occurs before pause. During the paused coroutine each unit is centered, marked RETREAT, HP is set to zero, and the authored hide action starts; after its callback visibility is cleared, HP restored, and retreat count may increment."],
        "pause": "Only when the rectangle search returns at least one unit; empty selection returns synchronously.",
        "resume": "ctrlUnitHide invokes its completion callback after serial unitHide completion for every unit. Each unit advances on optional retreat-dialogue callback and setAction2 completion callback.",
        "assetFailure": "No asset load. Missing dialogue/action callback, generator exception, or rejected child operation prevents the final callback; there is no finally resume.",
        "branches": ["multiple units are sorted before pause", "BAI_TUI can wait for retire dialogue and converts the self master to death animation"],
        "anchors": {"setRectUnitHide": [r"searchUnitByRect", r"this\.pause", r"ctrlUnitHide", r"resume\.bind"], "ctrlUnitHide": [r"unitHide", r"r && r\(\)"], "unitHide": [r"say4", r"setFlag", r"setCurHp\(0\)", r"setAction2", r"setVisible\(!1\)"]},
    },
    {
        "api": "stage.setStageName",
        "functions": [("ui/StageLayer.js", "setStageName"), ("ui/StageLayer.js", "info2")],
        "eagerMutation": ["Model.setStageName(t) runs first, before any presentation or pause."],
        "pause": "Only when !_skip && _isDraw.",
        "resume": "InfoLayer receives resume.bind(this) as func; resume occurs only when that layer invokes the callback.",
        "assetFailure": "No direct asset load. Failure to create/complete InfoLayer or invoke func leaves the script paused; no fallback is present.",
        "branches": ["skip or not-yet-drawn: model mutation only, synchronous return"],
        "anchors": {"setStageName": [r"setStageName\(t\)", r"!this\._skip && this\._isDraw", r"this\.pause", r"resume\.bind"], "info2": [r"InfoLayer", r"func: e"]},
    },
    {
        "api": "stage.setUnitAttr",
        "functions": [("battle/BattleLayer.js", "setUnitAttr"), ("ui/StageLayer.js", "setUnitAttr"), ("battle/BattleLayer.js", "_loadAvatar"), ("battle/BattleUnit.js", "loadAvatar"), ("framework/UILayer.js", "loadUnitPicture")],
        "eagerMutation": ["Battle-specific X/Y/action/direction/HP/MP/status branches mutate the live BattleUnit synchronously.", "The default branch first calls StageLayer.setUnitAttr, which updates model/base or additive attributes synchronously; equipment is then updated synchronously.", "S_AVATAR/POSTS call _loadAvatar only after the model mutation and only when a live battle unit exists."],
        "pause": "Only the live-unit S_AVATAR/POSTS path pauses, inside _loadAvatar before awaiting BattleUnit.loadAvatar. Other attributes and absent live units return without pause.",
        "resume": "_loadAvatar resumes immediately after the awaited loadAvatar resolves; it does not use try/finally.",
        "assetFailure": "Normal cc.loader/loadByUrl errors are converted by loadUnitPicture into resolved {code,data} entries, so loadAvatar still sets the avatar (possibly from a partial/empty array), runs defaultAction, and _loadAvatar resumes. A thrown/rejected Promise bypasses resume and strands the pause.",
        "branches": ["INDEX is ignored", "equipment WQ/HJ/FZ updates equipItem", "only S_AVATAR/POSTS trigger avatar refresh"],
        "anchors": {"setUnitAttr": [r"t\.prototype\.setUnitAttr", r"equipItem", r"_loadAvatar"], "_loadAvatar": [r"this\.pause", r"t\.loadAvatar", r"this\.resume"], "loadAvatar": [r"loadUnitPicture", r"_setAvater", r"defaultAction"], "loadUnitPicture": [r"code: t\.err", r"Promise\.all", r"return \[ 2, I \]"], "ui/StageLayer.js:setUnitAttr": [r"setUnitAttr2"]},
    },
    {
        "api": "stage.unit().setPosts",
        "functions": [("battle/BattleUnit.js", "setPosts"), ("battle/BattleUnit.js", "testAvatar"), ("battle/BattleUnit.js", "loadAvatar"), ("framework/UILayer.js", "loadUnitPicture")],
        "eagerMutation": ["_unit.setPosts(t,e) always mutates model state before avatar testing, pause, or load."],
        "pause": "Only when flag bit 16 is set and testAvatar() reports an attached node whose computed avatar differs. Bit 16 with no change/node returns after mutation without pause; other flags start loadAvatar without waiting or pausing.",
        "resume": "On the paused branch, loadAvatar's callback resumes after loadUnitPicture resolves, _setAvater runs, and defaultAction is restored.",
        "assetFailure": "loadUnitPicture converts normal loader errors to resolved code entries and returns available frames, so the callback/resume still occurs (possibly with partial/empty frames). Unexpected rejection/throw prevents callback and resume. testAvatar guards the no-node/already-current cases before pausing.",
        "branches": ["flag 16 + avatar change: blocking reload", "flag 16 + no change: synchronous", "without flag 16: nonblocking fire-and-forget reload"],
        "anchors": {"setPosts": [r"_unit\.setPosts", r"16 & e", r"testAvatar", r"_battleLayer\.pause", r"loadAvatar", r"_battleLayer\.resume"], "testAvatar": [r"!this\._node", r"this\._avatar != t"], "loadAvatar": [r"loadUnitPicture", r"t && t\(\)"], "loadUnitPicture": [r"code: t\.err", r"Promise\.all", r"return \[ 2, I \]"]},
    },
]


def _line_of(text: str, method: str) -> int:
    match = re.search(rf"\.prototype\.{re.escape(method)}\s*=\s*function", text)
    if not match:
        raise ValueError(f"source method not found: {method}")
    return text.count("\n", 0, match.start()) + 1


def build(source_root: Path) -> dict[str, Any]:
    file_cache: dict[str, tuple[str, dict[str, str]]] = {}
    contracts = []
    for definition in CONTRACTS:
        item = {key: value for key, value in definition.items() if key not in {"functions", "anchors"}}
        refs = []
        anchors = definition["anchors"]
        for relative, method in definition["functions"]:
            if relative not in file_cache:
                text = (source_root / relative).read_text(encoding="utf-8")
                file_cache[relative] = (text, source_methods(text))
            text, methods = file_cache[relative]
            body = methods.get(method)
            if body is None:
                raise ValueError(f"{relative}:{method} not found")
            patterns = anchors.get(f"{relative}:{method}", anchors.get(method, []))
            missing = [pattern for pattern in patterns if not re.search(pattern, body)]
            if missing:
                raise ValueError(f"{relative}:{method} missing evidence anchors: {missing}")
            refs.append({
                "file": relative, "method": method, "line": _line_of(text, method),
                "bodySha256": hashlib.sha256(body.encode()).hexdigest(),
                "evidenceAnchors": patterns,
            })
        item["sourceFunctions"] = refs
        contracts.append(item)
    return {"format": FORMAT, "sourceRoot": str(source_root), "contracts": contracts}


def markdown(report: dict[str, Any]) -> str:
    lines = ["# Battle stage API lifecycle contracts", "", "Recovered-JS callback contracts for the six APIs currently blocked by the surface audit. Line numbers and body hashes in the JSON make source drift detectable.", ""]
    for item in report["contracts"]:
        refs = ", ".join(f"`{ref['file']}:{ref['line']} {ref['method']}()`" for ref in item["sourceFunctions"])
        lines += [f"## `{item['api']}`", "", f"Source functions: {refs}", "", "Eager mutation:", ""]
        lines += [f"- {value}" for value in item["eagerMutation"]]
        lines += ["", f"Pause: {item['pause']}", "", f"Resume: {item['resume']}", "", f"Asset/failure path: {item['assetFailure']}", "", "Branches:", ""]
        lines += [f"- {value}" for value in item["branches"]]
        lines.append("")
    return "\n".join(lines)


def main(argv: list[str] | None = None) -> int:
    root = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-root", type=Path, default=root.parent / "jojo_mobile/sgccz-desktop/recovered-js/modules")
    parser.add_argument("--json", type=Path, default=root / "tools/battle_stage_api_lifecycle_contracts.json")
    parser.add_argument("--markdown", type=Path, default=root / "tools/battle_stage_api_lifecycle_contracts.md")
    args = parser.parse_args(argv)
    report = build(args.source_root.resolve())
    args.json.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    args.markdown.write_text(markdown(report), encoding="utf-8")
    print(f"BATTLE_STAGE_API_LIFECYCLE_CONTRACTS apis={len(report['contracts'])}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
