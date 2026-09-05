plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("android") version "2.2.20" apply false
    id("com.android.application") version "8.9.2" apply false
}

allprojects {
    group = "com.jojo.port"
    version = "0.1.0"

    repositories {
        google()
        mavenCentral()
    }
}

/**
 * Source-vs-port behaviour gate.  Each child task executes recovered JS and
 * the Kotlin port against the same fixture and compares their canonical
 * traces; more layer gates are added here as they are recovered.
 */
tasks.register("verifyBehaviorPairwise") {
    group = "verification"
    description = "Runs all recovered-JS ↔ Kotlin isolated behaviour trace comparisons."
    dependsOn(
        ":core:verifyMenuLayerTrace",
        ":core:verifyMenuLayerSwitchTrace",
        ":core:verifyTerrainLayerPairwise",
        ":core:verifyTreasureLayerPairwise",
        ":core:verifyPropertyLayerPairwise",
        ":core:verifySaveLayerPairwise",
        ":core:verifyLoadLayerPairwise",
        ":core:verifySettingLayerPairwise",
        ":core:verifyHelperLayerPairwise",
        ":core:verifyRoundLayerPairwise",
        ":core:verifyMapInfoLayerPairwise",
        ":core:verifyMiniMapLayerPairwise",
        ":core:verifyUnitInfoPairwise",
        ":core:verifyLoadGamePairwise",
        ":core:verifyBattleLayerPairwise",
        ":core:verifyFightPresentationPairwise",
        ":core:verifyEnemyTurnPairwise",
        ":core:verifyForcesListLayerPairwise",
        ":core:verifySectionLayerPairwise",
        ":core:verifyChoiceCommandPairwise",
        ":core:verifyItemEquipPairwise",
        ":core:verifyUpgradeSkillPairwise",
        ":core:verifyAutoBattlePairwise",
        ":core:verifyScenarioRuntimePairwise",
        ":core:verifyConfigFullPairwise",
        ":core:verifyWelcomePairwise",
        ":core:verifySendGiftsPairwise",
        ":core:verifyProgress2Pairwise",
        ":core:verifyModelStatePairwise",
        ":core:verifyModelPersistencePairwise",
        ":core:verifyModelLifecyclePairwise",
        ":core:verifyCharacterAbilityPairwise",
        ":core:verifyCmdLayerPairwise",
        ":core:verifyBattleViewPairwise",
        ":core:verifyMagicPairwise",
        ":core:verifyHallUiPairwise",
        ":core:verifyHallPrepPairwise",
        ":core:verifyEndFlowPairwise",
        ":core:verifySystemUiPairwise",
        ":core:verifyMiscUiPairwise",
        ":core:verifyPlatformPairwise",
        ":core:verifyGameDataPairwise",
        ":core:verifyFoundationPairwise",
        ":core:verifyShopRewardFullPairwise",
        ":desktop:verifyWinConditionsPairwise",
    )
}

/**
 * Isolated factory contracts. These tasks are deliberately outside
 * verifyBehaviorPairwise because they do not prove a normal game entry path.
 */
tasks.register("verifyIsolatedFixtureOracles") {
    group = "isolated oracle"
    description = "Runs fixture-only source comparisons; success is not runtime-route coverage."
    dependsOn(
        ":core:verifyHeadPairwise",
        ":core:verifyBattleScenePortBehavior",
        ":core:verifyProgressionLayerPairwise",
        ":core:verifyEditMutationPairwise",
        ":core:verifyUnitListInfoPairwise",
    )
}

tasks.register("auditRecoveredSourceInventories") {
    group = "source inventory"
    description = "Audits recovered-only registries and overridden fixture branch coverage; no port parity claim."
    dependsOn(
        ":core:auditCoreBoundarySourceInventory",
        ":core:auditBattleControlSourceInventory",
        ":core:auditBattleBootstrapSourceInventory",
    )
}
