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
    val springBootVersion = "3.2.5"
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:3.8.0"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:3.6.0"))
    // netty
    api(platform("io.netty:netty-bom:4.1.109.Final"))
    // ALIYUN ONS
    api(platform("com.aliyun.openservices:ons-client:2.0.7.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:5.2.0"))

    constraints {
        api("io.lettuce:lettuce-core:6.3.2.RELEASE")
        api("org.apache.kafka:kafka-clients:3.7.0")
        api("org.apache.pulsar:pulsar-client:3.2.2")
        // MongoDB
        val mongodbVersion = "5.1.0"
        api("org.mongodb:bson:$mongodbVersion")
        api("org.mongodb:mongodb-driver-core:$mongodbVersion")
        api("org.mongodb:mongodb-driver-sync:$mongodbVersion")
        api("org.mongodb:mongodb-driver-reactivestreams:$mongodbVersion")
        api("org.mongodb:mongodb-driver-legacy:$mongodbVersion")
    }
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.23.1"))

}

val javaVersion = 17

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(javaVersion)
    options.compilerArgs = listOf("-Xlint:deprecation")
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
