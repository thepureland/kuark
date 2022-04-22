package org.kuark.demo.tools.codegen.model.vo

import io.kuark.base.lang.string.StringKit
import javafx.beans.property.*

/**
 * 列信息值对象
 *
 * @author K
 * @since 1.0.0
 */
class ColumnInfo {

    private var name: String? = null
    private var origComment: String? = null
    private val customComment = SimpleStringProperty()
    private val searchItem = SimpleBooleanProperty()
    private val listItem = SimpleBooleanProperty()
    private val editItem = SimpleBooleanProperty()
    private val detailItem = SimpleBooleanProperty()
    private val cacheItem = SimpleBooleanProperty()

    fun getName(): String? = name

    fun setName(name: String?) {
        this.name = name
    }

    fun getSearchItem(): Boolean = searchItem.get()

    fun searchItemProperty(): BooleanProperty = searchItem

    fun setSearchItem(searchItem: Boolean) = this.searchItem.set(searchItem)

    fun getListItem(): Boolean = listItem.get()

    fun listItemProperty(): BooleanProperty = listItem

    fun setListItem(listItem: Boolean) = this.listItem.set(listItem)

    fun getEditItem(): Boolean = editItem.get()

    fun editItemProperty(): BooleanProperty = editItem

    fun setEditItem(editItem: Boolean) = this.editItem.set(editItem)

    fun getDetailItem(): Boolean = detailItem.get()

    fun detailItemProperty(): BooleanProperty = detailItem

    fun setDetailItem(detailItem: Boolean) = this.detailItem.set(detailItem)

    fun getCacheItem(): Boolean = cacheItem.get()

    fun cacheItemProperty(): BooleanProperty = cacheItem

    fun setCacheItem(cacheItem: Boolean) = this.cacheItem.set(cacheItem)

    fun getCustomComment(): String? = customComment.get()

    fun customCommentProperty(): StringProperty = customComment

    fun setCustomComment(customComment: String?) = this.customComment.set(customComment)

    fun getOrigComment(): String? = origComment

    fun setOrigComment(origComment: String?) {
        this.origComment = origComment
    }

    fun getComment(): String? =
        if (StringKit.isNotBlank(getCustomComment())) getCustomComment() else origComment

    fun getColumn(): String? = name
}