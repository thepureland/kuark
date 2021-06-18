dependencies {
    api(project(":kuark-context"))
    api("org.drools:drools-compiler")
    api("org.drools:drools-mvel")

    testImplementation(project(":kuark-test:kuark-test-common"))
}
