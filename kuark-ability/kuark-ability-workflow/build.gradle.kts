dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api("org.activiti:activiti-engine")
    api("org.activiti:activiti-spring")
    api("org.activiti:activiti-bpmn-model")
    api("org.activiti:activiti-bpmn-converter")
    api("org.activiti:activiti-json-converter")
    api("org.activiti:activiti-bpmn-layout")
    api("org.activiti:activiti-image-generator")
//    api("org.activiti.cloud:activiti-cloud-services-api")
    api("org.activiti:activiti-spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-security")

    testImplementation(project(":kuark-test"))
}