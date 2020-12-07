dependencies {
    api(project(":kuark-context"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")

    testApi(project(":kuark-test"))
}