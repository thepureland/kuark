dependencies {
    api(project(":kuark-data:kuark-data-redis"))
    api("javax.cache:cache-api")
    api("com.github.ben-manes.caffeine:caffeine")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-cache")
    api("com.alibaba:fastjson")
    testApi("org.springframework.boot:spring-boot-starter-test")
}