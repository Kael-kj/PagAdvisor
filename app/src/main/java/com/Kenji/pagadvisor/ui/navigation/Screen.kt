package com.Kenji.pagadvisor.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")

    // O Fluxo de Auth começa aqui
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object ConfirmPassword : Screen("confirm_password_screen")

    // Tela Principal
    object Home : Screen("home_screen")
}