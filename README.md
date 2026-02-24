# MyBoot
A boot library that provides additional extensions based on SpringBoot.

## JDK and SpringBoot Version Compatibility
| MyBoot Version | JDK Version | SpringBoot Version |
|----------------|-------------|--------------------|
| 4.x            | JDK 21+     | SpringBoot 4.x     |
| 3.x            | JDK 17+     | SpringBoot 3.x     |
| 2.x            | JDK 17+     | SpringBoot 2.x     |
| older          | JDK 11+     | SpringBoot 2.x     |

## Adding Dependencies
Every release version is published to [Maven Central](https://repo1.maven.org/maven2/)
### Adding Maven Dependencies
`pom.xml`
```xml
<pom>
  <dependencyManagement>
    <dependencies>
      <!-- Version management -->
      <dependency>
        <groupId>com.github.fmjsjx</groupId>
        <artifactId>myboot-bom</artifactId>
        <version>4.1.1</version>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- REDIS -->
    <dependency>
      <groupId>com.github.fmjsjx</groupId>
      <artifactId>myboot-starter-redis</artifactId>
      <exclusions>
        <exclusion>
          <groupId>org.apache.commons</groupId>
          <artifactId>commons-pool2</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <!-- MongoDB -->
    <dependency>
      <groupId>com.github.fmjsjx</groupId>
      <artifactId>myboot-starter-mongodb</artifactId>
    </dependency>
  </dependencies>
</pom>
```

### Adding Gradle Dependencies

#### Groovy DSL
```groovy
repositories {
    mavenCentral
}

dependencies {
    // Version management
    implementation platform('com.github.fmjsjx:myboot-bom:4.1.1')
    // REDIS
    implementation('com.github.fmjsjx:myboot-starter-redis') {
        // Exclude synchronous connection pool dependency
        exclude group: 'org.apache.commons', module: 'commons-pool2'
    }
    // MongoDB
    compileOnly 'com.github.fmjsjx:myboot-starter-mongodb'
}
```

#### Kotlin DSL
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // Version management
    implementation(platform("com.github.fmjsjx:myboot-bom:4.1.1"))
    // REDIS
    implementation("com.github.fmjsjx:myboot-starter-redis") {
        // Exclude synchronous connection pool dependency
        exclude(group = "org.apache.commons", module = "commons-pool2")
    }
    // MongoDB
    compileOnly("com.github.fmjsjx:myboot-starter-mongodb")
}
```
