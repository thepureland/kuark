
dependencies {
//    // kotlin
//    api("org.jetbrains.kotlin:kotlin-reflect")
//    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api("org.apache.commons:commons-lang3")
    api("org.apache.commons:commons-text")
    api("commons-io:commons-io")
    api("commons-codec:commons-codec")
    api("commons-beanutils:commons-beanutils")
    api("de.idyl:winzipaes")
    api("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
    api("net.sourceforge.jexcelapi:jxl")
    api("javax.xml.bind:jaxb-api")
    api("com.sun.xml.bind:jaxb-impl")
    api("org.glassfish.jaxb:jaxb-runtime")
    api("com.google.zxing:core")
    api("org.javamoney:moneta")
    api("xerces:xercesImpl")
    api("org.apache.xmlgraphics:batik-all")

    // log
    api("org.slf4j:slf4j-api")
    api("org.slf4j:jcl-over-slf4j")
    api("org.slf4j:log4j-over-slf4j")
    api("ch.qos.logback:logback-classic")

    // validation
    api("javax.validation:validation-api")
    api("org.hibernate.validator:hibernate-validator")
    api("com.alibaba:fastjson")
    api("javax.el:javax.el-api")
    api("org.glassfish.web:javax.el")

    testImplementation(project(":kuark-test:kuark-test-common"))
}