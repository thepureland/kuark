
dependencies {
    api(project(":kuark-config"))
    api("javax.validation:validation-api")
    api("org.hibernate.validator:hibernate-validator")
    api("com.alibaba:fastjson")

    testApi(project(":kuark-test"))
}