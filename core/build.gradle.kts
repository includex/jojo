plugins {
    kotlin("jvm")
}

// Trace harnesses are verification programs, not production entry points. The
// core-owned comparison tasks retain their public names, but execute the
// verification module's classes with that module's complete runtime classpath.
evaluationDependsOn(":verification")
val verificationMainRuntimeClasspath = project(":verification")
    .extensions
    .getByType<org.gradle.api.tasks.SourceSetContainer>()
    .getByName("main")
    .runtimeClasspath

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

// Every JavaExec trace consumes one or more checked-in JSON fixtures.  Gradle
// does not infer file inputs from command-line arguments, so declare the
// shared fixture set once to prevent stale Kotlin traces after a fixture edit.
val traceJsonFixtures = fileTree(rootProject.file("tools")) { include("**/*.json") }
tasks.withType<JavaExec>().configureEach {
    inputs.files(traceJsonFixtures)
}
// Source runners are Exec tasks, and several superseded declarations only listed
// outputs.  Track the same fixtures and recovered source tree centrally so an
// exact comparison can never reuse a source-side trace after either oracle or
// fixture changes.
val recoveredModuleSources = rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules")
tasks.withType<Exec>().configureEach {
    inputs.files(traceJsonFixtures)
    inputs.dir(recoveredModuleSources)
}

/** Guard the source-oracle location required by every recovered-JS trace. */
val verifySourceRootResolution = tasks.register<Exec>("verifySourceRootResolution") {
    inputs.file(rootProject.file("tools/verify_source_root_resolution.py"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    commandLine("python3", rootProject.file("tools/verify_source_root_resolution.py").absolutePath)
}
tasks.test { dependsOn(verifySourceRootResolution) }

/** Runs the Kotlin side of the shared MenuLayer source/game trace fixture. */
val menuLayerTrace = tasks.register<JavaExec>("menuLayerTrace") {
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.MenuLayerTraceHarness")
    args(System.getProperty("menuFixture") ?: rootProject.file("tools/menu_layer_trace_cases.json").absolutePath)
}
val terrainLayerTrace = tasks.register<JavaExec>("terrainLayerTrace") { dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath; mainClass.set("com.jojo.game.TerrainLayerTraceHarness"); args(rootProject.file("tools/terrain_layer_trace_cases.json").absolutePath) }

/** Executes the recovered source BattleUnit.countRate through its Cocos-minimal Node harness. */
val verifyBattleUnitCountRateSource = tasks.register<Exec>("verifyBattleUnitCountRateSource") {
    inputs.file(rootProject.file("tools/battle_unit_count_rate_source_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleUnit.js"))
    commandLine("node", rootProject.file("tools/battle_unit_count_rate_source_harness.js").absolutePath)
}

/** Executes recovered UnitInfoLayer.js lifecycle/listener trace with a minimal Cocos mock. */
val verifyUnitInfoSourceTrace = tasks.register<Exec>("verifyUnitInfoSourceTrace") {
    inputs.file(rootProject.file("tools/unit_info_source_trace_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/UnitInfoLayer.js"))
    commandLine("node", rootProject.file("tools/unit_info_source_trace_harness.js").absolutePath)
}
val unitInfoFixture = rootProject.file("tools/unit_info_trace_cases.json")
val unitInfoSourceOutput = layout.buildDirectory.file("unit-info/source-trace.json")
val unitInfoGameOutput = layout.buildDirectory.file("unit-info/game-trace.json")
verifyUnitInfoSourceTrace.configure { inputs.file(unitInfoFixture); args(unitInfoFixture.absolutePath, unitInfoSourceOutput.get().asFile.absolutePath); outputs.file(unitInfoSourceOutput) }
val unitInfoTrace = tasks.register<JavaExec>("unitInfoTrace") { dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath; mainClass.set("com.jojo.game.UnitInfoTraceHarness"); args(unitInfoFixture.absolutePath,unitInfoGameOutput.get().asFile.absolutePath); outputs.file(unitInfoGameOutput) }
val verifyUnitInfoPairwise = tasks.register<Exec>("verifyUnitInfoPairwise") { dependsOn(unitInfoTrace,verifyUnitInfoSourceTrace); commandLine("python3",rootProject.file("tools/verify_unit_info_pairwise.py").absolutePath,unitInfoSourceOutput.get().asFile.absolutePath,unitInfoGameOutput.get().asFile.absolutePath) }

val loadGameFixture=rootProject.file("tools/load_game_trace_cases.json")
val loadGameSourceOutput=layout.buildDirectory.file("load-game/source-trace.json")
val loadGameGameOutput=layout.buildDirectory.file("load-game/game-trace.json")
val verifyLoadGameSourceTrace=tasks.register<Exec>("verifyLoadGameSourceTrace"){inputs.files(loadGameFixture,rootProject.file("tools/load_game_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/LoadGameLayer.js"));commandLine("node",rootProject.file("tools/load_game_source_trace_harness.js").absolutePath,loadGameFixture.absolutePath,loadGameSourceOutput.get().asFile.absolutePath);outputs.file(loadGameSourceOutput)}
val loadGameTrace=tasks.register<JavaExec>("loadGameTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.LoadGameTraceHarness");args(loadGameFixture.absolutePath,loadGameGameOutput.get().asFile.absolutePath);outputs.file(loadGameGameOutput)}
val verifyLoadGamePairwise=tasks.register<Exec>("verifyLoadGamePairwise"){dependsOn(verifyLoadGameSourceTrace,loadGameTrace);commandLine("python3",rootProject.file("tools/verify_load_game_pairwise.py").absolutePath,loadGameSourceOutput.get().asFile.absolutePath,loadGameGameOutput.get().asFile.absolutePath)}
val battleLayerFixture=rootProject.file("tools/battle_layer_trace_cases.json");val battleLayerSourceOutput=layout.buildDirectory.file("battle-layer/source-trace.json");val battleLayerGameOutput=layout.buildDirectory.file("battle-layer/game-trace.json")
val verifyBattleScreenSourceTrace=tasks.register<Exec>("verifyBattleScreenSourceTrace"){commandLine("node",rootProject.file("tools/battle_layer_source_trace_harness.js").absolutePath,battleLayerFixture.absolutePath,battleLayerSourceOutput.get().asFile.absolutePath);outputs.file(battleLayerSourceOutput)}
val battleLayerTrace=tasks.register<JavaExec>("battleLayerTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.BattleScreenTraceHarness");args(battleLayerFixture.absolutePath,battleLayerGameOutput.get().asFile.absolutePath);outputs.file(battleLayerGameOutput)}
val verifyBattleScreenPairwise=tasks.register<Exec>("verifyBattleScreenPairwise"){dependsOn(verifyBattleScreenSourceTrace,battleLayerTrace);commandLine("python3",rootProject.file("tools/verify_battle_layer_pairwise.py").absolutePath,battleLayerSourceOutput.get().asFile.absolutePath,battleLayerGameOutput.get().asFile.absolutePath)}
val fightFixture=rootProject.file("tools/fight_presentation_trace_cases.json");val fightSource=layout.buildDirectory.file("fight/source.json");val fightGame=layout.buildDirectory.file("fight/game.json")
val verifyFightSource=tasks.register<Exec>("verifyFightPresentationSourceTrace"){commandLine("node",rootProject.file("tools/fight_presentation_source_trace_harness.js").absolutePath,fightFixture.absolutePath,fightSource.get().asFile.absolutePath);outputs.file(fightSource)}
val fightTrace=tasks.register<JavaExec>("fightPresentationTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.FightPresentationTraceHarness");args(fightFixture.absolutePath,fightGame.get().asFile.absolutePath);outputs.file(fightGame)}
val verifyFightPairwise=tasks.register<Exec>("verifyFightPresentationPairwise"){dependsOn(verifyFightSource,fightTrace);commandLine("python3",rootProject.file("tools/verify_fight_presentation_pairwise.py").absolutePath,fightSource.get().asFile.absolutePath,fightGame.get().asFile.absolutePath)}

/** WinConBox/Lose/End/Skip recovered factories and direct Kotlin lifecycle games. */
val endFlowFixture=rootProject.file("tools/end_flow_trace_cases.json")
val endFlowSource=layout.buildDirectory.file("end-flow/source.json")
val endFlowGame=layout.buildDirectory.file("end-flow/game.json")
val endFlowSourceTrace=tasks.register<Exec>("endFlowSourceTrace"){inputs.files(endFlowFixture,rootProject.file("tools/end_flow_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/WinConBoxLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/Lose.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/End.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SkipLayer.js"));outputs.file(endFlowSource);commandLine("node",rootProject.file("tools/end_flow_source_trace_harness.js").absolutePath,endFlowFixture.absolutePath,endFlowSource.get().asFile.absolutePath)}
val endFlowTrace=tasks.register<JavaExec>("endFlowTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.EndFlowTraceHarness");args(endFlowFixture.absolutePath,endFlowGame.get().asFile.absolutePath);outputs.file(endFlowGame)}
val verifyEndFlowPairwise=tasks.register<Exec>("verifyEndFlowPairwise"){dependsOn(endFlowSourceTrace,endFlowTrace);commandLine("python3",rootProject.file("tools/verify_end_flow_pairwise.py").absolutePath,endFlowSource.get().asFile.absolutePath,endFlowGame.get().asFile.absolutePath)}

tasks.test {
    dependsOn(verifyBattleUnitCountRateSource)
    dependsOn(verifyUnitInfoSourceTrace)
    dependsOn(verifyUnitInfoPairwise)
    dependsOn(verifyLoadGamePairwise)
    dependsOn(verifyBattleScreenPairwise)
    dependsOn(verifyFightPairwise)
    dependsOn(verifyEndFlowPairwise)
}

val verifyMenuLayerTrace = tasks.register<Exec>("verifyMenuLayerTrace") {
    dependsOn(menuLayerTrace)
    inputs.file(rootProject.file("tools/menu_layer_trace_cases.json"))
    inputs.file(rootProject.file("tools/menu_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("tools/verify_menu_layer_trace.py"))
    commandLine("python3", rootProject.file("tools/verify_menu_layer_trace.py").absolutePath)
}

tasks.test { dependsOn(verifyMenuLayerTrace) }

val verifyMenuLayerSwitchTrace = tasks.register<Exec>("verifyMenuLayerSwitchTrace") {
    dependsOn(menuLayerTrace)
    inputs.file(rootProject.file("tools/menu_layer_switch_trace_cases.json"))
    inputs.file(rootProject.file("tools/menu_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("tools/verify_menu_layer_trace.py"))
    commandLine("python3", rootProject.file("tools/verify_menu_layer_trace.py").absolutePath, rootProject.file("tools/menu_layer_switch_trace_cases.json").absolutePath)
}

tasks.test { dependsOn(verifyMenuLayerSwitchTrace) }

val verifyTerrainLayerPairwise = tasks.register<Exec>("verifyTerrainLayerPairwise") {
    dependsOn(terrainLayerTrace)
    inputs.file(rootProject.file("tools/terrain_layer_trace_cases.json"))
    inputs.file(rootProject.file("tools/terrain_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("tools/verify_terrain_layer_trace.py"))
    commandLine("python3", rootProject.file("tools/verify_terrain_layer_trace.py").absolutePath)
}
tasks.test { dependsOn(verifyTerrainLayerPairwise) }

/** Recovered ui/TreasureLayer.js factory and the direct Kotlin game. */
val treasureLayerFixture=rootProject.file("tools/treasure_layer_trace_cases.json")
val treasureLayerSource=layout.buildDirectory.file("treasure-layer/source.json")
val treasureLayerGame=layout.buildDirectory.file("treasure-layer/game.json")
val treasureLayerSourceTrace=tasks.register<Exec>("treasureLayerSourceTrace") {
    inputs.files(treasureLayerFixture,rootProject.file("tools/treasure_layer_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/TreasureLayer.js"))
    outputs.file(treasureLayerSource)
    commandLine("node",rootProject.file("tools/treasure_layer_source_trace_harness.js").absolutePath,treasureLayerFixture.absolutePath,treasureLayerSource.get().asFile.absolutePath)
}
val treasureLayerTrace=tasks.register<JavaExec>("treasureLayerTrace") { dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath; mainClass.set("com.jojo.game.TreasureLayerTraceHarness"); args(treasureLayerFixture.absolutePath,treasureLayerGame.get().asFile.absolutePath); outputs.file(treasureLayerGame) }
val verifyTreasureLayerPairwise=tasks.register<Exec>("verifyTreasureLayerPairwise") { dependsOn(treasureLayerSourceTrace,treasureLayerTrace); commandLine("python3",rootProject.file("tools/verify_treasure_layer_pairwise.py").absolutePath,treasureLayerSource.get().asFile.absolutePath,treasureLayerGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyTreasureLayerPairwise) }

/** Recovered ui/PropertyLayer.js and the Kotlin data/lifecycle game share this trace fixture. */
val propertyLayerFixture = rootProject.file("tools/property_layer_trace_cases.json")
val propertyLayerSourceOutput = layout.buildDirectory.file("property-layer/source-trace.json")
val propertyLayerGameOutput = layout.buildDirectory.file("property-layer/game-trace.json")
val propertyLayerSourceTrace = tasks.register<Exec>("propertyLayerSourceTrace") {
    inputs.file(propertyLayerFixture)
    inputs.file(rootProject.file("tools/property_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/PropertyLayer.js"))
    outputs.file(propertyLayerSourceOutput)
    commandLine("node", rootProject.file("tools/property_layer_source_trace_harness.js").absolutePath, propertyLayerFixture.absolutePath, propertyLayerSourceOutput.get().asFile.absolutePath)
}
val propertyLayerTrace = tasks.register<JavaExec>("propertyLayerTrace") {
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.PropertyLayerTraceHarness")
    args(propertyLayerFixture.absolutePath, propertyLayerGameOutput.get().asFile.absolutePath)
    outputs.file(propertyLayerGameOutput)
}
val verifyPropertyLayerPairwise = tasks.register<Exec>("verifyPropertyLayerPairwise") {
    dependsOn(propertyLayerSourceTrace, propertyLayerTrace)
    inputs.file(rootProject.file("tools/verify_property_layer_pairwise.py"))
    inputs.file(propertyLayerSourceOutput)
    inputs.file(propertyLayerGameOutput)
    commandLine("python3", rootProject.file("tools/verify_property_layer_pairwise.py").absolutePath, propertyLayerSourceOutput.get().asFile.absolutePath, propertyLayerGameOutput.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyPropertyLayerPairwise) }

val saveLayerFixture = rootProject.file("tools/save_layer_trace_cases.json")
val saveLayerSourceOutput = layout.buildDirectory.file("save-layer/source-trace.json")
val saveLayerGameOutput = layout.buildDirectory.file("save-layer/game-trace.json")
val saveLayerSourceTrace = tasks.register<Exec>("saveLayerSourceTrace") { dependsOn(tasks.classes); inputs.files(saveLayerFixture,rootProject.file("tools/save_layer_source_trace_harness.js")); outputs.file(saveLayerSourceOutput); commandLine("node",rootProject.file("tools/save_layer_source_trace_harness.js").absolutePath,saveLayerFixture.absolutePath,saveLayerSourceOutput.get().asFile.absolutePath) }
val saveLayerTrace = tasks.register<JavaExec>("saveLayerTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.SaveLayerTraceHarness");args(saveLayerFixture.absolutePath,saveLayerGameOutput.get().asFile.absolutePath);outputs.file(saveLayerGameOutput) }
val verifySaveLayerPairwise = tasks.register<Exec>("verifySaveLayerPairwise") { dependsOn(saveLayerSourceTrace,saveLayerTrace);commandLine("python3",rootProject.file("tools/verify_save_layer_pairwise.py").absolutePath,saveLayerSourceOutput.get().asFile.absolutePath,saveLayerGameOutput.get().asFile.absolutePath) }
val loadLayerFixture=rootProject.file("tools/load_layer_trace_cases.json");val loadLayerSource=layout.buildDirectory.file("load-layer/source.json");val loadLayerGame=layout.buildDirectory.file("load-layer/game.json")
val loadLayerSourceTrace=tasks.register<Exec>("loadLayerSourceTrace"){commandLine("node",rootProject.file("tools/load_layer_source_trace_harness.js").absolutePath,loadLayerFixture.absolutePath,loadLayerSource.get().asFile.absolutePath);outputs.file(loadLayerSource)}
val loadLayerTrace=tasks.register<JavaExec>("loadLayerTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.LoadLayerTraceHarness");args(loadLayerFixture.absolutePath,loadLayerGame.get().asFile.absolutePath);outputs.file(loadLayerGame)}
val verifyLoadLayerPairwise=tasks.register<Exec>("verifyLoadLayerPairwise"){dependsOn(loadLayerSourceTrace,loadLayerTrace);commandLine("python3",rootProject.file("tools/verify_load_layer_pairwise.py").absolutePath,loadLayerSource.get().asFile.absolutePath,loadLayerGame.get().asFile.absolutePath)}

/** SettingLayer: recovered JS and Kotlin receive identical lifecycle/input streams. */
val settingLayerFixture = rootProject.file("tools/setting_layer_trace_cases.json")
val settingLayerSourceOutput = layout.buildDirectory.file("setting-layer/source-trace.json")
val settingLayerGameOutput = layout.buildDirectory.file("setting-layer/game-trace.json")
val settingLayerSourceTrace = tasks.register<Exec>("settingLayerSourceTrace") {
    inputs.file(settingLayerFixture)
    inputs.file(rootProject.file("tools/setting_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SettingLayer.js"))
    outputs.file(settingLayerSourceOutput)
    commandLine("node", rootProject.file("tools/setting_layer_source_trace_harness.js").absolutePath, settingLayerFixture.absolutePath, settingLayerSourceOutput.get().asFile.absolutePath)
}
val settingLayerTrace = tasks.register<JavaExec>("settingLayerTrace") {
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.SettingLayerTraceHarness")
    args(settingLayerFixture.absolutePath, settingLayerGameOutput.get().asFile.absolutePath)
    outputs.file(settingLayerGameOutput)
}
val verifySettingLayerPairwise = tasks.register<Exec>("verifySettingLayerPairwise") {
    dependsOn(settingLayerSourceTrace, settingLayerTrace)
    inputs.file(rootProject.file("tools/verify_setting_layer_pairwise.py"))
    inputs.file(settingLayerSourceOutput)
    inputs.file(settingLayerGameOutput)
    commandLine("python3", rootProject.file("tools/verify_setting_layer_pairwise.py").absolutePath, settingLayerSourceOutput.get().asFile.absolutePath, settingLayerGameOutput.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifySettingLayerPairwise) }

/** HelperLayer: execute recovered JS and Kotlin against the same info/button fixture. */
val helperLayerFixture = rootProject.file("tools/helper_layer_trace_cases.json")
val helperLayerSourceOutput = layout.buildDirectory.file("helper-layer/source-trace.json")
val helperLayerGameOutput = layout.buildDirectory.file("helper-layer/game-trace.json")
val helperLayerSourceTrace = tasks.register<Exec>("helperLayerSourceTrace") {
    inputs.file(helperLayerFixture)
    inputs.file(rootProject.file("tools/helper_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/HelperLayer.js"))
    outputs.file(helperLayerSourceOutput)
    commandLine("node", rootProject.file("tools/helper_layer_source_trace_harness.js").absolutePath, helperLayerFixture.absolutePath, helperLayerSourceOutput.get().asFile.absolutePath)
}
val helperLayerTrace = tasks.register<JavaExec>("helperLayerTrace") {
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.HelperLayerTraceHarness")
    args(helperLayerFixture.absolutePath, helperLayerGameOutput.get().asFile.absolutePath)
    outputs.file(helperLayerGameOutput)
}
val verifyHelperLayerPairwise = tasks.register<Exec>("verifyHelperLayerPairwise") {
    dependsOn(helperLayerSourceTrace, helperLayerTrace)
    inputs.file(rootProject.file("tools/verify_helper_layer_pairwise.py"))
    inputs.file(helperLayerSourceOutput)
    inputs.file(helperLayerGameOutput)
    commandLine("python3", rootProject.file("tools/verify_helper_layer_pairwise.py").absolutePath, helperLayerSourceOutput.get().asFile.absolutePath, helperLayerGameOutput.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyHelperLayerPairwise) }

/** RoundLayer: exact round/camp indicator and two-second completion contract. */
val roundLayerFixture = rootProject.file("tools/round_layer_trace_cases.json")
val roundLayerSourceOutput = layout.buildDirectory.file("round-layer/source-trace.json")
val roundLayerGameOutput = layout.buildDirectory.file("round-layer/game-trace.json")
val roundLayerSourceTrace = tasks.register<Exec>("roundLayerSourceTrace") {
    inputs.file(roundLayerFixture)
    inputs.file(rootProject.file("tools/round_layer_source_trace_harness.js"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/RoundLayer.js"))
    outputs.file(roundLayerSourceOutput)
    commandLine("node", rootProject.file("tools/round_layer_source_trace_harness.js").absolutePath, roundLayerFixture.absolutePath, roundLayerSourceOutput.get().asFile.absolutePath)
}
val roundLayerTrace = tasks.register<JavaExec>("roundLayerTrace") {
    dependsOn(tasks.classes); classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.RoundLayerTraceHarness")
    args(roundLayerFixture.absolutePath, roundLayerGameOutput.get().asFile.absolutePath); outputs.file(roundLayerGameOutput)
}
val verifyRoundLayerPairwise = tasks.register<Exec>("verifyRoundLayerPairwise") {
    dependsOn(roundLayerSourceTrace, roundLayerTrace)
    inputs.file(rootProject.file("tools/verify_round_layer_pairwise.py")); inputs.file(roundLayerSourceOutput); inputs.file(roundLayerGameOutput)
    commandLine("python3", rootProject.file("tools/verify_round_layer_pairwise.py").absolutePath, roundLayerSourceOutput.get().asFile.absolutePath, roundLayerGameOutput.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyRoundLayerPairwise) }

val mapInfoFixture=rootProject.file("tools/map_info_layer_trace_cases.json")
val mapInfoSource=layout.buildDirectory.file("map-info/source.json");val mapInfoGame=layout.buildDirectory.file("map-info/game.json")
val mapInfoSourceTrace=tasks.register<Exec>("mapInfoLayerSourceTrace"){inputs.file(mapInfoFixture);inputs.file(rootProject.file("tools/map_info_layer_source_trace_harness.js"));inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/MapInfoLayer.js"));outputs.file(mapInfoSource);commandLine("node",rootProject.file("tools/map_info_layer_source_trace_harness.js").absolutePath,mapInfoFixture.absolutePath,mapInfoSource.get().asFile.absolutePath)}
val mapInfoTrace=tasks.register<JavaExec>("mapInfoLayerTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.MapInfoLayerTraceHarness");args(mapInfoFixture.absolutePath,mapInfoGame.get().asFile.absolutePath);outputs.file(mapInfoGame)}
val verifyMapInfoLayerPairwise=tasks.register<Exec>("verifyMapInfoLayerPairwise"){dependsOn(mapInfoSourceTrace,mapInfoTrace);commandLine("python3",rootProject.file("tools/verify_map_info_layer_pairwise.py").absolutePath,mapInfoSource.get().asFile.absolutePath,mapInfoGame.get().asFile.absolutePath)}
tasks.test { dependsOn(verifyMapInfoLayerPairwise) }

val miniMapFixture=rootProject.file("tools/mini_map_layer_trace_cases.json");val miniMapSource=layout.buildDirectory.file("mini-map/source.json");val miniMapGame=layout.buildDirectory.file("mini-map/game.json")
val miniMapSourceTrace=tasks.register<Exec>("miniMapLayerSourceTrace"){commandLine("node",rootProject.file("tools/mini_map_layer_source_trace_harness.js").absolutePath,miniMapFixture.absolutePath,miniMapSource.get().asFile.absolutePath);outputs.file(miniMapSource)}
val miniMapTrace=tasks.register<JavaExec>("miniMapLayerTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.MiniMapLayerTraceHarness");args(miniMapFixture.absolutePath,miniMapGame.get().asFile.absolutePath);outputs.file(miniMapGame)}
val verifyMiniMapLayerPairwise=tasks.register<Exec>("verifyMiniMapLayerPairwise"){dependsOn(miniMapSourceTrace,miniMapTrace);commandLine("python3",rootProject.file("tools/verify_mini_map_layer_pairwise.py").absolutePath,miniMapSource.get().asFile.absolutePath,miniMapGame.get().asFile.absolutePath)}
tasks.test { dependsOn(verifyMiniMapLayerPairwise) }

/** Enemy turn: recovered ControlManager/Control/Ctrl* selection and hand-off. */
val enemyTurnFixture=rootProject.file("tools/enemy_turn_trace_cases.json")
val enemyTurnSource=layout.buildDirectory.file("enemy-turn/source.json")
val enemyTurnGame=layout.buildDirectory.file("enemy-turn/game.json")
val enemyTurnSourceTrace=tasks.register<Exec>("enemyTurnSourceTrace") {
    inputs.file(enemyTurnFixture)
    inputs.file(rootProject.file("tools/enemy_turn_source_trace_harness.js"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle"))
    outputs.file(enemyTurnSource)
    commandLine("node",rootProject.file("tools/enemy_turn_source_trace_harness.js").absolutePath,enemyTurnFixture.absolutePath,enemyTurnSource.get().asFile.absolutePath)
}
val enemyTurnTrace=tasks.register<JavaExec>("enemyTurnTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.EnemyTurnTraceHarness")
    args(enemyTurnFixture.absolutePath,enemyTurnGame.get().asFile.absolutePath);outputs.file(enemyTurnGame)
}
val verifyEnemyTurnPairwise=tasks.register<Exec>("verifyEnemyTurnPairwise") {
    dependsOn(enemyTurnSourceTrace,enemyTurnTrace)
    inputs.file(rootProject.file("tools/verify_enemy_turn_pairwise.py"));inputs.file(enemyTurnSource);inputs.file(enemyTurnGame)
    commandLine("python3",rootProject.file("tools/verify_enemy_turn_pairwise.py").absolutePath,enemyTurnSource.get().asFile.absolutePath,enemyTurnGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyEnemyTurnPairwise) }

/** ForcesListLayer: original recovered factory and Kotlin game consume one input stream. */
val forcesListFixture=rootProject.file("tools/forces_list_layer_trace_cases.json")
val forcesListSource=layout.buildDirectory.file("forces-list/source-trace.json")
val forcesListGame=layout.buildDirectory.file("forces-list/game-trace.json")
val forcesListSourceTrace=tasks.register<Exec>("forcesListLayerSourceTrace") {
    inputs.files(forcesListFixture,rootProject.file("tools/forces_list_layer_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ForcesListLayer.js"))
    outputs.file(forcesListSource)
    commandLine("node",rootProject.file("tools/forces_list_layer_source_trace_harness.js").absolutePath,forcesListFixture.absolutePath,forcesListSource.get().asFile.absolutePath)
}
val forcesListTrace=tasks.register<JavaExec>("forcesListLayerTrace") {
    dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.ForcesListLayerTraceHarness")
    args(forcesListFixture.absolutePath,forcesListGame.get().asFile.absolutePath); outputs.file(forcesListGame)
}
val verifyForcesListLayerPairwise=tasks.register<Exec>("verifyForcesListLayerPairwise") {
    dependsOn(forcesListSourceTrace,forcesListTrace)
    inputs.file(rootProject.file("tools/verify_forces_list_layer_pairwise.py"))
    commandLine("python3",rootProject.file("tools/verify_forces_list_layer_pairwise.py").absolutePath,forcesListSource.get().asFile.absolutePath,forcesListGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyForcesListLayerPairwise) }

val sectionFixture=rootProject.file("tools/section_layer_trace_cases.json"); val sectionSource=layout.buildDirectory.file("section-layer/source.json"); val sectionGame=layout.buildDirectory.file("section-layer/game.json")
val sectionSourceTrace=tasks.register<Exec>("sectionLayerSourceTrace"){inputs.files(sectionFixture,rootProject.file("tools/section_layer_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SectionLayer.js"));outputs.file(sectionSource);commandLine("node",rootProject.file("tools/section_layer_source_trace_harness.js").absolutePath,sectionFixture.absolutePath,sectionSource.get().asFile.absolutePath)}
val sectionTrace=tasks.register<JavaExec>("sectionLayerTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.SectionLayerTraceHarness");args(sectionFixture.absolutePath,sectionGame.get().asFile.absolutePath);outputs.file(sectionGame)}
val verifySectionLayerPairwise=tasks.register<Exec>("verifySectionLayerPairwise"){dependsOn(sectionSourceTrace,sectionTrace);commandLine("python3",rootProject.file("tools/verify_section_layer_pairwise.py").absolutePath,sectionSource.get().asFile.absolutePath,sectionGame.get().asFile.absolutePath)}
tasks.test { dependsOn(verifySectionLayerPairwise) }

/** Recovered Magic/MagickListLayer UI selection and detail-sheet contract. */
val magicFixture=rootProject.file("tools/magic_trace_cases.json")
val magicSource=layout.buildDirectory.file("magic/source.json");val magicGame=layout.buildDirectory.file("magic/game.json")
val magicSourceTrace=tasks.register<Exec>("magicSourceTrace") { inputs.file(magicFixture);inputs.file(rootProject.file("tools/magic_source_trace_harness.js"));outputs.file(magicSource);commandLine("node",rootProject.file("tools/magic_source_trace_harness.js").absolutePath,magicFixture.absolutePath,magicSource.get().asFile.absolutePath) }
val magicTrace=tasks.register<JavaExec>("magicTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.MagicTraceHarness");args(magicFixture.absolutePath,magicGame.get().asFile.absolutePath);outputs.file(magicGame) }
val verifyMagicPairwise=tasks.register<Exec>("verifyMagicPairwise") { dependsOn(magicSourceTrace,magicTrace);commandLine("python3",rootProject.file("tools/verify_magic_pairwise.py").absolutePath,magicSource.get().asFile.absolutePath,magicGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyMagicPairwise) }

/** ChooseLayer, Choose2Layer and CommandLayer factory/listener contract. */
val choiceCommandFixture=rootProject.file("tools/choice_command_trace_cases.json")
val choiceCommandSource=layout.buildDirectory.file("choice-command/source.json")
val choiceCommandGame=layout.buildDirectory.file("choice-command/game.json")
val choiceCommandSourceTrace=tasks.register<Exec>("choiceCommandSourceTrace") {
    inputs.files(choiceCommandFixture,rootProject.file("tools/choice_command_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ChooseLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/Choose2Layer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/CommandLayer.js"))
    outputs.file(choiceCommandSource)
    commandLine("node",rootProject.file("tools/choice_command_source_trace_harness.js").absolutePath,choiceCommandFixture.absolutePath,choiceCommandSource.get().asFile.absolutePath)
}
val choiceCommandTrace=tasks.register<JavaExec>("choiceCommandTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.ChoiceCommandTraceHarness")
    args(choiceCommandFixture.absolutePath,choiceCommandGame.get().asFile.absolutePath);outputs.file(choiceCommandGame)
}
val verifyChoiceCommandPairwise=tasks.register<Exec>("verifyChoiceCommandPairwise") {
    dependsOn(choiceCommandSourceTrace,choiceCommandTrace)
    commandLine("python3",rootProject.file("tools/verify_choice_command_pairwise.py").absolutePath,choiceCommandSource.get().asFile.absolutePath,choiceCommandGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyChoiceCommandPairwise) }

/** ItemLayer / UsePropertyLayer / EquipConfirmLayer / EquipLayer direct factory parity. */
val itemEquipFixture=rootProject.file("tools/item_equip_trace_cases.json")
val itemEquipSource=layout.buildDirectory.file("item-equip/source.json")
val itemEquipGame=layout.buildDirectory.file("item-equip/game.json")
val itemEquipSourceTrace=tasks.register<Exec>("itemEquipSourceTrace") {
    inputs.files(itemEquipFixture,rootProject.file("tools/item_equip_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ItemLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/UsePropertyLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/EquipConfirmLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/EquipLayer.js"))
    outputs.file(itemEquipSource)
    commandLine("node",rootProject.file("tools/item_equip_source_trace_harness.js").absolutePath,itemEquipFixture.absolutePath,itemEquipSource.get().asFile.absolutePath)
}
val itemEquipTrace=tasks.register<JavaExec>("itemEquipTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.ItemEquipTraceHarness")
    args(itemEquipFixture.absolutePath,itemEquipGame.get().asFile.absolutePath);outputs.file(itemEquipGame)
}
val verifyItemEquipPairwise=tasks.register<Exec>("verifyItemEquipPairwise") {
    dependsOn(itemEquipSourceTrace,itemEquipTrace)
    commandLine("python3",rootProject.file("tools/verify_item_equip_pairwise.py").absolutePath,itemEquipSource.get().asFile.absolutePath,itemEquipGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyItemEquipPairwise) }

/** Feats/JiQi/Attribute/Exclusive recovered factory ↔ Kotlin isolated contract. */
val characterAbilityFixture=rootProject.file("tools/character_ability_trace_cases.json")
val characterAbilitySource=layout.buildDirectory.file("character-ability/source.json")
val characterAbilityGame=layout.buildDirectory.file("character-ability/game.json")
val characterAbilitySourceTrace=tasks.register<Exec>("characterAbilitySourceTrace") { inputs.files(characterAbilityFixture,rootProject.file("tools/character_ability_source_trace_harness.js"));outputs.file(characterAbilitySource);commandLine("node",rootProject.file("tools/character_ability_source_trace_harness.js").absolutePath,characterAbilityFixture.absolutePath,characterAbilitySource.get().asFile.absolutePath) }
val characterAbilityTrace=tasks.register<JavaExec>("characterAbilityTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.CharacterAbilityTraceHarness");args(characterAbilityFixture.absolutePath,characterAbilityGame.get().asFile.absolutePath);outputs.file(characterAbilityGame) }
val verifyCharacterAbilityPairwise=tasks.register<Exec>("verifyCharacterAbilityPairwise") { dependsOn(characterAbilitySourceTrace,characterAbilityTrace);commandLine("python3",rootProject.file("tools/verify_character_ability_pairwise.py").absolutePath,characterAbilitySource.get().asFile.absolutePath,characterAbilityGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyCharacterAbilityPairwise) }

/** CmdLayer is the 14-feature activation/store side-effect panel, separate from CommandLayer. */
val cmdLayerFixture=rootProject.file("tools/cmd_layer_trace_cases.json")
val cmdLayerSource=layout.buildDirectory.file("cmd-layer/source.json")
val cmdLayerGame=layout.buildDirectory.file("cmd-layer/game.json")
val cmdLayerSourceTrace=tasks.register<Exec>("cmdLayerSourceTrace") {
    inputs.files(cmdLayerFixture,rootProject.file("tools/cmd_layer_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/CmdLayer.js"))
    outputs.file(cmdLayerSource)
    commandLine("node",rootProject.file("tools/cmd_layer_source_trace_harness.js").absolutePath,cmdLayerFixture.absolutePath,cmdLayerSource.get().asFile.absolutePath)
}
val cmdLayerTrace=tasks.register<JavaExec>("cmdLayerTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.CmdLayerTraceHarness")
    args(cmdLayerFixture.absolutePath,cmdLayerGame.get().asFile.absolutePath);outputs.file(cmdLayerGame)
}
val verifyCmdLayerPairwise=tasks.register<Exec>("verifyCmdLayerPairwise") {
    dependsOn(cmdLayerSourceTrace,cmdLayerTrace)
    commandLine("python3",rootProject.file("tools/verify_cmd_layer_pairwise.py").absolutePath,cmdLayerSource.get().asFile.absolutePath,cmdLayerGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyCmdLayerPairwise) }

/** BattleViewLayer/DuleLayer/FightUnit recovered factory state and animation callbacks. */
val battleViewFixture=rootProject.file("tools/battle_view_trace_cases.json")
val battleViewSource=layout.buildDirectory.file("battle-view/source.json")
val battleViewGame=layout.buildDirectory.file("battle-view/game.json")
val battleViewSourceTrace=tasks.register<Exec>("battleViewSourceTrace") {
    inputs.files(battleViewFixture,rootProject.file("tools/battle_view_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleViewLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/DuleLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/FightUnit.js"))
    outputs.file(battleViewSource)
    commandLine("node",rootProject.file("tools/battle_view_source_trace_harness.js").absolutePath,battleViewFixture.absolutePath,battleViewSource.get().asFile.absolutePath)
}
val battleViewTrace=tasks.register<JavaExec>("battleViewTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.BattleViewTraceHarness")
    args(battleViewFixture.absolutePath,battleViewGame.get().asFile.absolutePath);outputs.file(battleViewGame)
}
val verifyBattleViewPairwise=tasks.register<Exec>("verifyBattleViewPairwise") {
    dependsOn(battleViewSourceTrace,battleViewTrace)
    commandLine("python3",rootProject.file("tools/verify_battle_view_pairwise.py").absolutePath,battleViewSource.get().asFile.absolutePath,battleViewGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyBattleViewPairwise) }

/** HallMenuLayer/HallCommandLayer: recovered factory routes, flags and animation completion. */
val hallUiFixture=rootProject.file("tools/hall_ui_trace_cases.json")
val hallUiSource=layout.buildDirectory.file("hall-ui/source.json")
val hallUiGame=layout.buildDirectory.file("hall-ui/game.json")
val hallUiSourceTrace=tasks.register<Exec>("hallUiSourceTrace") {
    inputs.files(hallUiFixture,rootProject.file("tools/hall_ui_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/HallMenuLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/HallCommandLayer.js"))
    outputs.file(hallUiSource)
    commandLine("node",rootProject.file("tools/hall_ui_source_trace_harness.js").absolutePath,hallUiFixture.absolutePath,hallUiSource.get().asFile.absolutePath)
}
val hallUiTrace=tasks.register<JavaExec>("hallUiTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.HallUiTraceHarness")
    args(hallUiFixture.absolutePath,hallUiGame.get().asFile.absolutePath);outputs.file(hallUiGame)
}
val verifyHallUiPairwise=tasks.register<Exec>("verifyHallUiPairwise") {
    dependsOn(hallUiSourceTrace,hallUiTrace)
    inputs.files(rootProject.file("tools/verify_hall_ui_pairwise.py"),hallUiSource,hallUiGame)
    commandLine("python3",rootProject.file("tools/verify_hall_ui_pairwise.py").absolutePath,hallUiSource.get().asFile.absolutePath,hallUiGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyHallUiPairwise) }

val hallPrepFixture=rootProject.file("tools/hall_prep_trace_cases.json")
val hallPrepSource=layout.buildDirectory.file("hall-prep/source.json")
val hallPrepGame=layout.buildDirectory.file("hall-prep/game.json")
val hallPrepSourceTrace=tasks.register<Exec>("hallPrepSourceTrace") { inputs.files(hallPrepFixture,rootProject.file("tools/hall_prep_source_trace_harness.js"));outputs.file(hallPrepSource);commandLine("node",rootProject.file("tools/hall_prep_source_trace_harness.js").absolutePath,hallPrepFixture.absolutePath,hallPrepSource.get().asFile.absolutePath) }
val hallPrepTrace=tasks.register<JavaExec>("hallPrepTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.HallPrepTraceHarness");args(hallPrepFixture.absolutePath,hallPrepGame.get().asFile.absolutePath);outputs.file(hallPrepGame) }
val verifyHallPrepPairwise=tasks.register<Exec>("verifyHallPrepPairwise") { dependsOn(hallPrepSourceTrace,hallPrepTrace);commandLine("python3",rootProject.file("tools/verify_hall_prep_pairwise.py").absolutePath,hallPrepSource.get().asFile.absolutePath,hallPrepGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyHallPrepPairwise) }

/** EditLayer owner mutations: source factory closures and Kotlin direct state games. */
val editMutationFixture=rootProject.file("tools/edit_mutation_trace_cases.json")
val editMutationSource=layout.buildDirectory.file("edit-mutation/source.json")
val editMutationGame=layout.buildDirectory.file("edit-mutation/game.json")
val editMutationSourceTrace=tasks.register<Exec>("editMutationSourceTrace") {
    inputs.files(editMutationFixture,rootProject.file("tools/edit_mutation_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/EditLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/misc/EditLayer2.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/misc/EditLayer3.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/misc/EditLayer4.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SAvatarEditLayer.js"))
    outputs.file(editMutationSource)
    commandLine("node",rootProject.file("tools/edit_mutation_source_trace_harness.js").absolutePath,editMutationFixture.absolutePath,editMutationSource.get().asFile.absolutePath)
}
val editMutationTrace=tasks.register<JavaExec>("editMutationTrace") { dependsOn(tasks.classes);inputs.file(editMutationFixture);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.EditMutationTraceHarness");args(editMutationFixture.absolutePath,editMutationGame.get().asFile.absolutePath);outputs.file(editMutationGame) }
val verifyEditMutationPairwise=tasks.register<Exec>("verifyEditMutationPairwise") { group="isolated oracle";description="Compares edit state contracts; does not verify their normal screen entry routes.";dependsOn(editMutationSourceTrace,editMutationTrace);commandLine("python3",rootProject.file("tools/verify_edit_mutation_pairwise.py").absolutePath,editMutationSource.get().asFile.absolutePath,editMutationGame.get().asFile.absolutePath) }
// Isolated source/game oracle only. Runtime coverage is enforced separately.

/** UnitListLayer plus Mine/OtherUnitInfoLayer and InfoBase value-animation. */
val unitListInfoFixture=rootProject.file("tools/unit_list_info_trace_cases.json")
val unitListInfoSource=layout.buildDirectory.file("unit-list-info/source.json")
val unitListInfoGame=layout.buildDirectory.file("unit-list-info/game.json")
val unitListInfoSourceTrace=tasks.register<Exec>("unitListInfoSourceTrace") {
    inputs.files(unitListInfoFixture,rootProject.file("tools/unit_list_info_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/UnitListLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/MineUnitInfoLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/OtherUnitInfoLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/InfoBaseLayer.js"))
    outputs.file(unitListInfoSource)
    commandLine("node",rootProject.file("tools/unit_list_info_source_trace_harness.js").absolutePath,unitListInfoFixture.absolutePath,unitListInfoSource.get().asFile.absolutePath)
}
val unitListInfoTrace=tasks.register<JavaExec>("unitListInfoTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.UnitListInfoLayerTraceHarness")
    args(unitListInfoFixture.absolutePath,unitListInfoGame.get().asFile.absolutePath);outputs.file(unitListInfoGame)
}
val verifyUnitListInfoPairwise=tasks.register<Exec>("verifyUnitListInfoPairwise") {
    group = "isolated oracle"
    description = "Compares list/info contracts; does not verify the complete normal navigation route."
    dependsOn(unitListInfoSourceTrace,unitListInfoTrace)
    commandLine("python3",rootProject.file("tools/verify_unit_list_info_pairwise.py").absolutePath,unitListInfoSource.get().asFile.absolutePath,unitListInfoGame.get().asFile.absolutePath)
}
// Isolated source/game oracle only. Runtime coverage is enforced separately.

// Candidate-only read-only source input.  The copied project lives below
// `.verification-work`, so the central project's relative source path is no
// longer its sibling.
val restoredScenarioDirectory = file("/Users/ain/workspace/jojo_mobile/sgccz-desktop/decompiled-python")
val generatedAstDirectory = layout.buildDirectory.dir("generated/scenario-ast")
val scenarioBranchSurface = layout.buildDirectory.file("reports/scenario-branch-surface.json")
val cocosAssetsDirectory = file("/Users/ain/workspace/jojo_mobile/sgccz-desktop/assets")
val generatedMapAssetsDirectory = layout.buildDirectory.dir("generated/map-assets")
val generatedAudioAssetsDirectory = layout.buildDirectory.dir("generated/audio-assets")
val generatedTitleAssetsDirectory = layout.buildDirectory.dir("generated/title-assets")
val generatedTitleLoadConfirmationDirectory = layout.buildDirectory.dir("generated/title-load-confirmations")
val generatedReferenceFramebuffersDirectory = layout.buildDirectory.dir("generated/reference-framebuffers")
val sourceLoginFramebuffers = files(
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login.rgba"),
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login-1-blank.rgba"),
    rootProject.file(".verification-work/natural-battle-capture/captures/source-login-2.rgba"),
)

/**
 * The Login scene is an authored, fully composited Cocos screen.  Preserve its
 * captured RGBA8 reference in the packaged application until the individual
 * Cocos title SpriteFrames have all been recovered.  Keeping this as a build
 * input also makes the title screenshot oracle explicit and reproducible.
 */
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
    commandLine(
        "python3",
        rootProject.file("tools/extract_title_load_confirmation_crops.py").absolutePath,
        rootProject.file(".verification-work/natural-battle-capture/captures").absolutePath,
        generatedTitleLoadConfirmationDirectory.get().asFile.absolutePath,
    )
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

val exportScenarioAst = tasks.register<Exec>("exportScenarioAst") {
    inputs.dir(restoredScenarioDirectory)
    inputs.file(rootProject.file("tools/export_python_ast.py"))
    outputs.dir(generatedAstDirectory)
    commandLine(
        "python3",
        rootProject.file("tools/export_python_ast.py").absolutePath,
        restoredScenarioDirectory.absolutePath,
        generatedAstDirectory.get().asFile.absolutePath
    )
}

// This records, rather than hides, the player-choice/RNG surface that the
// deterministic desktop scenario sweep cannot exhaust in one replay.
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

val exportMapAssets = tasks.register<Exec>("exportMapAssets") {
    inputs.dir(cocosAssetsDirectory)
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/choice-atlas.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/python-source-login-fixture-texture-2.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/python-source-login-load-fixture-texture-1.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/python-source-login-load-confirm-fixture-texture-1.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/python-source-login-setting-fixture-texture-1.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/battle-hud-atlas.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/terrain-layer-atlas.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/start-battle-atlas.png"))
    inputs.file(cocosAssetsDirectory.parentFile.resolve("build/python-source-battle-verification-dialogue3.png"))
    outputs.dir(generatedMapAssetsDirectory)
    commandLine(
        "python3",
        rootProject.file("tools/export_map_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath,
        generatedMapAssetsDirectory.get().asFile.absolutePath
    )
}

/** Source-to-game conformance gate for every BattleUnit atlas and animeBR row. */
val verifyBattleSpriteAssets = tasks.register<Exec>("verifyBattleSpriteAssets") {
    dependsOn(exportMapAssets)
    inputs.dir(cocosAssetsDirectory)
    inputs.dir(generatedMapAssetsDirectory)
    inputs.file(rootProject.file("tools/verify_battle_sprite_assets.py"))
    commandLine(
        "python3",
        rootProject.file("tools/verify_battle_sprite_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath,
        generatedMapAssetsDirectory.get().asFile.absolutePath,
    )
}

/** TerrainLayer's 28 Game/Terrain SpriteFrames must not be substituted. */
val verifyTerrainLayerAssets = tasks.register<Exec>("verifyTerrainLayerAssets") {
    dependsOn(exportMapAssets)
    inputs.dir(cocosAssetsDirectory)
    inputs.dir(generatedMapAssetsDirectory)
    inputs.file(rootProject.file("tools/verify_terrain_layer_assets.py"))
    commandLine(
        "python3",
        rootProject.file("tools/verify_terrain_layer_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath,
        generatedMapAssetsDirectory.get().asFile.absolutePath,
    )
}

val exportAudioAssets = tasks.register<Exec>("exportAudioAssets") {
    inputs.dir(cocosAssetsDirectory)
    inputs.file(rootProject.file("tools/export_audio_assets.py"))
    outputs.dir(generatedAudioAssetsDirectory)
    commandLine(
        "python3",
        rootProject.file("tools/export_audio_assets.py").absolutePath,
        cocosAssetsDirectory.absolutePath,
        generatedAudioAssetsDirectory.get().asFile.absolutePath,
    )
}

tasks.processResources {
    dependsOn(exportScenarioAst)
    dependsOn(exportMapAssets)
    dependsOn(exportAudioAssets)
    dependsOn(exportTitleLoginReference)
    dependsOn(extractTitleLoadConfirmations)
    dependsOn(exportScenarioChoiceReference)
    from(restoredScenarioDirectory) {
        include("*.py", "manifest.json")
        into("scenarios")
    }
    from(generatedAstDirectory) {
        into("scenario-ast")
    }
    from(generatedMapAssetsDirectory) {
        into("maps")
    }
    from(generatedAudioAssetsDirectory) {
        into("audio")
    }
    from(generatedTitleAssetsDirectory) {
        into("title")
    }
    from(generatedTitleLoadConfirmationDirectory) {
        into("title")
    }
    from(generatedReferenceFramebuffersDirectory) {
        into("reference")
    }
}

tasks.test {
    dependsOn(verifyTerrainLayerAssets)
}

// Focused reward-route regression entrypoint.  The ordinary `test` task also
// owns the repository-wide source/game pairwise gates; keeping this focused
// Test task lets reward input/state/callback behavior be verified without
// recursively launching those unrelated harnesses.
tasks.register<Test>("battleRewardFlowTest") {
    useJUnitPlatform()
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    filter {
        includeTestsMatching("com.jojo.game.BattleRewardFlowTest")
        includeTestsMatching("com.jojo.game.ScenarioRuntimeTest")
    }
}

/** Recovered MsgBox/Toast/Progress/Loading factories against direct Kotlin games. */
val systemUiFixture = rootProject.file("tools/system_ui_trace_cases.json")
val systemUiSource = layout.buildDirectory.file("system-ui/source.json")
val systemUiGame = layout.buildDirectory.file("system-ui/game.json")
val systemUiSourceTrace = tasks.register<Exec>("systemUiSourceTrace") {
    inputs.files(systemUiFixture, rootProject.file("tools/system_ui_source_trace_harness.js"))
    outputs.file(systemUiSource)
    commandLine("node", rootProject.file("tools/system_ui_source_trace_harness.js").absolutePath, systemUiFixture.absolutePath, systemUiSource.get().asFile.absolutePath)
}
val systemUiTrace = tasks.register<JavaExec>("systemUiTrace") {
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.SystemUiTraceHarness")
    args(systemUiFixture.absolutePath, systemUiGame.get().asFile.absolutePath)
    outputs.file(systemUiGame)
}
val verifySystemUiPairwise = tasks.register<Exec>("verifySystemUiPairwise") {
    dependsOn(systemUiSourceTrace, systemUiTrace)
    inputs.file(rootProject.file("tools/verify_system_ui_pairwise.py"))
    commandLine("python3", rootProject.file("tools/verify_system_ui_pairwise.py").absolutePath, systemUiSource.get().asFile.absolutePath, systemUiGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifySystemUiPairwise) }

/** Desktop-reachable recovered platform factories: consent, statement, version,
 * installer, hot-update boundary and login floor gate. */
val platformFixture=rootProject.file("tools/platform_trace_cases.json")
val platformSource=layout.buildDirectory.file("platform/source.json")
val platformGame=layout.buildDirectory.file("platform/game.json")
val platformSourceTrace=tasks.register<Exec>("platformSourceTrace") {
    inputs.files(platformFixture,rootProject.file("tools/platform_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/Login.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/InstallLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/HotUpdateLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/PrivacyLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/StatementLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/VersionInfoLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/SdkBase.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/Taptap.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/platform/VideoLayer.js"))
    outputs.file(platformSource)
    commandLine("node",rootProject.file("tools/platform_source_trace_harness.js").absolutePath,platformFixture.absolutePath,platformSource.get().asFile.absolutePath)
}
val platformTrace=tasks.register<JavaExec>("platformTrace") {
    dependsOn(tasks.classes)
    inputs.file(platformFixture)
    classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.PlatformTraceHarness")
    args(platformFixture.absolutePath,platformGame.get().asFile.absolutePath)
    outputs.file(platformGame)
}
val verifyPlatformPairwise=tasks.register<Exec>("verifyPlatformPairwise") { dependsOn(platformSourceTrace,platformTrace);commandLine("python3",rootProject.file("tools/verify_platform_pairwise.py").absolutePath,platformSource.get().asFile.absolutePath,platformGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyPlatformPairwise) }

/** Core foundation factories: UUID, encrypted defaults, state transitions and event mutation. */
val foundationFixture=rootProject.file("tools/foundation_trace_cases.json")
val foundationSource=layout.buildDirectory.file("foundation/source.json")
val foundationGame=layout.buildDirectory.file("foundation/game.json")
val foundationSourceTrace=tasks.register<Exec>("foundationSourceTrace") { inputs.files(foundationFixture,rootProject.file("tools/foundation_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/UUIDManager.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/UserDefault.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/StatusManager.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/JSEvent.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Tool.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/MD5.js"));outputs.file(foundationSource);commandLine("node",rootProject.file("tools/foundation_source_trace_harness.js").absolutePath,foundationFixture.absolutePath,foundationSource.get().asFile.absolutePath) }
val foundationTrace=tasks.register<JavaExec>("foundationTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.FoundationTraceHarness");args(foundationFixture.absolutePath,foundationGame.get().asFile.absolutePath);outputs.file(foundationGame)}
val verifyFoundationPairwise=tasks.register<Exec>("verifyFoundationPairwise"){dependsOn(foundationSourceTrace,foundationTrace);commandLine("python3",rootProject.file("tools/verify_foundation_pairwise.py").absolutePath,foundationSource.get().asFile.absolutePath,foundationGame.get().asFile.absolutePath)}
tasks.test { dependsOn(verifyFoundationPairwise) }
val modelRandomFixture=rootProject.file("tools/model_random_trace_cases.json")
val modelRandomSource=layout.buildDirectory.file("model-random/source.json")
val modelRandomGame=layout.buildDirectory.file("model-random/game.json")
val modelRandomSourceTrace=tasks.register<Exec>("modelRandomSourceTrace") {
    inputs.files(modelRandomFixture,rootProject.file("tools/model_random_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Tool.js"))
    outputs.file(modelRandomSource)
    commandLine("node",rootProject.file("tools/model_random_source_trace_harness.js").absolutePath,modelRandomFixture.absolutePath,modelRandomSource.get().asFile.absolutePath)
}
val modelRandomTrace=tasks.register<JavaExec>("modelRandomTrace") {
    dependsOn(tasks.classes); inputs.file(modelRandomFixture); classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.ModelRandomTraceHarness"); args(modelRandomFixture.absolutePath,modelRandomGame.get().asFile.absolutePath); outputs.file(modelRandomGame)
}
val verifyModelRandomPairwise=tasks.register<Exec>("verifyModelRandomPairwise") { dependsOn(modelRandomSourceTrace,modelRandomTrace); commandLine("python3",rootProject.file("tools/verify_model_random_pairwise.py").absolutePath,modelRandomSource.get().asFile.absolutePath,modelRandomGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyModelRandomPairwise) }
val frameworkServiceFixture=rootProject.file("tools/framework_trace_cases.json");val frameworkServiceSource=layout.buildDirectory.file("framework-service/source.json");val frameworkServiceGame=layout.buildDirectory.file("framework-service/game.json")
val frameworkServiceSourceTrace=tasks.register<Exec>("frameworkServiceSourceTrace"){inputs.files(frameworkServiceFixture,rootProject.file("tools/framework_source_trace_harness.js"));outputs.file(frameworkServiceSource);commandLine("node",rootProject.file("tools/framework_source_trace_harness.js").absolutePath,frameworkServiceFixture.absolutePath,frameworkServiceSource.get().asFile.absolutePath)}
val frameworkServiceTrace=tasks.register<JavaExec>("frameworkServiceTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.FrameworkServiceTraceHarness");args(frameworkServiceFixture.absolutePath,frameworkServiceGame.get().asFile.absolutePath);outputs.file(frameworkServiceGame)}
val verifyFrameworkServicePairwise=tasks.register<Exec>("verifyFrameworkServicePairwise"){dependsOn(frameworkServiceSourceTrace,frameworkServiceTrace);commandLine("python3",rootProject.file("tools/verify_framework_pairwise.py").absolutePath,frameworkServiceSource.get().asFile.absolutePath,frameworkServiceGame.get().asFile.absolutePath)}

/** Recovered miscellaneous overlay factories against direct Kotlin games. */
val miscUiFixture=rootProject.file("tools/misc_ui_trace_cases.json")
val miscUiSource=layout.buildDirectory.file("misc-ui/source.json")
val miscUiGame=layout.buildDirectory.file("misc-ui/game.json")
val miscUiSourceTrace=tasks.register<Exec>("miscUiSourceTrace") { inputs.files(miscUiFixture,rootProject.file("tools/misc_ui_source_trace_harness.js"));outputs.file(miscUiSource);commandLine("node",rootProject.file("tools/misc_ui_source_trace_harness.js").absolutePath,miscUiFixture.absolutePath,miscUiSource.get().asFile.absolutePath) }
val miscUiTrace=tasks.register<JavaExec>("miscUiTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.MiscUiTraceHarness");args(miscUiFixture.absolutePath,miscUiGame.get().asFile.absolutePath);outputs.file(miscUiGame) }
val verifyMiscUiPairwise=tasks.register<Exec>("verifyMiscUiPairwise") { dependsOn(miscUiSourceTrace,miscUiTrace);commandLine("python3",rootProject.file("tools/verify_misc_ui_pairwise.py").absolutePath,miscUiSource.get().asFile.absolutePath,miscUiGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyMiscUiPairwise) }

/** Recovered progression overlays: achievements, sign-in, raffle, reset, register. */
val progressionLayerFixture=rootProject.file("tools/progression_layer_trace_cases.json")
val progressionLayerSource=layout.buildDirectory.file("progression-layer/source.json")
val progressionLayerGame=layout.buildDirectory.file("progression-layer/game.json")
val progressionLayerSourceTrace=tasks.register<Exec>("progressionLayerSourceTrace") {
    inputs.files(progressionLayerFixture,rootProject.file("tools/progression_layer_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/AchievementsLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SignInLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/RaffleLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ResetLayer.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/RegisterLayer.js"))
    outputs.file(progressionLayerSource)
    commandLine("node",rootProject.file("tools/progression_layer_source_trace_harness.js").absolutePath,progressionLayerFixture.absolutePath,progressionLayerSource.get().asFile.absolutePath)
}
val progressionLayerTrace=tasks.register<JavaExec>("progressionLayerTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.ProgressionLayerTraceHarness");args(progressionLayerFixture.absolutePath,progressionLayerGame.get().asFile.absolutePath);outputs.file(progressionLayerGame) }
val verifyProgressionLayerPairwise=tasks.register<Exec>("verifyProgressionLayerPairwise") { group="isolated oracle";description="Compares progression state machines; does not verify their normal menu routes.";dependsOn(progressionLayerSourceTrace,progressionLayerTrace);commandLine("python3",rootProject.file("tools/verify_progression_layer_pairwise.py").absolutePath,progressionLayerSource.get().asFile.absolutePath,progressionLayerGame.get().asFile.absolutePath) }
// Isolated source/game oracle only. Runtime coverage is enforced separately.

/** RewardLayer + BuyLayer + SellLayer.  Deliberately not in the aggregate
 * until their direct factory trace is expanded through every inherited UI path. */
val shopRewardFixture=rootProject.file("tools/shop_reward_trace_cases.json")
val shopRewardSource=layout.buildDirectory.file("shop-reward/source.json")
val shopRewardGame=layout.buildDirectory.file("shop-reward/game.json")
val shopRewardSourceTrace=tasks.register<Exec>("shopRewardSourceTrace") {
    inputs.files(shopRewardFixture,rootProject.file("tools/shop_reward_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/RewardLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/BuyLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SellLayer.js"))
    outputs.file(shopRewardSource)
    commandLine("node",rootProject.file("tools/shop_reward_source_trace_harness.js").absolutePath,shopRewardFixture.absolutePath,shopRewardSource.get().asFile.absolutePath)
}
val shopRewardTrace=tasks.register<JavaExec>("shopRewardTrace") {
    dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.ShopRewardTraceHarness")
    args(shopRewardFixture.absolutePath,shopRewardGame.get().asFile.absolutePath); outputs.file(shopRewardGame)
}
val verifyShopRewardPairwise=tasks.register<Exec>("verifyShopRewardPairwise") {
    dependsOn(shopRewardSourceTrace,shopRewardTrace)
    commandLine("python3",rootProject.file("tools/verify_shop_reward_pairwise.py").absolutePath,shopRewardSource.get().asFile.absolutePath,shopRewardGame.get().asFile.absolutePath)
}

/** Full shop contract: Buy UnitInfoBase equip callback, Sell quantity callback,
 * and RewardLayer's source coroutine/card asset ordering. */
val shopRewardFullFixture=rootProject.file("tools/shop_reward_full_trace_cases.json")
val shopRewardFullSource=layout.buildDirectory.file("shop-reward-full/source.json")
val shopRewardFullGame=layout.buildDirectory.file("shop-reward-full/game.json")
val shopRewardFullSourceTrace=tasks.register<Exec>("shopRewardFullSourceTrace") {
    inputs.files(shopRewardFullFixture,rootProject.file("tools/shop_reward_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/RewardLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/BuyLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SellLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/UnitInfoBaseLayer.js"))
    outputs.file(shopRewardFullSource)
    commandLine("node",rootProject.file("tools/shop_reward_source_trace_harness.js").absolutePath,shopRewardFullFixture.absolutePath,shopRewardFullSource.get().asFile.absolutePath)
}
val shopRewardFullTrace=tasks.register<JavaExec>("shopRewardFullTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.ShopRewardTraceHarness")
    args(shopRewardFullFixture.absolutePath,shopRewardFullGame.get().asFile.absolutePath);outputs.file(shopRewardFullGame)
}
val verifyShopRewardFullPairwise=tasks.register<Exec>("verifyShopRewardFullPairwise") {
    dependsOn(shopRewardFullSourceTrace,shopRewardFullTrace)
    commandLine("python3",rootProject.file("tools/verify_shop_reward_pairwise.py").absolutePath,shopRewardFullSource.get().asFile.absolutePath,shopRewardFullGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyShopRewardFullPairwise) }

/** Foundation A/F: recovered Item + ItemStore inventory layout and HallCfg. */
val gameDataFixture=rootProject.file("tools/game_data_trace_cases.json")
val gameDataSource=layout.buildDirectory.file("game-data/source.json")
val gameDataGame=layout.buildDirectory.file("game-data/game.json")
val gameDataSourceTrace=tasks.register<Exec>("gameDataSourceTrace") {
    inputs.files(gameDataFixture,rootProject.file("tools/game_data_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/game-data/Item.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/game-data/ItemStore.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/game-data/HallCfg.js"))
    outputs.file(gameDataSource)
    commandLine("node",rootProject.file("tools/game_data_source_trace_harness.js").absolutePath,gameDataFixture.absolutePath,gameDataSource.get().asFile.absolutePath)
}
val gameDataTrace=tasks.register<JavaExec>("gameDataTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.GameDataTraceHarness");args(gameDataFixture.absolutePath,gameDataGame.get().asFile.absolutePath);outputs.file(gameDataGame) }
val verifyGameDataPairwise=tasks.register<Exec>("verifyGameDataPairwise") { dependsOn(gameDataSourceTrace,gameDataTrace);commandLine("python3",rootProject.file("tools/verify_game_data_pairwise.py").absolutePath,gameDataSource.get().asFile.absolutePath,gameDataGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyGameDataPairwise) }

/** Item upgrade result, starter item choice, and skill-editor mutation flows. */
val upgradeSkillFixture=rootProject.file("tools/upgrade_skill_trace_cases.json")
val upgradeSkillSource=layout.buildDirectory.file("upgrade-skill/source.json")
val upgradeSkillGame=layout.buildDirectory.file("upgrade-skill/game.json")
val upgradeSkillSourceTrace=tasks.register<Exec>("upgradeSkillSourceTrace") {
    inputs.files(upgradeSkillFixture,rootProject.file("tools/upgrade_skill_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/ItemUpgradeLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/StartItemLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/LearnUnitSkillLayer.js"))
    outputs.file(upgradeSkillSource)
    commandLine("node",rootProject.file("tools/upgrade_skill_source_trace_harness.js").absolutePath,upgradeSkillFixture.absolutePath,upgradeSkillSource.get().asFile.absolutePath)
}
val upgradeSkillTrace=tasks.register<JavaExec>("upgradeSkillTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.UpgradeSkillTraceHarness")
    args(upgradeSkillFixture.absolutePath,upgradeSkillGame.get().asFile.absolutePath);outputs.file(upgradeSkillGame)
}
val verifyUpgradeSkillPairwise=tasks.register<Exec>("verifyUpgradeSkillPairwise") {
    dependsOn(upgradeSkillSourceTrace,upgradeSkillTrace)
    commandLine("python3",rootProject.file("tools/verify_upgrade_skill_pairwise.py").absolutePath,upgradeSkillSource.get().asFile.absolutePath,upgradeSkillGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyUpgradeSkillPairwise) }

/** Actual Battle equipment mutation plus Global113 timer/cancel callback. */
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

/** Auto-battle cancellation overlay plus MsgBox4's TUOGUAN option persistence. */
val autoBattleFixture=rootProject.file("tools/auto_battle_trace_cases.json")
val autoBattleSource=layout.buildDirectory.file("auto-battle/source.json")
val autoBattleGame=layout.buildDirectory.file("auto-battle/game.json")
val autoBattleSourceTrace=tasks.register<Exec>("autoBattleSourceTrace") {
    inputs.files(autoBattleFixture,rootProject.file("tools/auto_battle_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/TuoGuanLayer.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/MsgBox4.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/MsgBox.js"))
    outputs.file(autoBattleSource)
    commandLine("node",rootProject.file("tools/auto_battle_source_trace_harness.js").absolutePath,autoBattleFixture.absolutePath,autoBattleSource.get().asFile.absolutePath)
}
val autoBattleTrace=tasks.register<JavaExec>("autoBattleTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.AutoBattleTraceHarness")
    args(autoBattleFixture.absolutePath,autoBattleGame.get().asFile.absolutePath);outputs.file(autoBattleGame)
}
val verifyAutoBattlePairwise=tasks.register<Exec>("verifyAutoBattlePairwise") {
    dependsOn(autoBattleSourceTrace,autoBattleTrace)
    commandLine("python3",rootProject.file("tools/verify_auto_battle_pairwise.py").absolutePath,autoBattleSource.get().asFile.absolutePath,autoBattleGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyAutoBattlePairwise) }

/** Actual END_ROUND confirmation and TuoGuan cancellation route only. */
tasks.register<Test>("autoBattleFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.AutoBattleFlowTest") }
}

/** Actual unit selection/move into CommandLayer, including child and rollback routes. */
tasks.register<Test>("battleCommandFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter { includeTestsMatching("com.jojo.game.BattleCommandFlowTest") }
}

/** Focused source-contract checks for the EDIT-gated battle/global/hall editors. */
tasks.register<Test>("editAdminFlowTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform()
    filter {
        includeTestsMatching("com.jojo.game.BattleEditLayer2Test")
        includeTestsMatching("com.jojo.game.EditAdminFlowsTest")
    }
}

/** Battle.js registry/routes and the complete BattleConfg numeric contract. */
val battleBootstrapFixture=rootProject.file("tools/battle_bootstrap_trace_cases.json")
val battleBootstrapSource=layout.buildDirectory.file("battle-bootstrap/source.json")
val battleBootstrapKotlin=layout.buildDirectory.file("battle-bootstrap/kotlin.json")
val battleBootstrapSourceTrace=tasks.register<Exec>("battleBootstrapSourceTrace") {
    inputs.files(battleBootstrapFixture,rootProject.file("tools/battle_bootstrap_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/Battle.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/BattleConfg.js"))
    outputs.file(battleBootstrapSource)
    commandLine("node",rootProject.file("tools/battle_bootstrap_source_trace_harness.js").absolutePath,battleBootstrapFixture.absolutePath,battleBootstrapSource.get().asFile.absolutePath)
}
val battleBootstrapTrace=tasks.register<JavaExec>("battleBootstrapTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.BattleBootstrapTraceHarness")
    args(battleBootstrapFixture.absolutePath,battleBootstrapKotlin.get().asFile.absolutePath);outputs.file(battleBootstrapKotlin)
}
val verifyBattleSceneCoordinatorBehavior=tasks.register<Exec>("verifyBattleSceneCoordinatorBehavior") {
    group = "isolated oracle"
    description = "Compares Battle.js event behavior through production BattleSceneCoordinator; not a screen-entry test."
    dependsOn(battleBootstrapSourceTrace,battleBootstrapTrace)
    inputs.file(rootProject.file("tools/verify_battle_scene_coordinator_behavior.py"))
    commandLine("python3",rootProject.file("tools/verify_battle_scene_coordinator_behavior.py").absolutePath,battleBootstrapSource.get().asFile.absolutePath,battleBootstrapKotlin.get().asFile.absolutePath)
}
val auditBattleBootstrapSourceInventory=tasks.register<Exec>("auditBattleBootstrapSourceInventory") {
    group="source inventory"
    description="Audits recovered BattleConfg constants and Battle.LAYER names without claiming Kotlin parity."
    dependsOn(battleBootstrapSourceTrace)
    inputs.file(rootProject.file("tools/audit_battle_bootstrap_source_inventory.py"))
    commandLine("python3",rootProject.file("tools/audit_battle_bootstrap_source_inventory.py").absolutePath,battleBootstrapSource.get().asFile.absolutePath)
}
// Isolated source/game oracle only. Runtime coverage is enforced separately.

/** Residual Ctrl* branch matrix excluded from the basic enemy-turn handoff gate. */
val battleControlFullFixture=rootProject.file("tools/battle_control_full_trace_cases.json")
val battleControlFullSource=layout.buildDirectory.file("battle-control-full/source.json")
val battleControlFullSourceTrace=tasks.register<Exec>("battleControlFullSourceTrace") {
    inputs.files(battleControlFullFixture,rootProject.file("tools/battle_control_full_source_trace_harness.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/Control.js"),
        rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlDZDD.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlGJWJ.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlGSWJ.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlTZZDD.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlYDDZDDBM.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/battle/CtrlYDDZDDGJ.js"))
    outputs.file(battleControlFullSource)
    commandLine("node",rootProject.file("tools/battle_control_full_source_trace_harness.js").absolutePath,battleControlFullFixture.absolutePath,battleControlFullSource.get().asFile.absolutePath)
}
val auditBattleControlSourceInventory=tasks.register<Exec>("auditBattleControlSourceInventory") { group="source inventory";description="Inventories overridden recovered Control fixture branches; makes no Kotlin parity claim.";dependsOn(battleControlFullSourceTrace);inputs.files(battleControlFullFixture,rootProject.file("tools/audit_battle_control_source_inventory.py"));commandLine("python3",rootProject.file("tools/audit_battle_control_source_inventory.py").absolutePath,battleControlFullFixture.absolutePath,battleControlFullSource.get().asFile.absolutePath) }
// Isolated source/game oracle only. Runtime coverage is enforced separately.

/** Recovered Python bytecode manager and RControlScript dispatch boundary. */
val scenarioRuntimeFixture=rootProject.file("tools/scenario_runtime_trace_cases.json")
val scenarioRuntimeSource=layout.buildDirectory.file("scenario-runtime/source.json")
val scenarioRuntimeGame=layout.buildDirectory.file("scenario-runtime/game.json")
val scenarioRuntimeSourceTrace=tasks.register<Exec>("scenarioRuntimeSourceTrace") {
    inputs.files(scenarioRuntimeFixture,rootProject.file("tools/scenario_runtime_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/scenario-runtime/PyManager.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/scenario-runtime/RControlScript.js"))
    outputs.file(scenarioRuntimeSource)
    commandLine("node",rootProject.file("tools/scenario_runtime_source_trace_harness.js").absolutePath,scenarioRuntimeFixture.absolutePath,scenarioRuntimeSource.get().asFile.absolutePath)
}
val scenarioRuntimeTrace=tasks.register<JavaExec>("scenarioRuntimeTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.ScenarioRuntimeTraceHarness");args(scenarioRuntimeFixture.absolutePath,scenarioRuntimeGame.get().asFile.absolutePath);outputs.file(scenarioRuntimeGame) }
val verifyScenarioRuntimePairwise=tasks.register<Exec>("verifyScenarioRuntimePairwise") { dependsOn(scenarioRuntimeSourceTrace,scenarioRuntimeTrace);commandLine("python3",rootProject.file("tools/verify_scenario_runtime_pairwise.py").absolutePath,scenarioRuntimeSource.get().asFile.absolutePath,scenarioRuntimeGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifyScenarioRuntimePairwise) }
val coreBoundaryFixture=rootProject.file("tools/core_boundary_trace_cases.json");val coreBoundarySource=layout.buildDirectory.file("core-boundary/source.json")
val coreBoundarySourceTrace=tasks.register<Exec>("coreBoundarySourceTrace"){inputs.files(coreBoundaryFixture,rootProject.file("tools/core_boundary_source_trace_harness.js"));outputs.file(coreBoundarySource);commandLine("node",rootProject.file("tools/core_boundary_source_trace_harness.js").absolutePath,coreBoundaryFixture.absolutePath,coreBoundarySource.get().asFile.absolutePath)}
val auditCoreBoundarySourceInventory=tasks.register<Exec>("auditCoreBoundarySourceInventory"){group="source inventory";description="Audits recovered EngineCfg and Instance registries without claiming Kotlin runtime parity.";dependsOn(coreBoundarySourceTrace);inputs.file(rootProject.file("tools/audit_core_boundary_source_inventory.py"));commandLine("python3",rootProject.file("tools/audit_core_boundary_source_inventory.py").absolutePath,coreBoundarySource.get().asFile.absolutePath)}
// Isolated source/game oracle only. Runtime coverage is enforced separately.

/** Complete enumerable core/Config.js inventory, source factory versus direct Kotlin game. */
val configFullFixture=rootProject.file("tools/config_full_trace_cases.json")
val configFullSource=layout.buildDirectory.file("config-full/source.json")
val configFullGame=layout.buildDirectory.file("config-full/game.json")
val configFullSourceTrace=tasks.register<Exec>("configFullSourceTrace") {
    inputs.files(configFullFixture,rootProject.file("tools/config_full_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Config.js"))
    outputs.file(configFullSource)
    commandLine("node",rootProject.file("tools/config_full_source_trace_harness.js").absolutePath,configFullFixture.absolutePath,configFullSource.get().asFile.absolutePath)
}
val configFullTrace=tasks.register<JavaExec>("configFullTrace") {
    dependsOn(tasks.classes); classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.ConfigFullTraceHarness")
    args(configFullFixture.absolutePath,configFullGame.get().asFile.absolutePath); outputs.file(configFullGame)
}
val verifyConfigFullPairwise=tasks.register<Exec>("verifyConfigFullPairwise") {
    dependsOn(configFullSourceTrace,configFullTrace)
    commandLine("python3",rootProject.file("tools/verify_config_full_pairwise.py").absolutePath,configFullSource.get().asFile.absolutePath,configFullGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyConfigFullPairwise) }

/** Minimal scene-entry factory: onCreate and the two source-recognized events. */
val welcomeFixture=rootProject.file("tools/welcome_trace_cases.json")
val welcomeSource=layout.buildDirectory.file("welcome/source.json")
val welcomeGame=layout.buildDirectory.file("welcome/game.json")
val welcomeSourceTrace=tasks.register<Exec>("welcomeSourceTrace") {
    inputs.files(welcomeFixture,rootProject.file("tools/welcome_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/Welcome.js"))
    outputs.file(welcomeSource)
    commandLine("node",rootProject.file("tools/welcome_source_trace_harness.js").absolutePath,welcomeFixture.absolutePath,welcomeSource.get().asFile.absolutePath)
}
val welcomeTrace=tasks.register<JavaExec>("welcomeTrace") {
    dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.WelcomeTraceHarness")
    args(welcomeFixture.absolutePath,welcomeGame.get().asFile.absolutePath);outputs.file(welcomeGame)
}
val verifyWelcomePairwise=tasks.register<Exec>("verifyWelcomePairwise") {
    dependsOn(welcomeSourceTrace,welcomeTrace)
    commandLine("python3",rootProject.file("tools/verify_welcome_pairwise.py").absolutePath,welcomeSource.get().asFile.absolutePath,welcomeGame.get().asFile.absolutePath)
}
tasks.test { dependsOn(verifyWelcomePairwise) }

val sendGiftsFixture=rootProject.file("tools/send_gifts_trace_cases.json")
val sendGiftsSource=layout.buildDirectory.file("send-gifts/source.json")
val sendGiftsGame=layout.buildDirectory.file("send-gifts/game.json")
val sendGiftsSourceTrace=tasks.register<Exec>("sendGiftsSourceTrace") { inputs.files(sendGiftsFixture,rootProject.file("tools/send_gifts_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/SendGiftsLayer.js"));outputs.file(sendGiftsSource);commandLine("node",rootProject.file("tools/send_gifts_source_trace_harness.js").absolutePath,sendGiftsFixture.absolutePath,sendGiftsSource.get().asFile.absolutePath) }
val sendGiftsTrace=tasks.register<JavaExec>("sendGiftsTrace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.SendGiftsTraceHarness");args(sendGiftsFixture.absolutePath,sendGiftsGame.get().asFile.absolutePath);outputs.file(sendGiftsGame) }
val verifySendGiftsPairwise=tasks.register<Exec>("verifySendGiftsPairwise") { dependsOn(sendGiftsSourceTrace,sendGiftsTrace);commandLine("python3",rootProject.file("tools/verify_send_gifts_pairwise.py").absolutePath,sendGiftsSource.get().asFile.absolutePath,sendGiftsGame.get().asFile.absolutePath) }
tasks.test { dependsOn(verifySendGiftsPairwise) }
val progress2Fixture=rootProject.file("tools/progress2_trace_cases.json");val progress2Source=layout.buildDirectory.file("progress2/source.json");val progress2Game=layout.buildDirectory.file("progress2/game.json")
val progress2SourceTrace=tasks.register<Exec>("progress2SourceTrace"){inputs.files(progress2Fixture,rootProject.file("tools/progress2_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/misc/ProgressLayer2.js"));outputs.file(progress2Source);commandLine("node",rootProject.file("tools/progress2_source_trace_harness.js").absolutePath,progress2Fixture.absolutePath,progress2Source.get().asFile.absolutePath)}
val progress2Trace=tasks.register<JavaExec>("progress2Trace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.Progress2TraceHarness");args(progress2Fixture.absolutePath,progress2Game.get().asFile.absolutePath);outputs.file(progress2Game)}
val verifyProgress2Pairwise=tasks.register<Exec>("verifyProgress2Pairwise"){dependsOn(progress2SourceTrace,progress2Trace);commandLine("python3",rootProject.file("tools/verify_progress2_pairwise.py").absolutePath,progress2Source.get().asFile.absolutePath,progress2Game.get().asFile.absolutePath)}
tasks.test { dependsOn(verifyProgress2Pairwise) }

/** Model.js isolated source/game contracts: state, persistence and lifecycle. */
fun modelGate(group:String) {
    val fixture=rootProject.file("tools/model_${group}_trace_cases.json")
    val source=layout.buildDirectory.file("model-$group/source.json"); val game=layout.buildDirectory.file("model-$group/game.json")
    val sourceTask=tasks.register<Exec>("model${group.replaceFirstChar { it.uppercase() }}SourceTrace") { inputs.files(fixture,rootProject.file("tools/model_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/core/Model.js")); outputs.file(source); commandLine("node",rootProject.file("tools/model_source_trace_harness.js").absolutePath,fixture.absolutePath,source.get().asFile.absolutePath,group) }
    val gameTask=tasks.register<JavaExec>("model${group.replaceFirstChar { it.uppercase() }}Trace") { dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.ModelTraceHarness");args(fixture.absolutePath,game.get().asFile.absolutePath,group);outputs.file(game) }
    val gate=tasks.register<Exec>("verifyModel${group.replaceFirstChar { it.uppercase() }}Pairwise") { dependsOn(sourceTask,gameTask);commandLine("python3",rootProject.file("tools/verify_model_pairwise.py").absolutePath,source.get().asFile.absolutePath,game.get().asFile.absolutePath,group) }
    tasks.test { dependsOn(gate) }
}
modelGate("state");modelGate("persistence");modelGate("lifecycle")
val headFixture=rootProject.file("tools/head_trace_cases.json");val headSource=layout.buildDirectory.file("head/source.json");val headGame=layout.buildDirectory.file("head/game.json")
val headSourceTrace=tasks.register<Exec>("headSourceTrace"){inputs.files(headFixture,rootProject.file("tools/head_source_trace_harness.js"),rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules/ui/Head.js"));outputs.file(headSource);commandLine("node",rootProject.file("tools/head_source_trace_harness.js").absolutePath,headFixture.absolutePath,headSource.get().asFile.absolutePath)}
val headTrace=tasks.register<JavaExec>("headTrace"){dependsOn(tasks.classes);classpath=sourceSets.main.get().runtimeClasspath;mainClass.set("com.jojo.game.HeadTraceHarness");args(headFixture.absolutePath,headGame.get().asFile.absolutePath);outputs.file(headGame)}
val verifyHeadPairwise=tasks.register<Exec>("verifyHeadPairwise"){group="isolated oracle";description="Compares Head lifecycle events through ScenarioStage; not a full screen-entry test.";dependsOn(headSourceTrace,headTrace);commandLine("python3",rootProject.file("tools/verify_head_pairwise.py").absolutePath,headSource.get().asFile.absolutePath,headGame.get().asFile.absolutePath)}
// Isolated source/game oracle only. Runtime coverage is enforced separately.

val verifyRuntimeTestIntegrity = tasks.register<Exec>("verifyRuntimeTestIntegrity") {
    group = "verification"
    description = "Rejects fixture-only gates as runtime evidence and detects harness-only production types."
    inputs.files(
        rootProject.file("tools/verify_runtime_test_integrity.py"),
        rootProject.file("tools/runtime_test_integrity_baseline.json"),
        rootProject.file("build.gradle.kts"),
        rootProject.file("desktop/build.gradle.kts"),
        rootProject.file("tools/verify_yingchuan_battle_regression.mjs"),
        fileTree("src/main/kotlin") { include("**/*.kt") },
        project.file("build.gradle.kts"),
    )
    commandLine("python3", rootProject.file("tools/verify_runtime_test_integrity.py").absolutePath)
}
tasks.test { dependsOn(verifyRuntimeTestIntegrity) }

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

private val relocatedTraceHarnesses = setOf(
    "AutoBattleTraceHarness",
    "BattleBootstrapTraceHarness",
    "BattleScreenTraceHarness",
    "BattleViewTraceHarness",
    "CharacterAbilityTraceHarness",
    "ChoiceCommandTraceHarness",
    "CmdLayerTraceHarness",
    "ConfigFullTraceHarness",
    "EditMutationTraceHarness",
    "EndFlowTraceHarness",
    "EnemyTurnTraceHarness",
    "FightPresentationTraceHarness",
    "ForcesListLayerTraceHarness",
    "FoundationTraceHarness",
    "FrameworkServiceTraceHarness",
    "GameDataTraceHarness",
    "HallPrepTraceHarness",
    "HallUiTraceHarness",
    "HeadTraceHarness",
    "HelperLayerTraceHarness",
    "ItemEquipTraceHarness",
    "LoadGameTraceHarness",
    "LoadLayerTraceHarness",
    "MagicTraceHarness",
    "MapInfoLayerTraceHarness",
    "MenuLayerTraceHarness",
    "MiniMapLayerTraceHarness",
    "MiscUiTraceHarness",
    "ModelRandomTraceHarness",
    "ModelTraceHarness",
    "PlatformTraceHarness",
    "Progress2TraceHarness",
    "ProgressionLayerTraceHarness",
    "PropertyLayerTraceHarness",
    "RoundLayerTraceHarness",
    "SaveLayerTraceHarness",
    "ScenarioRuntimeTraceHarness",
    "SectionLayerTraceHarness",
    "SendGiftsTraceHarness",
    "SettingLayerTraceHarness",
    "ShopRewardTraceHarness",
    "SystemUiTraceHarness",
    "TerrainLayerTraceHarness",
    "TreasureLayerTraceHarness",
    "UnitInfoTraceHarness",
    "UnitListInfoLayerTraceHarness",
    "UpgradeSkillTraceHarness",
    "WelcomeTraceHarness",
)

tasks.withType<JavaExec>().configureEach {
    val harnessName = mainClass.orNull?.substringAfterLast('.')
    if (harnessName in relocatedTraceHarnesses) {
        dependsOn(":verification:classes")
        classpath = verificationMainRuntimeClasspath
        mainClass.set("com.jojo.game.verification.$harnessName")
    }
}
