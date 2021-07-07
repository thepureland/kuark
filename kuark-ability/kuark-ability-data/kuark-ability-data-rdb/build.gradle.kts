dependencies {
    api(project(":kuark-context"))
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.ktorm:ktorm-core")
    api("org.ktorm:ktorm-jackson")

    // h2
    // h2可以用PostgreSqlDialect来实现分页
    implementation("com.h2database:h2:1.4.200")

    // postgres
    implementation("org.postgresql:postgresql:42.2.20")
    implementation("org.ktorm:ktorm-support-postgresql")

    // mysql
//    implementation("mysql:mysql-connector-java:8.0.25")
//    implementation("org.ktorm:ktorm-support-mysql")

    // sqlite
//    implementation("org.ktorm:ktorm-support-sqlite")
//    implementation("org.xerial:sqlite-jdbc:3.30.1")

    // oracle
//    implementation("com.oracle.database.jdbc:ojdbc10:19.11.0.0")
//    implementation("org.ktorm:ktorm-support-oracle")

    // sqlserver
//    implementation("com.microsoft.sqlserver:mssql-jdbc:9.2.1.jre11")
//    implementation("org.ktorm:ktorm-support-sqlserver")


    testImplementation(project(":kuark-test:kuark-test-common"))
}
