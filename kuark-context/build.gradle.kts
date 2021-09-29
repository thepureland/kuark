
dependencies {
    api(project(":kuark-base"))
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.session:spring-session-core")
    api("org.springframework.cloud:spring-cloud-commons")
    api("org.springframework.cloud:spring-cloud-context")
    api("org.springframework.cloud:spring-cloud-starter-bootstrap")
    api("org.springframework.cloud:spring-cloud-config-client")
//    api("com.alibaba.boot:nacos-config-spring-boot-starter")
//    api("com.alibaba.nacos:nacos-spring-context")
//    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    testImplementation(project(":kuark-test:kuark-test-common"))
}