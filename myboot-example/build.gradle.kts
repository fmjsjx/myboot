plugins {
	id("myboot.java-library-conventions")
    id("myboot.publish-conventions")
}

description = "MyBoot/Example"

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "io.r2dbc", module = "r2dbc-pool")
        exclude(group = "io.r2dbc", module = "r2dbc-spi")
        exclude(group = "org.springframework.boot", module = "spring-boot-r2dbc")
    }
}

dependencies {

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.slf4j:slf4j-api")
    implementation(project(":myboot-starter-mongodb"))
    implementation(project(":myboot-starter-redis"))
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.github.fmjsjx:libnetty-http-server")
    implementation("io.netty:netty-tcnative-boringssl-static::linux-aarch_64")
    implementation("io.netty:netty-tcnative-boringssl-static::linux-x86_64")
    implementation("io.netty:netty-tcnative-boringssl-static::osx-x86_64")
    implementation("io.netty:netty-tcnative-boringssl-static::windows-x86_64")
    api("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

val javaVersion = 21

java {
    withSourcesJar()
	withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
    // Use JUnit platform for unit tests.
    useJUnitPlatform()
}
