dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-memdb"))
    api("org.springframework.session:spring-session-data-redis")
    testApi(project(":kuark-test"))
}