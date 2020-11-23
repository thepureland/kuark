package io.kuark.server.seata

/**
 * 启动Seata Server
 *
 * @author K
 * @since 1.0.0
 */
class SeataServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            io.seata.server.Server.main(args)
        }
    }

}