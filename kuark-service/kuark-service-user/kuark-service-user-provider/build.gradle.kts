dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api(project(":kuark-ability:kuark-ability-cache"))
    api(project(":kuark-ability:kuark-ability-auth"))
//    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-webflux"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}