package com.steeplesoft.kmpform.validators

import com.steeplesoft.kmpform.Validator
import com.steeplesoft.kmpform.fields.MIN
import kotlinx.datetime.LocalDate

class DateValidator(minDateTime: () -> LocalDate, errorText: String? = null) : Validator<LocalDate?>(
    validate = {
        (it ?: LocalDate.MIN()) >= minDateTime()
    },
    errorText = errorText ?: "This field is not valid."
)
