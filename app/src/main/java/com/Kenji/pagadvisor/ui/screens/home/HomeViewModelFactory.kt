package com.Kenji.pagadvisor.ui.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kenji.pagadvisor.PagAdvisorApp
import com.Kenji.pagadvisor.domain.usecase.*
import com.Kenji.pagadvisor.ui.screens.auth.ProfileSetupViewModel
import com.Kenji.pagadvisor.ui.screens.home.chat.ChatViewModel
import com.Kenji.pagadvisor.ui.screens.home.dashboard.DashboardViewModel
import com.Kenji.pagadvisor.ui.screens.home.goals.GoalViewModel

class HomeViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContainer = (app as PagAdvisorApp)
        val salesRepo = appContainer.salesRepository
        val advisorRepo = appContainer.pagAdvisorRepository

        // UseCases necessários
        val getSalesGoalUseCase = GetSalesGoalUseCase(salesRepo)
        val getMockedSalesUseCase = GetMockedSalesUseCase(salesRepo)

        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(
                    getMockedSalesUseCase,
                    getSalesGoalUseCase,
                    salesRepo // 👈 (Vamos precisar disso na Fase 3)
                ) as T
            }

            // 👇 LÓGICA ATUALIZADA PARA O GOALVIEWMODEL
            modelClass.isAssignableFrom(GoalViewModel::class.java) -> {
                GoalViewModel(
                    getSalesGoalUseCase,
                    SetSalesGoalUseCase(salesRepo),
                    getMockedSalesUseCase,
                    salesRepo // Injeta o repositório para salvar a meta diária
                ) as T
            }

            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(
                    GetAiAnalysisUseCase(advisorRepo),
                    getMockedSalesUseCase,
                    getSalesGoalUseCase,
                    GetUserProfileUseCase(salesRepo), // 👈 (Vamos precisar disso na Fase 3)
                    salesRepo // 👈 (Vamos precisar disso na Fase 3)
                ) as T
            }

            modelClass.isAssignableFrom(ProfileSetupViewModel::class.java) -> {
                ProfileSetupViewModel(
                    SaveUserProfileUseCase(salesRepo)
                ) as T
            }

            else -> throw IllegalArgumentException("Classe de ViewModel Desconhecida: ${modelClass.name}")
        }
    }
}