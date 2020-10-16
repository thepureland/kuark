package io.kuark.service.msg.provider.sys.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.service.provider.dao.SysDictDao
import io.kuark.service.provider.dao.SysDictItemDao
import io.kuark.service.provider.sys.ibiz.ISysDictItemBiz
import io.kuark.service.provider.sys.model.po.SysDictItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 字典子表业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
@CacheConfig(cacheNames = [CacheNames.SYS_DICT_ITEM])
open class SysDictItemBiz : ISysDictItemBiz {
//endregion your codes 1

    @Autowired
    private lateinit var sysDictItemDao: SysDictItemDao

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    //region yur codes 2

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return 字典项列表。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     */
    @Cacheable(key = "#module.concat(':').concat(#type)", unless = "#result.isEmpty()")
    override fun getItemsByModuleAndType(module: String, type: String): List<SysDictItem> {
        // 查出对应的dict id
        val ids = sysDictDao.searchIdsByModuleAndType(module, type)

        return if (ids.isEmpty()) {
            listOf()
        } else {
            // 查出dict id的所有字典项
            sysDictItemDao.searchByIds(ids)
        }
    }


    //endregion your codes 2

}