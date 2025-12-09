plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    // spring boot
    val springBootVersion = "3.5.8"
    api(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:3.17.0"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:3.10.0"))

    constraints {
        api("org.apache.kafka:kafka-clients:4.1.1")
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
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.1"))

}

val javaVersion = 17

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
