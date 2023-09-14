plugins {
    id("myboot.java-library-conventions")
    id("myboot.publish-conventions")
}

java {
    registerFeature("httpServerSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("redisSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("mongodbSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("kafkaSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("pulsarSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("aliyunonsSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("rocketmqSupport") {
        usingSourceSet(sourceSets["main"])
    }
}

dependencies {

    implementation("org.slf4j:slf4j-api")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    api("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.github.fmjsjx:libcommon-util")

    "httpServerSupportApi"("com.github.fmjsjx:libnetty-http-server")

    "redisSupportApi"("io.lettuce:lettuce-core")
    "redisSupportApi"("org.apache.commons:commons-pool2")
    "redisSupportApi"("com.github.fmjsjx:libcommon-redis")

    "mongodbSupportApi"("org.mongodb:mongodb-driver-sync")
    "mongodbSupportApi"("org.mongodb:mongodb-driver-reactivestreams")

    "kafkaSupportApi"("org.apache.kafka:kafka-clients")

    "pulsarSupportApi"("org.apache.pulsar:pulsar-client")

    "aliyunonsSupportApi"("com.aliyun.openservices:ons-client")

    "rocketmqSupportApi"("org.apache.rocketmq:rocketmq-client") {
        exclude(group = "io.netty", module = "netty-all")
        exclude(group = "io.netty", module = "netty-tcnative-boringssl-static")
    }
    "rocketmqSupportApi"("org.apache.rocketmq:rocketmq-acl")
    "rocketmqSupportApi"("io.netty:netty-handler")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl")

}

description = "MyBoot/AutoConfigure"

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("MyBoot/AutoConfigure")
                description.set("A boot library provides some additional extensions based on SpringBoot.")
                url.set("https://github.com/fmjsjx/myboot")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("fmjsjx")
                        name.set("MJ Fang")
                        email.set("fmjsjx@163.com")
                        url.set("https://github.com/fmjsjx")
                        organization.set("fmjsjx")
                        organizationUrl.set("https://github.com/fmjsjx")
                    }
                }
                scm {
                    url.set("https://github.com/fmjsjx/myboot")
                    connection.set("scm:git:https://github.com/fmjsjx/myboot.git")
                    developerConnection.set("scm:git:https://github.com/fmjsjx/myboot.git")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
