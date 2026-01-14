pluginManagement {
    repositories {
        maven {
            url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "myboot"
include(":myboot-autoconfigure")
include(":myboot-bom")
include(":myboot-example")
include(":myboot-starter-http-router")
include(":myboot-starter-mongodb")
include(":myboot-starter-redis")
include(":myboot-starter-r2dbc")
