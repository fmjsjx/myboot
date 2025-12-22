plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
    mavenCentral()
}

configurations.all {
    // Enforce version of SpringBoot
    resolutionStrategy.force("org.springframework.boot:spring-boot-dependencies:3.5.8")
}

dependencies {
    // Spring Boot
    val springBootVersion = "3.5.8"
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // libcommon
    implementation(platform("com.github.fmjsjx:libcommon-bom:4.0.0-RC"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:4.0.0-RC"))

    constraints {
        // Apache Kafka
        api("org.apache.kafka:kafka-clients:4.1.1")
        // Apache Pulsar
        api("org.apache.pulsar:pulsar-client:4.1.2")
        // MongoDB
        val mongodbVersion = "5.6.2"
        api("org.mongodb:bson:$mongodbVersion")
        api("org.mongodb:mongodb-driver-core:$mongodbVersion")
        api("org.mongodb:mongodb-driver-sync:$mongodbVersion")
        api("org.mongodb:mongodb-driver-reactivestreams:$mongodbVersion")
        api("org.mongodb:mongodb-driver-legacy:$mongodbVersion")
    }
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.2"))

}

val javaVersion = 21

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = javaVersion
    options.compilerArgs = options.compilerArgs + listOf("-Xlint:deprecation")
}

tasks.withType<Javadoc> {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
    options.memberLevel = JavadocMemberLevel.PUBLIC
}
