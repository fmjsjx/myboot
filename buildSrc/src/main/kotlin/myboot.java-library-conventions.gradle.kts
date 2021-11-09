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
    api(platform("org.springframework.boot:spring-boot-dependencies:2.5.6"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:2.5.6"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:2.5.2"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:2.2.7"))
    // netty
    api(platform("io.netty:netty-bom:4.1.69.Final"))
    // ALIYUN ONS
    api(platform("com.aliyun.openservices:ons-client:2.0.0.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:4.9.2"))

    constraints {
        api("io.lettuce:lettuce-core:6.1.5.RELEASE")
        api("org.apache.kafka:kafka-clients:3.0.0")
        api("org.apache.pulsar:pulsar-client:2.8.1")
    }

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
