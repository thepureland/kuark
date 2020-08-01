
dependencies {
    api(project(":kuark-context"))
    api("javax.validation:validation-api")
    api("org.hibernate.validator:hibernate-validator")
    api("com.alibaba:fastjson")
    api("javax.el:javax.el-api")
    api("org.glassfish.web:javax.el")

    testApi(project(":kuark-test"))
}