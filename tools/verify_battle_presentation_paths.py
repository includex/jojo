#!/usr/bin/env python3
"""Fail when a gameplay battle action bypasses the visible callback chain."""

from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
LAYER = (ROOT / "core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt").read_text()
TURN = (ROOT / "core/src/main/kotlin/com/jojo/game/BattleTurnController.kt").read_text()

checks = {
    "incremental-ai": "battle.resolveAiTurn(maxUnits = 1)" in LAYER,
    "no-whole-turn-ai-in-renderer": "battle.resolveAiTurn()" not in LAYER,
    "ai-presentation-gate": "hasPendingAiPresentation = { activeAiCamp != null }" in LAYER,
    "move-action-order": all(token in LAYER for token in (
        "AiPresentationStage.FOCUS_DELAY",
        "AiPresentationStage.MOVING",
        "AiPresentationStage.ACTION_DELAY",
        "AiPresentationStage.ACTION",
    )),
    "ai-does-not-resume-scene-script": "continueBattleScript = false" in LAYER,
    "magic-is-a-blocking-presentation": "magicEffectAnimations.any { now < it.endsAt }" in LAYER,
    "abnormal-status-model-is-rendered": "drawUnitStateAnimation(unit)" in LAYER,
    "attribute-status-model-is-rendered": "drawUnitAttributeStatuses(unit)" in LAYER,
    "controller-awaits-ai": "completeAiPresentation" in TURN and TURN.count("hasPendingAiPresentation()") == 2,
}

# All real BattleScreen mutations must remain in the reviewed presentation
# entry points. Headless ScenarioBatchVerificationScreen intentionally has no
# renderer and is excluded from this gameplay audit.
expected_calls = {
    "resolveAiTurn": 1,
    "attack": 1,
    "castMagic": 2,
    "moveUnit": 1,
    "useProperty": 1,
}
for name, expected in expected_calls.items():
    actual = len(re.findall(rf"\bbattle\.{name}\(", LAYER))
    checks[f"reviewed-{name}-calls"] = actual == expected

failed = [name for name, ok in checks.items() if not ok]
if failed:
    raise SystemExit("BATTLE_PRESENTATION_PATHS_FAILED " + ",".join(failed))
print(
    "BATTLE_PRESENTATION_PATHS_OK "
    + " ".join(f"{name}={count}" for name, count in expected_calls.items())
    + " ai=focus-delay/move/action-delay/action/complete"
)
