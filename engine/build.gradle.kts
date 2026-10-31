plugins {
    `java-library`
    kotlin("jvm")
}

group = "com.jojo.engine"

dependencies {
    api("com.badlogicgames.gdx:gdx:${property("gdxVersion")}")
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}

val verifyEngineBoundaries = tasks.register("verifyEngineBoundaries") {
    group = "verification"
    description = "Rejects game, launcher and game-content dependencies in the engine."
    val engineSources = fileTree("src/main/kotlin") { include("**/*.kt") }
    inputs.files(engineSources)
    inputs.file("build.gradle.kts")
    doLast {
        check(configurations.flatMap { it.dependencies }.none { it is org.gradle.api.artifacts.ProjectDependency }) {
            "The engine must not depend on game or platform project modules."
        }
        val forbidden = Regex("""\bcom\.jojo\.(?:game|desktop|android|verification)\b|\bcom\.badlogic\.gdx\.backends\b|\"(?:maps|scenarios|scenario-ast|localization)/""")
        val violations = engineSources.files.filter { forbidden.containsMatchIn(it.readText()) }
        check(violations.isEmpty()) { "Game dependency in engine: ${violations.joinToString { it.relativeTo(projectDir).path }}" }
    }
}

tasks.test {
    useJUnitPlatform()
    dependsOn(verifyEngineBoundaries)
}
