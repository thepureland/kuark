dependencies {
    api(project(":kuark-context"))
    api("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    api("org.springframework.boot:spring-boot-starter-actuator")

    testApi(project(":kuark-test"))
}