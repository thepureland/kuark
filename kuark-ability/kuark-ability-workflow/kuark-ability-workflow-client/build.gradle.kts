dependencies {
    api(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-webflux"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api(project(":kuark-ability:kuark-ability-workflow:kuark-ability-workflow-common"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}