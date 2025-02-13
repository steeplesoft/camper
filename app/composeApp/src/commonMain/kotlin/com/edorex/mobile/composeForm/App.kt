package com.edorex.mobile.composeForm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ch.benlu.composeform.fields.CheckboxField
import ch.benlu.composeform.fields.DateField
import ch.benlu.composeform.fields.PasswordField
import ch.benlu.composeform.fields.PickerField
import ch.benlu.composeform.fields.TextField
import ch.benlu.composeform.formatters.dateLong
import ch.benlu.composeform.formatters.dateShort

@Composable
fun App() {
    MaterialTheme {
        FormPage()
    }
}

@Composable
fun FormPage() {
    val form = MainForm()

    Scaffold(
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())) {

                    Column {
                        TextField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Name",
                            form = form,
                            fieldState = form.name,
                            changed = {
                                // log the name to show tat changed is called
                                // clear countries (for no reason - just to show that options list is now mutable)
                                form.country.options = mutableListOf()
                            }
                        ).Field()

                        TextField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Last Name",
                            form = form,
                            fieldState = form.lastName,
                            isEnabled = false,
                        ).Field()

                        TextField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "E-Mail",
                            form = form,
                            fieldState = form.email,
                            keyboardType = KeyboardType.Email
                        ).Field()

                        PasswordField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Password",
                            form = form,
                            fieldState = form.password
                        ).Field()

                        PasswordField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Password Confirm",
                            form = form,
                            fieldState = form.passwordConfirm
                        ).Field()

                        PickerField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Country",
                            form = form,
                            fieldState = form.country,
                            submitOnSelect = true
                        ).Field()

                        PickerField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Country Not searchable",
                            form = form,
                            fieldState = form.countryNotSearchable,
                            isSearchable = false
                        ).Field()

                        DateField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "Start Date",
                            form = form,
                            fieldState = form.startDate,
                            formatter = ::dateShort
                        ).Field()

                        DateField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = "End Date",
                            form = form,
                            fieldState = form.endDate,
                            formatter = ::dateLong
                        ).Field()

                        CheckboxField(
                            modifier = Modifier.padding(bottom = 8.dp),
                            fieldState = form.agreeWithTerms,
                            label = "I agree to Terms & Conditions",
                            form = form
                        ).Field()
                    }
                }

                ButtonRow(nextClicked = {
                    form.validate(true)
                })

            }
        }
    )
}

@Composable
fun ButtonRow(nextClicked: () -> Unit) {
    Row {
        Button(
            enabled = false,
            modifier = Modifier.weight(1f),
            onClick = {
                // nothing
            }
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                nextClicked()
            }
        ) {
            Text("Validate")
        }
    }
}
