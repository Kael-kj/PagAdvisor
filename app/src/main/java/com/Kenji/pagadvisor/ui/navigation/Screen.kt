package com.Kenji.pagadvisor.ui.navigation

/**
 * Define todas as telas (rotas) do aplicativo para o Jetpack Navigation.
 *
 * Usar uma sealed class para as rotas garante que apenas as telas definidas aqui possam ser
 * usadas na navegação, prevenindo erros de digitação e tornando o código mais seguro e legível.
 *
 * @property route A string única que identifica a tela para o NavController.
 */
sealed class Screen(val route: String) {
    /** Tela de Splash inicial do aplicativo. */
    object Splash : Screen("splash_screen")

    /** Tela de Onboarding para novos usuários. */
    object Onboarding : Screen("onboarding_screen")

    // --- Fluxo de Autenticação ---

    /** Tela de Login. */
    object Login : Screen("login_screen")

    /** Tela de Registro de novo usuário. */
    object Register : Screen("register_screen")

    /** Tela para iniciar o processo de recuperação de senha. */
    object ForgotPassword : Screen("forgot_password_screen")

    /** Tela para confirmar a nova senha (após recuperação). */
    object ConfirmPassword : Screen("confirm_password_screen")

    // --- Fluxo de Configuração de Perfil ---

    /** Tela para o usuário configurar informações básicas do perfil. */
    object ProfileSetup : Screen("profile_setup_screen")

    /** Tela para o usuário selecionar seus interesses de negócio. */
    object ProfileInterests : Screen("profile_interests_screen")

    // --- Telas Principais do App ---

    /** Tela principal que contém a navegação com abas (Dashboard, Metas, Chat). */
    object Home : Screen("home_screen")

    /** Tela de Notificações. */
    object Notifications : Screen("notifications_screen")

    /** Tela de Perfil do usuário. */
    object Profile : Screen("profile_screen")
}