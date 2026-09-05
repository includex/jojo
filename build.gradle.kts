plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("android") version "2.2.20" apply false
    id("com.android.application") version "8.9.2" apply false
}

allprojects {
    group = "com.jojo.game"
    version = "0.1.0"

    repositories {
        google()
        mavenCentral()
    }
}

/**
 * Reference-behaviour gate. Each child task executes the source reference and
 * the current game against the same fixture and compares canonical traces.
 */
tasks.register("verifyBehaviorPairwise") {
    group = "verification"
    description = "Runs source-reference ↔ current-game isolated behaviour comparisons."
    dependsOn(
        ":verification:verifyMenuLayerTrace",
        ":verification:verifyMenuLayerSwitchTrace",
        ":verification:verifyTerrainLayerPairwise",
        ":verification:verifyTreasureLayerPairwise",
        ":verification:verifyPropertyLayerPairwise",
        ":verification:verifySaveLayerPairwise",
        ":verification:verifyLoadLayerPairwise",
        ":verification:verifySettingLayerPairwise",
        ":verification:verifyHelperLayerPairwise",
        ":verification:verifyRoundLayerPairwise",
        ":verification:verifyMapInfoLayerPairwise",
        ":verification:verifyMiniMapLayerPairwise",
        ":verification:verifyUnitInfoPairwise",
        ":verification:verifyLoadGamePairwise",
        ":verification:verifyBattleScreenPairwise",
        ":verification:verifyFightPresentationPairwise",
        ":verification:verifyEnemyTurnPairwise",
        ":verification:verifyForcesListLayerPairwise",
        ":verification:verifySectionLayerPairwise",
        ":verification:verifyChoiceCommandPairwise",
        ":verification:verifyItemEquipPairwise",
        ":verification:verifyUpgradeSkillPairwise",
        ":verification:verifyAutoBattlePairwise",
        ":verification:verifyScenarioRuntimePairwise",
        ":verification:verifyConfigFullPairwise",
        ":verification:verifyWelcomePairwise",
        ":verification:verifySendGiftsPairwise",
        ":verification:verifyProgress2Pairwise",
        ":verification:verifyModelStatePairwise",
        ":verification:verifyModelPersistencePairwise",
        ":verification:verifyModelLifecyclePairwise",
        ":verification:verifyCharacterAbilityPairwise",
        ":verification:verifyCmdLayerPairwise",
        ":verification:verifyBattleViewPairwise",
        ":verification:verifyMagicPairwise",
        ":verification:verifyHallUiPairwise",
        ":verification:verifyHallPrepPairwise",
        ":verification:verifyEndFlowPairwise",
        ":verification:verifySystemUiPairwise",
        ":verification:verifyMiscUiPairwise",
        ":verification:verifyPlatformPairwise",
        ":verification:verifyGameDataPairwise",
        ":verification:verifyFoundationPairwise",
        ":verification:verifyShopRewardFullPairwise",
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
        ":verification:verifyHeadPairwise",
        ":verification:verifyBattleSceneCoordinatorBehavior",
        ":verification:verifyProgressionLayerPairwise",
        ":verification:verifyEditMutationPairwise",
        ":verification:verifyUnitListInfoPairwise",
    )
}

tasks.register("auditRecoveredSourceInventories") {
    group = "source inventory"
    description = "Audits deprecated registries and overridden fixture branch coverage; no game-parity claim."
    dependsOn(
        ":verification:auditCoreBoundarySourceInventory",
        ":verification:auditBattleControlSourceInventory",
        ":verification:auditBattleBootstrapSourceInventory",
    )
}
