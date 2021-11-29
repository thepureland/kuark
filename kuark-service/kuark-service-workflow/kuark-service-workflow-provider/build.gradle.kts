dependencies {
    api(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api("org.activiti:activiti-engine")
    api("org.activiti:activiti-spring")
    api("org.activiti:activiti-bpmn-model")
    api("org.activiti:activiti-bpmn-converter")
    api("org.activiti:activiti-json-converter")
    api("org.activiti:activiti-bpmn-layout")
    api("org.activiti:activiti-image-generator")
//    api("org.activiti.cloud:activiti-cloud-services-api")
    api("org.activiti:activiti-spring-boot-starter")
    api("org.apache.xmlgraphics:batik-all")
    api("org.springframework.boot:spring-boot-starter-security")
    api(project(":kuark-service:kuark-service-workflow:kuark-service-workflow-common"))
    api(project(":kuark-service:kuark-service-sys:kuark-service-sys-common"))

    testImplementation(project(":kuark-test:kuark-test-common"))
}