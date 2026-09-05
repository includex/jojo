plugins {
    kotlin("jvm")
}

dependencies {
    api("com.badlogicgames.gdx:gdx:${property("gdxVersion")}")
    api("com.badlogicgames.gdx:gdx-freetype:${property("gdxVersion")}")
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}

tasks.test {
    useJUnitPlatform()
}

// Desktop coverage consumes this production-adjacent source inventory from
// core's build directory; it has no dependency on verification code.
val restoredScenarioDirectory = file("/Users/ain/workspace/jojo_mobile/sgccz-desktop/decompiled-python")
val scenarioBranchSurface = layout.buildDirectory.file("reports/scenario-branch-surface.json")
val auditScenarioBranchSurface = tasks.register<Exec>("auditScenarioBranchSurface") {
    inputs.dir(restoredScenarioDirectory)
    inputs.file(rootProject.file("tools/audit_scenario_branch_surface.py"))
    outputs.file(scenarioBranchSurface)
    commandLine(
        "python3",
        rootProject.file("tools/audit_scenario_branch_surface.py").absolutePath,
        restoredScenarioDirectory.absolutePath,
        scenarioBranchSurface.get().asFile.absolutePath,
    )
}
tasks.test { dependsOn(auditScenarioBranchSurface) }

// Production resources remain packaged by core; verification owns only the
// source/game oracle tasks.  Keep these inputs here so :core:processResources
// remains identical in purpose and does not acquire a verification edge.
val cocosAssetsDirectory = file("/Users/ain/workspace/jojo_mobile/sgccz-desktop/assets")
val generatedAstDirectory = layout.buildDirectory.dir("generated/scenario-ast")
val generatedMapAssetsDirectory = layout.buildDirectory.dir("generated/map-assets")
val generatedAudioAssetsDirectory = layout.buildDirectory.dir("generated/audio-assets")
val generatedTitleAssetsDirectory = layout.buildDirectory.dir("generated/title-assets")
val generatedTitleLoadConfirmationDirectory = layout.buildDirectory.dir("generated/title-load-confirmations")
val generatedReferenceFramebuffersDirectory = layout.buildDirectory.dir("generated/reference-framebuffers")
val exportScenarioAst = tasks.register<Exec>("exportScenarioAst") {
    inputs.dir(restoredScenarioDirectory)
    inputs.file(rootProject.file("tools/export_python_ast.py"))
    outputs.dir(generatedAstDirectory)
    commandLine("python3", rootProject.file("tools/export_python_ast.py").absolutePath,
        restoredScenarioDirectory.absolutePath, generatedAstDirectory.get().asFile.absolutePath)
}
val exportMapAssets = tasks.register<Exec>("exportMapAssets") {
    inputs.dir(cocosAssetsDirectory)
    inputs.files(
        cocosAssetsDirectory.parentFile.resolve("build/choice-atlas.png"),
        cocosAssetsDirectory.parentFile.resolve("build/python-source-login-fixture-texture-2.png"),
        cocosAssetsDirectory.parentFile.resolve("build/python-source-login-load-fixture-texture-1.png"),
        cocosAssetsDirectory.parentFile.resolve("build/python-source-login-load-confirm-fixture-texture-1.png"),
        cocosAssetsDirectory.parentFile.resolve("build/python-source-login-setting-fixture-texture-1.png"),
        cocosAssetsDirectory.parentFile.resolve("build/battle-hud-atlas.png"),
        cocosAssetsDirectory.parentFile.resolve("build/terrain-layer-atlas.png"),
        cocosAssetsDirectory.parentFile.resolve("build/start-battle-atlas.png"),
        cocosAssetsDirectory.parentFile.resolve("build/python-source-battle-verification-dialogue3.png"),
    )
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    outputs.dir(generatedMapAssetsDirectory)
    commandLine("python3", rootProject.file("tools/export_map_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath, generatedMapAssetsDirectory.get().asFile.absolutePath)
}
val exportAudioAssets = tasks.register<Exec>("exportAudioAssets") {
    inputs.dir(cocosAssetsDirectory)
    inputs.file(rootProject.file("tools/export_audio_assets.py"))
    outputs.dir(generatedAudioAssetsDirectory)
    commandLine("python3", rootProject.file("tools/export_audio_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath, generatedAudioAssetsDirectory.get().asFile.absolutePath)
}
val verifyBattleSpriteAssets = tasks.register<Exec>("verifyBattleSpriteAssets") {
    dependsOn(exportMapAssets)
    inputs.dir(cocosAssetsDirectory)
    inputs.dir(generatedMapAssetsDirectory)
    inputs.file(rootProject.file("tools/verify_battle_sprite_assets.py"))
    commandLine("python3", rootProject.file("tools/verify_battle_sprite_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath, generatedMapAssetsDirectory.get().asFile.absolutePath)
}
val verifyTerrainLayerAssets = tasks.register<Exec>("verifyTerrainLayerAssets") {
    dependsOn(exportMapAssets)
    inputs.dir(cocosAssetsDirectory)
    inputs.dir(generatedMapAssetsDirectory)
    inputs.file(rootProject.file("tools/verify_terrain_layer_assets.py"))
    commandLine("python3", rootProject.file("tools/verify_terrain_layer_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath, generatedMapAssetsDirectory.get().asFile.absolutePath)
}
val sourceLoginFramebuffers = files(
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login.rgba"),
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login-1-blank.rgba"),
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login-2.rgba"),
)
val exportTitleLoginReference = tasks.register<Sync>("exportTitleLoginReference") {
    inputs.files(sourceLoginFramebuffers)
    from(sourceLoginFramebuffers)
    into(generatedTitleAssetsDirectory)
}
val extractTitleLoadConfirmations = tasks.register<Exec>("extractTitleLoadConfirmations") {
    val sources = (0..7).map { rootProject.file(".verification-work/natural-battle-capture/captures/source-login-1-blank-row$it.rgba") }
    inputs.files(sources)
    inputs.file(rootProject.file("tools/extract_title_load_confirmation_crops.py"))
    outputs.dir(generatedTitleLoadConfirmationDirectory)
    commandLine("python3", rootProject.file("tools/extract_title_load_confirmation_crops.py").absolutePath,
        rootProject.file(".verification-work/natural-battle-capture/captures").absolutePath,
        generatedTitleLoadConfirmationDirectory.get().asFile.absolutePath)
}
val sourceReferenceFramebuffers = files(
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login-0.rgba"),
    rootProject.file(".verification-work/raw-framebuffer-common-space/infolayer-subtree-observation/source-hall-infolayer-bg-frame.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-choice.rgba"),
    rootProject.file(".verification-work/raw-framebuffer-common-space/dialogue1-live-raw/source-r00-dialogue-1.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-dialogue-2.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-menu.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-save.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-load.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-setting.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-helper.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-win-condition.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-terrain.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-property.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-treasure.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-forces.rgba"),
    rootProject.file(".verification-work/asset-recovery-audit/captures/source-unit-info.rgba"),
    rootProject.file(".verification-work/natural-battle-capture/captures/source-r00-win-result.rgba"),
)
val exportScenarioChoiceReference = tasks.register<Sync>("exportScenarioChoiceReference") {
    inputs.files(sourceReferenceFramebuffers)
    from(sourceReferenceFramebuffers)
    into(generatedReferenceFramebuffersDirectory)
}
tasks.processResources {
    dependsOn(exportScenarioAst, exportMapAssets, exportAudioAssets, exportTitleLoginReference,
        extractTitleLoadConfirmations, exportScenarioChoiceReference)
    from(restoredScenarioDirectory) { include("*.py", "manifest.json"); into("scenarios") }
    from(generatedAstDirectory) { into("scenario-ast") }
    from(generatedMapAssetsDirectory) { into("maps") }
    from(generatedAudioAssetsDirectory) { into("audio") }
    from(generatedTitleAssetsDirectory) { into("title") }
    from(generatedTitleLoadConfirmationDirectory) { into("title") }
    from(generatedReferenceFramebuffersDirectory) { into("reference") }
}
tasks.test { dependsOn(verifyTerrainLayerAssets) }

tasks.register<Test>("battleRewardFlowTest") {
    useJUnitPlatform()
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    filter {
        includeTestsMatching("com.jojo.game.BattleRewardFlowTest")
        includeTestsMatching("com.jojo.game.ScenarioRuntimeTest")
    }
}

tasks.register<Test>("itemUpgradeFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.ItemUpgradeFlowTest") }
}

tasks.register<Test>("loseSceneFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.LoseSceneFlowTest") }
}

tasks.register<Test>("autoBattleFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.AutoBattleFlowTest") }
}

tasks.register<Test>("battleCommandFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.BattleCommandFlowTest") }
}

tasks.register<Test>("editAdminFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter {
        includeTestsMatching("com.jojo.game.BattleEditLayer2Test")
        includeTestsMatching("com.jojo.game.EditAdminFlowsTest")
    }
}

/** Renderer-independent production contracts used by TitleScreen. */
tasks.register<Test>("titleInteractionContractTest") {
    group = "isolated contract"
    description = "Exercises TitleInteraction and overlay models without claiming a live TitleScreen entry."
    dependsOn(tasks.testClasses)
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = true
        includeTestsMatching("com.jojo.game.TitleInteractionTest")
        includeTestsMatching("com.jojo.game.SettingLayerTest")
        includeTestsMatching("com.jojo.game.ProgressLoadingLayerTest")
    }
}
