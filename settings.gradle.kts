rootProject.name = "tilleggsstonader-arena"

val versionCatalog = "2025.05.30-10.00.fb98fd578374"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    repositories {
        maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
    versionCatalogs {
        create("libs") {
            from("no.nav.tilleggsstonader:tilleggsstonader-version-catalog:$versionCatalog")
        }
    }
}