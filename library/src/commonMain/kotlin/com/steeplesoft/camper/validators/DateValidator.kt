package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator
import com.steeplesoft.camper.fields.MIN
import kotlinx.datetime.LocalDate

class DateValidator(minDateTime: () -> LocalDate, errorText: String? = null) : Validator<LocalDate?>(
    validate = {
        (it ?: LocalDate.MIN()) >= minDateTime()
    },
    errorText = errorText ?: "This field is not valid."
)
