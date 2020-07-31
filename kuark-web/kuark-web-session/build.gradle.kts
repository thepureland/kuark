dependencies {
    api(project(":kuark-data:kuark-data-redis"))
    api("org.springframework.session:spring-session-data-redis")
    testApi(project(":kuark-test"))
}