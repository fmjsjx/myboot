plugins {
    `maven-publish`
    signing
}

group = "com.github.fmjsjx"
version = "3.7.0-RC1-SNAPSHOT"

publishing {
    repositories {
        maven {
            url = uri(rootProject.layout.buildDirectory.dir("staging-deploy"))
        }
    }
}
