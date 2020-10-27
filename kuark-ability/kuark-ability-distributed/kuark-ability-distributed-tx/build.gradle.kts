dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api("io.seata:seata-all")

    testApi(project(":kuark-test"))
    testApi(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
}