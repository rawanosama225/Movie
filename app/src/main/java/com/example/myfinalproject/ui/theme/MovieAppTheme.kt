package com.example.myfinalproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val PurplePrimary = Color(0xFF6C47DB)
val DarkBackground = Color(0xFF0E0E0E)
val TextWhite = Color(0xFFFFFFFF)

val PurpleTransparent = Color(0x706C47DB)

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = TextWhite,
    onBackground = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    background = Color.White,
    surface = Color.White,
    onPrimary = TextWhite,
    onBackground = DarkBackground
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,

        content = content
    )
}