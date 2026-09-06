// Battle
package com.jojo.game.presentation.battle.verification

import com.badlogic.gdx.Gdx
import com.jojo.game.application.battle.Battle
import com.jojo.game.application.runtime.BattleVerificationRuntime
import com.jojo.game.application.runtime.ScriptedBattleValidationInput
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.isEnemySide

/** 전투 화면 검증 입력: 검증기가 필요로 하는 전투·시나리오 관측값을 화면 밖에서 전달한다. */
internal data class BattleScreenVerificationInput(
    val elapsed: Float,
    val sourceScenario: String,
    val sourceUnitCount: Int,
    val mapIndex: Int,
    val mapAvailable: Boolean,
    val battle: Battle,
    val sourceHasPlayerUnit: Boolean,
    val hasRenderableUnit: (BattleUnit) -> Boolean,
    val advanceTurn: () -> Unit,
)

/** 전투 화면 검증 결과: 검증 완료 뒤 기록할 전투 상태를 종류별로 보관한다. */
private sealed interface BattleScreenVerificationResult {
    data class Tutorial(val round: Int, val firstEventId: String) : BattleScreenVerificationResult
    data class Scripted(val sourceScenario: String, val unitCount: Int) : BattleScreenVerificationResult
}

/** 전투 화면 검증 조정기: 튜토리얼·스크립트 전투의 검증 입력 구성과 판정을 화면 렌더링에서 분리한다. */
internal object BattleScreenVerificationCoordinator {
    /** 검증: 준비 시각에 도달한 검증 모드만 실행하고, 완료된 검증의 표시용 결과를 돌려준다. */
    private fun validate(runtime: BattleVerificationRuntime, input: BattleScreenVerificationInput): BattleScreenVerificationResult? {
        if (runtime.usesTutorialBattle && runtime.isReady(input.elapsed)) {
            input.advanceTurn()
            input.advanceTurn()
            runtime.validateTutorial(input.battle.round, input.battle.firedEventIds, input.battle.units.keys)
            return BattleScreenVerificationResult.Tutorial(input.battle.round, input.battle.firedEventIds.first())
        }
        if (runtime.usesScriptedBattle && runtime.isReady(input.elapsed)) {
            val units = input.battle.units.values
            runtime.validateScripted(
                ScriptedBattleValidationInput(
                    input.sourceScenario, input.sourceUnitCount, input.mapIndex, input.mapAvailable, units.size,
                    input.sourceHasPlayerUnit, units.any { it.type() == Faction.PLAYER },
                    units.any { it.baseFaction.isEnemySide() }, units.any(input.hasRenderableUnit),
                )
            )
            return BattleScreenVerificationResult.Scripted(input.sourceScenario, units.size)
        }
        return null
    }

    /** 검증 종료: 준비된 튜토리얼·스크립트 검증을 실행하고 결과를 기록한 뒤 화면 런타임을 종료한다. */
    fun validateAndFinish(runtime: BattleVerificationRuntime, input: BattleScreenVerificationInput) {
        when (val result = validate(runtime, input)) {
            is BattleScreenVerificationResult.Tutorial -> {
                Gdx.app.log("JojoGame", "VERIFY_BATTLE_OK: round=${result.round}, event=${result.firstEventId}")
                Gdx.app.exit()
            }

            is BattleScreenVerificationResult.Scripted -> {
                Gdx.app.log(
                    "JojoGame",
                    "VERIFY_SCRIPTED_BATTLE_OK: ${result.sourceScenario} ${result.unitCount} source units rendered into tactical state",
                )
                Gdx.app.exit()
            }

            null -> Unit
        }
    }
}
