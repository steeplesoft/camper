package ch.benlu.composeform.validators

import ch.benlu.composeform.Validator
import ch.benlu.composeform.fields.MIN
import kotlinx.datetime.LocalDate

class DateValidator(minDateTime: () -> LocalDate, errorText: String? = null) : Validator<LocalDate?>(
    validate = {
        (it ?: LocalDate.MIN()) >= minDateTime()
    },
    errorText = errorText ?: "This field is not valid."
)
