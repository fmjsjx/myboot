plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
    jcenter()
}

dependencies {
    // spring boot
    api(platform("org.springframework.boot:spring-boot-dependencies:2.4.2"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:2.4.2"))
    // libcommon
    api(platform("com.github.fmjsjx:libcommon-bom:1.0.1.Final"))
    // netty
    api(platform("io.netty:netty-bom:4.1.58.Final"))
    // ALIYUN ONS
    api(platform("com.aliyun.openservices:ons-client:1.8.7.3.Final"))
    // rocketmq
    api(platform("org.apache.rocketmq:rocketmq-all:4.8.0"))

    constraints {
        api("io.lettuce:lettuce-core:6.0.2.RELEASE")
        api("org.apache.kafka:kafka-clients:2.7.0")
        api("org.apache.pulsar:pulsar-client:2.7.0")
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
