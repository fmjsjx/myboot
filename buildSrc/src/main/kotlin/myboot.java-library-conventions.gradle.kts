plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
	// spring boot
	api(platform("org.springframework.boot:spring-boot-dependencies:2.4.1"))
	// libcommon
	api(platform("com.github.fmjsjx:libcommon-bom:1.0.0.M3"))

    constraints {
        api("io.lettuce:lettuce-core:6.0.2.RELEASE")
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