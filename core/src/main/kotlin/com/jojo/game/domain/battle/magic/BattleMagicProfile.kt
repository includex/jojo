// Battle
package com.jojo.game.domain.battle.magic

/** BattleMagicProfile: 전투 마법 프로필이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleMagicProfile {
    /**
     * `id` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val id: Int
    /**
     * `name` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val name: String
    /**
     * `type` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val type: Int
    /**
     * `target` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val target: Int
    /**
     * `hitArea` (BattleMagicHitArea): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hitArea: BattleMagicHitArea
    /**
     * `effectAreaId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val effectAreaId: Int
    /**
     * `effectOffsets` (Set<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val effectOffsets: Set<Pair<Int, Int>>
    /**
     * `expendMp` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val expendMp: Int
    /**
     * `power` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val power: Int
    /**
     * `harmType` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val harmType: Int
    /**
     * `category` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val category: Int
    /**
     * `effectId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val effectId: Int
    /**
     * `condition` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val condition: Int
    /**
     * `aiUse` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val aiUse: Int
    /**
     * `hitRateLimit` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hitRateLimit: Int
    /**
     * `icon` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val icon: Int
    /**
     * `intro` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val intro: String
}

/** BattleMagicHitArea: 전투 마법 Hit 범위이며, 전투 계층 사이에서 필요한 동작과 데이터를 약속한다. */
interface BattleMagicHitArea {
    /**
     * `id` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val id: Int
    /**
     * `offsets` (Set<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val offsets: Set<Pair<Int, Int>>
    /**
     * `allScreen` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val allScreen: Boolean
    /**
     * `upgradeId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val upgradeId: Int
}

/**
 * `BattleMagicProfileValue` 클래스: magic 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleMagicProfileValue(
    /**
     * `id` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val id: Int,
    /**
     * `name` (String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val name: String,
    /**
     * `type` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val type: Int,
    /**
     * `target` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val target: Int,
    /**
     * `hitArea` (BattleMagicHitArea,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val hitArea: BattleMagicHitArea,
    /**
     * `effectAreaId` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val effectAreaId: Int,
    /**
     * `effectOffsets` (Set<Pair<Int, Int>>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val effectOffsets: Set<Pair<Int, Int>>,
    /**
     * `expendMp` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val expendMp: Int,
    /**
     * `power` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val power: Int,
    /**
     * `harmType` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val harmType: Int,
    /**
     * `category` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val category: Int,
    /**
     * `effectId` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val effectId: Int = 255,
    /**
     * `condition` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val condition: Int = -1,
    /**
     * `aiUse` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val aiUse: Int = 0,
    /**
     * `hitRateLimit` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val hitRateLimit: Int = 0,
    /**
     * `icon` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val icon: Int = 0,
    /**
     * `intro` (String): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val intro: String = "",
) : BattleMagicProfile

/**
 * `BattleMagicHitAreaValue` 클래스: magic 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleMagicHitAreaValue(
    /**
     * `id` (Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val id: Int,
    /**
     * `offsets` (Set<Pair<Int, Int>>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val offsets: Set<Pair<Int, Int>>,
    /**
     * `allScreen` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val allScreen: Boolean = false,
    /**
     * `upgradeId` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val upgradeId: Int = id,
) : BattleMagicHitArea
