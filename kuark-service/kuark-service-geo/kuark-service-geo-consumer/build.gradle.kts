dependencies {
    api(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api(project(":kuark-service:kuark-service-geo:kuark-service-geo-common"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}