
dependencies {
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-common"))
    api("io.ktor:ktor-server-sessions")
    api("io.ktor:ktor-server-netty")
    api("io.ktor:ktor-server-core")
    api("io.ktor:ktor-html-builder")
//    api("org.jetbrains:kotlin-css-jvm")
    api("io.ktor:ktor-client-core")
    api("io.ktor:ktor-client-core-jvm")
    api("io.ktor:ktor-client-apache")
    api("io.ktor:ktor-server-tests")

    testImplementation(project(":kuark-test:kuark-test-common"))
}
