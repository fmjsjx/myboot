plugins {
    `java-library`
}

repositories {
    exclusiveContent {
        forRepositories(
            maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") },
            mavenCentral(),
        )
        filter {
            includeGroupByRegex(".*")
        }
    }
}

dependencies {
    // SpringBoot
    val springBootVersion = "4.0.8"
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    // LibCommon
    implementation(platform("com.github.fmjsjx:libcommon-bom:4.3.0-RC3"))
    // LibNetty
    api(platform("com.github.fmjsjx:libnetty-bom:4.3.0-RC2"))

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

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
        extendsFrom(configurations.compileOnlyApi.get())
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
