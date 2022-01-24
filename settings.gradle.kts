pluginManagement {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
    }
}

rootProject.name = "myboot"
include(":myboot-autoconfigure")
include(":myboot-bom")
include(":myboot-example")
include(":myboot-starter-aliyunons")
include(":myboot-starter-http-router")
include(":myboot-starter-kafka")
include(":myboot-starter-mongodb")
include(":myboot-starter-pulsar")
include(":myboot-starter-redis")
include(":myboot-starter-rocketmq")
