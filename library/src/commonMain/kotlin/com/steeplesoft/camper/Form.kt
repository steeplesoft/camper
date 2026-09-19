package com.steeplesoft.camper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Form {
    var isValid by mutableStateOf(true)

    /** Returns all fields that participate in form validation. */
    abstract fun getFormFields(): List<FieldState<*>>

    /**
     * Validates all visible fields, optionally marking them as changed.
     * Validator failures are treated as invalid input without exposing
     * implementation details to the user.
     */
    fun validate(markAsChanged: Boolean = false, ignoreInvisible: Boolean = true) {
        var formIsValid = true

        getFormFields().forEach { fieldState ->
            if (ignoreInvisible && !fieldState.isVisible()) return@forEach
            if (!validateSingleField(fieldState, markAsChanged)) formIsValid = false
        }

        isValid = formIsValid
    }

    /** Validates one field, then recomputes validity from cached field state. */
    fun validateField(fieldState: FieldState<*>) {
        getFormFields().firstOrNull { it === fieldState }
            ?.let { validateSingleField(it, markAsChanged = false) }
        recomputeFormValidity()
    }

    private fun recomputeFormValidity() {
        isValid = getFormFields()
            .filter { it.isVisible() }
            .all { it.isValid.value }
    }

    private fun validateSingleField(
        fieldState: FieldState<*>,
        markAsChanged: Boolean
    ): Boolean {
        @Suppress("UNCHECKED_CAST")
        val validators = fieldState.validators as List<Validator<Any?>>
        var fieldIsValid = true

        fieldState.errorText.clear()
        validators.forEach { validator ->
            val valid = try {
                validator.validate(fieldState.state.value)
            } catch (_: Exception) {
                fieldState.errorText.add("Validation failed.")
                false
            }

            if (!valid) {
                fieldIsValid = false
                if (fieldState.errorText.lastOrNull() != "Validation failed.") {
                    fieldState.errorText.add(validator.errorText)
                }
            }
        }

        fieldState.isValid.value = fieldIsValid
        if (markAsChanged) fieldState.hasChanges.value = true
        return fieldIsValid
    }
}
