package com.Kenji.pagadvisor.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    // Estado para controlar se o código já foi enviado
    private val _isCodeSent = MutableStateFlow(false)
    val isCodeSent = _isCodeSent.asStateFlow()

    // Evento para navegar para a próxima tela
    private val _navigateToConfirmPassword = MutableSharedFlow<Unit>()
    val navigateToConfirmPassword = _navigateToConfirmPassword.asSharedFlow()

    fun onSendCodeClicked() {
        // TODO: Lógica de API para enviar o código
        _isCodeSent.value = true
    }

    fun onConfirmClicked() {
        // TODO: Lógica de API para validar o código

        // Simula sucesso e navega
        viewModelScope.launch {
            _navigateToConfirmPassword.emit(Unit)
        }
    }
}