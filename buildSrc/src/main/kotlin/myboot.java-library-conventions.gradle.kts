plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
    mavenCentral()
}

configurations.all {
    // Enforce version of SpringBoot
    resolutionStrategy.force("org.springframework.boot:spring-boot-dependencies:4.0.1")
}

dependencies {
    // Spring Boot
    val springBootVersion = "4.0.1"
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // libcommon
    implementation(platform("com.github.fmjsjx:libcommon-bom:4.0.0-SNAPSHOT"))
    // libnetty
    api(platform("com.github.fmjsjx:libnetty-bom:4.0.0-SNAPSHOT"))

}

val javaVersion = 21

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = javaVersion
    options.compilerArgs = options.compilerArgs + listOf("-Xlint:deprecation")
}

tasks.withType<Javadoc> {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
    options.memberLevel = JavadocMemberLevel.PUBLIC
}
