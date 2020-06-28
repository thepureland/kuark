package org.kuark.tools.fx.controls

import com.sun.javafx.scene.control.behavior.TableCellBehavior
import javafx.scene.control.TableCell
import javafx.scene.input.MouseButton
import java.util.logging.Logger

/**
 * Trying to intercept the selection process to not cancel an edit.
 *
 * Issues:
 * - the control of editing is centered completely inside the cell itself
 * - there's no way to get hold of the actual editing cell, it's only the
 * TablePosition of the cell that's available
 * - on mousePressed (actually simpleSelect) this behaviour is playing
 * squash: the table is the wall, the target is the editing fellow cell
 * whose edit is rudely cancelled
 * - with the current api, this behaviour can't do much better: table's
 * api is too weak, one method only
 * - hack here: add api to tableView that supports the notion of
 * terminate an edit and use it (note: needs cooperation of all cells
 * in the table)
 * - astonished: _why_ the need to cancel (or otherwise end an edit anywhere
 * outside this cell) - shouldn't the selection/focus update automagically
 * trigger the other cell to update its editing?
 *
 * Didn't compile in jdk8_u20 (joy of hacking ;-) :
 * - simpleSelect method signature changed
 * - edit handling at the end of former simpleSelect was extracted
 * to handleClicks (method in CellBehaviour, probably good move for
 * consistency across across cell containers)
 * - so now we override the latter and try terminate edits
 *
 *
 * @author K
 */
open class XTableCellBehavior<S, T>
/**
 * @param control
 */
    (control: TableCell<S, T>?) : TableCellBehavior<S, T>(control) {
    /**
     * This method is called in jdk8_u5. Signature changed
     * in jdk8_u20.
     *
     * @param e
     */
    //    @Override
    //    protected void simpleSelect(MouseEvent e) {
    //        tryTerminateEdit();
    //        super.simpleSelect(e);
    //    }
    /**
     * Tries to terminate edits if containing table is of type
     * XTableView.
     */
    private fun tryTerminateEdit() {
//        val cell = control
//        val table = cell.tableColumn.tableView
        if (cellContainer is XTableView<*>) {
            (cellContainer as XTableView<S>).terminateEdit()
        }
    }

    /**
     * This method is introduced in jdk8_u20. It's the editing
     * handling part of the former simpleSelect.
     *
     * Overridden to try terminating the edit before calling
     * super.
     *
     */
    override fun handleClicks(
        button: MouseButton, clickCount: Int,
        isAlreadySelected: Boolean
    ) {
        tryTerminateEdit()
        super.handleClicks(button, clickCount, isAlreadySelected)
    }

    companion object {
        private val LOG = Logger.getLogger(
            XTableCellBehavior::class.java
                .name
        )
    }
}