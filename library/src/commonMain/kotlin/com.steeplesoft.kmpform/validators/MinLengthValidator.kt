package com.steeplesoft.kmpform.validators

import com.steeplesoft.kmpform.Validator

class MinLengthValidator(minLength: Int, errorText: String? = null) : Validator<String?>(
    validate = {
        (it?.length ?: -1) >= minLength
    },
    errorText = errorText ?: "This field is too short"
)
