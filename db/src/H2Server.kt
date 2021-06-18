/**
 * 启动h2数据据及其web控制台
 *
 * 连接信息：
 * url: jdbc:h2:tcp://localhost:9092/./db/h2;DATABASE_TO_LOWER=TRUE
 * user name: sa
 * password: (放空)
 *
 * @author K
 * @since 1.0.0
 */
fun main() {
    org.h2.tools.Console.main()
}