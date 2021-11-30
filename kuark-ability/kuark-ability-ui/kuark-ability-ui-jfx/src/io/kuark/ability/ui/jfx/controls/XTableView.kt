package io.kuark.ability.ui.jfx.controls

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.scene.control.TablePosition
import javafx.scene.control.TableView

/**
 * Extended TableView that supports terminating an edit.
 *
 * Implemented by a custom property terminatingCell that supporting
 * TableCells can listen to and react as appropriate.
 *
 * Collaborators:
 * - an extended TableCellBehaviour that calls tableView.terminateEdit on
 * simpleSelect before messaging super
 * - an extended TableCell that is configured with the extended TableCellBehaviour
 * and listens to the table's terminatingCell property.
 *
 * Note: all TableCells in this table need the extended behaviour.
 *
 * @author K
 */
class XTableView<S> : TableView<S>() {

    fun terminateEdit() {
        if (!editing) {
            return
        }
        setTerminatingCell(getEditingCell())
        check(!editing) { "expected editing to be terminated but was $editingCell" }
        setTerminatingCell(null)
    }

    /**
     * Returns a boolean indicating whether this table is currently editing.
     *
     * PENDING JW: what's the exact semantics of editingCell? here we check
     * for null, what if != with row < 0 and tableColumn != null?
     *
     * @return
     */
    val editing: Boolean
        get() = editingCell != null

    /**
     * terminatingCell is the table position that is currently editing
     * and should terminate. It is set in edit(row, column) to the currently editing cell
     * before calling super and reset to null after calling super.
     * TableCells that support terminating an edit
     * can listen and commit as appropriate (at least that's the idea).
     */
    private var terminatingCell: ReadOnlyObjectWrapper<TablePosition<S?, *>?>? = null
    protected fun setTerminatingCell(terminatingPosition: TablePosition<S?, *>?) {
        terminatingCellPropertyImpl().set(terminatingPosition)
    }

    /**
     * Represents the current cell being edited, or null if
     * there is no cell being edited.
     */
    fun terminatingCellProperty(): ReadOnlyObjectProperty<TablePosition<S?, *>?> {
        return terminatingCellPropertyImpl().readOnlyProperty
    }

    fun getTerminatingCell(): TablePosition<S?, *>? {
        return terminatingCellPropertyImpl().get()
    }

    private fun terminatingCellPropertyImpl(): ReadOnlyObjectWrapper<TablePosition<S?, *>?> {
        if (terminatingCell == null) {
            terminatingCell = ReadOnlyObjectWrapper(this, "terminatingCell")
        }
        return terminatingCell as ReadOnlyObjectWrapper<TablePosition<S?, *>?>
    }
}