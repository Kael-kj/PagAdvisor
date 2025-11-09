package com.Kenji.pagadvisor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PagYellow,
    onPrimary = PagWhite,

    primaryContainer = PagYellow.copy(alpha = 0.2f),
    onPrimaryContainer = PagDarkGray,

    secondary = PagGradientBottom,
    onSecondary = PagBlack,

    background = PagLightGray,
    onBackground = PagDarkGray,

    surface = PagWhite,
    onSurface = PagDarkGray,

    surfaceVariant = PagLightGray,
    onSurfaceVariant = PagDarkGray,

    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PagYellow,
    onPrimary = PagDarkGray,

    primaryContainer = PagYellow.copy(alpha = 0.2f),
    onPrimaryContainer = PagWhite,

    secondary = PagGradientBottom,
    onSecondary = PagBlack,

    background = Color(0xFF121212),
    onBackground = PagWhite,

    surface = Color(0xFF1E1E1E),
    onSurface = PagWhite,

    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = PagLightGray,

    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun PagAdvisorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}