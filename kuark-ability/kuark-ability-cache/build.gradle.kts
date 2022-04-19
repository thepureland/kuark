dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-memdb"))
    api("com.github.ben-manes.caffeine:caffeine")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-cache")
//    api("com.alibaba:fastjson")

//    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}