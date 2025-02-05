package com.edorex.mobile.composeForm

import androidx.compose.runtime.mutableStateOf
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.FormField
import ch.benlu.composeform.fields.MIN
import ch.benlu.composeform.validators.DateValidator
import ch.benlu.composeform.validators.EmailValidator
import ch.benlu.composeform.validators.IsEqualValidator
import ch.benlu.composeform.validators.MinLengthValidator
import ch.benlu.composeform.validators.NotEmptyValidator
import com.edorex.mobile.composeForm.di.ResourcesProvider
import com.edorex.mobile.composeForm.models.Country
import kotlinx.datetime.LocalDate

class MainForm(resourcesProvider: ResourcesProvider): Form() {
    override fun self(): Form {
        return this
    }

    @FormField
    val name = FieldState<String?>(
        state = mutableStateOf(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 3,
                errorText = resourcesProvider.getString(R.string.error_min_length)
            )
        )
    )

    @FormField
    val lastName = FieldState<String?>(
        state = mutableStateOf(null)
    )

    @FormField
    val password = FieldState<String?>(
        state = mutableStateOf(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 8,
                errorText = resourcesProvider.getString(R.string.error_min_length)
            )
        )
    )

    @FormField
    val passwordConfirm = FieldState<String?>(
        state = mutableStateOf(null),
        isVisible = { password.state.value != null && password.state.value!!.isNotEmpty()  },
        validators = mutableListOf(
            IsEqualValidator({ password.state.value })
        )
    )

    @FormField
    val email = FieldState<String?>(
        state = mutableStateOf(null),
        validators = mutableListOf(
            EmailValidator()
        )
    )

    @FormField
    val country = FieldState<Country?>(
        state = mutableStateOf(null),
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
    val countryNotSearchable = FieldState<Country?>(
        state = mutableStateOf(null),
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
    val startDate = FieldState<LocalDate?>(
        state = mutableStateOf(LocalDate(1974, 12, 23)),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    @FormField
    val endDate = FieldState<LocalDate?>(
        state = mutableStateOf(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            DateValidator(
                minDateTime = {startDate.state.value ?: LocalDate.MIN()},
                errorText = resourcesProvider.getString(R.string.error_date_after_start_date)
            )
        )
    )

    @FormField
    val agreeWithTerms = FieldState<Boolean?>(
        state = mutableStateOf(null),
        validators = mutableListOf(
            IsEqualValidator({ true })
        )
    )

    override fun getFormFields(): List<FieldState<*>> = listOf(
        name, lastName, password, passwordConfirm, email, country, countryNotSearchable,
        startDate, endDate, agreeWithTerms
    )
}
