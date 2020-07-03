package org.kuark.tools.codegen.vo

import javafx.beans.property.*

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

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getSearchable(): Boolean {
        return searchable.get()
    }

    fun searchableProperty(): BooleanProperty {
        return searchable
    }

    fun setSearchable(searchable: Boolean) {
        this.searchable.set(searchable)
    }

    fun getOrderInList(): Int {
        return orderInList.get()
    }

    fun orderInListProperty(): IntegerProperty {
        return orderInList
    }

    fun setOrderInList(orderInList: Int) {
        this.orderInList.set(orderInList)
    }

    fun getSortable(): Boolean {
        return sortable.get()
    }

    fun sortableProperty(): BooleanProperty {
        return sortable
    }

    fun setSortable(sortable: Boolean) {
        this.sortable.set(sortable)
    }

    fun getDefaultOrder(): String? {
        return defaultOrder.get()
    }

    fun defaultOrderProperty(): StringProperty {
        return defaultOrder
    }

    fun setDefaultOrder(defaultOrder: String?) {
        this.defaultOrder.set(defaultOrder)
    }

    fun getOrderInEdit(): Int {
        return orderInEdit.get()
    }

    fun orderInEditProperty(): IntegerProperty {
        return orderInEdit
    }

    fun setOrderInEdit(orderInEdit: Int) {
        this.orderInEdit.set(orderInEdit)
    }

    fun getOrderInView(): Int {
        return orderInView.get()
    }

    fun orderInViewProperty(): IntegerProperty {
        return orderInView
    }

    fun setOrderInView(orderInView: Int) {
        this.orderInView.set(orderInView)
    }

    fun getCustomComment(): String? {
        return customComment.get()
    }

    fun customCommentProperty(): StringProperty {
        return customComment
    }

    fun setCustomComment(customComment: String?) {
        this.customComment.set(customComment)
    }

    fun getOrigComment(): String? {
        return origComment
    }

    fun setOrigComment(origComment: String?) {
        this.origComment = origComment
    }

    fun getComment(): String? {
        return if (getCustomComment()!=null && getCustomComment()!!.isNotBlank()) getCustomComment() else origComment
    }

    fun getColumn(): String? {
        return name
    }
}