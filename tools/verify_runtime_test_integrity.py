#!/usr/bin/env python3
"""Ratchet that keeps fixture/oracle checks distinct from runtime validation.

The source/port pairwise tasks listed below are useful isolated conformance
oracles, but they do not enter the game through a normal screen route.  They
must not be dependencies of the default ``core:test`` lifecycle until their
production types have a non-fixture caller.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MAIN = ROOT / "core/src/main/kotlin"
BUILD = ROOT / "core/build.gradle.kts"
ROOT_BUILD = ROOT / "build.gradle.kts"
DESKTOP_BUILD = ROOT / "desktop/build.gradle.kts"
YINGCHUAN_CHECK = ROOT / "tools/verify_yingchuan_battle_regression.mjs"
BASELINE = ROOT / "tools/runtime_test_integrity_baseline.json"

ISOLATED_GATES = (
    "verifyHeadPairwise",
    "verifyBattleScenePortBehavior",
    "verifyProgressionLayerPairwise",
    "verifyEditMutationPairwise",
    "verifyUnitListInfoPairwise",
)

# These are the runtime-looking implementations called out by the audit, plus
# the backing types of the isolated harnesses.  Keeping the list explicit
# avoids treating DTOs and private renderer helpers as production entry points.
TRACKED_TYPES = (
    "LoadingLayer", "LoginRegistrationCheckFlow",
    "BattleScenePort",
    "BattleAttackSequence",
    "AchievementsLayerPort", "SignInLayerPort", "RaffleLayerPort",
    "RegisterLayerPort", "BattleEditLayer2",
    "EditRosterFlow", "BattleUnitEditLayer", "HallUnitListLayer",
    "MineUnitInfoLayer", "OtherUnitInfoLayer", "ControlControllerFactory",
)

NON_RUNTIME_NAME = re.compile(r"(?:TraceHarness|Fixture|Test)")


def production_references(type_name: str) -> list[str]:
    declaration = re.compile(rf"\b(?:class|object|interface|enum\s+class)\s+{re.escape(type_name)}\b")
    token = re.compile(rf"\b{re.escape(type_name)}\b")
    refs: list[str] = []
    for path in MAIN.rglob("*.kt"):
        if NON_RUNTIME_NAME.search(path.stem):
            continue
        text = path.read_text(encoding="utf-8")
        if not token.search(text):
            continue
        # A type's declaration and KDoc self-link are not runtime callers.
        without_declaration = declaration.sub("", text)
        without_comments = re.sub(r"/\*.*?\*/|//[^\n]*", "", without_declaration, flags=re.S)
        if token.search(without_comments):
            refs.append(str(path.relative_to(ROOT)))
    return sorted(refs)


def main() -> int:
    errors: list[str] = []
    gradle = BUILD.read_text(encoding="utf-8")
    root_gradle = ROOT_BUILD.read_text(encoding="utf-8")
    desktop_gradle = DESKTOP_BUILD.read_text(encoding="utf-8")
    yingchuan_check = YINGCHUAN_CHECK.read_text(encoding="utf-8")
    for task in ISOLATED_GATES:
        if re.search(rf"tasks\.test\s*\{{[^}}]*dependsOn\(\s*{task}\s*\)", gradle, re.S):
            errors.append(f"isolated fixture gate is wired into core:test: {task}")
        behavior_body = root_gradle.split('tasks.register("verifyBehaviorPairwise")', 1)[-1].split(
            'tasks.register("verifyIsolatedFixtureOracles")', 1
        )[0]
        if f'":core:{task}"' in behavior_body:
            errors.append(f"isolated fixture gate is wired into verifyBehaviorPairwise: {task}")
        if f'":core:{task}"' not in root_gradle.split('tasks.register("verifyIsolatedFixtureOracles")', 1)[-1]:
            errors.append(f"isolated fixture gate is missing from the explicit oracle aggregate: {task}")

    # These used to be named as runtime/screen tests even though they invoke
    # renderer-independent production contracts directly. Keep the coverage,
    # but prevent it from being presented as a live Screen route.
    for misleading in ("naturalCampaignRuntimeTest", "titleUiRuntimeTest"):
        if misleading in gradle:
            errors.append(f"isolated production contract is mislabeled as runtime coverage: {misleading}")
    contract_tasks = {
        "titleInteractionContractTest": "com.jojo.port.TitleInteractionTest",
    }
    for task, test_class in contract_tasks.items():
        start = gradle.find(f'tasks.register<Test>("{task}")')
        if start < 0:
            errors.append(f"missing isolated production-contract task: {task}")
            continue
        body = gradle[start:start + 1200]
        if test_class not in body:
            errors.append(f"{task} no longer selects {test_class}")
        if "isFailOnNoMatchingTests = true" not in body:
            errors.append(f"{task} can pass without executing its selected test class")

    # This gate uses a deterministic roster bootstrap, but it must still launch
    # DesktopLauncher/BattleLayer, produce fresh evidence, and belong to check.
    capture_start = desktop_gradle.find('tasks.register<JavaExec>("captureYingchuanBattleRegressionTrace")')
    verify_start = desktop_gradle.find('tasks.register<Exec>("verifyYingchuanBattleRegression")')
    capture_body = desktop_gradle[capture_start:verify_start] if 0 <= capture_start < verify_start else ""
    required_capture = (
        'mainClass.set("com.jojo.port.desktop.DesktopLauncher")',
        '"--scenario=S_00"',
        '"--full-battle-trace=',
        "doFirst { delete(yingchuanBattleRegressionTrace.get().asFile) }",
    )
    for required in required_capture:
        if required not in capture_body:
            errors.append(f"Yingchuan production capture is missing required wiring: {required}")
    for forbidden in ("--capture-state", "--verify-win", "--verify-attributes"):
        if forbidden in capture_body:
            errors.append(f"Yingchuan production capture uses fixture/forced-result option: {forbidden}")
    if verify_start < 0 or "dependsOn(captureYingchuanBattleRegressionTrace)" not in desktop_gradle[verify_start:]:
        errors.append("Yingchuan verifier can run without the production capture task")
    check_body = desktop_gradle.split('tasks.named("check")', 1)[-1]
    if "verifyYingchuanBattleRegression" not in check_body:
        errors.append("Yingchuan production regression is not wired into desktop:check")
    for assertion in ('data.engine, "libgdx-port"', "data.frames.length > 0", "data.summary?.outcome"):
        if assertion not in yingchuan_check:
            errors.append(f"Yingchuan verifier lacks production trace assertion: {assertion}")

    observed = {name: production_references(name) for name in TRACKED_TYPES}
    harness_only = sorted(name for name, refs in observed.items() if not refs)
    baseline = json.loads(BASELINE.read_text(encoding="utf-8"))
    categories=("runtimeIntegrationDebt","isolatedSourceContracts","obsoleteDuplicates")
    known = sorted({name for category in categories for name in baseline[category]})
    duplicates=set(baseline["obsoleteDuplicates"])
    if duplicates:
        errors.append("obsolete duplicate types must be removed, not baselined: "+", ".join(sorted(duplicates)))
    newly_orphaned = sorted(set(harness_only) - set(known))
    if newly_orphaned:
        errors.append("new production types have no non-fixture caller: " + ", ".join(newly_orphaned))

    resolved = sorted(set(known) - set(harness_only))
    stale = sorted(set(known) - set(TRACKED_TYPES))
    if stale:
        errors.append("baseline names are no longer tracked: " + ", ".join(stale))

    # Direct expected-JSON branches are allowed only in explicitly isolated
    # gates above.  This check prevents silently promoting them to core:test.
    harness_files = [MAIN / f"com/jojo/port/{name}.kt" for name in (
        "HeadTraceHarness", "BattleBootstrapTraceHarness", "ProgressionLayerTraceHarness",
        "EditMutationTraceHarness", "UnitListInfoLayerTraceHarness",
    )]
    missing = [str(p.relative_to(ROOT)) for p in harness_files if not p.exists()]
    if missing:
        errors.append("audited harness disappeared without policy update: " + ", ".join(missing))

    if errors:
        print("RUNTIME_TEST_INTEGRITY_FAILED")
        for error in errors:
            print(f"- {error}")
        return 1
    print(
        "RUNTIME_TEST_INTEGRITY_OK "
        f"isolatedGates={len(ISOLATED_GATES)} trackedTypes={len(TRACKED_TYPES)} "
        f"knownHarnessOnly={len(harness_only)} integrationDebt={len(baseline['runtimeIntegrationDebt'])} "
        f"isolatedContracts={len(baseline['isolatedSourceContracts'])} resolved={','.join(resolved) or '-'}"
    )
    return 0


if __name__ == "__main__":
    sys.exit(main())
