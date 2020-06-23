
dependencies {
//    // kotlin
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-text")
    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
    implementation("commons-beanutils:commons-beanutils")
    implementation("de.idyl:winzipaes")
    implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
    implementation("net.sourceforge.jexcelapi:jxl")
    implementation("dom4j:dom4j")
    implementation("javax.xml.bind:jaxb-api")
    implementation("com.sun.xml.bind:jaxb-impl")
    implementation("org.glassfish.jaxb:jaxb-runtime")

    // log
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:jcl-over-slf4j")
    implementation("org.slf4j:log4j-over-slf4j")
    implementation("ch.qos.logback:logback-classic")

    testApi("org.springframework.boot:spring-boot-starter-test")
}