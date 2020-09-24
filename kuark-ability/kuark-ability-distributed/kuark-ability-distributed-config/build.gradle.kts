
dependencies {
    api("org.springframework.cloud:spring-cloud-starter-config")
    api("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-actuator")

    testApi(project(":kuark-test"))
}