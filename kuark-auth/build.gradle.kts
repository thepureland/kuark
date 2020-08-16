dependencies {
    api(project(":kuark-cache"))
    api("org.apache.shiro:shiro-spring")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.authzserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.resourceserver")
    api("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client")

    testApi(project(":kuark-test"))
}