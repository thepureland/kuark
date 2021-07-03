dependencies {
    api(project(":kuark-context"))
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.ktorm:ktorm-core")
    api("org.ktorm:ktorm-jackson")
    api("org.ktorm:ktorm-support-mysql") //TODO provider
    api("org.ktorm:ktorm-support-postgresql") //TODO provider
    api("org.ktorm:ktorm-support-oracle") //TODO provider
    api("org.ktorm:ktorm-support-sqlserver") //TODO provider
    api("org.ktorm:ktorm-support-sqlite") //TODO provider
    api("com.h2database:h2:1.4.200") //TODO provider
    api("org.postgresql:postgresql:42.2.20") //TODO provider
//    compileOnly("org.xerial:sqlite-jdbc:3.30.1")

    testImplementation(project(":kuark-test:kuark-test-common"))
}
