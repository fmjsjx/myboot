plugins {
	id("myboot.java-library-conventions")
    id("myboot.publish-conventions")
}

description = "MyBoot/Example"

dependencies {
	
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.slf4j:slf4j-api")
    implementation(project(":myboot-starter-aliyunons")) {
        exclude(group = "com.github.fmjsjx", module = "libcommon-aliyunons")
    }
    implementation(project(":myboot-starter-kafka"))
    implementation(project(":myboot-starter-mongodb"))
    implementation(project(":myboot-starter-pulsar"))
    implementation(project(":myboot-starter-redis"))
    implementation(project(":myboot-starter-rocketmq")) {
        exclude(group = "com.github.fmjsjx", module = "libcommon-rocketmq")
    }
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.github.fmjsjx:libnetty-http-server")
    implementation(group = "io.netty", name = "netty-tcnative-boringssl-static", classifier = "linux-aarch_64")
    implementation(group = "io.netty", name = "netty-tcnative-boringssl-static", classifier = "linux-x86_64")
    implementation(group = "io.netty", name = "netty-tcnative-boringssl-static", classifier = "osx-x86_64")
    implementation(group = "io.netty", name = "netty-tcnative-boringssl-static", classifier = "windows-x86_64")

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
                name.set("MyBoot/Example")
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
