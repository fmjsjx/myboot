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
    api(platform("org.springframework.boot:spring-boot-dependencies:2.6.13"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:2.6.13"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:2.7.3"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:2.6.1"))
    // netty
    api(platform("io.netty:netty-bom:4.1.84.Final"))
    // ALIYUN ONS
    api(platform("com.aliyun.openservices:ons-client:2.0.4.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:4.9.4"))

    constraints {
        api("io.lettuce:lettuce-core:6.2.1.RELEASE")
        api("org.apache.kafka:kafka-clients:3.3.1")
        api("org.apache.pulsar:pulsar-client:2.10.2")
        // MongoDB
        val mongodbVersion = "4.7.2"
        api("org.mongodb:bson:$mongodbVersion")
        api("org.mongodb:mongodb-driver-core:$mongodbVersion")
        api("org.mongodb:mongodb-driver-sync:$mongodbVersion")
        api("org.mongodb:mongodb-driver-reactivestreams:$mongodbVersion")
        api("org.mongodb:mongodb-driver-legacy:$mongodbVersion")
    }
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.19.0"))

}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(11)
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
