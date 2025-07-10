plugins {
    id("myboot.java-library-conventions")
    id("myboot.publish-conventions")
}

dependencies {

    implementation("org.slf4j:slf4j-api")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    api("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.github.fmjsjx:libcommon-util")

    compileOnlyApi("com.github.fmjsjx:libnetty-http-server")

    compileOnlyApi("io.lettuce:lettuce-core")
    compileOnlyApi("org.apache.commons:commons-pool2")
    compileOnlyApi("com.github.fmjsjx:libcommon-redis")

    compileOnlyApi("org.mongodb:mongodb-driver-sync")
    compileOnlyApi("org.mongodb:mongodb-driver-reactivestreams")

    compileOnlyApi("org.apache.kafka:kafka-clients")

    compileOnlyApi("org.apache.pulsar:pulsar-client")

    compileOnlyApi("com.aliyun.openservices:ons-client")

    compileOnlyApi("org.apache.rocketmq:rocketmq-client") {
        exclude(group = "io.netty", module = "netty-all")
        exclude(group = "io.netty", module = "netty-tcnative-boringssl-static")
    }
//    compileOnlyApi("org.apache.rocketmq:rocketmq-acl:5.3.2")
    compileOnlyApi("io.netty:netty-handler")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl")

}

description = "MyBoot/AutoConfigure"

tasks.withType<Test> {
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
