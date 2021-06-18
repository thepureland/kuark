dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api(project(":kuark-ability:kuark-ability-cache"))
    api("org.apache.shiro:shiro-spring")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.authzserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.resourceserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client")

    testImplementation(project(":kuark-test:kuark-test-common"))
}