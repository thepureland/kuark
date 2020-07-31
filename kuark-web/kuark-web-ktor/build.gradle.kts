
dependencies {
    api(project(":kuark-web"))
    api(project(":kuark-cache"))
    api("io.ktor:ktor-server-sessions")
    api("io.ktor:ktor-server-netty")
    api("io.ktor:ktor-server-core")
    api("io.ktor:ktor-html-builder")
    api("org.jetbrains:kotlin-css-jvm")
    api("io.ktor:ktor-client-core")
    api("io.ktor:ktor-client-core-jvm")
    api("io.ktor:ktor-client-apache")
    api("io.ktor:ktor-server-tests")

    testApi(project(":kuark-test"))
}
