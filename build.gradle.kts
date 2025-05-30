val javaVersion = JavaLanguageVersion.of(21)
val tilleggsstønaderLibsVersion = "2025.05.19-16.10.856a8b28ebfb"
val tilleggsstønaderKontrakterVersion = "2025.05.26-09.25.f07899b2b19c"

group = "no.nav.tilleggsstonader.arena"
version = "1.0.0"

plugins {
    application

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)

    alias(libs.plugins.spring.dependencyManagement)

    alias(libs.plugins.spotless)
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.useLatestVersions)

    alias(libs.plugins.cyclonedx)
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}

apply(plugin = "com.diffplug.spotless")

spotless {
    kotlin {
        ktlint(libs.versions.ktlint.get())
    }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}

dependencies {
    implementation(libs.bundles.spring)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.database.oracle)

    // Logging
    implementation(libs.logstash.logback.encoder)
    implementation(libs.micrometer.prometheus)

    // Tillegggsstønader libs
    implementation("no.nav.tilleggsstonader-libs:util:$tilleggsstønaderLibsVersion")
    implementation("no.nav.tilleggsstonader-libs:log:$tilleggsstønaderLibsVersion")
    implementation("no.nav.tilleggsstonader-libs:sikkerhet:$tilleggsstønaderLibsVersion")

    implementation("no.nav.tilleggsstonader.kontrakter:kontrakter-felles:$tilleggsstønaderKontrakterVersion")

    // Test
    testImplementation(libs.bundles.spring.test)
    testImplementation(libs.bundles.database.oracle.test)

    testImplementation("no.nav.tilleggsstonader-libs:test-util:$tilleggsstønaderLibsVersion")
}

kotlin {
    jvmToolchain(javaVersion.asInt())

    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

application {
    mainClass.set("no.nav.tilleggsstonader.arena.AppKt")
}

if (project.hasProperty("skipLint")) {
    gradle.startParameter.excludedTaskNames += "spotlessKotlinCheck"
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

tasks.cyclonedxBom {
    setIncludeConfigs(listOf("runtimeClasspath", "compileClasspath"))
}