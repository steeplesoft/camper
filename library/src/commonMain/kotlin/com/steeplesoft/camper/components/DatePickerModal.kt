package com.steeplesoft.camper.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.steeplesoft.camper.fields.now
import kotlinx.datetime.LocalDate

@ExperimentalMaterial3Api
@Composable
fun DatePickerModal(
    label: String,
    value: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit = { },
    isSelectable: (Long) -> Boolean = { true }
) {
    var selectedDate by remember { mutableStateOf(value) }
    var openDialog by remember { mutableStateOf(false) }

    val dismiss = {
        openDialog = false
        onDismiss()
    }
    // Up Icon when expanded and down icon when collapsed
    val icon = if (openDialog)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if (openDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = value.toEpochDays() * 86400000L,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return isSelectable(utcTimeMillis)
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = dismiss,
            confirmButton = {
                TextButton(onClick = {
                    val localDate = LocalDate.fromEpochDays(datePickerState.selectedDateMillis!!.div(86400000).toInt())
                    selectedDate = localDate
                    onDateSelected(localDate)
                    dismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismiss()
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
    OutlinedTextField(
        value = selectedDate.toString(),
        label = { Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = label) },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { },
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
//        keyboardActions = KeyboardActions(),
        enabled = true,
        readOnly = true,
        trailingIcon = {
            Icon(icon, "contentDescription",
                Modifier.clickable { openDialog = !openDialog })
        }
    )
}
