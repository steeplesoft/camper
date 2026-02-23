package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator

class NotEmptyValidator<T>(errorText: String? = null) : Validator<T>(
    validate = {
        it != null
    },
    errorText = errorText ?: "This field should not be empty"
)
