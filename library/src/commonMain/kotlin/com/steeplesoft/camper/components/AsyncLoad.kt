package com.steeplesoft.camper.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AsyncLoad(status: Status,
              message: @Composable () -> Unit = { Text("An error occurred") },
              content: @Composable () -> Unit) {
    when (status) {
        Status.LOADING -> { LoadingScreen() }
        Status.SUCCESS -> { content() }
        Status.ERROR -> { message() }
    }
}

enum class Status {
    LOADING, SUCCESS, ERROR
}
