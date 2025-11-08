package com.Kenji.pagadvisor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.ui.screens.auth.AuthScreen
import com.Kenji.pagadvisor.ui.screens.auth.ConfirmPasswordScreen
import com.Kenji.pagadvisor.ui.screens.auth.ForgotPasswordScreen
import com.Kenji.pagadvisor.ui.screens.auth.RegisterScreen
import com.Kenji.pagadvisor.ui.screens.onboarding.OnboardingScreen
import com.Kenji.pagadvisor.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        composable(route = Screen.Login.route) {
            AuthScreen(navController = navController)
        }


        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(route = Screen.ConfirmPassword.route) {
            ConfirmPasswordScreen(navController = navController)
        }

        composable(route = Screen.Home.route) {
            // TODO: Criar a HomeScreen
        }
    }
}