plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.8.22"
    id("com.google.devtools.ksp") version "1.8.22-1.0.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.4"
}

version = "0.1"
group = "com.roadlink.tripservice"

val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

val mockkVersion = "1.13.4"
val micronautTestJunit5 = "4.0.0-M3"
val testContainersVersion = "1.19.1"
val jakartaPersistenceApiVersion = "2.2.3"
val okhttpVersion = "4.12.0"

dependencies {
    // KOTLIN
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    // LOGGING
    runtimeOnly("ch.qos.logback:logback-classic")

    // MICRONAUT
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("org.yaml:snakeyaml")

    // MICRONAUT DATA
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.flyway:micronaut-flyway") {
        exclude("io.micronaut", "micronaut-management")
    }
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("org.flywaydb:flyway-mysql")

    // JSON
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // HTTP CLIENT
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // DB
    implementation("jakarta.persistence:jakarta.persistence-api:$jakartaPersistenceApiVersion")

    // TEST
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.micronaut.test:micronaut-test-junit5:$micronautTestJunit5")
    testImplementation("org.skyscreamer:jsonassert:1.5.0")

    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:mysql:$testContainersVersion")
    testRuntimeOnly("com.h2database:h2")
}


application {
    mainClass.set("com.roadlink.tripservice.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.roadlink.tripservice.*")
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
