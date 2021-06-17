dependencies {
    api(project(":kuark-base"))
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-test")
    api("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    implementation("javax.servlet:javax.servlet-api:4.0.1") //TODO provider
}