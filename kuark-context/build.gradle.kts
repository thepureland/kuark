
dependencies {
    api(project(":kuark-base"))
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.cloud:spring-cloud-commons")
    api("org.springframework.cloud:spring-cloud-context")
    api("org.springframework.cloud:spring-cloud-config-client")
//    api("com.alibaba.boot:nacos-config-spring-boot-starter")

    testApi(project(":kuark-test"))
}