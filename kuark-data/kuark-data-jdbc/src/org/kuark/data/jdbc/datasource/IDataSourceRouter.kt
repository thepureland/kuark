package org.kuark.data.jdbc.datasource

interface IDataSourceRouter {

    fun determineDataSourceId(): String?

}