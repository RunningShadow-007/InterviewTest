pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "InterviewTest"
include(":app")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":feature:wallet")
include(":feature:settings")
include(":feature:defi")
include(":core:model")
