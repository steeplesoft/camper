package com.steeplesoft.camper.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.steeplesoft.camper.Field
import com.steeplesoft.camper.FieldState
import com.steeplesoft.camper.Form
import com.steeplesoft.camper.components.ComboBox

class ComboBoxField<T>(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<T?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: T?) -> String)? = null,
    changed: ((v: T?) -> Unit)? = null
) : Field<T>(
    label = label,
    form = form,
    fieldState = fieldState,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    changed = changed
) {

    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!fieldState.isVisible()) {
            return
        }

        ComboBox(
            label = label,
            selected = fieldState.selectedOption(),
            items = fieldState.options,
            itemLabel = { item -> fieldState.optionItemFormatter?.let { func -> func(item) } ?: ""},
            onChange = {
                this.onChange(it, form)
            }
        )
    }
}
