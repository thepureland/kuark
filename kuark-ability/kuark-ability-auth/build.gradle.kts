dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api(project(":kuark-ability:kuark-ability-cache"))
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.authzserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.resourceserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client")

    api("javax.servlet:javax.servlet-api:4.0.1") //TODO provider

    testImplementation(project(":kuark-test:kuark-test-common"))
}