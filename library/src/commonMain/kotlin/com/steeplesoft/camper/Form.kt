package com.steeplesoft.camper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Exception thrown when form field operations fail.
 */
class FormFieldException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

abstract class Form {
    var isValid by mutableStateOf(true)

    //abstract fun self(): Form

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

                @Suppress("UNCHECKED_CAST")
                val validators: List<Validator<Any?>> = (fieldState.validators as List<Validator<Any?>>)

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

    /**
     * Validates only the given field and recomputes form-level validity
     * by reading each field's cached isValid state (without re-running their validators).
     * Use this for per-keystroke validation to avoid O(fields * validators) cost.
     *
     * @param fieldState The field state that changed and needs validation
     */
    fun validateField(fieldState: FieldState<*>) {
        try {
            val formFields = getFormFields()
            // Find the cached field matching this fieldState and validate it
            for (fs in formFields) {
                try {
                    if (fs === fieldState) {
                        validateSingleField( fs, markAsChanged = false)
                        break
                    }
                } catch (e: Exception) {
                    // Continue searching
                }
            }
        } catch (e: Exception) {
//            Log.e("Form", "Error during single-field validation: ${e.message}", e)
        }
        recomputeFormValidity()
    }

    /**
     * Recomputes form-level isValid by reading each field's cached isValid state
     * without re-running any validators.
     */
    private fun recomputeFormValidity() {
        var formIsValid = true
        try {
            val formFields = getFormFields()
            for (fs in formFields) {
                try {
                    if (!fs.isVisible()) continue
                    if (!fs.isValid.value) {
                        formIsValid = false
                        break
                    }
                } catch (e: Exception) {
                    formIsValid = false
                }
            }
        } catch (e: Exception) {
            formIsValid = false
        }
        this.isValid = formIsValid
    }

    /**
     * Validates a single field with comprehensive error handling.
     *
     * @param cachedField The cached field metadata
     * @param fieldState The field state to validate
     * @param markAsChanged Whether to mark the field as changed
     * @return True if the field is valid, false otherwise
     */
    private fun validateSingleField(
        fieldState: FieldState<*>,
        markAsChanged: Boolean
    ): Boolean {
        return try {
            // Safely cast the FieldState to handle Any type
            @Suppress("UNCHECKED_CAST")
            val typedFieldState = fieldState as FieldState<Any>

            val value = typedFieldState.state.value
            val validators = typedFieldState.validators

            var isFieldValid = true

            // Clear previous error messages
            typedFieldState.errorText.clear()

            // Run all validators
            validators.forEach { validator ->
                try {
                    if (!validator.validate(value)) {
                        isFieldValid = false
                        typedFieldState.errorText.add(validator.errorText)
                    }
                } catch (e: Exception) {
                    isFieldValid = false
                    typedFieldState.errorText.add("Validation error: ${e.message}")
                }
            }

            // Update field state
            typedFieldState.isValid.value = isFieldValid

            // Mark as changed if requested
            if (markAsChanged) {
                typedFieldState.hasChanges.value = true
            }

            isFieldValid

        } catch (e: ClassCastException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}
