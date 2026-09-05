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
        ":core:verifyBattleScreenPairwise",
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
        ":core:verifyBattleSceneCoordinatorBehavior",
        ":core:verifyProgressionLayerPairwise",
        ":core:verifyEditMutationPairwise",
        ":core:verifyUnitListInfoPairwise",
    )
}

tasks.register("auditRecoveredSourceInventories") {
    group = "source inventory"
    description = "Audits deprecated registries and overridden fixture branch coverage; no game-parity claim."
    dependsOn(
        ":core:auditCoreBoundarySourceInventory",
        ":core:auditBattleControlSourceInventory",
        ":core:auditBattleBootstrapSourceInventory",
    )
}
