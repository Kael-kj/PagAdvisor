package com.Kenji.pagadvisor.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kenji.pagadvisor.domain.usecase.GetUserProfileUseCase
import com.Kenji.pagadvisor.domain.usecase.LogoutUseCase
import com.Kenji.pagadvisor.domain.usecase.SaveUserProfileUseCase
import com.Kenji.pagadvisor.ui.screens.auth.ProfileSetupState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupState())
    val uiState = _uiState.asStateFlow()

    // Evento para navegar para a tela de Login
    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().firstOrNull()?.let { profile ->
                // Mapeia os dados do perfil para o UiState
                // (Aqui teríamos que fazer a lógica inversa de "Outro: ...",
                // mas vamos manter simples por enquanto e apenas carregar os dados brutos)
                _uiState.value = ProfileSetupState(
                    businessName = profile.businessName,
                    businessType = profile.businessType,
                    selectedProducts = profile.businessProducts
                )
            }
        }
    }

    // (As funções de 'on...Changed' são idênticas ao ProfileSetupViewModel)
    fun onBusinessNameChanged(name: String) { _uiState.update { it.copy(businessName = name) } }
    fun onBusinessTypeSelected(type: String) { _uiState.update { it.copy(businessType = type) } }
    fun onOtherBusinessTypeChanged(otherType: String) { _uiState.update { it.copy(otherBusinessType = otherType) } }
    fun onProductToggled(product: String) {
        val current = _uiState.value.selectedProducts.toMutableSet()
        if (current.contains(product)) current.remove(product) else current.add(product)
        _uiState.update { it.copy(selectedProducts = current) }
    }
    fun onOtherProductChanged(otherProd: String) { _uiState.update { it.copy(otherProduct = otherProd) } }


    fun onSaveClicked() {
        viewModelScope.launch {
            val state = _uiState.value
            // (A mesma lógica de formatação do ProfileSetupViewModel)
            val finalType = if (state.businessType == "Outro") "Outro: ${state.otherBusinessType}" else state.businessType
            val finalProducts = state.selectedProducts.toMutableSet().apply {
                if (state.otherProduct.isNotBlank()) {
                    remove("Outro"); add("Outro: ${state.otherProduct}")
                }
            }
            saveUserProfileUseCase(state.businessName, finalType, finalProducts)
            // TODO: Mostrar um Snackbar de "Salvo!"
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase()
            _navigateToLogin.emit(Unit)
        }
    }
}