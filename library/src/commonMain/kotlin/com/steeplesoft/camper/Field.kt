package com.steeplesoft.camper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

abstract class Field<T> (
    open val fieldState: FieldState<T?>,
    open val label: String,
    open val form: Form,
    open val keyboardType: KeyboardType = KeyboardType.Text,
    open val visualTransformation: VisualTransformation = VisualTransformation.None,
    open val isEnabled: Boolean = true,
    open val modifier: Modifier? = Modifier,
    open val imeAction: ImeAction? = ImeAction.Next,
    open val formatter: ((raw: T?) -> String)? = null,
    open var changed: ((v: T?) -> Unit)? = null
) {
    var value: MutableState<T?> = mutableStateOf(null)

    /**
     * This method is called when the value on the input field is changed
     */
    fun onChange(v: Any?, form: Form) {
        @Suppress("UNCHECKED_CAST")
        this.value.value = v as T?
        this.updateFormValue()
        form.validateField(this.fieldState)
        changed?.invoke(v)
    }

    fun updateComposableValue() {
        val newValue = fieldState.state.value
        if (this.value.value != newValue) {
            this.value.value = newValue
        }
    }

    fun updateFormValue() {
        fieldState.state.value = this.value.value
        fieldState.hasChanges.value = true
    }

    @Suppress("NotConstructor")
    @Composable
    abstract fun Field()
}
