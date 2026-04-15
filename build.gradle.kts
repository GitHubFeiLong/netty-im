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

    // ========== Spring Boot Starters ==========
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // ========== MyBatis-Plus ==========
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.15")
    implementation("com.baomidou:mybatis-plus-jsqlparser:3.5.15")

    // MyBatis-Plus 代码生成器
    implementation("com.baomidou:mybatis-plus-generator:3.5.15")
    implementation("org.freemarker:freemarker:2.3.33")

    // ========== Netty (使用 Spring Boot 管理的版本) ==========
    implementation("io.netty:netty-all")

    // ========== Hutool (按需引入，减小体积) ==========
    implementation("cn.hutool:hutool-core:5.8.36")
    implementation("cn.hutool:hutool-json:5.8.36")

    // ========== API 文档 (二选一) ==========
    // Knife4j
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.4.0") {
        exclude(group = "org.apache.commons", module = "commons-lang3")
        // 排除它自带的老旧版 springdoc-ui，防止与高版本 Spring Boot 3.5.9 冲突
        exclude(group = "org.springdoc", module = "springdoc-openapi-starter-webmvc-ui")
    }
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    // ========== 工具库 ==========
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("com.google.guava:guava:33.5.0-jre")

    // ========== JWT (JJWT) ==========
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // ========== Database ==========
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // ========== MapStruct & Lombok ==========
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // Lombok (测试代码)
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    // ========== Test ==========
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.test {
    useJUnitPlatform()
}

// 解决增量编译问题（重要！）
tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}
