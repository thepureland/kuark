dependencies {
    api(project(":kuark-data"))
    api("javax.cache:cache-api")
    api("com.github.ben-manes.caffeine:caffeine")
    api("redis.clients:jedis")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-cache")
}