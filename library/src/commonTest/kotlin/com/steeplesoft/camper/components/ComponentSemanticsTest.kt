package com.steeplesoft.camper.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.setContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ComponentSemanticsTest {

    @Test
    fun checkbox_exposesLabelAndToggleSemantics() = runComposeUiTest {
        setContent {
            CheckboxComponent(
                checked = false,
                onCheckedChange = {},
                label = "Accept terms"
            )
        }

        onNodeWithText("Accept terms").assertTextEquals("Accept terms")
        onNodeWithText("Accept terms").assertIsToggleable()
    }

    @Test
    fun switch_exposesLabelAndToggleSemantics() = runComposeUiTest {
        setContent {
            SwitchComponent(
                checked = true,
                onCheckedChange = {},
                label = "Notifications"
            )
        }

        onNodeWithText("Notifications").assertTextEquals("Notifications")
        onNodeWithText("Notifications").assertIsToggleable()
    }
}
