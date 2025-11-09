package com.Kenji.pagadvisor.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define as rotas de navegação para as telas principais acessíveis a partir da Bottom Navigation Bar.
 *
 * Esta classe selada (sealed class) garante que apenas as rotas definidas aqui possam ser usadas,
 * proporcionando segurança de tipo (type-safety).
 *
 * @property route A string única que identifica a rota para o Navigation Component.
 * @property label O texto que será exibido no item da Bottom Navigation Bar.
 * @property icon O ícone que será exibido no item da Bottom Navigation Bar.
 */
sealed class HomeRoute(val route: String, val label: String, val icon: ImageVector) {
    /** Representa a tela do Dashboard. */
    object Dashboard : HomeRoute("dashboard", "Dashboard", Icons.Default.Dashboard)

    /** Representa a tela de Metas. */
    object Goals : HomeRoute("goals", "Metas", Icons.Default.TrackChanges)

    /** Representa a tela de Chat com a IA. */
    object Chat : HomeRoute("chat", "Chat", Icons.Default.Chat)
}

/**
 * Uma lista contendo todas as rotas da tela Home.
 * Usada para popular dinamicamente os itens da Bottom Navigation Bar.
 */
val homeScreens = listOf(HomeRoute.Dashboard, HomeRoute.Goals, HomeRoute.Chat)
