dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api("io.seata:seata-all")
    api("com.alibaba.cloud:spring-cloud-alibaba-seata")

    testImplementation(project(":kuark-test:kuark-test-common"))
    testImplementation(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
    testImplementation(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
}