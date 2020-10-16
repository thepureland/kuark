package io.kuark.tools.codegen.model.vo

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
    private val customComment: StringProperty = SimpleStringProperty()
    private val searchable: BooleanProperty = SimpleBooleanProperty()
    private val orderInList: IntegerProperty = SimpleIntegerProperty()
    private val sortable: BooleanProperty = SimpleBooleanProperty()
    private val defaultOrder: StringProperty = SimpleStringProperty()
    private val orderInEdit: IntegerProperty = SimpleIntegerProperty()
    private val orderInView: IntegerProperty = SimpleIntegerProperty()

    fun getName(): String? = name

    fun setName(name: String?) {
        this.name = name
    }

    fun getSearchable(): Boolean = searchable.get()

    fun searchableProperty(): BooleanProperty = searchable

    fun setSearchable(searchable: Boolean) = this.searchable.set(searchable)

    fun getOrderInList(): Int = orderInList.get()

    fun orderInListProperty(): IntegerProperty = orderInList

    fun setOrderInList(orderInList: Int) = this.orderInList.set(orderInList)

    fun getSortable(): Boolean = sortable.get()

    fun sortableProperty(): BooleanProperty = sortable

    fun setSortable(sortable: Boolean) = this.sortable.set(sortable)

    fun getDefaultOrder(): String? = defaultOrder.get()

    fun defaultOrderProperty(): StringProperty = defaultOrder

    fun setDefaultOrder(defaultOrder: String?) = this.defaultOrder.set(defaultOrder)

    fun getOrderInEdit(): Int = orderInEdit.get()

    fun orderInEditProperty(): IntegerProperty = orderInEdit

    fun setOrderInEdit(orderInEdit: Int) = this.orderInEdit.set(orderInEdit)

    fun getOrderInView(): Int = orderInView.get()

    fun orderInViewProperty(): IntegerProperty = orderInView

    fun setOrderInView(orderInView: Int) = this.orderInView.set(orderInView)

    fun getCustomComment(): String? = customComment.get()

    fun customCommentProperty(): StringProperty = customComment

    fun setCustomComment(customComment: String?) = this.customComment.set(customComment)

    fun getOrigComment(): String? = origComment

    fun setOrigComment(origComment: String?) {
        this.origComment = origComment
    }

    fun getComment(): String? =
        if (getCustomComment() != null && getCustomComment()!!.isNotBlank()) getCustomComment() else origComment

    fun getColumn(): String? = name
}