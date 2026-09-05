package com.jojo.game.domain.battle

/** Original BATTLE_CAMP: Mine, Friend, Enemy, Reinforcements. */
enum class Faction { PLAYER, FRIEND, ENEMY, REINFORCEMENTS }

fun Faction.isEnemySide(): Boolean = this == Faction.ENEMY || this == Faction.REINFORCEMENTS
fun Faction.isPlayerSide(): Boolean = !isEnemySide()

/** Original BATTLE_UNIT_STATUS2 persistent abnormal states. */
enum class BattleStatus {
    PARALYSIS, SILENCE, CONFUSION, POISON, LOST;

    companion object {
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

/** Original BATTLE_UNIT_STATUS2 0..5: temporary ability lift/down states. */
enum class BattleAttribute { ATTACK, DEFENSE, SPIRIT, CRITICAL, MORALE, MOVEMENT }

enum class BattleWeather { CLEAR, CLOUDY, WINDY, HEAVY_RAIN, SNOW }

fun BattleStatus.label(): String = when (this) {
    BattleStatus.PARALYSIS -> "마비"
    BattleStatus.SILENCE -> "금주"
    BattleStatus.CONFUSION -> "혼란"
    BattleStatus.POISON -> "중독"
    BattleStatus.LOST -> "길 잃음"
}

fun BattleAttribute.label(): String = when (this) {
    BattleAttribute.ATTACK -> "공격력"
    BattleAttribute.DEFENSE -> "방어력"
    BattleAttribute.SPIRIT -> "정신력"
    BattleAttribute.CRITICAL -> "폭발력"
    BattleAttribute.MORALE -> "사기"
    BattleAttribute.MOVEMENT -> "이동력"
}
