package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator

class MinLengthValidator(minLength: Int, errorText: String? = null) : Validator<String?>(
    validate = {
        it != null && it.length >= minLength
    },
    errorText = errorText ?: "This field is too short"
) {
    init {
        require(minLength >= 0) { "minLength must be non-negative" }
    }
}
