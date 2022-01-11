import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

object Version {
    const val KOTLIN = "1.6.10"
    const val SPRING_BOOT = "2.4.6"
    const val SPRING_SESSION = "2.5.2"
    const val SPRING_CLOUD = "2020.0.3"
    const val ALIBABA_CLOUD = "2.2.1.RELEASE"
    const val KTORM = "3.2.0"
    const val LOGBACK = "1.2.3"
    const val SLF4J = "1.7.30"
    const val KTOR = "1.4.0"
    const val ACTIVITI = "7.1.0-M13"
    const val DROOLS = "7.55.0.Final"
}

plugins {
//    application
//    java
    id("org.springframework.boot") version "2.4.6"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.openjfx.javafxplugin") version "0.0.8"
    kotlin("jvm") version "1.6.10"
//    kotlin("plugin.spring") version "1.6.10"
}

/* 插件的配置需要在buildscript元素中 */
buildscript {
    /* 插件仓库 */
//    repositories {
//        maven { url = uri("https://plugins.gradle.org/m2/") }
//    }
    /* 插件依赖 */
//    dependencies {
//        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.4.6")
//        classpath("io.spring.gradle:dependency-management-plugin:1.0.9.RELEASE")
//    }
}


allprojects {
    group = "io.kuark"
    version = "1.0.0-SNAPSHOT"

    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
        maven { url = uri("https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases") }
        mavenLocal()
        jcenter()
    }

    apply {
        plugin("kotlin")
        plugin("idea")
//        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
//        plugin("maven-publish")
        plugin("org.openjfx.javafxplugin")
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
            jvmTarget = "11"
        }
    }

//    sourceSets {
//        getByName("main").java.srcDirs("src")
//        getByName("main").resources.srcDirs("resources")
//        getByName("test").java.srcDirs("test")
//        getByName("test").resources.srcDirs("testresources")
//    }

    kotlin.sourceSets["main"].kotlin.srcDirs("src")
    kotlin.sourceSets["test"].kotlin.srcDirs("test")

    sourceSets["main"].resources.srcDirs("resources")
    sourceSets["test"].resources.srcDirs("testresources")

    dependencies {
        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect:${Version.KOTLIN}")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.9")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${Version.SPRING_BOOT}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Version.SPRING_CLOUD}")
            mavenBom("com.alibaba.cloud:spring-cloud-alibaba-dependencies:${Version.ALIBABA_CLOUD}")
//            mavenBom("com.alibaba.cloud:aliyun-spring-boot-dependencies:${Version.ALIBABA_CLOUD}")
            mavenBom("org.activiti:activiti-dependencies:${Version.ACTIVITI}")
        }
        dependencies {
            dependency("de.idyl:winzipaes:1.0.1")

            // commons
            dependency("org.apache.commons:commons-lang3:3.10")
            dependency("org.apache.commons:commons-text:1.8")
            dependency("commons-io:commons-io:2.6")
            dependency("commons-fileupload:commons-fileupload:1.4")
            dependency("commons-codec:commons-codec:1.14")
            dependency("commons-beanutils:commons-beanutils:1.9.4")
            dependency("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:2.9.7")
            dependency("net.sourceforge.jexcelapi:jxl:2.6.12")
            dependency("javax.xml.bind:jaxb-api:2.3.1")
            dependency("com.sun.xml.bind:jaxb-impl:2.3.1")
            dependency("org.glassfish.jaxb:jaxb-runtime:2.3.1")
            dependency("com.google.zxing:core:3.4.0")
            dependency("org.javamoney:moneta:1.4.2")
            dependency("org.apache.xmlgraphics:batik-all:1.1.4")
            dependency("xerces:xercesImpl:2.12.1")

            // log
            dependency("org.slf4j:slf4j-api:${Version.SLF4J}")
            dependency("org.slf4j:jcl-over-slf4j:${Version.SLF4J}")
            dependency("org.slf4j:log4j-over-slf4j:${Version.SLF4J}")
            dependency("ch.qos.logback:logback-classic:${Version.LOGBACK}")

            // validation
//            dependency("javax.validation:validation-api:2.0.1.Final")
            dependency("org.hibernate.validator:hibernate-validator:6.1.5.Final")
            dependency("javax.el:javax.el-api:3.0.1-b06")
            dependency("org.glassfish.web:javax.el:2.2.6")

            // springboot
//            dependency("org.springframework.boot:spring-boot-starter-aop:${Version.SPRING_BOOT}")
//            dependency("org.springframework.boot:spring-boot-starter-web:${Version.SPRING_BOOT}")
//            dependency("org.springframework.boot:spring-boot-starter-webflux:${Version.SPRING_BOOT}")
//            dependency("org.springframework.boot:spring-boot-starter-actuator:${Version.SPRING_BOOT}")
//            dependency("org.springframework.boot:spring-boot-starter-data-redis-reactive:${Version.SPRING_BOOT}") // 限流用
//            dependency("de.codecentric:spring-boot-admin-starter-server:2.3.0")
//            dependency("org.springframework.boot:spring-boot-starter-test:${Version.SPRING_BOOT}") {
//                exclude("org.junit.vintage:junit-vintage-engine")
//            }

            // spring
            dependency("org.springframework.session:spring-session-core:${Version.SPRING_SESSION}")

            // swagger
            dependency("io.springfox:springfox-boot-starter:3.0.0")

            // redis
            dependency("org.redisson:redisson:3.13.4")
//            dependency("org.redisson:redisson-spring-boot-starter:3.13.4")

            // data
            dependency("org.ktorm:ktorm-core:${Version.KTORM}")
            dependency("org.ktorm:ktorm-jackson:${Version.KTORM}")
            dependency("org.ktorm:ktorm-support-mysql:${Version.KTORM}")
            dependency("org.ktorm:ktorm-support-postgresql:${Version.KTORM}")
            dependency("org.ktorm:ktorm-support-oracle:${Version.KTORM}")
            dependency("org.ktorm:ktorm-support-sqlserver:${Version.KTORM}")
            dependency("org.ktorm:ktorm-support-sqlite:${Version.KTORM}")
            dependency("org.springframework.boot:spring-boot-starter-data-redis:${Version.SPRING_BOOT}")
            dependency("org.apache.commons:commons-pool2:2.8.0")
//            dependency("me.liuwj.ktorm:ktorm-support-sqlite:$KTORM_VERSION")
            dependency("org.springframework.boot:spring-boot-starter-jdbc:${Version.SPRING_BOOT}")

            // cache
            dependency("com.github.ben-manes.caffeine:caffeine:2.8.1")
            dependency("org.springframework.boot:spring-boot-starter-cache:${Version.SPRING_BOOT}")
            dependency("com.alibaba:fastjson:1.2.72")

            // session
            dependency("org.springframework.session:spring-session-data-redis:2.2.2.RELEASE")

            // auth
            dependency("org.apache.oltu.oauth2:org.apache.oltu.oauth2.authzserver:1.0.2")
            dependency("org.apache.oltu.oauth2:org.apache.oltu.oauth2.resourceserver:1.0.2")
            dependency("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client:1.0.2")

            // ktor
            dependency("io.ktor:ktor-server-sessions:${Version.KTOR}")
            dependency("io.ktor:ktor-server-netty:${Version.KTOR}")
//            dependency("io.ktor:ktor-server-tomcat:${DependencyVersions.KTOR_VERSION}")
            dependency("io.ktor:ktor-server-core:${Version.KTOR}")
            dependency("io.ktor:ktor-html-builder:${Version.KTOR}")
//            dependency("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
            dependency("io.ktor:ktor-client-core:${Version.KTOR}")
            dependency("io.ktor:ktor-client-core-jvm:${Version.KTOR}")
            dependency("io.ktor:ktor-client-apache:${Version.KTOR}")
            dependency("io.ktor:ktor-server-tests:${Version.KTOR}")

            // javafx
//            dependency("de.roskenet:springboot-javafx-support:2.1.6")
//            dependency("org.openjfx:javafx-controls:12.0.1")
            dependency("org.controlsfx:controlsfx:8.40.10")

            // tools
            dependency("org.freemarker:freemarker:2.3.30")

            // spring cloud
//            dependency("org.springframework.cloud:spring-cloud-commons:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-context:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-starter-bootstrap:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-config-server:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-config-client:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-starter-config:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${Version.SPRING_CLOUD}")
//            dependency("org.springframework.cloud:spring-cloud-starter-openfeign:${Version.SPRING_CLOUD}")
            dependency("org.springframework.cloud:spring-cloud-starter-netflix-hystrix-dashboard:2.2.6.RELEASE")
            dependency("org.springframework.cloud:spring-cloud-starter-netflix-hystrix:2.2.6.RELEASE")
//            dependency("org.springframework.cloud:spring-cloud-starter-gateway:${Version.SPRING_CLOUD}")
            dependency("org.springframework.cloud:spring-cloud-starter-bus-amqp:2.2.3.RELEASE")

            // seata
            dependency("io.seata:seata-all:1.4.2")
            dependency("com.alibaba.cloud:spring-cloud-alibaba-seata:2.2.0.RELEASE")

            dependency("com.alibaba.nacos:nacos-spring-context:0.3.6")

            // activiti
            dependency("org.activiti:activiti-spring-boot-starter:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-engine:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-spring:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-bpmn-model:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-bpmn-converter:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-json-converter:${Version.ACTIVITI}")
//            dependency("org.activiti:activiti-bpmn-layout:${Version.ACTIVITI}")
            dependency("org.activiti:activiti-image-generator:${Version.ACTIVITI}")
//            dependency("org.activiti.cloud:activiti-cloud-services-api:7-201802-EA")
            dependency("org.apache.xmlgraphics:batik-all:1.14")

            // drools
            dependency("org.drools:drools-compiler:${Version.DROOLS}")
            dependency("org.drools:drools-mvel:${Version.DROOLS}")

//            dependency("com.alibaba.boot:nacos-config-spring-boot-starter:0.2.7")
//            dependency("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2021.1")
//            {
//                exclude("org.springframework.cloud:spring-cloud-context")
//            }
//            dependency("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2021.1")
//            {
//                exclude("org.springframework.cloud:spring-cloud-context")
//            }


            // 依赖maven中不存在的jar
//            ext.jarTree = fileTree(dir: 'libs', include: '**/*.jar')
//            ext.rootProjectLibs = new File(rootProject.rootDir, 'libs').getAbsolutePath()
//            ext.jarTree += fileTree(dir: rootProjectLibs, include: '**/*.jar')
//            compile jarTree
        }
    }

}

