package com.steeplesoft.camper.fields

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.steeplesoft.camper.Field
import com.steeplesoft.camper.components.TextFieldComponent

class IntegerField(
    label: String,
    form: com.steeplesoft.camper.Form,
    modifier: Modifier? = Modifier,
    fieldState: com.steeplesoft.camper.FieldState<Int?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    changed: ((v: Int?) -> Unit)? = null
) : Field<Int?>(
    label = label,
    form = form,
    fieldState = fieldState,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    keyboardType = KeyboardType.Number,
    visualTransformation = visualTransformation,
    changed = changed
) {

    fun onChange(v: String) {
        if (v.isEmpty()) {
            this.value.value = null
        } else {
            this.value.value = v.toIntOrNull() ?: return
        }

        this.updateFormValue()
        form.validateField(fieldState)
        changed?.invoke(this.value.value)
    }

    /**
     * Returns a composable representing the DateField / Picker for this field
     */
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!fieldState.isVisible()) {
            return
        }

        TextFieldComponent(
            modifier = modifier ?: Modifier,
            imeAction = imeAction ?: ImeAction.Next,
            isEnabled = isEnabled,
            keyBoardActions = KeyboardActions.Default,
            keyboardType = keyboardType,
            onChange = {
                this.onChange(it)
            },
            label = label,
            text = (value.value?.toString() ?: ""),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            visualTransformation = visualTransformation
        )
    }
}
