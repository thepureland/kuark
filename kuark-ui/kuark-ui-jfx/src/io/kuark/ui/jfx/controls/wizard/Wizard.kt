/*
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package io.kuark.ui.jfx.controls.wizard

import io.kuark.base.lang.collections.MapKit
import io.kuark.base.lang.string.StringKit
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.DialogPane
import java.util.*

class Wizard constructor(owner: Any?, title: String = "") {
    /**************************************************************************
     *
     * Static fields
     *
     */
    /**************************************************************************
     *
     * Private fields
     *
     */
    private var dialog: Dialog<ButtonType?>? = null

    // --- settings
    val settings = FXCollections.observableHashMap<String, Any>()
    private val pageHistory = Stack<WizardPane>()
    private var currentPage: Optional<WizardPane> = Optional.empty()

    //    private final ValidationSupport validationSupport = new ValidationSupport();
    //
    private val BUTTON_PREVIOUS = ButtonType("上一步", ButtonData.BACK_PREVIOUS)
    private val BUTTON_PREVIOUS_ACTION_HANDLER = EventHandler { actionEvent: ActionEvent ->
        actionEvent.consume()
        currentPage = Optional.ofNullable(if (pageHistory.isEmpty()) null else pageHistory.pop())
        updatePage(dialog, false)
    }
    private val BUTTON_NEXT = ButtonType("下一步", ButtonData.NEXT_FORWARD)
    private val BUTTON_NEXT_ACTION_HANDLER = EventHandler { actionEvent: ActionEvent ->
        actionEvent.consume()
        currentPage.ifPresent { page: WizardPane -> pageHistory.push(page) }
        currentPage = getFlow().advance(currentPage.orElse(null))
        updatePage(dialog, true)
    }
    /**************************************************************************
     *
     * Constructors
     *
     */
    /**
     *
     */
    constructor() : this(null)

    /**************************************************************************
     *
     * Public API
     *
     */
    fun show() {
        dialog!!.show()
    }

    fun showAndWait(): Optional<ButtonType?> {
        return dialog!!.showAndWait()
    }

    /**************************************************************************
     *
     * Properties
     *
     */
    // --- flow
    private val flow = object : SimpleObjectProperty<Flow>(
        LinearWizardFlow()
    ) {
        override fun invalidated() {
            updatePage(dialog, false)
        }

        override fun set(flow: Flow) {
            super.set(flow)
            pageHistory.clear()
            if (flow != null) {
                currentPage = flow.advance(currentPage.orElse(null))
                updatePage(dialog, true)
            }
        }
    }

    fun flowProperty(): ObjectProperty<Flow> {
        return flow
    }

    fun getFlow(): Flow {
        return flow.get()
    }

    fun setFlow(flow: Flow) {
        this.flow.set(flow)
    }

    // A map containing a set of properties for this Wizard
    private var properties: ObservableMap<Any, Any>? = null

    /**
     * Returns an observable map of properties on this Wizard for use primarily
     * by application developers.
     *
     * @return an observable map of properties on this Wizard for use primarily
     * by application developers
     */
    fun getProperties(): ObservableMap<Any, Any>? {
        if (properties == null) {
            properties = FXCollections.observableMap(HashMap())
        }
        return properties
    }

    /**
     * Tests if this Wizard has properties.
     * @return true if this Wizard has properties.
     */
    fun hasProperties(): Boolean = MapKit.isNotEmpty(properties)

    // --- UserData
    /**
     * Convenience method for setting a single Object property that can be
     * retrieved at a later date. This is functionally equivalent to calling
     * the getProperties().put(Object key, Object value) method. This can later
     * be retrieved by calling [hello.dialog.wizard.Wizard.getUserData].
     *
     * @param value The value to be stored - this can later be retrieved by calling
     * [hello.dialog.wizard.Wizard.getUserData].
     */
    fun setUserData(value: Any) {
        getProperties()!![USER_DATA_KEY] = value
    }

    /**
     * Returns a previously set Object property, or null if no such property
     * has been set using the [hello.dialog.wizard.Wizard.setUserData] method.
     *
     * @return The Object that was previously set, or null if no property
     * has been set or if null was set.
     */
    val userData: Any?
        get() = getProperties()!![USER_DATA_KEY]
    //    public ValidationSupport getValidationSupport() {
    //      return validationSupport;
    //  }
    /**************************************************************************
     *
     * Private implementation
     *
     */
    private fun updatePage(dialog: Dialog<ButtonType?>?, advancing: Boolean) {
        val flow = getFlow()
        val prevPage = Optional.ofNullable(if (pageHistory.isEmpty()) null else pageHistory.peek())
        prevPage.ifPresent { page: WizardPane ->
            // if we are going forward in the wizard, we read in the settings
            // from the page and store them in the settings map.
            // If we are going backwards, we do nothing
            if (advancing) {
                readSettings(page)
            }

            // give the previous wizard page a chance to update the pages list
            // based on the settings it has received
            page.onExitingPage(this)
        }
        currentPage.ifPresent { currentPage: WizardPane? ->
            // put in default actions
            val buttons: MutableList<ButtonType> = currentPage!!.buttonTypes
            if (!buttons.contains(BUTTON_PREVIOUS)) {
                buttons.add(BUTTON_PREVIOUS)
                val button = currentPage.lookupButton(BUTTON_PREVIOUS) as Button
                button.addEventFilter(ActionEvent.ACTION, BUTTON_PREVIOUS_ACTION_HANDLER)
            }
            if (!buttons.contains(BUTTON_NEXT)) {
                buttons.add(BUTTON_NEXT)
                val button = currentPage.lookupButton(BUTTON_NEXT) as Button
                button.addEventFilter(ActionEvent.ACTION, BUTTON_NEXT_ACTION_HANDLER)
            }
            if (!buttons.contains(ButtonType.FINISH)) buttons.add(ButtonType.FINISH)
            if (!buttons.contains(ButtonType.CANCEL)) buttons.add(ButtonType.CANCEL)

            // then give user a chance to modify the default actions
            currentPage.onEnteringPage(this)

            // and then switch to the new pane
            dialog!!.setDialogPane(currentPage)
        }
        validateActionState()
    }

    private fun validateActionState() {
        val currentPaneButtons: MutableList<ButtonType> = dialog!!.dialogPane.buttonTypes

        // TODO can't set a DialogButton to be disabled at present
//        BUTTON_PREVIOUS.setDisabled(pageHistory.isEmpty());

        // Note that we put the 'next' and 'finish' actions at the beginning of
        // the actions list, so that it takes precedence as the default button,
        // over, say, cancel. We will probably want to handle this better in the
        // future...
        if (!getFlow().canAdvance(currentPage.orElse(null))) {
            currentPaneButtons.remove(BUTTON_NEXT)

//            currentPaneActions.add(0, ACTION_FINISH);
//            ACTION_FINISH.setDisabled( validationSupport.isInvalid());
        } else {
            if (currentPaneButtons.contains(BUTTON_NEXT)) {
                currentPaneButtons.remove(BUTTON_NEXT)
                currentPaneButtons.add(0, BUTTON_NEXT)
                val button = dialog!!.dialogPane.lookupButton(BUTTON_NEXT) as Button
                button.addEventFilter(ActionEvent.ACTION, BUTTON_NEXT_ACTION_HANDLER)
            }
            currentPaneButtons.remove(ButtonType.FINISH)
            //            ACTION_NEXT.setDisabled( validationSupport.isInvalid());
        }
    }

    private var settingCounter = 0
    private fun readSettings(page: WizardPane) {
        // for now we cannot know the structure of the page, so we just drill down
        // through the entire scenegraph (from page.content down) until we get
        // to the leaf nodes. We stop only if we find a node that is a
        // ValueContainer (either by implementing the interface), or being
        // listed in the internal valueContainers map.
        settingCounter = 0
        checkNode(page.content)
    }

    private fun checkNode(n: Node?): Boolean {
        val success = readSetting(n)
        return if (success) {
            // we've added the setting to the settings map and we should stop drilling deeper
            true
        } else {
            // go into children of this node (if possible) and see if we can get
            // a value from them (recursively)
            val children = ImplUtils.getChildren(n)

            // we're doing a depth-first search, where we stop drilling down
            // once we hit a successful read
            var childSuccess = false
            for (child in children) {
                childSuccess = childSuccess or checkNode(child)
            }
            childSuccess
        }
    }

    private fun readSetting(n: Node?): Boolean {
        if (n == null) {
            return false
        }
        val setting = ValueExtractor.getValue(n)
        if (setting != null) {
            // save it into the settings map.
            // if the node has an id set, we will use that as the setting name
            var settingName = n.id

            // but if the id is not set, we will use a generic naming scheme
            if (StringKit.isEmpty(settingName)) {
                settingName = "page_.setting_$settingCounter"
            }
            settings[settingName] = setting
            settingCounter++
        }
        return setting != null
    }
    /**************************************************************************
     *
     * Support classes
     *
     */
    /**
     *
     */
    // TODO this should just contain a ControlsFX Form, but for now it is hand-coded
    open class WizardPane : DialogPane() {
        // TODO we want to change this to an event-based API eventually
        open fun onEnteringPage(wizard: Wizard?) {}

        // TODO same here - replace with events
        open fun onExitingPage(wizard: Wizard?) {}
    }

    interface Flow {
        fun advance(currentPage: WizardPane?): Optional<WizardPane>
        fun canAdvance(currentPage: WizardPane?): Boolean
    }

    companion object {
        // --- Properties
        private val USER_DATA_KEY = Any()
    }
    /**
     *
     * @param owner
     * @param title
     */
    /**
     *
     * @param owner
     */
    init {
//        this.owner = owner;
//        this.title = title;

//        validationSupport.validationResultProperty().addListener( (o, ov, nv) -> validateActionState());
        dialog = Dialog()
        dialog!!.title = title
        //        hello.dialog.initOwner(owner); // TODO add initOwner API
    }
}