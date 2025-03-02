package com.steeplesoft.kmpform.validators

import com.steeplesoft.kmpform.Validator

class NotEmptyValidator<T>(errorText: String? = null) : Validator<T>(
    validate = {
        it != null
    },
    errorText = errorText ?: "This field should not be empty"
)
