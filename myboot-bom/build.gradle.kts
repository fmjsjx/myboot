plugins {
    `java-platform`
    id("myboot.publish-conventions")
}

description = "MyBoot/BOM"

dependencies {
	constraints {
        api(project(":myboot-autoconfigure"))
        api(project(":myboot-example"))
        api(project(":myboot-starter-redis"))
    }
}

publishing {
    publications {
    	create<MavenPublication>("mavenJava") {
        	from(components["javaPlatform"])
            pom {
                name.set("MyBoot/BOM")
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