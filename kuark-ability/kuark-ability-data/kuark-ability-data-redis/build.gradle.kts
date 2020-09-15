
dependencies {
    api(project(":kuark-ability:kuark-ability-data"))
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.apache.commons:commons-pool2")

    testApi(project(":kuark-test"))
}