dependencies {
    api(project(":kuark-service:kuark-service-sys:kuark-service-sys-provider"))
    api(project(":kuark-service:kuark-service-auth:kuark-service-auth-provider"))
    api(project(":kuark-service:kuark-service-workflow:kuark-service-workflow-provider"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}