package com.jojo.game.domain.campaign

/** 원본 정보 전달에서 추가된 무장 기술을 나타낸다. */
data class CampaignMagic(val unitId: Int, val magicId: Int, val learnLevel: Int, val intro: String)

/** 보조 정보와 저장 상태에 유지되는 추가 정보 항목이다. */
data class CampaignInfo(val type: Int, val reserved: String = "", var text: String)

/** 원본 정보 전달에서 추가된 특성 변경을 나타낸다. */
data class CampaignTalent(val talentIndex: Int, val slot: Int, val effect: Int, val intro: String)

/** 유닛이 장착한 무기·방어구·보조 장비 상태이다. */
data class CampaignEquipment(
    val weapon: Int,
    val weaponLevel: Int,
    val armor: Int,
    val armorLevel: Int,
    val auxiliary: Int,
    val weaponExperience: Int = 0,
    val armorExperience: Int = 0,
) {
    fun asScriptValues(): List<Int> = listOf(weapon, weaponLevel, armor, armorLevel, auxiliary)
}

/** 유닛 경험치 적용 결과를 나타낸다. */
data class CampaignExperienceResult(
    val gained: Int,
    val level: Int,
    val experience: Int,
    val leveledUp: Boolean,
    val oldLevel: Int = level,
    val oldExperience: Int = 0,
    val learnedMagicIds: List<Int> = emptyList(),
)

/** 유닛 레벨 변경과 재계산된 능력치를 나타낸다. */
data class CampaignUnitLevelChange(
    val unitId: Int,
    val oldLevel: Int,
    val newLevel: Int,
    val attributes: Map<Int, Int>,
    val cacheRefreshOrder: List<String> = listOf("equipmentSkills", "unitSkills", "postsSkills", "magic"),
)

/** 유닛 병과 변경과 파생 능력치 갱신 결과를 나타낸다. */
data class CampaignUnitPostsChange(
    val unitId: Int,
    val oldPosts: Int,
    val newPosts: Int,
    val flags: Int,
    val postsWritten: Boolean,
    val cacheRefreshOrder: List<String>,
    val derivedAttributes: Map<Int, Int>,
)

/** 장비 경험치 적용 결과를 나타낸다. */
data class CampaignEquipmentExperienceResult(
    val unitId: Int,
    val slot: CampaignEquipmentSlot,
    val itemId: Int,
    val gained: Int,
    val oldLevel: Int,
    val newLevel: Int,
    val oldExperience: Int,
    val newExperience: Int,
    val oldValue: Int,
    val newValue: Int,
) {
    val leveledUp: Boolean get() = newLevel > oldLevel
}
