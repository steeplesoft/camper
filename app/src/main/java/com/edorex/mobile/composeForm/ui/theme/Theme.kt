package com.edorex.mobile.composeForm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    inversePrimary = Purple700,
    secondary = Teal200
)

val LightColorPalette = lightColorScheme(
    primary = Purple500,
    inversePrimary = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeFormTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        },
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
