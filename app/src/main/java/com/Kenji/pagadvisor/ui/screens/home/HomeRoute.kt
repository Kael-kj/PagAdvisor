package com.Kenji.pagadvisor.ui.screens.home


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.ui.graphics.vector.ImageVector

// Define as 3 telas da sua barra de navegação inferior
sealed class HomeRoute(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : HomeRoute("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Goals : HomeRoute("goals", "Metas", Icons.Default.TrackChanges)
    object Chat : HomeRoute("chat", "Chat", Icons.Default.Chat)
}

// Cria a lista que a BottomBar vai usar
val homeScreens = listOf(HomeRoute.Dashboard, HomeRoute.Goals, HomeRoute.Chat)