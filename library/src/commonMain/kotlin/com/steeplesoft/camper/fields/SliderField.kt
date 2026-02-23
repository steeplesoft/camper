package com.steeplesoft.camper.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.steeplesoft.camper.Field
import com.steeplesoft.camper.FieldState
import com.steeplesoft.camper.Form
import com.steeplesoft.camper.components.SliderComponent

class SliderField(
    label: String,
    form: Form,
    fieldState: FieldState<Float?>,
    modifier: Modifier? = Modifier,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: Float?) -> String)? = null,
    private val valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    private val steps: Int = 0,
    changed: ((v: Float?) -> Unit)? = null
) : Field<Float>(
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

        val current = value.value ?: valueRange.start

        SliderComponent(
            modifier = modifier ?: Modifier,
            value = current,
            onValueChange = { this.onChange(it, form) },
            label = label,
            valueRange = valueRange,
            steps = steps,
            isEnabled = isEnabled,
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText
        )
    }
}
