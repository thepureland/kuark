package io.kuark.base.tree

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

/**
 * 树结点状态
 *
 * @author K
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class TreeNodeState : Serializable {

    private var loaded: Boolean? = null
    private var opened: Boolean? = null
    private var selected: Boolean? = null
    private var disabled: Boolean? = null

    constructor() {}

    constructor(selected: Boolean?) {
        this.selected = selected
    }

    constructor(selected: Boolean?, opened: Boolean?) {
        this.selected = selected
        this.opened = opened
    }

    fun getLoaded(): Boolean? = loaded

    fun setLoaded(loaded: Boolean?) {
        this.loaded = loaded
    }

    fun getOpened(): Boolean? = opened

    fun setOpened(opened: Boolean?) {
        this.opened = opened
    }

    fun getSelected(): Boolean? = selected

    fun setSelected(selected: Boolean?) {
        this.selected = selected
    }

    fun getDisabled(): Boolean? = disabled

    fun setDisabled(disabled: Boolean?) {
        this.disabled = disabled
    }

    companion object {
        private const val serialVersionUID = 4133941367895177876L
    }
}