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
    api(platform("org.springframework.boot:spring-boot-dependencies:2.6.3"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:2.6.3"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:2.6.2"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:2.4.3"))
    // netty
    api(platform("io.netty:netty-bom:4.1.74.Final"))
    // ALIYUN ONS
    api(platform("com.aliyun.openservices:ons-client:2.0.0.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:4.9.2"))

    constraints {
        api("io.lettuce:lettuce-core:6.1.6.RELEASE")
        api("org.apache.kafka:kafka-clients:3.1.0")
        api("org.apache.pulsar:pulsar-client:2.9.1")
        // MongoDB
        api("org.mongodb:bson:4.5.0")
        api("org.mongodb:mongodb-driver-core:4.5.0")
        api("org.mongodb:mongodb-driver-sync:4.5.0")
        api("org.mongodb:mongodb-driver-reactivestreams:4.5.0")
        api("org.mongodb:mongodb-driver-legacy:4.5.0")
    }
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.17.1"))

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
