plugins {
    id("myboot.starter-conventions")
    id("myboot.publish-conventions")
}

dependencies {

    api("io.lettuce:lettuce-core")
    compileOnlyApi("org.apache.commons:commons-pool2")
    api("com.github.fmjsjx:libcommon-redis")

}

description = "MyBoot/Starter REDIS"

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
                name.set("MyBoot/Starter REDIS")
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
