dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api(project(":kuark-ability:kuark-ability-cache"))
//    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-webflux"))

    api("org.springframework.boot:spring-boot-starter-security")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.authzserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.resourceserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client")

    api("javax.servlet:javax.servlet-api:4.0.1") //TODO provider

    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api(project(":kuark-service:kuark-service-user:kuark-service-user-common"))
    api(project(":kuark-service:kuark-service-sys:kuark-service-sys-common"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}