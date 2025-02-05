package ch.benlu.composeform.fields

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import ch.benlu.composeform.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.components.TextFieldComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<LocalDate?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: LocalDate?) -> String)? = null,
    private val themeResId: Int = 0,
    changed: ((v: LocalDate?) -> Unit)? = null
) : Field<LocalDate>(
    label = label,
    form = form,
    fieldState = fieldState,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    changed = changed
) {

    /**
     * Returns a composable representing the DateField / Picker for this field
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!fieldState.isVisible()) {
            return
        }

        val openDialog = remember { mutableStateOf(false) }


        val focusRequester = FocusRequester()

        val calendar = value.value ?: LocalDate.now()

        val date = remember { mutableStateOf("") }
        if (openDialog.value) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = calendar.toEpochDays() * 86400000L
//                    Clock.System.now().toEpochMilliseconds()
            )


            DatePickerDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                fieldState.state.value = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                            }
                            openDialog.value = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }


        }
        TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            label = label,
            text = formatter?.invoke(value.value) ?: value.value.toString(),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            isReadOnly = true,
            focusRequester = focusRequester,
            focusChanged = {
                if (it.isFocused) {
                    openDialog.value = true
                }
            }
        )
    }
}


fun LocalDate.Companion.now() = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.Companion.MIN() = LocalDate(-999_999_999, 1, 1)
fun LocalDate.Companion.MAX() = LocalDate(999999999, 12, 31)
fun LocalTime.Companion.MIN() = LocalTime(0, 0, 0, 0)
fun LocalTime.Companion.MAX() = LocalTime(23, 5, 59, 999999999)
