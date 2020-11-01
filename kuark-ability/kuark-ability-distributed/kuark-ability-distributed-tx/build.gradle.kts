dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api("io.seata:seata-all")
    api("com.alibaba.cloud:spring-cloud-alibaba-seata")

    testApi(project(":kuark-test"))
    testApi(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
    testApi(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
}