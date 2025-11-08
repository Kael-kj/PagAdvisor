package com.Kenji.pagadvisor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import android.app.Application
import com.Kenji.pagadvisor.ui.screens.auth.*
import com.Kenji.pagadvisor.ui.screens.onboarding.*
import com.Kenji.pagadvisor.ui.screens.splash.*
import com.Kenji.pagadvisor.ui.screens.home.*


// Define uma rota "mãe" para o fluxo de autenticação
const val AUTH_GRAPH_ROUTE = "auth_graph"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = HomeViewModelFactory(context.applicationContext as Application)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // --- Fluxo Padrão ---
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        // --- Fluxo de Auth (Agora é um Gráfico Aninhado) ---
        authGraph(navController, factory)

        // --- Tela Principal ---
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}

/**
 * Cria um gráfico de navegação aninhado para todo o fluxo de Auth.
 * Isso permite que todas as telas (Login, Register, ProfileSetup, ProfileInterests)
 * compartilhem o mesmo ProfileSetupViewModel.
 */
fun NavGraphBuilder.authGraph(navController: NavHostController, factory: HomeViewModelFactory) {
    navigation(
        startDestination = Screen.Login.route,
        route = AUTH_GRAPH_ROUTE
    ) {

        composable(Screen.Login.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screen.ProfileSetup.route) {
            val viewModel: ProfileSetupViewModel = viewModel(factory = factory)
            ProfileSetupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.ProfileInterests.route) {
            val viewModel: ProfileSetupViewModel = viewModel(factory = factory)
            ProfileInterestsScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(route = Screen.ConfirmPassword.route) {
            ConfirmPasswordScreen(navController = navController)
        }
    }
}