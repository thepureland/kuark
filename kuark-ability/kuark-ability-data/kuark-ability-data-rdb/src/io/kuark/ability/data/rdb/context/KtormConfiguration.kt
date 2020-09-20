package io.kuark.ability.data.rdb.context

import com.fasterxml.jackson.databind.Module
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.jackson.KtormModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * ktorm sprintboot配置类
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
//@EnableConfigurationProperties
open class KtormConfiguration {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    open fun database(): Database {
        return Database.connectWithSpringSupport(dataSource)
    }

    @Bean
    open fun ktormModule(): Module {
        return KtormModule()
    }

//    @Bean("txManager")
//    fun txManager(dataSource: DataSource): DataSourceTransactionManager {
//        return DataSourceTransactionManager(dataSource)
//    }

}