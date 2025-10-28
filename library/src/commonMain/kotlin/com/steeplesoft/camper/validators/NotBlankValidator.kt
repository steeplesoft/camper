package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator

class NotBlankValidator(errorText: String = "This field should not be empty") : Validator<String?>(
    validate = {
        !it.isNullOrBlank()
    },
    errorText = errorText
)
