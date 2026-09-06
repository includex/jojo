// Battle
package com.jojo.game.domain.battle

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
    BattleStatus.PARALYSIS -> "마비"
    BattleStatus.SILENCE -> "금주"
    BattleStatus.CONFUSION -> "혼란"
    BattleStatus.POISON -> "중독"
    BattleStatus.LOST -> "길 잃음"
}

/**
 * `BattleAttribute`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

fun BattleAttribute.label(): String = when (this) {
    BattleAttribute.ATTACK -> "공격력"
    BattleAttribute.DEFENSE -> "방어력"
    BattleAttribute.SPIRIT -> "정신력"
    BattleAttribute.CRITICAL -> "폭발력"
    BattleAttribute.MORALE -> "사기"
    BattleAttribute.MOVEMENT -> "이동력"
}
