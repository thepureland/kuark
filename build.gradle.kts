import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktorm_version: String by project
val spring_boot_version: String by project
val slf4j_version: String by project

plugins {
//    application
//    java
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.spring") version "1.3.70"
}

/* 插件的配置需要在buildscript元素中 */
buildscript {
    /* 插件仓库 */
//    repositories {
//        maven { url = uri("https://plugins.gradle.org/m2/") }
//    }
    /* 插件依赖 */
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.5.RELEASE")
        classpath("io.spring.gradle:dependency-management-plugin:1.0.9.RELEASE")
    }
}


allprojects {
    group = "org.kuark"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenLocal()
        jcenter()
        maven { url  = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    }

    apply {
        plugin("kotlin")
        plugin("idea")
//        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
//        plugin("maven-publish")
    }
}



subprojects {

    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11

    ext {

    }

    configurations {
        // 所有需要忽略的包定义在此
//        all*.exclude group: 'commons-httpclient'
//        all*.exclude group: 'commons-logging'
//        all*.exclude group: 'commons-beanutils', module: 'commons-beanutils'
    }

    // 显示当前项目下所有用于 compile 的 jar.
//    tasks.listJars(description: 'Display all compile jars.') << {
//       configurations.compile.each { File file -> println file.name }
//    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<BootJar> {
        enabled = false
    }

    tasks.withType<Jar> {
        enabled = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    kotlin.sourceSets["main"].kotlin.srcDirs("src")
    kotlin.sourceSets["test"].kotlin.srcDirs("test")

    sourceSets["main"].resources.srcDirs("resources")
    sourceSets["test"].resources.srcDirs("testresources")

    dependencies {
        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    }

    dependencyManagement {
//        imports {
//            mavenBom("org.springframework.boot:spring-boot-dependencies:$spring_boot_version")
//        }
        dependencies {
            dependency("de.idyl:winzipaes:1.0.1")
            dependency("com.alibaba.boot:nacos-config-spring-boot-starter:0.2.7")

            // apache commons
            dependency("org.apache.commons:commons-lang3:3.10")
            dependency("org.apache.commons:commons-text:1.8")
            dependency("commons-io:commons-io:2.6")
            dependency("commons-codec:commons-codec:1.14")

            // log
            dependency("org.slf4j:slf4j-api:$slf4j_version")
            dependency("org.slf4j:jcl-over-slf4j:$slf4j_version")
            dependency("org.slf4j:log4j-over-slf4j:$slf4j_version")
            dependency("ch.qos.logback:logback-classic:$logback_version")

            // springboot
            dependency("org.springframework.boot:spring-boot-starter-aop:$spring_boot_version")
            dependency("org.springframework.boot:spring-boot-starter-test:$spring_boot_version") {
                exclude("org.junit.vintage:junit-vintage-engine")
            }

            // data
            dependency("me.liuwj.ktorm:ktorm-core:$ktorm_version")
            dependency("me.liuwj.ktorm:ktorm-jackson:$ktorm_version")
            dependency("org.springframework.boot:spring-boot-starter-data-redis:$spring_boot_version")
//            dependency("me.liuwj.ktorm:ktorm-support-sqlite:$ktorm_version")
            dependency("org.springframework.boot:spring-boot-starter-jdbc:$spring_boot_version")

            // cache
            dependency("javax.cache:cache-api:1.1.1")
//            dependency("org.ehcache:ehcache:3.8.1")
            dependency("com.github.ben-manes.caffeine:caffeine:2.8.1")
            dependency("redis.clients:jedis:3.2.0")
            dependency("org.springframework.boot:spring-boot-starter-cache:$spring_boot_version")
            dependency("com.alibaba:fastjson:1.2.68")

            // ktor
            dependency("io.ktor:ktor-server-netty:$ktor_version")
//            dependency("io.ktor:ktor-server-tomcat:$ktor_version")
            dependency("io.ktor:ktor-server-core:$ktor_version")
            dependency("io.ktor:ktor-html-builder:$ktor_version")
            dependency("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
            dependency("io.ktor:ktor-client-core:$ktor_version")
            dependency("io.ktor:ktor-client-core-jvm:$ktor_version")
            dependency("io.ktor:ktor-client-apache:$ktor_version")
            dependency("io.ktor:ktor-server-tests:$ktor_version")

            dependency("javax.validation:validation-api:2.0.1.Final")

            // 依赖maven中不存在的jar
//            ext.jarTree = fileTree(dir: 'libs', include: '**/*.jar')
//            ext.rootProjectLibs = new File(rootProject.rootDir, 'libs').getAbsolutePath()
//            ext.jarTree += fileTree(dir: rootProjectLibs, include: '**/*.jar')
//            compile jarTree
        }
    }

}

