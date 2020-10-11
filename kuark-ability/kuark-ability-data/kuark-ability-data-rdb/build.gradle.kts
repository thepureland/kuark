dependencies {
    api(project(":kuark-context"))
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.ktorm:ktorm-core")
    api("org.ktorm:ktorm-jackson")
    api("com.h2database:h2:1.4.200")
//    compileOnly("org.xerial:sqlite-jdbc:3.30.1")
    testApi(project(":kuark-test"))
}
