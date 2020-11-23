dependencies {
    api(project(":kuark-base"))
    implementation("com.ieooc.nacos:nacos-istio:1.3.2")
    implementation("cglib:cglib-nodep:2.1_3")
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
}