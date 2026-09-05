pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "jojo-libgdx-port"

include("core", "desktop", "android")
