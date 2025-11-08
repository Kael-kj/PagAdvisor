package com.Kenji.pagadvisor.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    // Estado da página atual do Onboarding
    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    // Evento para navegar para a próxima tela (Auth ou Home)
    private val _navigateToNextScreenEvent = MutableSharedFlow<Unit>()
    val navigateToNextScreenEvent = _navigateToNextScreenEvent.asSharedFlow()

    // Total de páginas
    val totalPages = 3

    fun onNextPageClicked() {
        if (_currentPage.value < totalPages - 1) {
            // Avança para a próxima página
            _currentPage.value++
        } else {
            // Se for a última página, navega para a próxima tela (Auth)
            markOnboardingViewedAndNavigate()
        }
    }

    private fun markOnboardingViewedAndNavigate() {
        viewModelScope.launch {
            // TODO: Aqui vamos usar o DataStore para salvar que o onboarding foi visto (Fase 2)
            // Por enquanto, apenas emite o evento de navegação.
            _navigateToNextScreenEvent.emit(Unit)
        }
    }
    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }
}