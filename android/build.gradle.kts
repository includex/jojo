plugins {
    id("com.android.application")
    kotlin("android")
}

val armV7Natives by configurations.creating
val arm64Natives by configurations.creating
val generatedJniLibs = layout.buildDirectory.dir("generated/jniLibs")

android {
    namespace = "com.jojo.game.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jojo.game"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets.getByName("main").jniLibs.srcDir(generatedJniLibs)
    // LibGDX internal files must be Android assets.  A JVM dependency JAR
    // alone does not expose core/processResources to AndroidFileHandle.
    sourceSets.getByName("main").assets.srcDir(project(":core").layout.buildDirectory.dir("resources/main"))
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:${property("gdxVersion")}")
    add(armV7Natives.name, "com.badlogicgames.gdx:gdx-platform:${property("gdxVersion")}:natives-armeabi-v7a")
    add(armV7Natives.name, "com.badlogicgames.gdx:gdx-freetype-platform:${property("gdxVersion")}:natives-armeabi-v7a")
    add(arm64Natives.name, "com.badlogicgames.gdx:gdx-platform:${property("gdxVersion")}:natives-arm64-v8a")
    add(arm64Natives.name, "com.badlogicgames.gdx:gdx-freetype-platform:${property("gdxVersion")}:natives-arm64-v8a")
}

val copyAndroidNatives by tasks.registering(Copy::class) {
    doFirst { delete(generatedJniLibs) }
    from(armV7Natives.map { zipTree(it) }) { include("**/*.so"); into("armeabi-v7a") }
    from(arm64Natives.map { zipTree(it) }) { include("**/*.so"); into("arm64-v8a") }
    into(generatedJniLibs)
}

tasks.named("preBuild").configure {
    dependsOn(copyAndroidNatives)
    dependsOn(project(":core").tasks.named("processResources"))
}

kotlin {
    compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
}
