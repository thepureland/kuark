dependencies {
    api(project(":kuark-data"))
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("me.liuwj.ktorm:ktorm-core")
    api("me.liuwj.ktorm:ktorm-jackson")
    compileOnly("com.h2database:h2:1.4.200")
//    compileOnly("org.xerial:sqlite-jdbc:3.30.1")
}
