package com.steeplesoft.camper

import androidx.compose.runtime.mutableStateOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FormTest {

    private lateinit var form: TestForm

    class TestForm : Form() {
        val name = FieldState(
            state = mutableStateOf<String?>(null),
            validators = mutableListOf(
                object : Validator<String?>(
                    validate = { !it.isNullOrEmpty() },
                    errorText = "Name is required"
                ) {}
            )
        )

        val email = FieldState(
            state = mutableStateOf<String?>(null),
            validators = mutableListOf(
                object : Validator<String?>(
                    validate = { !it.isNullOrEmpty() },
                    errorText = "Email is required"
                ) {}
            )
        )

        val optional = FieldState(
            state = mutableStateOf<String?>(null)
        )

        override fun getFormFields() = listOf(name, email, optional)
    }

    @BeforeTest
    fun setUp() {
        form = TestForm()
    }

    // validate() tests

    @Test
    fun validate_allFieldsEmpty_formIsInvalid() {
        form.validate()

        assertFalse(form.isValid)
    }

    @Test
    fun validate_allRequiredFieldsFilled_formIsValid() {
        form.name.state.value = "John"
        form.email.state.value = "john@example.com"

        form.validate()

        assertTrue(form.isValid)
    }

    @Test
    fun validate_oneRequiredFieldMissing_formIsInvalid() {
        form.name.state.value = "John"

        form.validate()

        assertFalse(form.isValid)
    }

    @Test
    fun validate_setsFieldIsValid() {
        form.name.state.value = "John"
        form.email.state.value = ""

        form.validate()

        assertTrue(form.name.isValid.value)
        assertFalse(form.email.isValid.value)
    }

    @Test
    fun validate_withMarkAsChanged_setsHasChanges() {
        form.validate(markAsChanged = true)

        assertTrue(form.name.hasChanges.value)
        assertTrue(form.email.hasChanges.value)
    }

    @Test
    fun validate_populatesErrorText() {
        form.validate()

        assertTrue(form.name.errorText.contains("Name is required"))
        assertTrue(form.email.errorText.contains("Email is required"))
    }

    @Test
    fun validate_validatorThrows_marksFormInvalid() {
        val throwingForm = object : Form() {
            val value = FieldState(
                state = mutableStateOf<String?>("value"),
                validators = mutableListOf(
                    object : Validator<String?>({ error("boom") }, "Invalid") {}
                )
            )

            override fun getFormFields() = listOf(value)
        }

        throwingForm.validate()

        assertFalse(throwingForm.isValid)
        assertFalse(throwingForm.value.isValid.value)
    }

    @Test
    fun validate_clearsOldErrorsBeforeRevalidating() {
        form.validate()
        assertTrue(form.name.errorText.isNotEmpty())

        form.name.state.value = "John"
        form.validate()

        assertTrue(form.name.errorText.isEmpty())
    }

    // validateField() tests

    @Test
    fun validateField_validatesOnlyTargetField() {
        form.name.state.value = "John"
        form.email.state.value = ""

        form.validateField(form.name)

        assertTrue(form.name.isValid.value)
        // email was not re-validated, still has default isValid = false
        assertFalse(form.email.isValid.value)
    }

    @Test
    fun validateField_recomputesFormValidity() {
        // First validate everything so all fields have proper isValid states
        form.name.state.value = "John"
        form.email.state.value = "john@example.com"
        form.validate()
        assertTrue(form.isValid)

        // Now clear email and validate only name
        form.email.state.value = ""
        form.validateField(form.name)

        // name is still valid, but email's cached isValid was true from prior validate()
        // so form should still appear valid (email not re-validated)
        assertTrue(form.isValid)

        // Now validate email specifically
        form.validateField(form.email)
        assertFalse(form.isValid)
    }

    @Test
    fun validateField_setsErrorTextOnInvalidField() {
        form.name.state.value = ""

        form.validateField(form.name)

        assertTrue(form.name.errorText.contains("Name is required"))
    }

    @Test
    fun validateField_clearsErrorTextOnValidField() {
        form.name.state.value = ""
        form.validateField(form.name)
        assertTrue(form.name.errorText.isNotEmpty())

        form.name.state.value = "John"
        form.validateField(form.name)

        assertTrue(form.name.errorText.isEmpty())
    }

    @Test
    fun validateField_formValidWhenAllFieldsValid() {
        form.name.state.value = "John"
        form.email.state.value = "john@example.com"

        // Full validate first so all fields (including optional) get isValid set
        form.validate()
        assertTrue(form.isValid)

        // Now single-field validate keeps form valid
        form.name.state.value = "Jane"
        form.validateField(form.name)

        assertTrue(form.isValid)
    }

    @Test
    fun validateField_formInvalidWhenAnyFieldInvalid() {
        form.name.state.value = "John"
        form.email.state.value = "john@example.com"
        form.validate()
        assertTrue(form.isValid)

        // Invalidate one field
        form.email.state.value = ""
        form.validateField(form.email)

        assertFalse(form.isValid)
    }

    // Invisible field tests

    @Test
    fun validate_skipsInvisibleFields() {
        val formWithInvisible = object : Form() {
            val visible = FieldState(
                state = mutableStateOf<String?>("value"),
                validators = mutableListOf(
                    object : Validator<String?>(
                        validate = { !it.isNullOrEmpty() },
                        errorText = "Required"
                    ) {}
                )
            )

            val invisible = FieldState(
                state = mutableStateOf<String?>(null),
                isVisible = { false },
                validators = mutableListOf(
                    object : Validator<String?>(
                        validate = { !it.isNullOrEmpty() },
                        errorText = "Required"
                    ) {}
                )
            )

            override fun getFormFields() = listOf(visible, invisible)
        }

        formWithInvisible.validate()

        assertTrue(formWithInvisible.isValid)
    }
}
