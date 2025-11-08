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

// --- Paleta de Cores CLARA (Light) ---
// Esta é a paleta principal que o app usará
private val LightColorScheme = lightColorScheme(
    primary = PagYellow,            // Cor do Botão, Cor do Contorno/Label Focado do TextField
    onPrimary = PagWhite,           // Cor da Fonte do Botão

    primaryContainer = PagYellow.copy(alpha = 0.2f), // Usado para fundos sutis (ex: ChatBubble)
    onPrimaryContainer = PagDarkGray,

    secondary = PagGradientBottom,  // Cor secundária (pode ajustar)
    onSecondary = PagBlack,

    background = PagLightGray,      // Fundo padrão do app (telas Home, Dashboard)
    onBackground = PagDarkGray,     // Texto padrão no fundo

    surface = PagWhite,             // Fundo dos Cards, Menus, TextFields
    onSurface = PagDarkGray,        // Texto *digitado* no TextField, Texto em Cards

    surfaceVariant = PagLightGray,  // Fundo de componentes "neutros" (ex: ChatBubble do Bot)
    onSurfaceVariant = PagDarkGray,

    error = Color(0xFFB00020),      // Cor de erro padrão
    onError = Color.White
)

// --- Paleta de Cores ESCURA (Dark) ---
private val DarkColorScheme = darkColorScheme(
    primary = PagYellow,
    onPrimary = PagDarkGray, // Texto escuro em botões amarelos fica melhor

    primaryContainer = PagYellow.copy(alpha = 0.2f),
    onPrimaryContainer = PagWhite,

    secondary = PagGradientBottom,
    onSecondary = PagBlack,

    background = Color(0xFF121212), // Fundo escuro
    onBackground = PagWhite,        // Texto claro

    surface = Color(0xFF1E1E1E),    // Fundo de Cards escuros
    onSurface = PagWhite,           // Texto claro em Cards

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
            // Define a cor da Barra de Status (topo)
            window.statusBarColor = colorScheme.background.toArgb() // Usa a cor de fundo do app

            // Define se os ícones da Barra de Status são escuros (para fundo claro) ou claros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}