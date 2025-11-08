package com.Kenji.pagadvisor.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){
    // Evento para navegar de volta ao Login após o sucesso
    private val _navigateToProfileSetupEvent = MutableSharedFlow<Unit>()
    val navigateToProfileSetupEvent = _navigateToProfileSetupEvent.asSharedFlow()

    fun onRegisterClicked() {
        // TODO: Adicionar lógica real de registro (API, etc.)

        // Por enquanto, apenas simula o sucesso e emite o evento
        viewModelScope.launch {
            _navigateToProfileSetupEvent.emit(Unit)
        }
    }
}