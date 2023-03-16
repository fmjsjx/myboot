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
    val springBootVersion = "2.6.14"
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:3.2.0"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:3.0.0"))
    // netty
    api(platform("io.netty:netty-bom:4.1.89.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:5.1.0"))

    constraints {
        api("io.lettuce:lettuce-core:6.2.3.RELEASE")
        api("org.apache.kafka:kafka-clients:3.4.0")
        api("org.apache.pulsar:pulsar-client:2.11.0")
        // ALIYUN ONS
        api("com.aliyun.openservices:ons-client:2.0.5.Final")
        // MongoDB
        val mongodbVersion = "4.9.0"
        api("org.mongodb:bson:$mongodbVersion")
        api("org.mongodb:mongodb-driver-core:$mongodbVersion")
        api("org.mongodb:mongodb-driver-sync:$mongodbVersion")
        api("org.mongodb:mongodb-driver-reactivestreams:$mongodbVersion")
        api("org.mongodb:mongodb-driver-legacy:$mongodbVersion")
    }
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.20.0"))

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
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
