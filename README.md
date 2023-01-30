# myboot
A boot library provides some additional extensions based on SpringBoot.

## 添加依赖
每个release版本都将发布至[Maven中央仓库](https://repo1.maven.org/maven2/)
### 添加Maven依赖
`pom.xml`
```xml
...
  <dependencyManagement>
    <dependencies>
      ...
      <!-- 版本控制 -->
      <dependency>
        <groupId>com.github.fmjsjx</groupId>
        <artifactId>myboot-bom</artifactId>
        <version>1.1.12</version>
      </dependency>
      ...
    </dependencies>
  </dependencyManagement>
...
  <dependencies>
    ...
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
    ...
  </dependencies>
...
```

### 添加Gradle依赖

#### Groovy DSL
```groovy
...
repositories {
    mavenCentral
}

dependencies {
    // 版本控制
    implementation platform('com.github.fmjsjx:myboot-bom:1.1.12')
    // REDIS
    implementation('com.github.fmjsjx:myboot-starter-redis') {
        // 移除同步连接池依赖
        exclude group: 'org.apache.commons', module: 'commons-pool2'
    }
    // MongoDB
    compileOnly 'com.github.fmjsjx:myboot-starter-mongodb'
    ...
}
...
```
#### Kotlin DSL
```kotlin
...
repositories {
    mavenCentral()
}

dependencies {
    // 版本控制
    implementation(platform("com.github.fmjsjx:myboot-bom:1.1.12"))
    // REDIS
    implementation("com.github.fmjsjx:myboot-starter-redis") {
        // 移除同步连接池依赖
        exclude(group = "org.apache.commons", module = "commons-pool2")
    }
    // MongoDB
    compileOnly("com.github.fmjsjx:myboot-starter-mongodb")
    ...
}
...
```
