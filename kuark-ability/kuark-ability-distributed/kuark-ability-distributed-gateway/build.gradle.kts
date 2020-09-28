dependencies {
    api(project(":kuark-context"))
    api("org.springframework.cloud:spring-cloud-starter-gateway")
    api("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive") // 限流用
    api("org.springframework.boot:spring-cloud-starter-netflix-eureka-client") // 动态路由用

    testApi(project(":kuark-test"))
}