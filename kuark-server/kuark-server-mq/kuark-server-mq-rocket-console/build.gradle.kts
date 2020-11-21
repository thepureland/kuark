dependencies {
    api(project(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc"))
    api(project(":kuark-server:kuark-server-mq:kuark-server-mq-rocket"))
    implementation(fileTree("dir" to "libs", "include" to arrayOf("rocketmq-console-ng-2.0.0.jar")))
}