package com.edorex.mobile.composeForm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.benlu.composeform.fields.CheckboxField
import ch.benlu.composeform.fields.DateField
import ch.benlu.composeform.fields.PasswordField
import ch.benlu.composeform.fields.PickerField
import ch.benlu.composeform.fields.TextField
import ch.benlu.composeform.formatters.dateLong
import ch.benlu.composeform.formatters.dateShort
import com.edorex.mobile.composeForm.ui.theme.ComposeFormTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeFormTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormPage()
                }
            }
        }
    }
}

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormPage() {
    val viewModel = hiltViewModel<MainViewModel>()

    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                Column {
                    TextField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Name",
                        form = viewModel.form,
                        fieldState = viewModel.form.name,
                        changed = {
                            // log the name to show tat changed is called
                            Log.d("Form", "Name changed: $it")
                            // clear countries (for no reason - just to show that options list is now mutable)
                            viewModel.form.country.options = mutableListOf()
                        }
                    ).Field()

                    TextField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Last Name",
                        form = viewModel.form,
                        fieldState = viewModel.form.lastName,
                        isEnabled = false,
                    ).Field()

                    TextField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "E-Mail",
                        form = viewModel.form,
                        fieldState = viewModel.form.email,
                        keyboardType = KeyboardType.Email
                    ).Field()

                    PasswordField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Password",
                        form = viewModel.form,
                        fieldState = viewModel.form.password
                    ).Field()

                    PasswordField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Password Confirm",
                        form = viewModel.form,
                        fieldState = viewModel.form.passwordConfirm
                    ).Field()

                    PickerField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Country",
                        form = viewModel.form,
                        fieldState = viewModel.form.country
                    ).Field()

                    PickerField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Country Not searchable",
                        form = viewModel.form,
                        fieldState = viewModel.form.countryNotSearchable,
                        isSearchable = false
                    ).Field()

                    DateField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "Start Date",
                        form = viewModel.form,
                        fieldState = viewModel.form.startDate,
                        formatter = ::dateShort

                    ).Field()

                    DateField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        label = "End Date",
                        form = viewModel.form,
                        fieldState = viewModel.form.endDate,
                        themeResId = R.style.customDatePickerStyle,
                        formatter = ::dateLong
                    ).Field()

                    CheckboxField(
                        modifier = Modifier.padding(bottom = 8.dp),
                        fieldState = viewModel.form.agreeWithTerms,
                        label = "I agree to Terms & Conditions",
                        form = viewModel.form
                    ).Field()
                }
            }

            ButtonRow(nextClicked = {
                viewModel.validate()
            })

        }
    }

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

@Preview
@Composable
fun FormPagePreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        FormPage()
    }
}
