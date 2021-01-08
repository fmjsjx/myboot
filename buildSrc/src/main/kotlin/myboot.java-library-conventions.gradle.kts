plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
	jcenter()
}

dependencies {
	// spring boot
	api(platform("org.springframework.boot:spring-boot-dependencies:2.4.1"))
	// libcommon
	api(platform("com.github.fmjsjx:libcommon-bom:1.0.0.M1"))

    constraints {
        api("io.lettuce:lettuce-core:6.0.1.RELEASE")
    }

}

java {
    withSourcesJar()
	withJavadocJar()
	toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
	options.release.set(11)
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}