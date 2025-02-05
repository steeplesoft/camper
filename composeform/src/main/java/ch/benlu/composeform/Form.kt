package ch.benlu.composeform

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Form {
    var isValid by mutableStateOf(true)

    abstract fun self(): Form

    /**
     * Returns a list of all fields in the form.
     */
    abstract fun getFormFields(): List<FieldState<*>>

    /**
     * Triggers validation for all fields in the form.
     * @param markAsChanged If true, all fields will be marked as changed.
     * @param ignoreInvisible If true, invisible fields will be ignored during validation.
     */
    fun validate(markAsChanged: Boolean = false, ignoreInvisible: Boolean = true) {
        var isValid = true
        val formFields = getFormFields()

        formFields.forEach { fieldState ->
            try {
                // if we should ignore invisible fields, skip validation
                if (ignoreInvisible && !fieldState.isVisible()) {
                    return@forEach
                }

                val validators = fieldState.validators

                var isFieldValid = true

                // first clear all error text before validation
                fieldState.errorText.clear()

                validators.forEach {
                    if (!it.validate(fieldState.state.value)) {
                        isValid = false
                        isFieldValid = false
                        // add error text to fieldState
                        fieldState.errorText.add(it.errorText)
                    }
                }
                fieldState.isValid.value = isFieldValid

                // if we should ignore untouched fields, every field should be marked as changed
                if (markAsChanged) {
                    fieldState.hasChanges.value = true
                }

            } catch (e: Exception) {
                println("Form: $e")
            }
        }

        this.isValid = isValid
    }
}
