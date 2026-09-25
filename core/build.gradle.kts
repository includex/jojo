import java.time.Duration

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
val generatedSourceMapTexturesDirectory = layout.buildDirectory.dir("generated/source-map-textures")
val generatedStreetBodyLabelsDirectory = layout.buildDirectory.dir("generated/street-body-labels")
val generatedInfoLabelsDirectory = layout.buildDirectory.dir("generated/info-labels")
val generatedStreetSpeakerLabelsDirectory = layout.buildDirectory.dir("generated/street-speaker-labels")
val generatedAudioAssetsDirectory = layout.buildDirectory.dir("generated/audio-assets")
val generatedTitleAssetsDirectory = layout.buildDirectory.dir("generated/title-assets")
val generatedTitleLoadConfirmationDirectory = layout.buildDirectory.dir("generated/title-load-confirmations")
val generatedReferenceFramebuffersDirectory = layout.buildDirectory.dir("generated/reference-framebuffers")
// The exporter also re-nests branches the decompiler mis-attached, using the
// original bytecode in `cocosAssetsDirectory` as the authority.  The restored
// `.py` files stay exactly as recovered; only the executed AST is corrected.
val exportScenarioAst = tasks.register<Exec>("exportScenarioAst") {
    inputs.dir(restoredScenarioDirectory)
    inputs.dir(cocosAssetsDirectory)
    inputs.file(rootProject.file("tools/export_python_ast.py"))
    inputs.file(rootProject.file("tools/repair_scenario_branch_nesting.py"))
    outputs.dir(generatedAstDirectory)
    commandLine("python3", rootProject.file("tools/export_python_ast.py").absolutePath,
        restoredScenarioDirectory.absolutePath, cocosAssetsDirectory.absolutePath,
        generatedAstDirectory.get().asFile.absolutePath)
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
val exportSourceMapTextures = tasks.register<Exec>("exportSourceMapTextures") {
    dependsOn(exportMapAssets)
    timeout.set(Duration.ofSeconds(60))
    inputs.file(rootProject.file("tools/export_source_map_textures.cjs"))
    inputs.file(generatedMapAssetsDirectory.map { it.file("manifest.json") })
    inputs.dir(cocosAssetsDirectory.resolve("Game/native"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("node_modules/electron/package.json"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("package-lock.json"))
    inputs.property("decodePlatform", System.getProperty("os.name") + " " + System.getProperty("os.version"))
    outputs.dir(generatedSourceMapTexturesDirectory)
    commandLine("node", rootProject.file("tools/export_source_map_textures.cjs").absolutePath,
        cocosAssetsDirectory.parentFile.absolutePath,
        generatedMapAssetsDirectory.get().file("manifest.json").asFile.absolutePath,
        generatedSourceMapTexturesDirectory.get().asFile.absolutePath)
}
val exportAudioAssets = tasks.register<Exec>("exportAudioAssets") {
    inputs.dir(cocosAssetsDirectory)
    inputs.file(rootProject.file("tools/export_audio_assets.py"))
    outputs.dir(generatedAudioAssetsDirectory)
    commandLine("python3", rootProject.file("tools/export_audio_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath, generatedAudioAssetsDirectory.get().asFile.absolutePath)
}
val exportStreetSpeakerLabels = tasks.register<Exec>("exportStreetSpeakerLabels") {
    dependsOn(exportMapAssets)
    timeout.set(Duration.ofSeconds(60))
    inputs.file(rootProject.file("tools/export_street_speaker_labels.cjs"))
    inputs.file(rootProject.file("tools/street_speaker_label_contract.json"))
    inputs.file(generatedMapAssetsDirectory.map { it.file("data/unit.bin") })
    inputs.file(cocosAssetsDirectory.parentFile.resolve("package-lock.json"))
    inputs.files(
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/assets/CCTexture2D.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/renderer/gfx/texture-2d.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/webgl/assemblers/label/2d/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("recovered-js/modules/game-data/Unit.js"),
        cocosAssetsDirectory.parentFile.resolve("node_modules/electron/package.json"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/label/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/utils/text-utils.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/utils.js"),
    )
    inputs.property("labelPlatform", System.getProperty("os.name") + " " + System.getProperty("os.version"))
    inputs.files(listOf(
        file("/System/Library/Fonts/AppleSDGothicNeo.ttc"),
        file("/System/Library/Fonts/Supplemental/Arial.ttf"),
    ).filter { it.isFile })
    outputs.dir(generatedStreetSpeakerLabelsDirectory)
    commandLine("node", rootProject.file("tools/export_street_speaker_labels.cjs").absolutePath,
        cocosAssetsDirectory.parentFile.absolutePath,
        generatedMapAssetsDirectory.get().file("data/unit.bin").asFile.absolutePath,
        generatedStreetSpeakerLabelsDirectory.get().asFile.absolutePath)
}
val exportStreetBodyLabels = tasks.register<Exec>("exportStreetBodyLabels") {
    val maxRenderedPages = providers.gradleProperty("jojo.bodyLabels.maxPages").orElse("6")
    inputs.property("maxRenderedPages", maxRenderedPages)
    environment("JOJO_BODY_LABEL_MAX_PAGES", maxRenderedPages.get())
    inputs.file(rootProject.file("tools/street_body_label_pages.cjs"))
    inputs.file(rootProject.file("core/src/main/resources/scenarios/dialogue-text.json"))
    dependsOn(exportScenarioAst)
    timeout.set(Duration.ofSeconds(60))
    inputs.file(rootProject.file("tools/export_street_body_labels.cjs"))
    inputs.file(rootProject.file("tools/hold_source_verification_exit.cjs"))
    inputs.file(rootProject.file("tools/street_body_label_contract.json"))
    inputs.file(generatedAstDirectory.map { it.file("R_00.json") })
    inputs.file(cocosAssetsDirectory.parentFile.resolve("package-lock.json"))
    inputs.files(
        cocosAssetsDirectory.parentFile.resolve("web/cocos2d-js.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/components/CCRichText.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/utils/html-text-parser.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/assets/CCTexture2D.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/renderer/gfx/texture-2d.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/webgl/assemblers/label/2d/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("recovered-js/modules/ui/DialogueLayer.js"),
        cocosAssetsDirectory.parentFile.resolve("decompiled-python/R_00.py"),
        cocosAssetsDirectory.parentFile.resolve("node_modules/electron/package.json"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/label/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/utils/text-utils.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/utils.js"),
    )
    inputs.property("labelPlatform", System.getProperty("os.name") + " " + System.getProperty("os.version"))
    inputs.files(listOf(
        file("/System/Library/Fonts/AppleSDGothicNeo.ttc"),
        file("/System/Library/Fonts/Supplemental/Arial.ttf"),
    ).filter { it.isFile })
    outputs.dir(generatedStreetBodyLabelsDirectory)
    commandLine("node", rootProject.file("tools/export_street_body_labels.cjs").absolutePath,
        cocosAssetsDirectory.parentFile.absolutePath,
        generatedAstDirectory.get().file("R_00.json").asFile.absolutePath,
        generatedStreetBodyLabelsDirectory.get().asFile.absolutePath)
}
val exportInfoLabels = tasks.register<Exec>("exportInfoLabels") {
    inputs.file(rootProject.file("tools/street_body_label_pages.cjs"))
    inputs.file(rootProject.file("core/src/main/resources/scenarios/dialogue-text.json"))
    dependsOn(exportScenarioAst)
    timeout.set(Duration.ofSeconds(60))
    inputs.file(rootProject.file("tools/export_street_body_labels.cjs"))
    inputs.file(rootProject.file("tools/hold_source_verification_exit.cjs"))
    inputs.file(rootProject.file("tools/info_label_contract.json"))
    inputs.file(generatedAstDirectory.map { it.file("R_00.json") })
    inputs.file(cocosAssetsDirectory.parentFile.resolve("package-lock.json"))
    inputs.files(
        cocosAssetsDirectory.parentFile.resolve("web/cocos2d-js.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/components/CCRichText.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/utils/html-text-parser.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/assets/CCTexture2D.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/renderer/gfx/texture-2d.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/webgl/assemblers/label/2d/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("recovered-js/modules/ui/InfoLayer.js"),
        cocosAssetsDirectory.parentFile.resolve("decompiled-python/R_00.py"),
        cocosAssetsDirectory.parentFile.resolve("node_modules/electron/package.json"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/label/ttf.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/utils/text-utils.js"),
        cocosAssetsDirectory.parentFile.resolve("cocos-engine-web/cocos2d/core/renderer/utils/utils.js"),
    )
    inputs.property("labelPlatform", System.getProperty("os.name") + " " + System.getProperty("os.version"))
    inputs.files(listOf(
        file("/System/Library/Fonts/AppleSDGothicNeo.ttc"),
        file("/System/Library/Fonts/Supplemental/Arial.ttf"),
    ).filter { it.isFile })
    outputs.dir(generatedInfoLabelsDirectory)
    environment("JOJO_LABEL_STYLE", "info")
    commandLine("node", rootProject.file("tools/export_street_body_labels.cjs").absolutePath,
        cocosAssetsDirectory.parentFile.absolutePath,
        generatedAstDirectory.get().file("R_00.json").asFile.absolutePath,
        generatedInfoLabelsDirectory.get().asFile.absolutePath)
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
    dependsOn(exportScenarioAst, exportMapAssets, exportSourceMapTextures, exportAudioAssets, exportStreetSpeakerLabels, exportStreetBodyLabels, exportInfoLabels, exportTitleLoginReference,
        extractTitleLoadConfirmations, exportScenarioChoiceReference)
    from(restoredScenarioDirectory) { include("*.py", "manifest.json"); into("scenarios") }
    from(generatedAstDirectory) { into("scenario-ast") }
    from(generatedMapAssetsDirectory) { into("maps") }
    from(generatedSourceMapTexturesDirectory) { include("*.png", "manifest.json"); into("source-map-textures") }
    from(generatedStreetBodyLabelsDirectory) { include("*.png", "manifest.json"); into("street-body-labels") }
    from(generatedInfoLabelsDirectory) { include("*.png", "manifest.json"); into("info-labels") }
    from(generatedStreetSpeakerLabelsDirectory) { include("*.png", "manifest.json"); into("street-speaker-labels") }
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
