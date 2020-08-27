
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
    implementation("javax.xml.bind:jaxb-api")
    implementation("com.sun.xml.bind:jaxb-impl")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("com.google.zxing:core")
    implementation("org.javamoney:moneta")

    // log
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:jcl-over-slf4j")
    implementation("org.slf4j:log4j-over-slf4j")
    implementation("ch.qos.logback:logback-classic")

    // validation
    api("javax.validation:validation-api")
    api("org.hibernate.validator:hibernate-validator")
    api("com.alibaba:fastjson")
    api("javax.el:javax.el-api")
    api("org.glassfish.web:javax.el")

    testApi(project(":kuark-test"))
}