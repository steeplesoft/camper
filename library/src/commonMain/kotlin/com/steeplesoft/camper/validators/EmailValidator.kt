package com.steeplesoft.camper.validators

import com.steeplesoft.camper.Validator

class EmailValidator(errorText: String? = null) : Validator<String?>(
    validate = { EMAIL_PATTERN.matches(it ?: "") },
    errorText = errorText ?: "Please enter a valid e-mail address."
)

// Client-side shape check only; delivery validation remains server-side.
private val EMAIL_PATTERN = """^[^@\s]+@[^@\s]+\.[^@\s]+$""".toRegex()
