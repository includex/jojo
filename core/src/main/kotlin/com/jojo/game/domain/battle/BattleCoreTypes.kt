// Battle
package com.jojo.game.domain.battle

import com.jojo.game.presentation.i18n.SystemMessage

/** Faction: 전투 유닛의 소속 진영을 나타내며, 턴 순서와 아군·적군 판정에 사용한다. */
enum class Faction { PLAYER, FRIEND, ENEMY, REINFORCEMENTS }

/**
 * `Faction`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun Faction.isEnemySide(): Boolean = this == Faction.ENEMY || this == Faction.REINFORCEMENTS
/**
 * `Faction`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun Faction.isPlayerSide(): Boolean = !isEnemySide()

/** BattleStatus: 유닛에게 적용되는 전투 상태 이상 종류를 나타내며, 턴 정산과 행동 제약에 사용한다. */
enum class BattleStatus {
    PARALYSIS, SILENCE, CONFUSION, POISON, LOST;

    companion object {
        /**
         * `fromSourceIndex`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun fromSourceIndex(index: Int): BattleStatus? = when (index) {
            7 -> PARALYSIS
            8 -> SILENCE
            9 -> CONFUSION
            10 -> POISON
            13 -> LOST
            else -> null
        }
    }
}

/** BattleAttribute: 공격·방어·정신·필살·사기·이동의 전투 능력치 종류를 정의한다. */
enum class BattleAttribute { ATTACK, DEFENSE, SPIRIT, CRITICAL, MORALE, MOVEMENT }

/** BattleWeather: 전장에 적용되는 날씨 종류를 나타내며, 라운드 전환 시 지형과 능력치 효과에 사용한다. */
enum class BattleWeather { CLEAR, CLOUDY, WINDY, HEAVY_RAIN, SNOW }

/**
 * `BattleStatus`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun BattleStatus.label(): String = when (this) {
    BattleStatus.PARALYSIS -> SystemMessage.S_38CA304494
    BattleStatus.SILENCE -> SystemMessage.S_682998EE62
    BattleStatus.CONFUSION -> SystemMessage.S_738A3F61E4
    BattleStatus.POISON -> SystemMessage.S_2B0B630E57
    BattleStatus.LOST -> SystemMessage.S_87F35DB204
}

/**
 * `BattleAttribute`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun BattleAttribute.label(): String = when (this) {
    BattleAttribute.ATTACK -> SystemMessage.S_50B8DF8C55
    BattleAttribute.DEFENSE -> SystemMessage.S_5C349008C3
    BattleAttribute.SPIRIT -> SystemMessage.S_F3F57B591C
    BattleAttribute.CRITICAL -> SystemMessage.S_9EC6283754
    BattleAttribute.MORALE -> SystemMessage.S_C91E618B9A
    BattleAttribute.MOVEMENT -> SystemMessage.S_B2A0B8369E
}
