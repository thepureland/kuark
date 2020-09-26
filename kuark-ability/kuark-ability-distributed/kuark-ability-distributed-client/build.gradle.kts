dependencies {
    api(project(":kuark-context"))
//    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")

    testApi(project(":kuark-test"))
}