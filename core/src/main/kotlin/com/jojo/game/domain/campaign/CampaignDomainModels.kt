package com.jojo.game.domain.campaign

/** A learned tactic injected by the original Model.infoTransfer(type = 4). */
data class CampaignMagic(val unitId: Int, val magicId: Int, val learnLevel: Int, val intro: String)

/** Original Model._exInfo entry, retained for HelperLayer / save-state parity. */
data class CampaignInfo(val type: Int, val reserved: String = "", var text: String)

/** A UNIT_TIANFU row mutation injected by Model.infoTransfer(type = 5). */
data class CampaignTalent(val talentIndex: Int, val slot: Int, val effect: Int, val intro: String)

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

data class CampaignExperienceResult(
    val gained: Int,
    val level: Int,
    val experience: Int,
    val leveledUp: Boolean,
    val oldLevel: Int = level,
    val oldExperience: Int = 0,
    val learnedMagicIds: List<Int> = emptyList(),
)

data class CampaignUnitLevelChange(
    val unitId: Int,
    val oldLevel: Int,
    val newLevel: Int,
    val attributes: Map<Int, Int>,
    val cacheRefreshOrder: List<String> = listOf("equipmentSkills", "unitSkills", "postsSkills", "magic"),
)

data class CampaignUnitPostsChange(
    val unitId: Int,
    val oldPosts: Int,
    val newPosts: Int,
    val flags: Int,
    val postsWritten: Boolean,
    val cacheRefreshOrder: List<String>,
    val derivedAttributes: Map<Int, Int>,
)

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
