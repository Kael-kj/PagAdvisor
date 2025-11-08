package com.Kenji.pagadvisor.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){
    // Evento para navegar de volta ao Login após o sucesso
    private val _navigateToLoginEvent = MutableSharedFlow<Unit>()
    val navigateToLoginEvent = _navigateToLoginEvent.asSharedFlow()

    fun onRegisterClicked() {
        // TODO: Adicionar lógica real de registro (API, etc.)

        // Por enquanto, apenas simula o sucesso e emite o evento
        viewModelScope.launch {
            _navigateToLoginEvent.emit(Unit)
        }
    }
}