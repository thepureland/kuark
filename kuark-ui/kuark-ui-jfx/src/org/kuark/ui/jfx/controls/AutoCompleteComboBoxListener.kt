package org.kuark.ui.jfx.controls

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class AutoCompleteComboBoxListener<T>(private val comboBox: ComboBox<Any>?) : EventHandler<KeyEvent> {
    private val sb: StringBuilder
    private val data: ObservableList<T>
    private var moveCaretToPos = false
    private var caretPos = 0
    override fun handle(event: KeyEvent) {
        if (event.code == KeyCode.UP) {
            caretPos = -1
            moveCaret(comboBox!!.editor.text.length)
            return
        } else if (event.code == KeyCode.DOWN) {
            if (!comboBox!!.isShowing) {
                comboBox.show()
            }
            caretPos = -1
            moveCaret(comboBox.editor.text.length)
            return
        } else if (event.code == KeyCode.BACK_SPACE) {
            moveCaretToPos = true
            caretPos = comboBox!!.editor.caretPosition
        } else if (event.code == KeyCode.DELETE) {
            moveCaretToPos = true
            caretPos = comboBox!!.editor.caretPosition
        }
        if (event.code == KeyCode.RIGHT || event.code == KeyCode.LEFT || event.isControlDown || event.code == KeyCode.HOME || event.code == KeyCode.END || event.code == KeyCode.TAB) {
            return
        }
        val list = FXCollections.observableArrayList<Any>()
        for (i in data.indices) {
            if (comboBox != null) {
                if (data[i].toString().toLowerCase().startsWith(
                        comboBox.editor.text.toLowerCase()
                    )
                ) {
                    list.add(data[i])
                }
            }
        }
        val t = comboBox!!.editor.text
        comboBox.items = list
        comboBox.editor.text = t
        if (!moveCaretToPos) {
            caretPos = -1
        }
        moveCaret(t.length)
        if (!list.isEmpty()) {
            comboBox.show()
        }
    }

    private fun moveCaret(textLength: Int) {
        if (caretPos == -1) {
            comboBox!!.editor.positionCaret(textLength)
        } else {
            comboBox!!.editor.positionCaret(caretPos)
        }
        moveCaretToPos = false
    }

    init {
        sb = StringBuilder()
        data = comboBox!!.items as ObservableList<T>
        comboBox.isEditable = true
        comboBox.onKeyPressed = EventHandler { comboBox.hide() }
        comboBox.onKeyReleased = this@AutoCompleteComboBoxListener
    }
}