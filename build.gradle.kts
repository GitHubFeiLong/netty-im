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
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.9")
    implementation("com.baomidou:mybatis-plus-jsqlparser:3.5.9")
    implementation("io.netty:netty-all:4.1.131.Final")

    implementation("com.google.guava:guava:33.5.0-jre")
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
