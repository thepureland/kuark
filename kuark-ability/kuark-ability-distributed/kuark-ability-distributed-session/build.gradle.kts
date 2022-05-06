dependencies {
    api(project(":kuark-context"))
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-memdb"))
    api("org.springframework.session:spring-session-core")
    api("org.springframework.session:spring-session-data-redis")

    testImplementation(project(":kuark-test:kuark-test-common"))
}