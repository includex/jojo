plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
    implementation("com.badlogicgames.gdx:gdx-backend-headless:${property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${property("gdxVersion")}")
    runtimeOnly("com.badlogicgames.gdx:gdx-platform:${property("gdxVersion")}:natives-desktop")
    runtimeOnly("com.badlogicgames.gdx:gdx-freetype-platform:${property("gdxVersion")}:natives-desktop")
}

application {
    mainClass.set("com.jojo.game.verification.VerificationHeadlessLauncher")
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

tasks.register<JavaExec>("verifyAllHeadless") {
    group = "verification"
    description = "Runs the isolated scenario and battle catalog verification suite headlessly."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.verification.VerificationHeadlessLauncher")
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

/**
 * Runs the real LibGDX campaign-input verifier without making desktop or core
 * depend on verification. Defaults to a bounded R_00 scene-1 smoke; pass
 * `-PcampaignE2eArgs="--stop=R_01:1 --assert-bootstrap"` for the full route.
 */
tasks.register<JavaExec>("campaignE2e") {
    group = "verification"
    description = "Runs the verification-owned campaign E2E driver (default: bounded smoke)."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.verification.campaign.CampaignE2eDesktopLauncher")
    if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        jvmArgs("-XstartOnFirstThread")
    }
    jvmArgs("--enable-native-access=ALL-UNNAMED")
    val configuredArgs = providers.gradleProperty("campaignE2eArgs")
        .map { it.trim().split(Regex("\\s+")).filter(String::isNotBlank) }
        .orElse(emptyList())
    doFirst { setArgs(configuredArgs.get()) }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}

// The repository's source/game trace gates are verification-owned.  Keeping
// their declarations here means they execute with this project's runtime
// classpath and retain a one-way dependency on :core.
apply(from = "trace-tasks.gradle.kts")
