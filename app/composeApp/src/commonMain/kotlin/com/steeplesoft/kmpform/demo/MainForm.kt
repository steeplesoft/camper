package com.steeplesoft.camper.demo

import androidx.compose.runtime.mutableStateOf
import com.steeplesoft.camper.FieldState
import com.steeplesoft.camper.Form
import com.steeplesoft.camper.demo.models.Country
import com.steeplesoft.camper.fields.MIN
import com.steeplesoft.camper.validators.DateValidator
import com.steeplesoft.camper.validators.EmailValidator
import com.steeplesoft.camper.validators.IsEqualValidator
import com.steeplesoft.camper.validators.MinLengthValidator
import com.steeplesoft.camper.validators.NotEmptyValidator
import kotlinx.datetime.LocalDate

class MainForm: Form() {
    override fun getFormFields(): List<FieldState<*>> {
        return listOf(name, lastName, password, passwordConfirm, email,
            country, countryNotSearchable, startDate, endDate, agreeWithTerms)
    }

    val name = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 3,
                errorText = "This field doesn't match the min-length requirement." // TODO stringResource
            )
        )
    )

    val lastName = FieldState(
        state = mutableStateOf<String?>(null)
    )

    val password = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 8,
                errorText = "This field doesn't match the min-length requirement." // TODO stringResource
            )
        )
    )

    val passwordConfirm = FieldState(
        state = mutableStateOf<String?>(null),
        isVisible = { password.state.value != null && password.state.value!!.isNotEmpty()  },
        validators = mutableListOf(
            IsEqualValidator({ password.state.value })
        )
    )

    val email = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            EmailValidator()
        )
    )

    val country = FieldState(
        state = mutableStateOf<Country?>(null),
        options = mutableListOf(
            Country(code = "CH", name = "Switzerland"),
            Country(code = "DE", name = "Germany"),
            Country(code = "FR", name = "France"),
            Country(code = "US", name = "United States"),
            Country(code = "ES", name = "Spain"),
            Country(code = "BR", name = "Brazil"),
            Country(code = "CN", name = "China"),
        ),
        optionItemFormatter = { "${it?.name} (${it?.code})" },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    val countryNotSearchable = FieldState(
        state = mutableStateOf<Country?>(null),
        options = mutableListOf(
            null,
            Country(code = "CH", name = "Switzerland"),
            Country(code = "DE", name = "Germany")
        )
    ) {
        if (it != null) {
            "${it.name} (${it.code})"
        } else {
            "All"
        }
    }

    val startDate = FieldState(
        state = mutableStateOf<LocalDate?>(null),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    val endDate = FieldState(
        state = mutableStateOf<LocalDate?>(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            DateValidator(
                minDateTime = {startDate.state.value ?: LocalDate.MIN()},
                errorText = "This date should be after start date."
            )
        )
    )

    val agreeWithTerms = FieldState(
        state = mutableStateOf<Boolean?>(null),
        validators = mutableListOf(
            IsEqualValidator({ true })
        )
    )
}
