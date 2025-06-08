plugins {
    id("java-library")
}

group = "dev.rg.kafka-poc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Add BOM for dependency version management
    implementation(platform("io.kroxylicious:kroxylicious-bom:0.12.0"))
    implementation(platform("io.micrometer:micrometer-bom:1.15.0"))
    compileOnly("io.kroxylicious:kroxylicious-api")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("io.micrometer:micrometer-core")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}