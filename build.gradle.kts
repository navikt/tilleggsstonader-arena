val javaVersion = JavaLanguageVersion.of(21)
val tilleggsstønaderLibsVersion = "2025.03.14-14.38.d650c79601e9"
val tilleggsstønaderKontrakterVersion = "2025.03.14-14.41.da22448d0489"
val familieProsesseringVersion = "2.20231212093500_bfa0e7c"
val tokenSupportVersion = "5.0.11"

group = "no.nav.tilleggsstonader.arena"
version = "1.0.0"

plugins {
    application

    kotlin("jvm") version "2.0.21"
    id("com.diffplug.spotless") version "7.0.1"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"

    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.spring") version "2.0.21"

    id("org.cyclonedx.bom") version "1.10.0"
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
        ktlint("1.5.0")
    }
}

configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.oracle.database.jdbc:ojdbc8:23.6.0.24.10")

    // Logging
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")

    implementation("io.micrometer:micrometer-registry-prometheus")

    // Tillegggsstønader libs
    implementation("no.nav.tilleggsstonader-libs:util:$tilleggsstønaderLibsVersion")
    implementation("no.nav.tilleggsstonader-libs:log:$tilleggsstønaderLibsVersion")
    implementation("no.nav.tilleggsstonader-libs:sikkerhet:$tilleggsstønaderLibsVersion")

    implementation("no.nav.tilleggsstonader.kontrakter:tilleggsstonader-kontrakter:$tilleggsstønaderKontrakterVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.13")

    testImplementation("org.flywaydb:flyway-core")
    testImplementation("com.h2database:h2")

    testImplementation("no.nav.security:token-validation-spring-test:$tokenSupportVersion")
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
