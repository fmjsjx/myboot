plugins {
    id("myboot.java-library-conventions")
    id("myboot.publish-conventions")
}

java {
    registerFeature("redisSupport") {
        usingSourceSet(sourceSets["main"])
    }
    registerFeature("mongodbSupport") {
        usingSourceSet(sourceSets["main"])
    }
}

dependencies {

    implementation("org.slf4j:slf4j-api")

	compileOnly("org.projectlombok:lombok:1.18.16")
    
    api("org.springframework.boot:spring-boot-autoconfigure")
    
    "redisSupportApi"("io.lettuce:lettuce-core")
    "redisSupportApi"("org.apache.commons:commons-pool2")
    
    "mongodbSupportApi"("org.mongodb:mongodb-driver-sync")
    "mongodbSupportApi"("org.mongodb:mongodb-driver-reactivestreams")

    testImplementation("org.junit.jupiter:junit-jupiter-api")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl")

}

description = "myboot/AutoConfigure"

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
                name.set("libcommon/Util")
                description.set("A set of some common useful libraries.")
                url.set("https://github.com/fmjsjx/libcommon")
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
