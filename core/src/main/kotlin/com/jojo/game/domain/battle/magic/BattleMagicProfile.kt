// Battle
package com.jojo.game.domain.battle.magic

/** BattleMagicProfile: 전투 마법 프로필이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleMagicProfile {
    val id: Int
    val name: String
    val type: Int
    val target: Int
    val hitArea: BattleMagicHitArea
    val effectAreaId: Int
    val effectOffsets: Set<Pair<Int, Int>>
    val expendMp: Int
    val power: Int
    val harmType: Int
    val category: Int
    val effectId: Int
    val condition: Int
    val aiUse: Int
    val hitRateLimit: Int
    val icon: Int
    val intro: String
}

/** BattleMagicHitArea: 전투 마법 Hit 범위이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleMagicHitArea {
    val id: Int
    val offsets: Set<Pair<Int, Int>>
    val allScreen: Boolean
    val upgradeId: Int
}

data class BattleMagicProfileValue(
    override val id: Int,
    override val name: String,
    override val type: Int,
    override val target: Int,
    override val hitArea: BattleMagicHitArea,
    override val effectAreaId: Int,
    override val effectOffsets: Set<Pair<Int, Int>>,
    override val expendMp: Int,
    override val power: Int,
    override val harmType: Int,
    override val category: Int,
    override val effectId: Int = 255,
    override val condition: Int = -1,
    override val aiUse: Int = 0,
    override val hitRateLimit: Int = 0,
    override val icon: Int = 0,
    override val intro: String = "",
) : BattleMagicProfile

data class BattleMagicHitAreaValue(
    override val id: Int,
    override val offsets: Set<Pair<Int, Int>>,
    override val allScreen: Boolean = false,
    override val upgradeId: Int = id,
) : BattleMagicHitArea
