plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-headless:${property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-platform:${property("gdxVersion")}:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:${property("gdxVersion")}:natives-desktop")
}

tasks.register("printClasspath") {
    doLast {
        println(sourceSets["main"].runtimeClasspath.asPath)
    }
}


val winConditionsFixture = rootProject.file("tools/fixtures/win_conditions_layer_cases.json")
val winConditionsTraceDir = layout.buildDirectory.dir("verification/win-conditions")
val dumpWinConditionsGameTrace = tasks.register<JavaExec>("dumpWinConditionsGameTrace") {
    group = "verification"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.LayerTraceDump")
    args(winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}
tasks.register<Exec>("verifyWinConditionsPairwise") {
    group = "verification"
    dependsOn(dumpWinConditionsGameTrace)
    inputs.file(winConditionsFixture)
    inputs.file(rootProject.file("tools/win_conditions_source_trace_harness.js"))
    inputs.file(rootProject.file("tools/verify_win_conditions_pairwise.mjs"))
    commandLine("node", rootProject.file("tools/verify_win_conditions_pairwise.mjs").absolutePath, winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("source.json").asFile.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}

/**
 * State-addressed scenario branches exercise the real LWJGL application,
 * rather than a headless approximation. Each task must reach every supplied
 * stage.choice and then complete the source scene.
 */
fun scenarioBranchFixture(name: String, vararg arguments: String) = tasks.register<JavaExec>(name) {
    group = "verification"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    val trace = layout.buildDirectory.file("reports/scenario-choice-traces/$name.json")
    args(arguments.toList() + "--choice-trace=${trace.get().asFile.absolutePath}")
    outputs.file(trace)
}

/** Executes one real source `model.random()` call, then stops before any enclosing source loop can repeat. */
fun scenarioRandomFixture(name: String, expectedLine: Int, expectedValue: Int, vararg arguments: String, stopAfterRandomTraceCount: Int = 1) = tasks.register<JavaExec>(name) {
    group = "verification"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    val trace = layout.buildDirectory.file("reports/scenario-random-traces/$name.json")
    args(arguments.toList() + "--verify-stop-after-random-count=$stopAfterRandomTraceCount" + "--random-trace=${trace.get().asFile.absolutePath}")
    outputs.file(trace)
    doFirst {
        // A failed/short-circuited fixture must never be allowed to reuse a
        // trace produced by an older build.
        trace.get().asFile.delete()
    }
    doLast {
        val expected = "\"line\":$expectedLine,\"value\":$expectedValue"
        check(trace.get().asFile.readText().contains(expected)) { "$name did not trace R_00:$expectedLine with random value $expectedValue" }
    }
}

val scenarioBranchFixtures = listOf(
    scenarioBranchFixture("verifyR00ChoicePathOne", "--scenario=R_00", "--verify-choice-script=0,0,3"),
    scenarioBranchFixture("verifyR00ChoicePathTwo", "--scenario=R_00", "--verify-choice-script=1,0,3"),
    scenarioBranchFixture("verifyR00ClassicRecalculate", "--scenario=R_00", "--verify-choice-script=0,0,2"),
    scenarioBranchFixture("verifyR00ClassicTrainingMode", "--scenario=R_00", "--verify-choice-script=0,0,0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00ClassicDifficulty", "--scenario=R_00", "--verify-choice-script=0,0,1"),
    scenarioBranchFixture("verifyR00ExpandedMode", "--scenario=R_00", "--verify-choice-script=0,1"),
    scenarioBranchFixture("verifyR00SelectMode", "--scenario=R_00", "--verify-choice-script=0,2"),
    scenarioBranchFixture("verifyR00ModeDescription", "--scenario=R_00", "--verify-choice-script=0,3"),
    scenarioBranchFixture("verifyR25ChoiceOne", "--scenario=R_25", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR25ChoiceTwo", "--scenario=R_25", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR31ChoiceOne", "--scenario=R_31", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR31ChoiceTwo", "--scenario=R_31", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR03SceneTwoChoiceOne", "--scenario=R_03", "--verify-scene=scene2", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR03SceneTwoChoiceTwo", "--scenario=R_03", "--verify-scene=scene2", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR01SceneTwoChoices00", "--scenario=R_01", "--verify-scene=scene2", "--verify-choice-script=0,0"),
    scenarioBranchFixture("verifyR01SceneTwoChoices01", "--scenario=R_01", "--verify-scene=scene2", "--verify-choice-script=0,1"),
    scenarioBranchFixture("verifyR01SceneTwoChoices10", "--scenario=R_01", "--verify-scene=scene2", "--verify-choice-script=1,0"),
    scenarioBranchFixture("verifyR01SceneTwoChoices11", "--scenario=R_01", "--verify-scene=scene2", "--verify-choice-script=1,1"),
    scenarioBranchFixture("verifyR04SceneEightChoiceOne", "--scenario=R_04", "--verify-scene=scene8", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR04SceneEightChoiceTwo", "--scenario=R_04", "--verify-scene=scene8", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR10SceneEightChoiceOne", "--scenario=R_10", "--verify-scene=scene8", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR10SceneEightChoiceTwo", "--scenario=R_10", "--verify-scene=scene8", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR15SceneFiveChoiceOne", "--scenario=R_15", "--verify-scene=scene5", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR15SceneFiveChoiceTwo", "--scenario=R_15", "--verify-scene=scene5", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR16SceneZeroChoiceOne", "--scenario=R_16", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR16SceneZeroChoiceTwo", "--scenario=R_16", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR18SceneThreeChoiceOne", "--scenario=R_18", "--verify-scene=scene3", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR18SceneThreeChoiceTwo", "--scenario=R_18", "--verify-scene=scene3", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR22SceneThreeChoiceOne", "--scenario=R_22", "--verify-scene=scene3", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR22SceneThreeChoiceTwo", "--scenario=R_22", "--verify-scene=scene3", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR23SceneZeroChoiceOne", "--scenario=R_23", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR23SceneZeroChoiceTwo", "--scenario=R_23", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR27SceneFourChoiceOne", "--scenario=R_27", "--verify-scene=scene4", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR27SceneFourChoiceTwo", "--scenario=R_27", "--verify-scene=scene4", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR35SceneTwoChoiceOne", "--scenario=R_35", "--verify-scene=scene2", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR35SceneTwoChoiceTwo", "--scenario=R_35", "--verify-scene=scene2", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS07SceneZeroChoiceOne", "--scenario=S_07", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS07SceneZeroChoiceTwo", "--scenario=S_07", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS11SceneZeroChoiceOne", "--scenario=S_11", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS11SceneZeroChoiceTwo", "--scenario=S_11", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS13SceneZeroChoiceOne", "--scenario=S_13", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS13SceneZeroChoiceTwo", "--scenario=S_13", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS16SceneZeroChoiceOne", "--scenario=S_16", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS16SceneZeroChoiceTwo", "--scenario=S_16", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS17SceneZeroChoiceOne", "--scenario=S_17", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS17SceneZeroChoiceTwo", "--scenario=S_17", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS18SceneZeroChoiceOne", "--scenario=S_18", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS18SceneZeroChoiceTwo", "--scenario=S_18", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS18SceneZeroChoiceThree", "--scenario=S_18", "--verify-scene=scene0", "--verify-choice-script=2"),
    scenarioBranchFixture("verifyS20SceneZeroChoiceOne", "--scenario=S_20", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS20SceneZeroChoiceTwo", "--scenario=S_20", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS20SceneZeroChoiceThree", "--scenario=S_20", "--verify-scene=scene0", "--verify-choice-script=2"),
    scenarioBranchFixture("verifyS22SceneZeroChoiceOne", "--scenario=S_22", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS22SceneZeroChoiceTwo", "--scenario=S_22", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS24SceneZeroChoiceOne", "--scenario=S_24", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS24SceneZeroChoiceTwo", "--scenario=S_24", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS25SceneZeroChoiceOne", "--scenario=S_25", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS25SceneZeroChoiceTwo", "--scenario=S_25", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS28SceneZeroChoiceOne", "--scenario=S_28", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS28SceneZeroChoiceTwo", "--scenario=S_28", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS28SceneZeroChoiceThree", "--scenario=S_28", "--verify-scene=scene0", "--verify-choice-script=2"),
    scenarioBranchFixture("verifyS30SceneZeroChoiceOne", "--scenario=S_30", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS30SceneZeroChoiceTwo", "--scenario=S_30", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS31SceneZeroChoiceOne", "--scenario=S_31", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS31SceneZeroChoiceTwo", "--scenario=S_31", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS33SceneZeroChoiceOne", "--scenario=S_33", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS33SceneZeroChoiceTwo", "--scenario=S_33", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS39SceneZeroChoiceOne", "--scenario=S_39", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS39SceneZeroChoiceTwo", "--scenario=S_39", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS40SceneZeroChoiceOne", "--scenario=S_40", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS40SceneZeroChoiceTwo", "--scenario=S_40", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS41SceneZeroChoiceOne", "--scenario=S_41", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS41SceneZeroChoiceTwo", "--scenario=S_41", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS42Attack", "--scenario=S_42", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS42NegotiateTwoThousand", "--scenario=S_42", "--verify-scene=scene0", "--verify-choice-script=1,0"),
    scenarioBranchFixture("verifyS42NegotiateFourThousandAttack", "--scenario=S_42", "--verify-scene=scene0", "--verify-choice-script=1,1,0"),
    scenarioBranchFixture("verifyS42NegotiateFourThousand", "--scenario=S_42", "--verify-scene=scene0", "--verify-choice-script=1,1,1"),
    scenarioBranchFixture("verifyS43SceneZeroChoiceOne", "--scenario=S_43", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS43SceneZeroChoiceTwo", "--scenario=S_43", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS44SceneZeroChoiceOne", "--scenario=S_44", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS44SceneZeroChoiceTwo", "--scenario=S_44", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS47SceneZeroChoiceOne", "--scenario=S_47", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS47SceneZeroChoiceTwo", "--scenario=S_47", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS49SceneZeroChoiceOne", "--scenario=S_49", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS49SceneZeroChoiceTwo", "--scenario=S_49", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS50SceneZeroChoiceOne", "--scenario=S_50", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS50SceneZeroChoiceTwo", "--scenario=S_50", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS10RectangleChoiceOne", "--scenario=S_10", "--verify-choice-script=0", "--verify-positions=0:0:7"),
    scenarioBranchFixture("verifyS10RectangleChoiceTwo", "--scenario=S_10", "--verify-choice-script=1", "--verify-positions=0:0:7"),
    scenarioBranchFixture("verifyS35Position2316ChoiceOne", "--scenario=S_35", "--verify-choice-script=0", "--verify-positions=1026:23:16"),
    scenarioBranchFixture("verifyS35Position2316ChoiceTwo", "--scenario=S_35", "--verify-choice-script=1", "--verify-positions=1026:23:16"),
    scenarioBranchFixture("verifyS35Position1901ChoiceOne", "--scenario=S_35", "--verify-choice-script=0", "--verify-positions=1026:19:1"),
    scenarioBranchFixture("verifyS35Position1901ChoiceTwo", "--scenario=S_35", "--verify-choice-script=1", "--verify-positions=1026:19:1"),
    scenarioBranchFixture("verifyS15CompositeVarsChoiceOne", "--scenario=S_15", "--verify-choice-script=0", "--verify-vars=51:1,52:1,53:1,54:1", "--verify-round=1"),
    scenarioBranchFixture("verifyS15CompositeVarsChoiceTwo", "--scenario=S_15", "--verify-choice-script=1", "--verify-vars=51:1,52:1,53:1,54:1", "--verify-round=1"),
    scenarioBranchFixture("verifyS03CompositeStateChoiceOne", "--scenario=S_03", "--verify-choice-script=0", "--verify-vars=6:1", "--verify-round=11", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0"),
    scenarioBranchFixture("verifyS03CompositeStateChoiceTwo", "--scenario=S_03", "--verify-choice-script=1", "--verify-vars=6:1", "--verify-round=11", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0"),
    scenarioBranchFixture("verifyS21RoundTenChoiceOne", "--scenario=S_21", "--verify-choice-script=0", "--verify-vars=30:1", "--verify-round=10"),
    scenarioBranchFixture("verifyS21RoundTenChoiceTwo", "--scenario=S_21", "--verify-choice-script=1", "--verify-vars=30:1", "--verify-round=10"),
    scenarioBranchFixture("verifyS21RoundFifteenChoiceOne", "--scenario=S_21", "--verify-choice-script=0", "--verify-vars=30:1,11:1", "--verify-round=15"),
    scenarioBranchFixture("verifyS21RoundFifteenChoiceTwo", "--scenario=S_21", "--verify-choice-script=1", "--verify-vars=30:1,11:1", "--verify-round=15"),
    scenarioBranchFixture("verifyS21RoundTwentyChoiceOne", "--scenario=S_21", "--verify-choice-script=0", "--verify-vars=30:1,11:1,12:1", "--verify-round=20"),
    scenarioBranchFixture("verifyS21RoundTwentyChoiceTwo", "--scenario=S_21", "--verify-choice-script=1", "--verify-vars=30:1,11:1,12:1", "--verify-round=20"),
    scenarioBranchFixture("verifyS06EnemyCountChoiceOne", "--scenario=S_06", "--verify-choice-script=0", "--verify-round=7", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0"),
    scenarioBranchFixture("verifyS06EnemyCountChoiceTwo", "--scenario=S_06", "--verify-choice-script=1", "--verify-round=7", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0"),
    scenarioBranchFixture("verifyS23EnemyRectanglesChoiceOne", "--scenario=S_23", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS23EnemyRectanglesChoiceTwo", "--scenario=S_23", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS27MultiCampChoiceOne", "--scenario=S_27", "--verify-choice-script=0", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0,2:9:0"),
    scenarioBranchFixture("verifyS27MultiCampChoiceTwo", "--scenario=S_27", "--verify-choice-script=1", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0,2:9:0"),
    scenarioBranchFixture("verifyS28MultiCampChoiceOne", "--scenario=S_28", "--verify-choice-script=0", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0,2:9:0"),
    scenarioBranchFixture("verifyS28MultiCampChoiceTwo", "--scenario=S_28", "--verify-choice-script=1", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0,2:8:0,2:9:0"),
    scenarioBranchFixture("verifyS34ReinforcementChoices00", "--scenario=S_34", "--verify-choice-script=0,0", "--verify-vars=8:1", "--verify-camp=2", "--verify-camp-positions=0:0:28,0:1:28,0:2:28,0:3:28,0:4:28"),
    scenarioBranchFixture("verifyS34ReinforcementChoices01", "--scenario=S_34", "--verify-choice-script=0,1", "--verify-vars=8:1", "--verify-camp=2", "--verify-camp-positions=0:0:28,0:1:28,0:2:28,0:3:28,0:4:28"),
    scenarioBranchFixture("verifyS34ReinforcementChoices10", "--scenario=S_34", "--verify-choice-script=1,0", "--verify-vars=8:1", "--verify-camp=2", "--verify-camp-positions=0:0:28,0:1:28,0:2:28,0:3:28,0:4:28"),
    scenarioBranchFixture("verifyS34ReinforcementChoices11", "--scenario=S_34", "--verify-choice-script=1,1", "--verify-vars=8:1", "--verify-camp=2", "--verify-camp-positions=0:0:28,0:1:28,0:2:28,0:3:28,0:4:28"),
    scenarioBranchFixture("verifyS01CompositeVarsChoiceOne", "--scenario=S_01", "--verify-choice-script=0", "--verify-vars=51:1,52:1,53:1"),
    scenarioBranchFixture("verifyS01CompositeVarsChoiceTwo", "--scenario=S_01", "--verify-choice-script=1", "--verify-vars=51:1,52:1,53:1"),
    scenarioBranchFixture("verifyS34SecondReinforcementChoiceOne", "--scenario=S_34", "--verify-choice-script=0", "--verify-vars=9:1", "--verify-camp=2"),
    scenarioBranchFixture("verifyS34SecondReinforcementChoiceTwo", "--scenario=S_34", "--verify-choice-script=1", "--verify-vars=9:1", "--verify-camp=2"),
    scenarioBranchFixture("verifyS04ChoiceOne", "--scenario=S_04", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS04ChoiceTwo", "--scenario=S_04", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR12ChoiceOne", "--scenario=R_12", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR12ChoiceTwo", "--scenario=R_12", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS02RectangleChoiceOne", "--scenario=S_02", "--verify-choice-script=0", "--verify-round=4", "--verify-camp=0", "--verify-camp-positions=1:19:8,1:19:9,1:19:10"),
    scenarioBranchFixture("verifyS02RectangleChoiceTwo", "--scenario=S_02", "--verify-choice-script=1", "--verify-round=4", "--verify-camp=0", "--verify-camp-positions=1:19:8,1:19:9,1:19:10"),
    scenarioBranchFixture("verifyS05RoundEightChoiceOne", "--scenario=S_05", "--verify-choice-script=0", "--verify-round=8"),
    scenarioBranchFixture("verifyS05RoundEightChoiceTwo", "--scenario=S_05", "--verify-choice-script=1", "--verify-round=8"),
    scenarioBranchFixture("verifyS08WinChoiceOne", "--scenario=S_08", "--verify-choice-script=0", "--verify-vars=6:1", "--verify-attributes=15:7:1,16:7:1", "--verify-win"),
    scenarioBranchFixture("verifyS08WinChoiceTwo", "--scenario=S_08", "--verify-choice-script=1", "--verify-vars=6:1", "--verify-attributes=15:7:1,16:7:1", "--verify-win"),
    scenarioBranchFixture("verifyS12VarsChoiceOne", "--scenario=S_12", "--verify-choice-script=0", "--verify-vars=30:1", "--verify-round=1", "--verify-camp=0"),
    scenarioBranchFixture("verifyS12VarsChoiceTwo", "--scenario=S_12", "--verify-choice-script=1", "--verify-vars=30:1", "--verify-round=1", "--verify-camp=0"),
    scenarioBranchFixture("verifyS19AttributeChoiceOne", "--scenario=S_19", "--verify-choice-script=0", "--verify-attributes=15:7:50"),
    scenarioBranchFixture("verifyS19AttributeChoiceTwo", "--scenario=S_19", "--verify-choice-script=1", "--verify-attributes=15:7:50"),
    scenarioBranchFixture("verifyS19XuChuChoiceOne", "--scenario=S_19", "--verify-choice-script=0", "--verify-vars=6:1", "--verify-attributes=15:7:100", "--verify-positions=15:0:0,115:1:0"),
    scenarioBranchFixture("verifyS19XuChuChoiceTwo", "--scenario=S_19", "--verify-choice-script=1", "--verify-vars=6:1", "--verify-attributes=15:7:100", "--verify-positions=15:0:0,115:1:0"),
    scenarioBranchFixture("verifyS09PositionChoiceOne", "--scenario=S_09", "--verify-choice-script=0", "--verify-positions=0:0:0,149:0:1"),
    scenarioBranchFixture("verifyS09PositionChoiceTwo", "--scenario=S_09", "--verify-choice-script=1", "--verify-positions=0:0:0,149:0:1"),
    scenarioBranchFixture("verifyS36CampCountChoiceOne", "--scenario=S_36", "--verify-choice-script=0", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0"),
    scenarioBranchFixture("verifyS36CampCountChoiceTwo", "--scenario=S_36", "--verify-choice-script=1", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,2:7:0"),
    scenarioBranchFixture("verifyR26SceneZeroChoiceOne", "--scenario=R_26", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR26SceneZeroChoiceTwo", "--scenario=R_26", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR31SceneThreeChoiceOne", "--scenario=R_31", "--verify-scene=scene3", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR31SceneThreeChoiceTwo", "--scenario=R_31", "--verify-scene=scene3", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR13SceneZeroChoiceOne", "--scenario=R_13", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR13SceneZeroChoiceTwo", "--scenario=R_13", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR21SceneZeroChoiceOne", "--scenario=R_21", "--verify-scene=scene0", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR21SceneZeroChoiceTwo", "--scenario=R_21", "--verify-scene=scene0", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyR25SceneFiveChoiceOne", "--scenario=R_25", "--verify-scene=scene5", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyR25SceneFiveChoiceTwo", "--scenario=R_25", "--verify-scene=scene5", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS55RoundFiveBreakthrough", "--scenario=S_55", "--verify-choice-script=0", "--verify-vars=754:1", "--verify-round=5", "--verify-camp=2"),
    scenarioBranchFixture("verifyS55RoundFiveEliminate", "--scenario=S_55", "--verify-choice-script=1", "--verify-vars=754:1", "--verify-round=5", "--verify-camp=2"),
    scenarioBranchFixture("verifyS55RoundSevenBreakthrough", "--scenario=S_55", "--verify-choice-script=0", "--verify-vars=754:1", "--verify-round=7", "--verify-camp=2"),
    scenarioBranchFixture("verifyS55RoundSevenEliminate", "--scenario=S_55", "--verify-choice-script=1", "--verify-vars=754:1", "--verify-round=7", "--verify-camp=2"),
    scenarioBranchFixture("verifyS54FirstAmbushBreakthrough", "--scenario=S_54", "--verify-choice-script=0", "--verify-camp=2", "--verify-camp-positions=0:11:14,0:12:14,0:13:14,0:14:14,0:15:14,0:16:14,0:17:14,0:18:14,0:19:14,0:20:14,0:21:14,0:22:14"),
    scenarioBranchFixture("verifyS54FirstAmbushEliminate", "--scenario=S_54", "--verify-choice-script=1", "--verify-camp=2", "--verify-camp-positions=0:11:14,0:12:14,0:13:14,0:14:14,0:15:14,0:16:14,0:17:14,0:18:14,0:19:14,0:20:14,0:21:14,0:22:14"),
    scenarioBranchFixture("verifyS54NorthGateBreakthrough", "--scenario=S_54", "--verify-choice-script=0", "--verify-camp=2", "--verify-camp-positions=0:0:14"),
    scenarioBranchFixture("verifyS54NorthGateEliminate", "--scenario=S_54", "--verify-choice-script=1", "--verify-camp=2", "--verify-camp-positions=0:0:14"),
    scenarioBranchFixture("verifyS54WestGateBreakthrough", "--scenario=S_54", "--verify-choice-script=0", "--verify-camp=2", "--verify-camp-positions=0:12:0"),
    scenarioBranchFixture("verifyS54WestGateEliminate", "--scenario=S_54", "--verify-choice-script=1", "--verify-camp=2", "--verify-camp-positions=0:12:0"),
    scenarioBranchFixture("verifyS54CommanderDefeatedBreakthrough", "--scenario=S_54", "--verify-choice-script=0", "--verify-attributes=50:7:0"),
    scenarioBranchFixture("verifyS54CommanderDefeatedEliminate", "--scenario=S_54", "--verify-choice-script=1", "--verify-attributes=50:7:0"),
    scenarioBranchFixture("verifyS53NearSeventyChoiceOne", "--scenario=S_53", "--verify-choice-script=0", "--verify-positions=0:0:0,70:1:0"),
    scenarioBranchFixture("verifyS53NearSeventyChoiceTwo", "--scenario=S_53", "--verify-choice-script=1", "--verify-positions=0:0:0,70:1:0"),
    scenarioBranchFixture("verifyS53SeventyDefeatedChoiceOne", "--scenario=S_53", "--verify-choice-script=0", "--verify-attributes=35:7:1,69:7:1,50:7:1,58:7:1,37:7:1,68:7:1,70:7:0"),
    scenarioBranchFixture("verifyS53SeventyDefeatedChoiceTwo", "--scenario=S_53", "--verify-choice-script=1", "--verify-attributes=35:7:1,69:7:1,50:7:1,58:7:1,37:7:1,68:7:1,70:7:0"),
    scenarioBranchFixture("verifyS26ZhaoYunChoiceOne", "--scenario=S_26", "--verify-choice-script=0", "--verify-vars=50:1,51:1,52:1,53:1", "--verify-attributes=34:7:0"),
    scenarioBranchFixture("verifyS26ZhaoYunChoiceTwo", "--scenario=S_26", "--verify-choice-script=1", "--verify-vars=50:1,51:1,52:1,53:1", "--verify-attributes=34:7:0"),
    scenarioBranchFixture("verifyS38FanchengDefend", "--scenario=S_38", "--verify-choice-script=0", "--verify-camp=2", "--verify-camp-positions=0:8:10,0:9:10,0:10:10"),
    scenarioBranchFixture("verifyS38FanchengAbandon", "--scenario=S_38", "--verify-choice-script=1", "--verify-camp=2", "--verify-camp-positions=0:8:10,0:9:10,0:10:10"),
    scenarioBranchFixture("verifyS37Breakout", "--scenario=S_37", "--verify-choice-script=0", "--verify-vars=3:1", "--verify-round=4", "--verify-camp=2"),
    scenarioBranchFixture("verifyS37Resist", "--scenario=S_37", "--verify-choice-script=1", "--verify-vars=3:1", "--verify-round=4", "--verify-camp=2"),
    scenarioBranchFixture("verifyS48AmbushReinforcement", "--scenario=S_48", "--verify-choice-script=0", "--verify-vars=2:1", "--verify-round=1", "--verify-camp=0"),
    scenarioBranchFixture("verifyS48FocusSunQuan", "--scenario=S_48", "--verify-choice-script=1", "--verify-vars=2:1", "--verify-round=1", "--verify-camp=0"),
    scenarioBranchFixture("verifyS46DingjunRetreat", "--scenario=S_46", "--verify-choice-script=0", "--verify-positions=1026:23:16"),
    scenarioBranchFixture("verifyS46DingjunFight", "--scenario=S_46", "--verify-choice-script=1", "--verify-positions=1026:23:16"),
    scenarioBranchFixture("verifyS46HanzhongRetreat", "--scenario=S_46", "--verify-choice-script=0", "--verify-positions=1026:19:1"),
    scenarioBranchFixture("verifyS46HanzhongFight", "--scenario=S_46", "--verify-choice-script=1", "--verify-positions=1026:19:1"),
    scenarioBranchFixture("verifyS29PleadGuanYu", "--scenario=S_29", "--verify-choice-script=0", "--verify-camp-positions=0:0:0"),
    scenarioBranchFixture("verifyS29ChallengeGuanYu", "--scenario=S_29", "--verify-choice-script=1", "--verify-camp-positions=0:0:0"),
    scenarioBranchFixture("verifyS37RoundThreeBreakout", "--scenario=S_37", "--verify-choice-script=0", "--verify-round=3", "--verify-camp=2"),
    scenarioBranchFixture("verifyS37RoundThreeResist", "--scenario=S_37", "--verify-choice-script=1", "--verify-round=3", "--verify-camp=2"),
    scenarioBranchFixture("verifyS37CampFourBreakout", "--scenario=S_37", "--verify-choice-script=0", "--verify-vars=3:1", "--verify-camp-positions=4:0:17"),
    scenarioBranchFixture("verifyS37CampFourResist", "--scenario=S_37", "--verify-choice-script=1", "--verify-vars=3:1", "--verify-camp-positions=4:0:17"),
    scenarioBranchFixture("verifyS52RoundEightWithdraw", "--scenario=S_52", "--verify-choice-script=0", "--verify-vars=25:1", "--verify-round=8"),
    scenarioBranchFixture("verifyS52RoundEightAttack", "--scenario=S_52", "--verify-choice-script=1", "--verify-vars=25:1", "--verify-round=8"),
    scenarioBranchFixture("verifyS52RoundSixteenWithdraw", "--scenario=S_52", "--verify-choice-script=0", "--verify-vars=25:1", "--verify-round=16"),
    scenarioBranchFixture("verifyS52RoundSixteenAttack", "--scenario=S_52", "--verify-choice-script=1", "--verify-vars=25:1", "--verify-round=16"),
    scenarioBranchFixture("verifyS52RoundTwentyFourWithdraw", "--scenario=S_52", "--verify-choice-script=0", "--verify-vars=25:1", "--verify-round=24"),
    scenarioBranchFixture("verifyS52RoundTwentyFourAttack", "--scenario=S_52", "--verify-choice-script=1", "--verify-vars=25:1", "--verify-round=24"),
    scenarioBranchFixture("verifyS06RoundTenRetreat", "--scenario=S_06", "--verify-choice-script=0", "--verify-vars=13:1", "--verify-round=10"),
    scenarioBranchFixture("verifyS06RoundTenAttack", "--scenario=S_06", "--verify-choice-script=1", "--verify-vars=13:1", "--verify-round=10"),
    scenarioBranchFixture("verifyS06LowForceRetreat", "--scenario=S_06", "--verify-choice-script=0", "--verify-round=12", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,0:0:0,0:1:0,0:2:0,0:3:0,0:4:0,0:5:0,0:6:0"),
    scenarioBranchFixture("verifyS06LowForceAttack", "--scenario=S_06", "--verify-choice-script=1", "--verify-round=12", "--verify-camp-positions=2:0:0,2:1:0,2:2:0,2:3:0,2:4:0,2:5:0,2:6:0,0:0:0,0:1:0,0:2:0,0:3:0,0:4:0,0:5:0,0:6:0"),
    scenarioBranchFixture("verifyS03AmbushRetreat", "--scenario=S_03", "--verify-choice-script=0", "--verify-camp=2", "--verify-camp-positions=0:11:0,0:12:0,0:13:0,0:14:0"),
    scenarioBranchFixture("verifyS03AmbushPursue", "--scenario=S_03", "--verify-choice-script=1", "--verify-camp=2", "--verify-camp-positions=0:11:0,0:12:0,0:13:0,0:14:0"),
    scenarioBranchFixture("verifyS23RejectSurrender", "--scenario=S_23", "--verify-choice-script=0"),
    scenarioBranchFixture("verifyS23AcceptOnlyShenPing", "--scenario=S_23", "--verify-choice-script=1"),
    scenarioBranchFixture("verifyS23AcceptSurrender", "--scenario=S_23", "--verify-choice-script=2"),
    scenarioBranchFixture("verifyS04ThirdCounsel", "--scenario=S_04", "--verify-choice-script=2", "--verify-round=1"),
    scenarioBranchFixture("verifyR39AscendThrone", "--scenario=R_39", "--verify-scene=scene2", "--verify-choice-script=0", "--verify-ambition=85"),
    scenarioBranchFixture("verifyR39DeclineThrone", "--scenario=R_39", "--verify-scene=scene2", "--verify-choice-script=1", "--verify-ambition=85"),
    scenarioBranchFixture("verifyR00AltMenuTraining", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltMenuDifficulty", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=1", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltMenuHitType", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=2", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltMenuKillBonus", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=3", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltMenuHelp", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=4", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltMenuStart", "--scenario=R_00", "--verify-label=lab509", "--verify-choice-script=5", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltHelpMode", "--scenario=R_00", "--verify-label=lab665", "--verify-choice-script=0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltHelpHit", "--scenario=R_00", "--verify-label=lab665", "--verify-choice-script=1", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltHelpBonus", "--scenario=R_00", "--verify-label=lab665", "--verify-choice-script=2", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00AltHelpBack", "--scenario=R_00", "--verify-label=lab665", "--verify-choice-script=3", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageTraining", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageUnits", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=1", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageTreasure", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=2", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageCharacterSkill", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=3", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageUnitSkill", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=4", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00SetupPageNext", "--scenario=R_00", "--verify-label=lab744", "--verify-choice-script=5", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPageDifficulty", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPageHitType", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=1", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPageKillBonus", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=2", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPageHelp", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=3", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPagePrevious", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=4", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FinalPageConfirm", "--scenario=R_00", "--verify-label=lab911", "--verify-choice-script=5", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpMode", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=0", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpUnits", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=1", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpSkills", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=2", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpHit", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=3", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpBonus", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=4", "--verify-stop-at-choice"),
    scenarioBranchFixture("verifyR00FullHelpReturn", "--scenario=R_00", "--verify-label=lab1031", "--verify-choice-script=5", "--verify-stop-at-choice"),
)
tasks.register("verifyScenarioBranchFixtures") {
    group = "verification"
    description = "Runs the state-addressed real-app scenario choice fixtures."
    dependsOn(scenarioBranchFixtures)
}

val scenarioRandomFixtures = listOf(
    scenarioRandomFixture("verifyR00Random128Low", 958, 0, "--scenario=R_00", "--verify-label=lab1148", "--verify-vars=1042:1", "--verify-globals=0:0,4025:128", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random128High", 958, 100, "--scenario=R_00", "--verify-label=lab1148", "--verify-vars=1042:1", "--verify-globals=0:0,4025:128", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1232Low", 1232, 0, "--scenario=R_00", "--verify-label=lab1841", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1232High", 1232, 100, "--scenario=R_00", "--verify-label=lab1841", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1234Low", 1234, 0, "--scenario=R_00", "--verify-label=lab1841", "--verify-random=100,0", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Random1234High", 1234, 100, "--scenario=R_00", "--verify-label=lab1841", "--verify-random=100,100", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Random1748Low", 1748, 0, "--scenario=R_00", "--verify-label=lab2942", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1748High", 1748, 100, "--scenario=R_00", "--verify-label=lab2942", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random2004Low", 2004, 0, "--scenario=R_00", "--verify-label=lab3409", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random2004High", 2004, 100, "--scenario=R_00", "--verify-label=lab3409", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random2178Low", 2178, 0, "--scenario=R_00", "--verify-label=lab3703", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random2178High", 2178, 100, "--scenario=R_00", "--verify-label=lab3703", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random2434Low", 2434, 0, "--scenario=R_00", "--verify-label=lab4170", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random2434High", 2434, 100, "--scenario=R_00", "--verify-label=lab4170", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1089Low", 1089, 0, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=66", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1089High", 1089, 100, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=66", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1091Low", 1091, 0, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=67", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1091High", 1091, 100, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=67", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1093Low", 1093, 0, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=72", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1093High", 1093, 100, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=72", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1097Low", 1097, 0, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=78", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1097High", 1097, 100, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=78", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1099Low", 1099, 0, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=79", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1099High", 1099, 100, "--scenario=R_00", "--verify-label=lab1542", "--verify-globals=0:0", "--verify-info-random=79", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1275Low", 1275, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=56", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1275High", 1275, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=56", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1277Low", 1277, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=57", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1277High", 1277, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=57", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1279Low", 1279, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=83", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1279High", 1279, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=83", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1281Low", 1281, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=84", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1281High", 1281, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=84", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1283Low", 1283, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=88", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1283High", 1283, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=88", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1285Low", 1285, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=89", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1285High", 1285, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=89", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1287Low", 1287, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=138", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1287High", 1287, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=138", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1289Low", 1289, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=73", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1289High", 1289, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=73", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1291Low", 1291, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=128", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1291High", 1291, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-info-random=128", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1376Low", 1376, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-vars=1032:1", "--verify-unit-attrs=0:11:40", "--verify-info-random=29", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1376High", 1376, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-vars=1032:1", "--verify-unit-attrs=0:11:40", "--verify-info-random=29", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Random1378Low", 1378, 0, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-vars=1032:1", "--verify-unit-attrs=0:11:40", "--verify-info-random=41", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Random1378High", 1378, 100, "--scenario=R_00", "--verify-label=lab1900", "--verify-globals=0:0", "--verify-vars=1032:1", "--verify-unit-attrs=0:11:40", "--verify-info-random=41", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Scene2RandomLow", 2900, 0, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Scene2RandomHigh", 2900, 100, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Scene2Random2902Low", 2902, 0, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,0", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Scene2Random2902High", 2902, 100, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,100", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Scene2Random2904Low", 2904, 0, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,0,0", stopAfterRandomTraceCount = 3),
    scenarioRandomFixture("verifyR00Scene2Random2904High", 2904, 100, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,0,100", stopAfterRandomTraceCount = 3),
    scenarioRandomFixture("verifyR00Scene2Random2907Low", 2907, 0, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,0,100,0", stopAfterRandomTraceCount = 4),
    scenarioRandomFixture("verifyR00Scene2Random2907High", 2907, 100, "--scenario=R_00", "--verify-scene=scene2", "--verify-label=lab4773", "--verify-random=0,0,100,100", stopAfterRandomTraceCount = 4),
    scenarioRandomFixture("verifyR00Scene3RandomLow", 3318, 0, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0"),
    scenarioRandomFixture("verifyR00Scene3RandomHigh", 3318, 100, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=100"),
    scenarioRandomFixture("verifyR00Scene3Random3320Low", 3320, 0, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,0", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Scene3Random3320High", 3320, 100, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,100", stopAfterRandomTraceCount = 2),
    scenarioRandomFixture("verifyR00Scene3Random3322Low", 3322, 0, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,0,0", stopAfterRandomTraceCount = 3),
    scenarioRandomFixture("verifyR00Scene3Random3322High", 3322, 100, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,0,100", stopAfterRandomTraceCount = 3),
    scenarioRandomFixture("verifyR00Scene3Random3324Low", 3324, 0, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,0,100,0", stopAfterRandomTraceCount = 4),
    scenarioRandomFixture("verifyR00Scene3Random3324High", 3324, 100, "--scenario=R_00", "--verify-scene=scene3", "--verify-vars=1002:1,1011:1", "--verify-random=0,0,100,100", stopAfterRandomTraceCount = 4),
)
tasks.register("verifyScenarioRandomFixtures") {
    group = "verification"
    description = "Runs state-addressed real-app random-call fixtures."
    dependsOn(scenarioRandomFixtures)
}
val scenarioRandomCoverageReport = layout.buildDirectory.file("reports/scenario-random-coverage.json")
tasks.register<Exec>("verifyScenarioRandomCoverage") {
    group = "verification"
    description = "Maps each real-app random trace to a recovered source random-call site."
    dependsOn("verifyScenarioRandomFixtures", project(":core").tasks.named("auditScenarioBranchSurface"))
    inputs.dir(layout.buildDirectory.dir("reports/scenario-random-traces"))
    inputs.file(project(":core").layout.buildDirectory.file("reports/scenario-branch-surface.json"))
    outputs.file(scenarioRandomCoverageReport)
    commandLine(
        "python3",
        rootProject.file("tools/verify_scenario_random_coverage.py").absolutePath,
        project(":core").layout.buildDirectory.file("reports/scenario-branch-surface.json").get().asFile.absolutePath,
        layout.buildDirectory.dir("reports/scenario-random-traces").get().asFile.absolutePath,
        scenarioRandomCoverageReport.get().asFile.absolutePath,
    )
}

val scenarioChoiceCoverageReport = layout.buildDirectory.file("reports/scenario-choice-coverage.json")
tasks.register<Exec>("verifyScenarioChoiceCoverage") {
    group = "verification"
    description = "Maps each real-app fixture choice to its recovered source option site."
    dependsOn("verifyScenarioBranchFixtures", project(":core").tasks.named("auditScenarioBranchSurface"))
    inputs.dir(layout.buildDirectory.dir("reports/scenario-choice-traces"))
    inputs.file(project(":core").layout.buildDirectory.file("reports/scenario-branch-surface.json"))
    outputs.file(scenarioChoiceCoverageReport)
    commandLine(
        "python3",
        rootProject.file("tools/verify_scenario_choice_coverage.py").absolutePath,
        project(":core").layout.buildDirectory.file("reports/scenario-branch-surface.json").get().asFile.absolutePath,
        layout.buildDirectory.dir("reports/scenario-choice-traces").get().asFile.absolutePath,
        scenarioChoiceCoverageReport.get().asFile.absolutePath,
    )
}

/** Runs the recovered Electron battle through the same three dialogue inputs as the game. */
val verifyYingchuanActorState = tasks.register<Exec>("verifyYingchuanActorState") {
    group = "verification"
    description = "Compares original Cocos and LibGDX Yingchuan dialogue actors, text, and fixture geometry."
    inputs.file(rootProject.file("tools/verify_yingchuan_actor_state.mjs"))
    inputs.file(rootProject.file("tools/verify_yingchuan_dialogue_fixture.py"))
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/build/python-source-battle-verification-dialogue3.png"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-actor-state.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_actor_state.mjs").absolutePath)
}

/** Rebuilds the render evidence which is too timing-sensitive to accept from a prior check run. */
val verifyFreshBattleRenderParity = tasks.register<Exec>("verifyFreshBattleRenderParity") {
    group = "verification"
    description = "Freshly captures all Yingchuan dialogue PNGs and strict battle character/dialogue JSONL plus framebuffer pairs."
    dependsOn(tasks.classes)
    inputs.files(
        rootProject.file("tools/verify_fresh_battle_render_parity.mjs"),
        rootProject.file("tools/verify_yingchuan_actor_state.mjs"),
        rootProject.file("tools/verify_yingchuan_dialogue_fixture.py"),
        rootProject.file("tools/compare_battle_render_frames.py"),
        rootProject.file("tools/compare_render_logs.py"),
        rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"),
    )
    // A passing report from yesterday is not evidence about today's renderer.
    outputs.upToDateWhen { false }
    environment("JOJO_DESKTOP_CLASSPATH", sourceSets.main.get().runtimeClasspath.asPath)
    commandLine("node", rootProject.file("tools/verify_fresh_battle_render_parity.mjs").absolutePath)
}

val verifyRenderParityScope = tasks.register<Exec>("verifyRenderParityScope") {
    group = "verification"
    description = "Validates the exhaustive render scope and requires selected battle evidence from this fresh capture run."
    dependsOn(verifyFreshBattleRenderParity)
    inputs.files(
        rootProject.file("tools/render_parity_scope.json"),
        rootProject.file("tools/render_layer_inventory.json"),
        rootProject.file("tools/verify_render_parity_scope.py"),
        rootProject.file("tools/verify_render_parity_reports.py"),
    )
    outputs.upToDateWhen { false }
    commandLine(
        "python3", rootProject.file("tools/verify_render_parity_scope.py").absolutePath,
        "--scope", rootProject.file("tools/render_parity_scope.json").absolutePath,
        "--repository", rootProject.projectDir.absolutePath,
    )
}

/** Compares the real source Control._process range/cursor overlay to the game framebuffer state. */
val verifyYingchuanSelectionRender = tasks.register<Exec>("verifyYingchuanSelectionRender") {
    group = "verification"
    description = "Compares original Cocos and LibGDX Yingchuan selection ranges, attack boxes, and cursor."
    inputs.file(rootProject.file("tools/verify_yingchuan_selection_render.mjs"))
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-selection-render.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_selection_render.mjs").absolutePath)
}

/** Ensures every original-menu modal still renders as a real desktop framebuffer. */
val verifyYingchuanModalCaptures = tasks.register<Exec>("verifyYingchuanModalCaptures") {
    group = "verification"
    description = "Checks original modal/SayLayer stacks and LibGDX modal capture states."
    inputs.file(rootProject.file("tools/verify_yingchuan_modal_captures.mjs"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/build"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-modal-captures.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_modal_captures.mjs").absolutePath)
}

/**
 * Runs the production BattleScreen continuously and validates movement,
 * attack-hit-reaction timing, camera bounds and the terminal outcome. This is
 * intentionally separate from capture-state fixtures: its input is the live
 * frame trace emitted by the production S_00 BattleScreen after its explicit,
 * deterministic full-battle roster bootstrap.
 */
val yingchuanBattleRegressionTrace = layout.buildDirectory.file("reports/yingchuan-battle-regression-trace.json")
val captureYingchuanBattleRegressionTrace = tasks.register<JavaExec>("captureYingchuanBattleRegressionTrace") {
    group = "verification"
    description = "Runs the production S_00 BattleScreen and records its complete frame trace."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    args(
        "--battle",
        "--scenario=S_00",
        "--full-battle-trace=${yingchuanBattleRegressionTrace.get().asFile.absolutePath}",
        // Simulation timestamps, callback ordering and source-tick assertions
        // are invariant under this renderer time scale. Running at 1x emits
        // tens of thousands of large JSON frames and can take hours because
        // the recorder intentionally retains every frame for post-analysis.
        "--full-battle-time-scale=8",
        "--full-battle-max-sim-seconds=600",
        "--full-battle-seed=1000",
        "--full-battle-math-seed=305419896",
    )
    inputs.files(
        project(":core").sourceSets.main.get().allSource,
        rootProject.file("tools/verify_yingchuan_battle_regression.mjs"),
    )
    outputs.file(yingchuanBattleRegressionTrace)
    // Never allow a successful launcher exit that failed to emit a new trace
    // to reuse evidence from an earlier invocation.
    doFirst { delete(yingchuanBattleRegressionTrace.get().asFile) }
}
val verifyYingchuanBattleRegression = tasks.register<Exec>("verifyYingchuanBattleRegression") {
    group = "verification"
    description = "Validates the complete production S_00 battle trace without capture fixtures."
    dependsOn(captureYingchuanBattleRegressionTrace)
    inputs.file(yingchuanBattleRegressionTrace)
    inputs.file(rootProject.file("tools/verify_yingchuan_battle_regression.mjs"))
    commandLine(
        "node",
        rootProject.file("tools/verify_yingchuan_battle_regression.mjs").absolutePath,
        yingchuanBattleRegressionTrace.get().asFile.absolutePath,
    )
}

/**
 * Starts at the production TitleScreen and dispatches pointer/key events to
 * each screen's installed InputProcessor. The existing deterministic full
 * battle trace observes tactical turns only. The driver enables the source
 * entrusted-battle option through visible UI; production lifecycle advances it.
 */
val campaignScreenE2eTrace = layout.buildDirectory.file("reports/campaign-screen-e2e.json")
val campaignScreenE2eBattleTrace = layout.buildDirectory.file("reports/campaign-screen-e2e-battle.json")
val captureCampaignScreenE2e = tasks.register<JavaExec>("captureCampaignScreenE2e") {
    group = "verification"
    description = "Runs original Title -> R_00 -> S_00 -> save prompt -> R_01 through production screens and input."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    args(
        "--campaign-e2e-trace=${campaignScreenE2eTrace.get().asFile.absolutePath}",
        "--full-battle-trace=${campaignScreenE2eBattleTrace.get().asFile.absolutePath}",
        "--full-battle-time-scale=1",
        "--full-battle-max-sim-seconds=600",
        "--full-battle-seed=1000",
        "--full-battle-math-seed=305419896",
    )
    inputs.files(project(":core").sourceSets.main.get().allSource)
    outputs.files(campaignScreenE2eTrace, campaignScreenE2eBattleTrace)
    doFirst {
        delete(campaignScreenE2eTrace.get().asFile)
        delete(campaignScreenE2eBattleTrace.get().asFile)
    }
}
val verifyCampaignScreenE2e = tasks.register<Exec>("verifyCampaignScreenE2e") {
    group = "verification"
    description = "Validates actual screen classes and zero transition-boundary Enter presses."
    dependsOn(captureCampaignScreenE2e)
    inputs.files(campaignScreenE2eTrace, campaignScreenE2eBattleTrace, rootProject.file("tools/verify_campaign_screen_e2e.py"))
    commandLine(
        "python3",
        rootProject.file("tools/verify_campaign_screen_e2e.py").absolutePath,
        campaignScreenE2eTrace.get().asFile.absolutePath,
        campaignScreenE2eBattleTrace.get().asFile.absolutePath,
    )
}

// A desktop verification run is not valid unless this recovered-JS ↔ Kotlin
// lifecycle contract has also been compared.
tasks.named("check") { dependsOn("verifyWinConditionsPairwise", "verifyScenarioChoiceCoverage", "verifyScenarioRandomCoverage", verifyRenderParityScope, verifyYingchuanSelectionRender, verifyYingchuanModalCaptures, verifyYingchuanBattleRegression, verifyCampaignScreenE2e) }
application {
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    applicationDefaultJvmArgs = listOf(
        "-XstartOnFirstThread",
        "--enable-native-access=ALL-UNNAMED"
    )
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}
