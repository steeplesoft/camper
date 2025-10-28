package com.steeplesoft.camper.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SecuredTextFieldComponent(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    isEnabled: Boolean = true,
    hasError: Boolean = false,
    errorText: MutableList<String> = mutableListOf(),
    interactionSource: MutableInteractionSource? = null,
    focusChanged: ((focus: FocusState) -> Unit)? = null,
    focusRequester: FocusRequester = FocusRequester(),
) {
    var showPassword by remember { mutableStateOf(false) }
    val state = remember { TextFieldState() }
    var password by remember { mutableStateOf("") }
    LaunchedEffect(state) {
        snapshotFlow { state.text } // Observe changes to the text property
            .collect { newText ->
                if (newText != password) {
                    password = newText.toString()
                    onChange(password)
                }
            }
    }
    OutlinedSecureTextField(
        state = state,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        enabled = isEnabled,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
//            .padding(6.dp)
//            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
//            .padding(6.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                focusChanged?.invoke(it)
            },
        isError = hasError,
        label = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = label
            )
        },
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        placeholder = null,
        trailingIcon = {
            Icon(
                if (showPassword) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                contentDescription = "Toggle password visibility",
                modifier = Modifier
                    .requiredSize(48.dp).padding(16.dp)
                    .clickable { showPassword = !showPassword }
            )
        }
    )
    if (hasError) {
        Text(
            text = errorText.joinToString("\n"),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}


@Composable
fun PasswordTextField() {
    val state = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }
    BasicSecureTextField(
        state = state,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(6.dp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    innerTextField()
                }
                Icon(
                    if (showPassword) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp).padding(16.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}
