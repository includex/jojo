// Game
package com.jojo.game.infrastructure.data


import com.jojo.game.domain.battle.TerrainArmRow
import com.jojo.game.domain.battle.TerrainRow

import com.jojo.game.domain.campaign.*

/** GameDataCatalogUnitDomain: 유닛, 병과, 직위, 전투 정보, 캠페인 레벨 조회를 제공한다. */
/** FeatsProgress: 공훈 화면 한 줄이 보여 주는 적성·공훈·다음 목표·다음 단계다. */
data class FeatsProgress(
    val aptitude: Int,
    val progress: Int,
    val nextProgress: Int,
    val nextPhase: Int,
)

internal class GameDataCatalogUnitDomain(
    tables: GameDataTableBundle,
    /**
     * `combat` (GameDataCatalogCombatDomain,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val combat: GameDataCatalogCombatDomain,
) : GameDataCatalogTableDomain(tables) {
    /**
     * `skillsForUnit`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    /**
     * 특성 이름 목록: `unitPostsSkill` 표의 이름 열을 순서대로 돌려준다.
     *
     * 원본 `LearnUnitSkillLayer`는 이 표를 그대로 훑어 목록의 각 줄을
     * `"<번호>.<이름>"`으로 적는다(`UNIT_POSTS_SKILL_ATTR.NAME`은 0번 열이다).
     */
    fun postSkillNames(): List<String> = unitPostSkills.map { it.getString("0", "") }

    /**
     * 관직 이름 목록: `posts` 표의 이름 열을 순서대로 돌려준다.
     *
     * 이 표도 [postSkillNames]처럼 열 번호를 열쇠로 쓴다. 이름은 0번 열이다.
     */
    fun postsNames(): List<String> = posts.map { it.getString("0", "") }

    /**
     * 특성 슬롯 값: `unitPostsSkill` 표에서 한 특성의 슬롯 값을 돌려준다.
     *
     * 원본 `skillAttr2(skillId, attr, default)`와 같은 계약이다. 표를 벗어나면 기본값을
     * 그대로 돌려준다. 열 번호는 `UNIT_POSTS_SKILL_ATTR`을 따른다(UNIT0=6, POSTS=2).
     */
    fun postSkillAttribute(skillId: Int, attribute: Int, fallback: Int): Int =
        unitPostSkills.getOrNull(skillId)?.int(attribute.toString(), fallback) ?: fallback

    fun skillsForUnit(characterId: Int, postsId: Int, campaign: CampaignState?): Map<Int, Int> {
        val basePosts = if (postsId >= 60) postsId else postsId - postsId % 3
        val upperPosts = if (postsId >= 60) postsId + 1 else basePosts + 3
        val postContributions = mutableListOf<Pair<Int, Int>>()
        val unitContributions = mutableListOf<Pair<Int, Int>>()
        unitPostSkills.forEachIndexed { index, raw ->
            val post = campaign?.talents?.get(index to 3)?.effect ?: raw.int("2", 255)
            val isPostSkill = post in basePosts until upperPosts
            val isUnitSkill = (0..2).any { slot ->
                (campaign?.talents?.get(index to slot)?.effect ?: raw.int((6 + slot).toString(), 1024)) == characterId
            }
            if (isPostSkill || isUnitSkill) {
                val skill = raw.int("3", 65536 or index) to raw.int("5", 255).coerceIn(0, 255)
                if (isPostSkill) postContributions += skill
                if (isUnitSkill) unitContributions += skill
            }
        }
        return combat.mergeSkillEntries(postContributions + unitContributions)
    }

    /**
     * `unitProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitProfile(id: Int): GameDataCatalog.UnitProfile? {
        val value = units.getOrNull(id) ?: return null
        return GameDataCatalog.UnitProfile(
            id, value.string("0") ?: "유닛 $id", value.int("1"), value.int("2"), value.int("14"), value.int("21", -1),
            value.int("19") != 0, value.int("11"), value.int("12", value.int("lv", 1)).coerceAtLeast(1),
            value.int("4"), value.int("5"), value.int("6"), value.int("7"), value.int("8"),
            value.int("9", 100).coerceAtLeast(1), value.int("10").coerceAtLeast(0), criticalSpeechProfile(id, value),
        )
    }

    /**
     * `criticalSpeechProfile`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun criticalSpeechProfile(
        unitId: Int,
        unit: com.badlogic.gdx.utils.JsonValue
    ): GameDataCatalog.CriticalSpeechProfile {
        /**
         * `custom` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val custom = unit.get("15")?.let(::stringValues).orEmpty().filter(String::isNotEmpty)
        if (custom.isNotEmpty()) return GameDataCatalog.CriticalSpeechProfile(custom, true)
        /**
         * `ids` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ids = config.get("criIds")?.let(::intValues).orEmpty()
        /**
         * `configured` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val configured = config.get("criTxt")?.let(::stringValues).orEmpty()
        /**
         * `named` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val named = ids.indexOf(unitId)
        if (named >= 0) return GameDataCatalog.CriticalSpeechProfile(
            listOfNotNull(
                configured.getOrNull(named)?.takeIf(String::isNotEmpty)
            ), false
        )
        /**
         * `group` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val group = configured.drop(unit.int("3") * 3 + 21).take(3).filter(String::isNotEmpty)
        return if (group.isNotEmpty()) GameDataCatalog.CriticalSpeechProfile(group, true)
        else GameDataCatalog.CriticalSpeechProfile(DEFAULT_CRITICAL_SPEECH, true, true)
    }

    /**
     * `allUnitNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    /**
     * `unitIntro`: 무장 소개 문구를 돌려준다.
     *
     * 원본 `UnitInfoLayer._ref0`이 `unitAttr(id, UNIT_ATTR_NAME.INTRO, "")`로 읽는 값이고,
     * 유닛 표의 13번 열이다. 소개가 없는 유닛은 빈 문자열이다.
     */
    fun unitIntro(id: Int): String = units.getOrNull(id)?.string("13") ?: ""

    fun allUnitNames(): List<String> = units.indices.mapNotNull(::unitProfile).map(GameDataCatalog.UnitProfile::name)
    /**
     * `allUnitIds`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allUnitIds(): List<Int> = units.indices.filter { unitProfile(it) != null }
    /**
     * `retreatText`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun retreatText(unitId: Int): String? =
        config.get("retreatTxt")?.get(unitId)?.asString()?.takeIf(String::isNotEmpty)

    /**
     * `allRetreatTexts`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allRetreatTexts(): List<String> =
        generateSequence(config.get("retreatTxt")?.child) { it.next }.map { it.asString() }.toList()

    /**
     * `battleName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleName(stageIndex: Int): String = shops.getOrNull(stageIndex)?.get("0")?.asString()?.trim().orEmpty()
    /**
     * `allBattleNames`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allBattleNames(): List<String> =
        shops.mapNotNull { it.get("0")?.asString()?.trim()?.takeIf(String::isNotEmpty) }

    /**
     * `postsName`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun postsName(postsId: Int): String = posts.getOrNull(postsId)?.string("0") ?: ""
    /**
     * `armProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun armProfile(id: Int): GameDataCatalog.ArmProfile? {
        val value = arms.getOrNull(id) ?: return null
        return GameDataCatalog.ArmProfile(
            id,
            value.string("0") ?: "병종 $id",
            value.int("5"),
            value.get("9")?.asBoolean() ?: false,
            value.int("6") != 0,
            value.int("10", 100),
            value.int("12", -1),
            numericChildren(value.get("1"), "arms"),
            numericChildren(value.get("8")?.get("expend")),
            numericChildren(value.get("8")?.get("rise")),
            value.int("3") == 0,
            value.int("2")
        )
    }

    /** `terrainRows`: 지형 표의 식별자·이름·플래그를 순서대로 돌려준다. */

    fun terrainRows(): List<TerrainRow> =
        generateSequence(gameConfig.get("terrain")?.child) { it.next }.mapIndexed { id, value ->
            TerrainRow(
                id,
                value.getString("name", "지형 $id"),
                value.getInt("flag", 0),
                value.getInt("magic", 0),
            )
        }.toList()

    /**
     * `terrainArmRows`: 병과별 지형 상승치와 이동 비용 표를 돌려준다.
     * 상승치는 표에 없으면 원본과 같이 100으로 채우고, 이동 비용은 없는 지형을 담지 않는다.
     */

    fun terrainArmRows(): List<TerrainArmRow> {
        val rows = terrainRows()
        return arms.indices.mapNotNull(::armProfile).map { arm ->
            TerrainArmRow(
                arm.id, arm.name,
                rows.associate { it.id to (arm.terrainRiseForDisplay(it.id) ?: 100) },
                rows.mapNotNull { entry -> arm.terrainExpendForDisplay(entry.id)?.let { entry.id to it } }.toMap(),
            )
        }
    }

    /**
     * `battleProfile`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleProfile(unitId: Int, scriptLevel: Int, postsOverride: Int?): GameDataCatalog.BattleProfile? {
        val unit = unitProfile(unitId) ?: return null
        val level = if (scriptLevel > 0) scriptLevel + 1 else unit.level
        val finalPosts = postsOverride ?: turnPosts(unit.posts, level, 2)
        val post = posts.getOrNull(finalPosts)
        /**
         * `bonus`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun bonus(attribute: String) = post?.get(attribute)?.asInt() ?: 0
        /**
         * `ability`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun ability(raw: Int, attribute: String) = raw + (abilityPhase(raw) + bonus(attribute)).floorDiv(2) * level
        val arm = armProfile(if (finalPosts < 60) finalPosts / 3 else finalPosts - 40) ?: return null
        return GameDataCatalog.BattleProfile(
            unit,
            level,
            finalPosts,
            bonus("1"),
            ability(unit.attack, "3"),
            ability(unit.defense, "4"),
            ability(unit.spirit, "5"),
            ability(unit.critical, "6"),
            ability(unit.morale, "7"),
            (unit.maxHitPoints + bonus("8") * level).coerceAtLeast(1),
            (unit.maxMagicPoints + bonus("9") * level).coerceAtLeast(0),
            arm,
            combat.hitAreaProfile(bonus("2")) ?: return null,
            combat.allMagicProfiles()
                .filter { magic -> combat.magicLearnLevel(magic.id, finalPosts)?.let { level >= it } == true })
    }

    /**
     * `unitExperienceLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitExperienceLimit(level: Int): Int = (config.get("unit")?.getInt("expLimit", 100) ?: 100) +
            (config.get("transfer")
                ?.let { generateSequence(it.child) { node -> node.next }.map { node -> node.asInt() }.toList() }
                .orEmpty().take(2).count { level >= it } * 25)

    /**
     * `unitLevelLimit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitLevelLimit(): Int = config.get("unit")?.getInt("lvLimit", 50) ?: 50
    /**
     * `unitLevelGrowth`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitLevelGrowth(unitId: Int, postsId: Int, campaign: CampaignState?): LinkedHashMap<Int, Int> {
        val profile = unitProfile(unitId) ?: return linkedMapOf()
        val post = posts.getOrNull(postsId)
        val result = linkedMapOf<Int, Int>()
        listOf(
            profile.attack,
            profile.defense,
            profile.spirit,
            profile.critical,
            profile.morale
        ).forEachIndexed { index, fallback ->
            val aptitude = campaign?.unitAttribute(unitId, 9 + index, fallback) ?: fallback
            result[2 + index] = (abilityPhase(aptitude) + (post?.get((3 + index).toString())?.asInt() ?: 3)).floorDiv(2)
        }
        result[7] = post?.get("8")?.asInt() ?: 0; result[8] = post?.get("9")?.asInt() ?: 0; return result
    }

    /**
     * `unitLevelDerivedAttributes`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun unitLevelDerivedAttributes(
        unitId: Int,
        postsId: Int,
        level: Int,
        mine: Boolean,
        campaign: CampaignState?
    ): LinkedHashMap<Int, Int> {
        /**
         * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val profile = unitProfile(unitId) ?: return linkedMapOf()
        /**
         * `growth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val growth = unitLevelGrowth(unitId, postsId, campaign)
        /**
         * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val result = linkedMapOf<Int, Int>()
        listOf(
            profile.attack,
            profile.defense,
            profile.spirit,
            profile.critical,
            profile.morale
        ).forEachIndexed { index, base ->
            /**
             * `add` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val add = if (mine) campaign?.unitAttribute(unitId, 39 + index, 0) ?: 0 else 0; result[2 + index] =
            base + growth.getValue(2 + index) * level + add.coerceAtLeast(0)
        }
        listOf(profile.maxHitPoints, profile.maxMagicPoints).forEachIndexed { index, base ->
            /**
             * `add` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val add = if (mine) campaign?.unitAttribute(unitId, 44 + index, 0) ?: 0 else 0; result[7 + index] =
            base + (growth.getValue(7 + index) * level + add).coerceAtLeast(0)
        }; return result
    }

    /**
     * `promotionTarget`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun promotionTarget(postsId: Int, level: Int): Int? {
        if (postsId !in 0 until 60 || postsId % 3 !in 0..1) return null
        val rank = postsId % 3
        val threshold = indexed(config.get("transfer"), rank)?.asInt() ?: if (rank == 0) 15 else 30
        return (postsId + 1).takeIf { level >= threshold }
    }

    /**
     * `configTopLevelKeys`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun configTopLevelKeys(): String =
        generateSequence(config.child) { it.next }.take(20).joinToString { "${it.name ?: "#"}:${it.type()}" }

    /**
     * `abilityPhase`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun abilityPhase(raw: Int): Int = ABILITY_PHASE_MAX - ABILITY_PHASE_THRESHOLDS.count { raw < it }

    /**
     * 공훈 진행: 다섯 능력의 적성·모은 공훈·다음 승급까지의 공훈·다음 단계를 돌려준다.
     *
     * 원본 `Unit.featsAttrByName`(=`feat`), `nextFeats`, `nextAbilityPhase`와 같은 계약이다.
     * 적성과 모은 공훈은 저장 자료에서 오고(없으면 정적 표), 나머지는 그 값으로 계산한다.
     */
    fun featsProgress(unitId: Int, campaign: CampaignState?): List<FeatsProgress> {
        val profile = unitProfile(unitId) ?: return emptyList()
        val postsId = campaign?.unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts) ?: profile.posts
        val post = posts.getOrNull(postsId)
        return listOf(profile.attack, profile.defense, profile.spirit, profile.critical, profile.morale)
            .mapIndexed { index, fallback ->
                val aptitude = campaign?.unitAttribute(unitId, UNIT_ATTR_APTITUDE + index, fallback) ?: fallback
                val postsRate = post?.get((POSTS_ATTR_RATE + index).toString())?.asInt() ?: DEFAULT_POSTS_RATE
                val phase = (abilityPhase(aptitude) + postsRate).floorDiv(2)
                // 원본 `nextFeats`: 승급에 필요한 공훈은 적어도 100이다.
                val next = maxOf(100, (phase * (aptitude / 2.0) * 1.2).toInt())
                // 원본 `nextAbilityPhase`: 단계가 오르는 첫 걸음 수를 찾아 문턱 표를 거꾸로 읽는다.
                val step = (0..ABILITY_PHASE_MAX).firstOrNull { (postsRate + it).floorDiv(2) > phase } ?: 0
                FeatsProgress(
                    aptitude = aptitude,
                    progress = campaign?.unitAttribute(unitId, UNIT_ATTR_FEATS + index, 0) ?: 0,
                    nextProgress = next,
                    // 원본은 표를 벗어나면 `undefined`를 그대로 적는다. 이식본은 0으로 두어
                    // 화면이 `MAX`를 적게 한다.
                    nextPhase = if (step == 0) 0
                    else ABILITY_PHASE_THRESHOLDS.getOrElse(ABILITY_PHASE_MAX - step) { 0 },
                )
            }
    }
    /**
     * `turnPosts`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun turnPosts(posts: Int, level: Int, armLimit: Int): Int {
        if (posts >= 60) return posts
        val eligible = if (level >= 30) 2 else if (level >= 15) 1 else 0; return posts - posts % 3 + maxOf(
            posts % 3,
            minOf(eligible, armLimit)
        )
    }

    private companion object {
        /** 능력 단계의 최댓값이다. 원본 `Unit.abilityPhase`의 `i` 초깃값과 같다. */
        const val ABILITY_PHASE_MAX = 5

        /** 능력 단계 문턱이다. 원본 `Model.property`의 ABILITY_X·S·A·B다. */
        val ABILITY_PHASE_THRESHOLDS = listOf(127, 45, 35, 25)

        /** 저장 자료의 적성 열 번호다(`UNIT_ATTR_NAME2.WL`). */
        const val UNIT_ATTR_APTITUDE = 9

        /** 저장 자료의 관직 열 번호다(`UNIT_ATTR_NAME2.POSTS`). */
        const val UNIT_ATTR_POSTS = 17

        /** 저장 자료의 공훈 열 번호다(`UNIT_ATTR_NAME2.GX_WL`). */
        const val UNIT_ATTR_FEATS = 28

        /** 관직 표의 능력 성장 열 번호다(`POSTS_ATTR_NAME2.ATT`). */
        const val POSTS_ATTR_RATE = 3

        /** 관직 표에 값이 없을 때 원본이 쓰는 기본 성장치다. */
        const val DEFAULT_POSTS_RATE = 3

        /**
         * `DEFAULT_CRITICAL_SPEECH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val DEFAULT_CRITICAL_SPEECH = listOf(
            "음... 정말 한 방에 쓰러뜨릴 거야!",
            "길 막지 마! 길 막지 마!",
            "가르침을 내리노라!",
            "무명 병사! 빨리 물러서라!",
            "가로막는 자는 죽는다! 비켜라, 비켜라……!",
            "야헤야헤야……!",
            "오호호...!",
            "하아……!",
            "아악……!",
            "크윽……!",
            "음...!",
            "죽여라아...!",
            "기술을 보여주마...!",
            "나의 이 기술을 받아라!!",
            "죽여라...!",
            "죽어라!!!",
            "호호……!",
            "야호……!",
            "응응응...!",
            "으윽...!",
            "후우후……!",
            "응응!?",
            "흥!!",
            "응응응!",
            "아이쿠!!",
            "나를 봐라!",
            "모든 것이 이 한 번의 공격에 달렸어!",
            "반드시 당신과 우열을 가려야 해!\n절대로 질 수 없어!",
            "이 치명타를 받아라!",
            "받아치기 준비해라!!",
            "죽을 준비를 해라!",
            "나 왔다, 나 왔다, 나 왔다!!"
        )
    }
}
