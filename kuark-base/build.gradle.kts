
dependencies {
//    // kotlin
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-text")
    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
    implementation("de.idyl:winzipaes")

    // log
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:jcl-over-slf4j")
    implementation("org.slf4j:log4j-over-slf4j")
    implementation("ch.qos.logback:logback-classic")

    testApi("org.springframework.boot:spring-boot-starter-test")
}