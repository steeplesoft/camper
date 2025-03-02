package com.steeplesoft.kmpform.demo

import androidx.compose.runtime.mutableStateOf
import com.steeplesoft.kmpform.FieldState
import com.steeplesoft.kmpform.Form
import com.steeplesoft.kmpform.FormField
import com.steeplesoft.kmpform.demo.models.Country
import com.steeplesoft.kmpform.fields.MIN
import com.steeplesoft.kmpform.validators.DateValidator
import com.steeplesoft.kmpform.validators.EmailValidator
import com.steeplesoft.kmpform.validators.IsEqualValidator
import com.steeplesoft.kmpform.validators.MinLengthValidator
import com.steeplesoft.kmpform.validators.NotEmptyValidator
import kotlinx.datetime.LocalDate

class MainForm: Form() {
    override fun getFormFields(): List<FieldState<*>> {
        return listOf(name, lastName, password, passwordConfirm, email,
            country, countryNotSearchable, startDate, endDate, agreeWithTerms)
    }

    override fun self(): Form {
        return this
    }

    @FormField
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

    @FormField
    val lastName = FieldState(
        state = mutableStateOf<String?>(null)
    )

    @FormField
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

    @FormField
    val passwordConfirm = FieldState(
        state = mutableStateOf<String?>(null),
        isVisible = { password.state.value != null && password.state.value!!.isNotEmpty()  },
        validators = mutableListOf(
            IsEqualValidator({ password.state.value })
        )
    )

    @FormField
    val email = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            EmailValidator()
        )
    )

    @FormField
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

    @FormField
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

    @FormField
    val startDate = FieldState(
        state = mutableStateOf<LocalDate?>(null),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    @FormField
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

    @FormField
    val agreeWithTerms = FieldState(
        state = mutableStateOf<Boolean?>(null),
        validators = mutableListOf(
            IsEqualValidator({ true })
        )
    )
}
