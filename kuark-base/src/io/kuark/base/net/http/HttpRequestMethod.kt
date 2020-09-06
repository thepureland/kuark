package io.kuark.base.net.http

import io.kuark.base.support.enums.IDictEnum


/**
 * http 1.1 请求方法
 *
 * @author admin
 * @time 8/24/15 10:07 AM
 */
enum class HttpRequestMethod {
    /**
     * 请求指定的页面信息，并返回实体主体
     */
    GET,

    /**
     * 只请求页面的首部。
     */
    HEAD,

    /**
     * 请求服务器接受所指定的文档作为对所标识的URI的新的从属实体
     */
    POST,

    /**
     * 从客户端向服务器传送的数据取代指定的文档的内容
     */
    PUT,

    /**
     * 请求服务器删除指定的页面
     */
    DELETE,

    /**
     * 允许客户端查看服务器的性能
     */
    OPTIONS,

    /**
     * 请求服务器在响应中的实体主体部分返回所得到的内容
     */
    TRACE,

    /**
     * 实体中包含一个表，表中说明与该URI所表示的原内容的区别
     */
    PATCH,

    /**
     * 请求服务器将指定的页面移至另一个网络地址
     */
    MOVE,

    /**
     * 请求服务器将指定的页面拷贝至另一个网络地址
     */
    COPY,

    /**
     * 请求服务器建立链接关系
     */
    LINK,

    /**
     * 断开链接关系
     */
    UNLINK,

    /**
     * 允许客户端发送经过封装的请求
     */
    WRAPPED,

    /**
     * 在不改动协议的前提下，可增加另外的方法
     */
    EXTENSION_MOTHED


}