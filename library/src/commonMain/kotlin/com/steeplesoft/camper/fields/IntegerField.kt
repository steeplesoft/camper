package com.steeplesoft.camper.fields

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.steeplesoft.camper.Field
import com.steeplesoft.camper.FieldState
import com.steeplesoft.camper.Form
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
) : com.steeplesoft.camper.Field<Int?>(
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
        try {
            if (v.isNotEmpty()) {
                this.value.value = v.toInt()
                this.updateFormValue()

                form.validate()
                changed?.invoke(this.value.value)
            } else {
                fieldState.state.value = null
                fieldState.hasChanges.value = true
            }
        } catch (nfe: NumberFormatException) {
        }
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

        _root_ide_package_.com.steeplesoft.camper.components.TextFieldComponent(
            modifier = modifier ?: Modifier,
            imeAction = imeAction ?: ImeAction.Next,
            isEnabled = isEnabled,
            keyBoardActions = KeyboardActions.Default,
            keyboardType = keyboardType,
            onChange = {
                this.onChange(it)
            },
            label = label,
            text = (_root_ide_package_.com.steeplesoft.camper.Field.value.value?.toString() ?: ""),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            visualTransformation = visualTransformation
        )
    }
}
