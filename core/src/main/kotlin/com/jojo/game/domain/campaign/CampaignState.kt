// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.infrastructure.data.GameDataCatalog
import kotlin.collections.ArrayDeque
import kotlin.random.Random

/** CampaignState: 캠페인 진행 중 변하는 금전·유닛·인벤토리·전역 변수와 종료 정보를 보존한다. */
class CampaignState(private val randomSource: (Int) -> Int = { upperExclusive -> Random.nextInt(upperExclusive) }) {
    /** injectedInfoTransferRandomValues: 정보 전달 스크립트의 결과를 재현하기 위해 우선 소비할 난수열이다. */
    private val injectedInfoTransferRandomValues = ArrayDeque<Int>()

    /** extraInfo: 정보 전달로 획득한 문구·기술·분기 데이터를 순서대로 기록한다. */
    val extraInfo = mutableListOf<CampaignInfo>()
    /** globalVariables: 시나리오와 전투가 공유하는 정수 주소 기반의 전역 값 저장소다. */
    val globalVariables = linkedMapOf<Int, Any?>()
    /** money: 캠페인 상점·보상에서 사용하는 현재 금전이며 0 이상으로 제한된다. */
    var money: Int = 0
        private set

    /**
     * `addMoney`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addMoney(delta: Int) {
        money = (money.toLong() + delta).coerceIn(0L, 9_999_999L).toInt()
    }

    /**
     * `setInfoTransferRandomSequence`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setInfoTransferRandomSequence(values: Iterable<Int>) {
        injectedInfoTransferRandomValues.clear()
        values.forEach { injectedInfoTransferRandomValues.addLast(it) }
    }

    /**
     * `random`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun random(upperExclusive: Int): Int {
        if (injectedInfoTransferRandomValues.isEmpty()) return randomSource(upperExclusive)
        return injectedInfoTransferRandomValues.removeFirst().also {
            require(it in 0 until upperExclusive) { "infoTransfer random value $it is outside 0..${upperExclusive - 1}" }
        }
    }

    /**
     * `unitAttributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitAttributes = linkedMapOf<Int, MutableMap<Int, Int>>()
    /**
     * `unitNames` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitNames = linkedMapOf<Int, String>()
    /**
     * `joinedUnits` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val joinedUnits = linkedSetOf<Int>()
    /**
     * `extraMagic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val extraMagic = linkedMapOf<Pair<Int, Int>, CampaignMagic>()
    /**
     * `talents` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val talents = linkedMapOf<Pair<Int, Int>, CampaignTalent>()
    /**
     * `formationTalents` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val formationTalents = mutableListOf<String>()
    /**
     * `inventory` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val inventory = CampaignInventory(joinedUnitIds = { joinedUnits }, unitAttribute = ::unitAttribute)
    /**
     * `equipmentProgression` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val equipmentProgression = CampaignEquipmentProgression(inventory)
    /**
     * `roster` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val roster = CampaignRoster { joinedUnits }
    /**
     * `endingId` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var endingId: Int? = null
        private set

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reset() {
        money = 0
        globalVariables.clear()
        extraInfo.clear()
        unitAttributes.clear()
        unitNames.clear()
        joinedUnits.clear()
        extraMagic.clear()
        talents.clear()
        formationTalents.clear()
        inventory.reset()
        roster.reset()
        endingId = null
    }

    /**
     * `unitAttribute`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int =
        unitAttributes[unitId]?.get(attribute) ?: default

    /**
     * `setUnitAttribute`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) {
        unitAttributes.getOrPut(unitId) { linkedMapOf() }[attribute] = value
    }

    /**
     * `setUnitPosts`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog,
        registeredFeatures: Int = 0,
    ): CampaignUnitPostsChange? {
        /**
         * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val profile = data.unitProfile(unitId) ?: return null
        /**
         * `oldPosts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldPosts = unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        /**
         * `postsWritten` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val postsWritten = flags and 2 == 0 || oldPosts != posts
        if (postsWritten) setUnitAttribute(unitId, UNIT_ATTR_POSTS, posts)
        /**
         * `mine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mine = unitAttribute(unitId, UNIT_ATTR_JOIN, 0) != 0
        /**
         * `refreshAbility` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val refreshAbility = flags and 8 == 0 && mine && (
            flags and 4 != 0 ||
                (globalVariables[GLOBAL_SJCS] as? Number)?.toInt() == 1 ||
                registeredFeatures and ENABLED_FEATURE_ZZSJCS != 0
            )
        /**
         * `derived` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val derived = if (refreshAbility) {
            /**
             * `level` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, profile.level).coerceAtLeast(1)
            data.unitLevelDerivedAttributes(
                unitId,
                unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts),
                level,
                mine = true,
                campaign = this,
            ).also { values -> values.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) } }
        } else emptyMap()
        return CampaignUnitPostsChange(
            unitId, oldPosts, posts, flags, postsWritten,
            if (postsWritten) listOf("postsSkills", "magic") else emptyList(), derived,
        )
    }

    /**
     * `addUnitLevels`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        data: GameDataCatalog,
        registeredFeatures: Int = 0,
    ): CampaignUnitLevelChange? {
        /**
         * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val profile = data.unitProfile(unitId) ?: return null
        /**
         * `oldLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val oldLevel = unitAttribute(unitId, UNIT_ATTR_LEVEL, profile.level)
        /**
         * `newLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val newLevel = (oldLevel + delta).coerceIn(1, data.unitLevelLimit())
        if (newLevel == oldLevel) return null
        if (unitAttributes[unitId]?.containsKey(UNIT_ATTR_POSTS) != true) setUnitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        setUnitAttribute(unitId, UNIT_ATTR_LEVEL, newLevel)
        /**
         * `posts` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        /**
         * `mine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mine = unitAttribute(unitId, UNIT_ATTR_JOIN, 0) != 0
        /**
         * `refreshAll` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val refreshAll = mine && (
            (globalVariables[GLOBAL_SJCS] as? Number)?.toInt() == 1 ||
                registeredFeatures and ENABLED_FEATURE_ZZSJCS != 0
            )
        /**
         * `attributes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attributes = if (refreshAll) {
            data.unitLevelDerivedAttributes(unitId, posts, newLevel, mine = true, campaign = this)
        } else {
            /**
             * `growth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val growth = data.unitLevelGrowth(unitId, posts, this)
            /**
             * `defaults` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val defaults = data.unitLevelDerivedAttributes(unitId, posts, oldLevel, mine, this)
            linkedMapOf<Int, Int>().apply {
                growth.forEach { (attribute, perLevel) ->
                    put(attribute, unitAttribute(unitId, attribute, defaults.getValue(attribute)) + perLevel * (newLevel - oldLevel))
                }
            }
        }
        attributes.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) }
        return CampaignUnitLevelChange(unitId, oldLevel, newLevel, attributes)
    }

    /**
     * `averageJoinedLevel`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun averageJoinedLevel(): Int {
        if (joinedUnits.isEmpty()) return 1
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        return levels.subList(trim, levels.size - trim).sum() / (levels.size - trim * 2)
    }

    /**
     * `info`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun info(type: Int, text: String) {
        val normalized = text.replace("\n", "<br/>")
        val open = extraInfo.filter { it.reserved.isEmpty() }
        if (open.isNotEmpty()) open.forEach { it.text = normalized } else extraInfo += CampaignInfo(type, "", normalized)
    }

    /**
     * `promote`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun promote(unitId: Int, fallbackPosts: Int, fallbackLevel: Int, data: GameDataCatalog): Int? {
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, fallbackPosts)
        val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, fallbackLevel).coerceAtLeast(1)
        val upgraded = data.promotionTarget(posts, level) ?: return null
        setUnitAttribute(unitId, UNIT_ATTR_POSTS, upgraded)
        return upgraded
    }

    /**
     * `grantExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun grantExperience(unitId: Int, fallbackLevel: Int, amount: Int, data: GameDataCatalog): CampaignExperienceResult {
        var level = unitAttribute(unitId, UNIT_ATTR_LEVEL, fallbackLevel).coerceAtLeast(1)
        var experience = unitAttribute(unitId, UNIT_ATTR_EXPERIENCE, 0).coerceAtLeast(0)
        val oldLevel = level
        val oldExperience = experience
        var remaining = amount.coerceAtLeast(0)
        var gained = 0
        while (remaining > 0) {
            val limit = data.unitExperienceLimit(level).coerceAtLeast(1)
            val applied = minOf(remaining, (limit - experience).coerceAtLeast(0))
            experience += applied
            gained += applied
            remaining -= applied
            if (experience >= limit && level < data.unitLevelLimit()) {
                level++
                experience = 0
            } else break
        }
        setUnitAttribute(unitId, UNIT_ATTR_LEVEL, level)
        setUnitAttribute(unitId, UNIT_ATTR_EXPERIENCE, experience)
        return CampaignExperienceResult(gained, level, experience, level != oldLevel, oldLevel, oldExperience)
    }

    /**
     * `applyInfoTransfer`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun applyInfoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) {
        when (type) {
            0 -> if (selectedUnitId >= 0) unitNames[selectedUnitId] = payload
            18 -> normalizeJoinedUnitLevels()
            4 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val magicId = values[0].toIntOrNull() ?: return@let
                    val level = values[1].toIntOrNull() ?: return@let
                    val unitId = values[2].toIntOrNull() ?: return@let
                    extraMagic[unitId to magicId] = CampaignMagic(unitId, magicId, level, values.drop(3).firstOrNull()?.ifBlank { DEFAULT_SKILL_INTRO } ?: DEFAULT_SKILL_INTRO)
                }
            }
            5 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val talentIndex = values[0].toIntOrNull() ?: return@let
                    val slot = values[1].toIntOrNull() ?: return@let
                    val effect = values[2].toIntOrNull() ?: return@let
                    talents[talentIndex to slot] = CampaignTalent(talentIndex, slot, effect, values.drop(3).lastOrNull().orEmpty())
                }
            }
            10 -> formationTalents += payload
            22 -> endingId = payload.toIntOrNull()
            26 -> payload.toIntOrNull()?.takeIf { it > 0 }?.let { globalVariables[4025] = random(it) }
        }
    }

    /**
     * `normalizeJoinedUnitLevels`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun normalizeJoinedUnitLevels() {
        if (joinedUnits.isEmpty()) return
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        val middle = levels.subList(trim, levels.size - trim)
        val average = middle.sum() / middle.size
        joinedUnits.forEach { unitId -> if (unitAttribute(unitId, UNIT_ATTR_LEVEL, 1) < average) setUnitAttribute(unitId, UNIT_ATTR_LEVEL, average) }
    }

    private companion object {
        /**
         * `UNIT_ATTR_LEVEL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_LEVEL = 18
        /**
         * `UNIT_ATTR_EXPERIENCE` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_EXPERIENCE = 19
        /**
         * `UNIT_ATTR_POSTS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_POSTS = 17
        /**
         * `UNIT_ATTR_JOIN` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_JOIN = 16
        /**
         * `GLOBAL_SJCS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val GLOBAL_SJCS = 4094
        /**
         * `ENABLED_FEATURE_ZZSJCS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ENABLED_FEATURE_ZZSJCS = 4
        /**
         * `DEFAULT_SKILL_INTRO` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val DEFAULT_SKILL_INTRO = "기본 설명"
    }
}
