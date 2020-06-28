package org.kuark.tools.fx.controls

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.StringConverter
import java.util.stream.Collectors


/**
 * TexFieldTableCell which supports terminating an ongoing edit.
 *
 *
 *
 * There are two parts of the support:
 * - listens to the textField's focusProperty and commits on loosing. This
 * handles the case when the focus is moved to something outside the table
 * - installs a custom skin with a behaviour that tries to terminate the
 * edit (vs. cancelling it)
 * - listens to the table's terminatingCell property and commits if the new
 * property matches this cell's position (requires the table to be
 * of type XTableView)
 *
 * @author K
 */
class XTextFieldTableCell<S, T> @JvmOverloads constructor(converter: StringConverter<T?>? = null) :
    TextFieldTableCell<S?, T?>(converter) {
    /** local copy of the textfield that's installed by super.  */
    private var myTextField: TextField? = null

    /** the changeListener for the table's terminatingCell property  */
    private val terminatingListener =
        ChangeListener { e: ObservableValue<out TablePosition<S?, *>?>?, oldPosition: TablePosition<S?, *>?,
                         newPosition: TablePosition<S?, *>? ->
            terminateEdit(
                newPosition
            )
        }

    /**
     * {@inheritDoc}
     *
     *
     *
     * Overridden to lookup the textField created by super and register a listener
     * with its focusedProperty.
     *
     * TBD: cleanup, probably needs WeakListener
     */
    override fun startEdit() {
        super.startEdit()
        if (isEditing && myTextField == null) {
            myTextField = findTextField()
            if (myTextField == null) {
                // something unexpected happened ...
                // either throw an exception or return silently
                return
            }
            myTextField!!.focusedProperty().addListener { e, old, nvalue ->
                if (!nvalue) {
                    commitEdit(converter.fromString(myTextField!!.text))
                    System.out.println(myTextField!!.text)
                    println("commit")
                }
            }
        }
    }

    /**
     * @param newPosition
     * @return
     */
    protected fun terminateEdit(newPosition: TablePosition<S?, *>?) {
        if (!isEditing || !match(newPosition)) return
        commitEdit()
    }

    protected fun commitEdit() {
        val edited: T? = converter.fromString(myTextField!!.text)
        commitEdit(edited)
    }

    /**
     * c&p of super (WTF is that method private?)
     *
     * @param pos a TablePosition to check for matching
     * @return true if the given position matches this cell, false otherwise.
     */
    protected fun match(pos: TablePosition<S?, *>?): Boolean {
        return pos != null && pos.row == index && pos.tableColumn === tableColumn
    }

    /**
     * Lookup and returns the textField installed by super.
     *
     * @return
     */
    protected fun findTextField(): TextField? {
        if (graphic is TextField) {
            // no "real" graphic so the textfield _is_ the graphic
            return graphic as TextField
        }
        // TBD: untested!
        // has a "real" graphic, the graphic property is a pane which
        // contains the textfield
        val nodes = lookupAll(".text-field")
        // sane use-case: it's only one field
        if (nodes.size == 1) {
            return nodes.toTypedArray()[0] as TextField
        }
        // corner case: there's a "real" graphic which is/contains
        // a textfield, differentiate by text
        val expectedText = converter.toString(item)
        val fields = nodes.stream()
            .filter { field: Node? -> field is TextField }
            .filter { field: Node -> expectedText == (field as TextInputControl).text }
            .collect(Collectors.toList())
        return if (fields.size == 1) fields[0] as TextField else null
    }

    /**
     * @param newTable
     */
    private fun installTerminatingListener(newTable: TableView<S?>) {
        if (newTable is XTableView<*>) {
            (newTable as XTableView<S?>).terminatingCellProperty().addListener(terminatingListener)
        }
    }

    /**
     * @param oldTable
     */
    private fun uninstallTerminatingListener(oldTable: TableView<S?>) {
        if (oldTable is XTableView<*>) {
            (oldTable.terminatingCellProperty() as ObservableValue<*>).removeListener(terminatingListener as ChangeListener<in Any>)
        }
    }

    /**
     * Implemented to listen to the tableViewProperty and un-/wire the terminatingListener
     * as appropriate.
     *
     * @param converter
     */
    init {
        tableViewProperty().addListener { e: ObservableValue<out TableView<S?>>?, oldTable: TableView<S?>, newTable: TableView<S?> ->
            uninstallTerminatingListener(oldTable)
            installTerminatingListener(newTable)
        }
    }
}