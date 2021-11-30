dependencies {
    implementation(project(":kuark-base"))
    api("org.controlsfx:controlsfx")
//    api("de.roskenet:springboot-javafx-support")
}

javafx {
    version = "11"
    modules("javafx.controls", "javafx.fxml")
//    configuration = "compileOnly"
}