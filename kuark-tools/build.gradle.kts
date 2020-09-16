dependencies {
    implementation(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    implementation(project(":kuark-ui:kuark-ui-jfx"))
    implementation("org.freemarker:freemarker")
    testApi(project(":kuark-test"))
}

javafx {
    version = "11"
    modules("javafx.controls", "javafx.fxml")
//    configuration = "compileOnly"
}