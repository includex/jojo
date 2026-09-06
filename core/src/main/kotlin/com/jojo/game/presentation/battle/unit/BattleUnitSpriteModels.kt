// Battle
package com.jojo.game.presentation.battle.unit

/** 전투 스프라이트 원본: 유닛 애니메이션이 참조할 이동·공격·특수 아틀라스 종류를 정의한다. */
enum class UnitSpriteSource { MOVEMENT, ATTACK, SPECIAL }

/** 전투 스프라이트 프레임: 현재 유닛을 그릴 원본 영역, 좌우 반전, 화면 보정값을 정의한다. */
internal data class UnitSpriteFrame(
    val source: UnitSpriteSource,
    val sourceY: Int,
    val sourceWidth: Int = 48,
    val sourceHeight: Int = 48,
    val flipX: Boolean = false,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
)

/** 스크립트 유닛 시각 상태: 시나리오 명령이 지정한 동작 번호와 시작 시점을 보관한다. */
internal data class ScriptedUnitVisual(val action: Int, val startedAt: Float)
