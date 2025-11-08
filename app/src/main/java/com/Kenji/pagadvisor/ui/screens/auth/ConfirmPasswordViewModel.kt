package com.Kenji.pagadvisor.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ConfirmPasswordViewModel : ViewModel() {

    // Evento para navegar de volta ao Login
    private val _navigateToLoginEvent = MutableSharedFlow<Unit>()
    val navigateToLoginEvent = _navigateToLoginEvent.asSharedFlow()

    fun onConfirmNewPasswordClicked() {
        // TODO: Lógica de API para redefinir a senha

        viewModelScope.launch {
            _navigateToLoginEvent.emit(Unit)
        }
    }
}