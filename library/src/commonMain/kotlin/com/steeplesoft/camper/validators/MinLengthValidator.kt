package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator

class MinLengthValidator(minLength: Int, errorText: String? = null) : Validator<String?>(
    validate = {
        (it?.length ?: -1) >= minLength
    },
    errorText = errorText ?: "This field is too short"
)
