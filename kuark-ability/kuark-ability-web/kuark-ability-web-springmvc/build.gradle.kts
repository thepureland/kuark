
dependencies {
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-common"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("commons-fileupload:commons-fileupload")

    testImplementation(project(":kuark-test:kuark-test-common"))
}
