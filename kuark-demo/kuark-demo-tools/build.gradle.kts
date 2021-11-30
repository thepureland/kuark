dependencies {
    implementation(project(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb"))
    implementation(project(":kuark-ability::kuark-ability-ui::kuark-ability-ui-jfx"))
    implementation("org.freemarker:freemarker")

    testImplementation(project(":kuark-test:kuark-test-common"))
}

javafx {
    version = "11"
    modules("javafx.controls", "javafx.fxml")
//    configuration = "compileOnly"
}