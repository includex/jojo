pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "jojo-game"

include("engine", "core", "desktop", "android", "verification")
