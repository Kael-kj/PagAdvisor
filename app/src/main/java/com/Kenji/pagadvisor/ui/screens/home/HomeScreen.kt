package com.Kenji.pagadvisor.ui.screens.home

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatScreen
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatViewModel
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardScreen
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardViewModel
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalScreen
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalViewModel
import com.Kenji.pagadvisor.ui.screens.home.HomeViewModelFactory
// --- Definição das Rotas da Home ---
sealed class HomeRoute(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : HomeRoute("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Goals : HomeRoute("goals", "Metas", Icons.Default.TrackChanges)
    object Chat : HomeRoute("chat", "Chat", Icons.Default.Chat)
}
val homeScreens = listOf(HomeRoute.Dashboard, HomeRoute.Goals, HomeRoute.Chat)


// --- A Tela Principal ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeNavController = rememberNavController()

    // Instancia nossa Factory
    val context = LocalContext.current
    val factory = HomeViewModelFactory(context.applicationContext as Application)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = homeNavController)
        }
    ) { innerPadding ->
        // O NavHost interno que gerencia as 3 telas
        NavHost(
            navController = homeNavController,
            startDestination = HomeRoute.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HomeRoute.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(vm)
            }
            composable(HomeRoute.Goals.route) {
                val vm: GoalViewModel = viewModel(factory = factory)
                GoalScreen(vm)
            }
            composable(HomeRoute.Chat.route) {
                val vm: ChatViewModel = viewModel(factory = factory)
                ChatScreen(vm)
            }
        }
    }
}

// --- Componente da Barra de Navegação ---
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        homeScreens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Evita empilhar a mesma tela várias vezes
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}