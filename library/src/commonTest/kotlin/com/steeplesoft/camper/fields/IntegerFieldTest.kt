package com.steeplesoft.camper.fields

import androidx.compose.runtime.mutableStateOf
import com.steeplesoft.camper.FieldState
import com.steeplesoft.camper.Form
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntegerFieldTest {

    @Test
    fun onChange_emptyValue_clearsStateAndNotifies() {
        val form = object : Form() {
            val value = FieldState(state = mutableStateOf<Int?>(42))
            override fun getFormFields() = listOf(value)
        }
        var changedValue: Int? = 42
        val field = IntegerField(
            label = "Count",
            form = form,
            fieldState = form.value,
            changed = { changedValue = it }
        )

        field.onChange("")

        assertEquals(null, form.value.state.value)
        assertEquals(null, changedValue)
        assertTrue(form.value.hasChanges.value)
    }
}
