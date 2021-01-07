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
	// libcommon
	api(platform("com.github.fmjsjx:libcommon-bom:1.0.0.M1"))
	// spring boot
	api(platform("org.springframework.boot:spring-boot-dependencies:2.4.1"))
	// netty
    api(platform("io.netty:netty-bom:4.1.56.Final"))
    
	// jackson2
	api(platform("com.fasterxml.jackson:jackson-bom:2.11.4"))
    // log4j2
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.14.0"))
	// junit
	testImplementation(platform("org.junit:junit-bom:5.7.0"))

    constraints {
    	compileOnly("org.projectlombok:lombok:1.18.16")
        implementation("org.slf4j:slf4j-api:1.7.30")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        api("io.lettuce:lettuce-core:6.0.1.RELEASE")
        api("com.dslplatform:dsl-json-java8:1.9.7")
        api("com.jsoniter:jsoniter:0.9.23")
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