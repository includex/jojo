// Battle
package com.jojo.game.application.runtime

/** 전투 검증 런타임: 자동 검증의 시작 조건과 완료 검사를 화면 구현에서 분리한다. */
class BattleVerificationRuntime(
    private val tutorial: Boolean,
    private val scripted: Boolean,
) {
    /** 활성 상태: 일반 전투 흐름을 중단해야 하는 자동 검증 실행 여부를 반환한다. */
    val active get() = tutorial || scripted

    /** 튜토리얼 상태: 전용 전투 편성을 사용할지 반환한다. */
    val usesTutorialBattle get() = tutorial

    /** 스크립트 상태: 원본 스테이지 변환 검증 실행 여부를 반환한다. */
    val usesScriptedBattle get() = scripted

    /** 완료 시점: 자동 검증 결과를 확인할 프레임 시점을 판별한다. */
    fun isReady(elapsed: Float): Boolean = elapsed > 0.8f

    /** 튜토리얼 결과: 턴 진행과 증원 유닛 검증에 필요한 상태를 확인한다. */
    fun validateTutorial(round: Int, firedEventIds: Set<String>, unitIds: Set<String>) {
        check("reinforcement-arrival" in firedEventIds) { "Battle reinforcement event did not fire" }
        check("reinforcement" in unitIds) { "Battle reinforcement did not join" }
    }

    /** 스크립트 결과: 원본 스테이지·전장·유닛 변환 결과를 확인한다. */
    fun validateScripted(input: ScriptedBattleValidationInput) {
        check(input.sourceUnitCount > 0) { "${input.sourceScenario} 원본 전투 유닛이 없습니다." }
        check(input.mapAvailable) { "${input.sourceScenario} 원본 HM 전장 이미지를 찾을 수 없습니다: ${input.mapIndex + 1}" }
        check(input.actualUnitCount == input.sourceUnitCount) { "${input.sourceScenario} 전투 유닛 변환 수가 일치하지 않습니다." }
        if (input.sourceHasPlayerUnit) {
            check(input.hasPlayerUnit) { "${input.sourceScenario} 플레이어 유닛 변환에 실패했습니다." }
        }
        check(input.hasEnemyUnit) { "${input.sourceScenario} 적군이 없습니다." }
        check(input.hasRenderableUnit) { "${input.sourceScenario} 원본 유닛 스프라이트를 찾을 수 없습니다." }
    }
}

/** 스크립트 전투 검증 입력: 스테이지 변환 결과의 핵심 수치를 전달한다. */
data class ScriptedBattleValidationInput(
    val sourceScenario: String,
    val sourceUnitCount: Int,
    val mapIndex: Int,
    val mapAvailable: Boolean,
    val actualUnitCount: Int,
    val sourceHasPlayerUnit: Boolean,
    val hasPlayerUnit: Boolean,
    val hasEnemyUnit: Boolean,
    val hasRenderableUnit: Boolean,
)
