package com.stoiclock.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF1B1B1D)
private val Marble = Color(0xFFF5F2EC)
private val Bronze = Color(0xFF8C6A43)
private val BronzeLight = Color(0xFFD7B98E)
private val Stone = Color(0xFF5E5A54)

private val LightColors = lightColorScheme(
    primary = Bronze,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF1E3CF),
    onPrimaryContainer = Color(0xFF2C1D0B),
    secondary = Stone,
    background = Marble,
    onBackground = Ink,
    surface = Marble,
    onSurface = Ink,
    surfaceVariant = Color(0xFFE8E3DA),
    onSurfaceVariant = Stone,
)

private val DarkColors = darkColorScheme(
    primary = BronzeLight,
    onPrimary = Color(0xFF3A2709),
    primaryContainer = Color(0xFF4F3B20),
    onPrimaryContainer = Color(0xFFF1E3CF),
    secondary = Color(0xFFCBC5BB),
    background = Color(0xFF121214),
    onBackground = Color(0xFFE7E3DC),
    surface = Color(0xFF121214),
    onSurface = Color(0xFFE7E3DC),
    surfaceVariant = Color(0xFF2A2A2D),
    onSurfaceVariant = Color(0xFFCBC5BB),
)

private val base = Typography()

/** Tipografía editorial: serif para las citas, sans para la interfaz. */
val StoicTypography = base.copy(
    displaySmall = base.displaySmall.copy(fontFamily = FontFamily.Serif),
    headlineMedium = base.headlineMedium.copy(fontFamily = FontFamily.Serif),
    headlineSmall = base.headlineSmall.copy(fontFamily = FontFamily.Serif),
)

val QuoteTextStyle = TextStyle(
    fontFamily = FontFamily.Serif,
    fontStyle = FontStyle.Italic,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 34.sp,
)

@Composable
fun StoicLockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(colorScheme = colors, typography = StoicTypography, content = content)
}
