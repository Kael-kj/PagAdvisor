package com.Kenji.pagadvisor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de Cores CLARA (Light)
private val LightColorScheme = lightColorScheme(
    primary = PagYellow,            // Cor do Botão, Cor do Contorno/Label Focado do TextField
    onPrimary = PagWhite,           // Cor da Fonte do Botão

    secondary = PagGradientBottom,  // Cor secundária (pode ajustar)
    onSecondary = PagBlack,

    background = PagWhite,          // Fundo padrão do app (branco)
    onBackground = PagBlack,        // Texto padrão no fundo

    surface = PagWhite,             // Cor de cartões, menus, etc.
    onSurface = PagBlack            // Cor do texto *digitado* no TextField
)

// Paleta de Cores ESCURA (Dark) - Vamos usar a clara por enquanto
private val DarkColorScheme = lightColorScheme(
    primary = PagYellow,
    onPrimary = PagWhite,
    secondary = PagGradientBottom,
    onSecondary = PagBlack,
    background = PagBlack,
    onBackground = PagWhite,
    surface = PagBlack,
    onSurface = PagWhite
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
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // (Vem do seu arquivo Type.kt)
        content = content
    )
}