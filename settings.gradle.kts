pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "jojo-game"

include("core", "desktop", "android", "verification")
