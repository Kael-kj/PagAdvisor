package com.Kenji.pagadvisor.ui.screens.home

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.Kenji.pagadvisor.ui.navigation.Screen // Importe o Screen
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatScreen
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatViewModel
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardScreen
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardViewModel
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalScreen
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalViewModel
import com.Kenji.pagadvisor.ui.theme.PagDarkGray
import com.Kenji.pagadvisor.ui.theme.PagYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController // O NavController PRINCIPAL
) {
    val homeNavController = rememberNavController() // O NavController INTERNO
    val context = LocalContext.current
    val factory = HomeViewModelFactory(context.applicationContext as Application)

    // 1. O scrollBehavior FOI REMOVIDO daqui.

    Scaffold(
        // 2. O topBar FOI REMOVIDO daqui.
        // 3. O modifier nestedScroll FOI REMOVIDO daqui.

        bottomBar = {
            BottomNavigationBar(navController = homeNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = HomeRoute.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 4. Passamos o NavController PRINCIPAL para o Dashboard
            composable(HomeRoute.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(vm, navController) // Passa o navController principal
            }
            // 5. Removemos o scrollBehavior das outras telas
            composable(HomeRoute.Goals.route) {
                val vm: GoalViewModel = viewModel(factory = factory)
                GoalScreen(vm) // Não precisa mais do scrollBehavior
            }
            composable(HomeRoute.Chat.route) {
                val vm: ChatViewModel = viewModel(factory = factory)
                ChatScreen(vm) // Não precisa mais do scrollBehavior
            }
        }
    }
}

// --- (BottomNavigationBar e ViewModelFactory permanecem iguais) ---
// (Certifique-se que o HomeViewModelFactory está em seu próprio arquivo)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        homeScreens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        tint = if (navController.currentDestination?.route == screen.route) PagYellow else PagDarkGray
                    )
                },
                label = {
                    Text(
                        screen.label,
                        color = if (navController.currentDestination?.route == screen.route) PagYellow else PagDarkGray
                    )
                },
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(route = screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surface,
                    selectedIconColor = PagYellow,
                    selectedTextColor = PagYellow,
                    unselectedIconColor = PagDarkGray,
                    unselectedTextColor = PagDarkGray
                )
            )
        }
    }
}