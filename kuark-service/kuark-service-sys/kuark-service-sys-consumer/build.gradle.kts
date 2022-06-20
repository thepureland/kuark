dependencies {
    api(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api(project(":kuark-service:kuark-service-sys:kuark-service-sys-common"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}