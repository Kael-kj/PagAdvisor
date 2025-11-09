package com.Kenji.pagadvisor.ui.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kenji.pagadvisor.PagAdvisorApp
import com.Kenji.pagadvisor.domain.repository.SalesRepository // Importe o SalesRepository
import com.Kenji.pagadvisor.domain.usecase.*
import com.Kenji.pagadvisor.ui.screens.auth.ProfileSetupViewModel
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatViewModel
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardViewModel
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalViewModel
import com.Kenji.pagadvisor.domain.usecase.LogoutUseCase
import com.Kenji.pagadvisor.ui.screens.profile.ProfileViewModel

class HomeViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContainer = (app as PagAdvisorApp)
        val salesRepo = appContainer.salesRepository
        val advisorRepo = appContainer.pagAdvisorRepository

        // UseCases para o Dashboard e Chat
        val getMockedSalesUseCase = GetMockedSalesUseCase(salesRepo)
        val getUserProfileUseCase = GetUserProfileUseCase(salesRepo)
        val getReceivablesUseCase = GetReceivablesUseCase(salesRepo)
        val getCustomerStatsUseCase = GetCustomerStatsUseCase(salesRepo)
        val saveUserProfileUseCase = SaveUserProfileUseCase(salesRepo)

        // 👇 UseCases de Metas (com nomes atualizados e novos)
        val getWeeklyGoalUseCase = GetWeeklyGoalUseCase(salesRepo)
        val setWeeklyGoalUseCase = SetWeeklyGoalUseCase(salesRepo)
        // (Vamos criar os UseCases de Monthly/Annual depois, por enquanto o ViewModel acessa o repo)

        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(
                    getMockedSalesUseCase,
                    getWeeklyGoalUseCase, // 👈 Nome atualizado
                    salesRepo,
                    getReceivablesUseCase,
                    getCustomerStatsUseCase
                ) as T
            }

            modelClass.isAssignableFrom(GoalViewModel::class.java) -> {
                GoalViewModel(
                    getWeeklyGoalUseCase, // 👈 Nome atualizado
                    setWeeklyGoalUseCase, // 👈 Nome atualizado
                    getMockedSalesUseCase,
                    salesRepo // Injeta o repositório para salvar todas as metas
                ) as T
            }

            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(
                    GetAiAnalysisUseCase(advisorRepo),
                    getMockedSalesUseCase,
                    getWeeklyGoalUseCase, // 👈 Nome atualizado
                    getUserProfileUseCase,
                    salesRepo
                ) as T
            }

            modelClass.isAssignableFrom(ProfileSetupViewModel::class.java) -> {
                ProfileSetupViewModel(
                    SaveUserProfileUseCase(salesRepo)
                ) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    getUserProfileUseCase,
                    saveUserProfileUseCase,
                    LogoutUseCase(salesRepo)
                ) as T
            }

            else -> throw IllegalArgumentException("Classe de ViewModel Desconhecida: ${modelClass.name}")
        }
    }
}