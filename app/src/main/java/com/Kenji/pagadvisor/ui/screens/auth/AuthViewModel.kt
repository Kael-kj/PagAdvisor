package com.Kenji.pagadvisor.ui.screens.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Esta função será chamada quando o usuário clicar em "Entrar".
     * Aqui é onde você colocaria a lógica de Retrofit, mas por enquanto,
     * vamos apenas simular um sucesso e mandar o evento de navegação.
     */
    fun onLoginClicked() {
        viewModelScope.launch {
            // TODO: Adicionar lógica de login real aqui (Fase 2/3)

            // Emite o evento para navegar para a Home
            _navigationEvent.emit(Unit)
        }
    }
}