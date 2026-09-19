package com.steeplesoft.camper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    val value: MutableState<T?> = fieldState.state

    /**
     * This method is called when the value on the input field is changed
     */
    fun onChange(v: T?, form: Form = this.form) {
        this.value.value = v
        this.updateFormValue()
        form.validateField(this.fieldState)
        changed?.invoke(v)
    }

    fun updateComposableValue() {
        // FieldState is the single source of truth for the field value.
    }

    fun updateFormValue() {
        fieldState.hasChanges.value = true
    }

    @Suppress("NotConstructor")
    @Composable
    abstract fun Field()
}
