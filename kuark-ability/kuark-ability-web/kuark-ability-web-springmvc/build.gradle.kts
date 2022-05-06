
dependencies {
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-common"))
    api(project(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-session"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("commons-fileupload:commons-fileupload")

    testImplementation(project(":kuark-test:kuark-test-common"))
}
