dependencies {
    implementation(project(":kuark-data:kuark-data-jdbc"))
    implementation(project(":kuark-ui:kuark-ui-jfx"))
    implementation("org.freemarker:freemarker")
}

javafx {
    version = "11"
    modules("javafx.controls", "javafx.fxml")
    configuration = "compileOnly"
}