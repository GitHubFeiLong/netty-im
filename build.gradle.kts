plugins {
    id("java")
    // Spring Boot 插件
    id("org.springframework.boot") version "3.5.9"
    // 依赖管理插件
    id("io.spring.dependency-management") version "1.1.7"
}



group = "com.feilong.im"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    // 自动推断模块路径
    modularity.inferModulePath = true
}

// 使用 configure 方法替代直接调用 configurations 块
configurations.configureEach {
    if (name == "compileOnly") {
        extendsFrom(configurations.getByName("annotationProcessor"))
    }
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    maven { url = uri("https://maven.aliyun.com/repositories/jcenter") }
    maven { url = uri("https://maven.aliyun.com/repositories/google") }
    maven { url = uri("https://maven.aliyun.com/repositories/central") }
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.9")
    implementation("com.baomidou:mybatis-plus-jsqlparser:3.5.9")
    implementation("io.netty:netty-all:4.1.131.Final")
    implementation("cn.hutool:hutool-all:5.8.36")

    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.4.0") {
        exclude(group = "org.apache.commons", module = "commons-lang3")
        // 2. 排除它自带的老旧版 springdoc-ui，防止与高版本 Spring Boot 3.5.9 冲突
        exclude(group = "org.springdoc", module = "springdoc-openapi-starter-webmvc-ui")
    }
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    implementation("com.google.guava:guava:33.5.0-jre")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    runtimeOnly("com.mysql:mysql-connector-j:9.5.0")
    // MapStruct 核心
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    // Lombok 核心
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    // MapStruct 注解处理器（必须显式声明）
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    // 关键：Lombok-MapStruct 桥接器（解决执行顺序问题）
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

// 解决增量编译问题（重要！）
tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}
