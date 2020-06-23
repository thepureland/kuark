package org.kuark.tools.fx.controls

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.Cell
import javafx.scene.control.ContentDisplay
import javafx.scene.control.TableCell
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.util.StringConverter

/**
 * Create by (admin) on 6/30/15.
 */
class TextFieldCell<S, T> @JvmOverloads constructor(private val sc: StringConverter<Any>? = null) : TableCell<S, T>() {
    private val textField: TextField
    private val boundToCurrently: Property<T>? = null
    override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        //        updateItem(this, sc, null, null, textField);
        if (!empty) {
            // Show the Text Field
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY)

//            // Retrieve the actual String Property that should be bound to the TextField
//            // If the TextField is currently bound to a different StringProperty
//            // Unbind the old property and rebind to the new one
//            Property<T> sp = (Property<T>) getTableColumn().getCellObservableValue(getIndex());
////            SimpleStringProperty sp = (SimpleStringProperty) ov;
//
//            if (this.boundToCurrently == null) {
//                this.boundToCurrently = sp;
//                this.textField.textProperty().bindBidirectional((Property<String>) sp);
//            } else {
//                if (this.boundToCurrently != sp) {
//                    this.textField.textProperty().unbindBidirectional(this.boundToCurrently);
//                    this.boundToCurrently = sp;
//                    this.textField.textProperty().bindBidirectional((Property<String>) this.boundToCurrently);
//                }
//            }
//            System.out.println("item=" + item + " ObservableValue<String>=" + ov.getValue());
            //this.textField.setText(item);  // No longer need this!!!
            textField.text = if (sc == null) item as String else sc.toString(item)
        } else {
            setContentDisplay(ContentDisplay.TEXT_ONLY)
        }
    }

    companion object {
        fun <T> updateItem(
            cell: Cell<T?>,
            converter: StringConverter<T?>?,
            hbox: HBox,
            graphic: Node?,
            textField: TextField?
        ) {
            if (cell.isEmpty) {
                cell.text = null
                cell.setGraphic(null)
            } else {
                if (cell.isEditing) {
                    if (textField != null) {
                        textField.text =
                            getItemText(
                                cell,
                                converter
                            )
                    }
                    cell.text = null
                    if (graphic != null) {
                        hbox.children.setAll(graphic, textField)
                        cell.setGraphic(hbox)
                    } else {
                        cell.setGraphic(textField)
                    }
                } else {
                    cell.text = getItemText(
                        cell,
                        converter
                    )
                    cell.setGraphic(graphic)
                }
            }
        }

        private fun <T> getItemText(cell: Cell<T?>, converter: StringConverter<T?>?): String {
            return if (converter == null) if (cell.item == null) "" else cell.item.toString() else converter.toString(
                cell.item
            )
        }
    }

    init {
        var strCss: String
        // Padding in Text field cell is not wanted - we want the Textfield itself to "be"
        // The cell.  Though, this is aesthetic only.  to each his own.  comment out
        // to revert back.
        strCss = "-fx-padding: 0;"
        style = strCss
        textField = TextField()

        //
        // Default style pulled from caspian.css. Used to play around with the inset background colors
        // ---trying to produce a text box without borders
        strCss =
            "" +  //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                    "-fx-background-color: -fx-control-inner-background;" +  //"-fx-background-insets: 0, 1, 2;" +
                    "-fx-background-insets: 0;" +  //"-fx-background-radius: 3, 2, 2;" +
                    "-fx-background-radius: 0;" +
                    "-fx-padding: 3 5 3 5;" +  /*Play with this value to center the text depending on cell height??*/ //"-fx-padding: 0 0 0 0;" +
                    "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
                    "-fx-cursor: text;" +
                    ""
        // Focused and hover states should be set in the CSS.  This is just a test
        // to see what happens when we set the style in code
        textField.focusedProperty().addListener { observable, oldValue, newValue ->
            val tf = graphic as TextField
            val strStyleGotFocus = "-fx-background-color: purple, -fx-text-box-border, -fx-control-inner-background;" +
                    "-fx-background-insets: -0.4, 1, 2;" +
                    "-fx-background-radius: 3.4, 2, 2;"
            val strStyleLostFocus =
                //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                "-fx-background-color: -fx-control-inner-background;" +  //"-fx-background-insets: 0, 1, 2;" +
                        "-fx-background-insets: 0;" +  //"-fx-background-radius: 3, 2, 2;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 3 5 3 5;" +  /**/ //"-fx-padding: 0 0 0 0;" +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
                        "-fx-cursor: text;" +
                        ""
            if (newValue) {
                tf.style = strStyleGotFocus
            } else {
                tf.style = strStyleLostFocus
            }
        }
        textField.hoverProperty().addListener { observable, oldValue, newValue ->
            val tf = graphic as TextField
            val strStyleGotHover =
                "-fx-background-color: derive(purple,90%), -fx-text-box-border, derive(-fx-control-inner-background, 10%);" +
                        "-fx-background-insets: 1, 2.8, 3.8;" +
                        "-fx-background-radius: 3.4, 2, 2;"
            val strStyleLostHover =
                //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                "-fx-background-color: -fx-control-inner-background;" +  //"-fx-background-insets: 0, 1, 2;" +
                        "-fx-background-insets: 0;" +  //"-fx-background-radius: 3, 2, 2;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 3 5 3 5;" +  /**/ //"-fx-padding: 0 0 0 0;" +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);" +
                        "-fx-cursor: text;" +
                        ""
            val strStyleHasFocus = "-fx-background-color: purple, -fx-text-box-border, -fx-control-inner-background;" +
                    "-fx-background-insets: -0.4, 1, 2;" +
                    "-fx-background-radius: 3.4, 2, 2;"
            if (newValue) {
                tf.style = strStyleGotHover
            } else {
                if (!tf.focusedProperty().get()) {
                    tf.style = strStyleLostHover
                } else {
                    tf.style = strStyleHasFocus
                }
            }
        }
        textField.style = strCss
        graphic = textField
    }
}