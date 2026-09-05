plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-headless:${property("gdxVersion")}")
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

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}
