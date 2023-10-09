plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.8.22"
    id("com.google.devtools.ksp") version "1.8.22-1.0.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.4"
    id("org.openrewrite.rewrite") version("latest.release")
}

version = "0.1"
group = "com.roadlink.tripservice"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

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
    rewrite("org.openrewrite.recipe:rewrite-micronaut:2.1.1")

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
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // DB
    //implementation("com.mysql:mysql-connector-j:8.0.33")
    //implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")

    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("io.micronaut.test:micronaut-test-junit5:4.0.0-M3")

    testImplementation("org.testcontainers:testcontainers:1.19.1")
    testImplementation("org.testcontainers:mysql:1.19.1")
    testRuntimeOnly("com.h2database:h2")
}


application {
    mainClass.set("com.roadlink.tripservice.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
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

rewrite {
    activeRecipe("org.openrewrite.java.micronaut.Micronaut3to4Migration")
}
