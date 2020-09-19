dependencies {
    api(project(":kuark-ability:kuark-ability-data"))
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("me.liuwj.ktorm:ktorm-core")
    api("me.liuwj.ktorm:ktorm-jackson")
    api("com.h2database:h2:1.4.200")
//    compileOnly("org.xerial:sqlite-jdbc:3.30.1")
    testApi(project(":kuark-test"))
}