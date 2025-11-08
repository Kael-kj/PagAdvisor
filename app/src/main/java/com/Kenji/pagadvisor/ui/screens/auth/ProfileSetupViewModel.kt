package com.Kenji.pagadvisor.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.Kenji.pagadvisor.domain.usecase.SaveUserProfileUseCase

// Define os ramos de negócio que o usuário pode escolher
val businessTypes = listOf("Restaurante", "Loja de Varejo", "Serviços", "Outro")

val productMap = mapOf(
    "Restaurante" to listOf("Marmitas", "Prato Feito", "Lanches", "Sobremesas", "Bebidas", "Pizza", "Sushi"),
    "Loja de Varejo" to listOf("Roupas", "Eletrônicos", "Calçados", "Acessórios", "Cosméticos", "Livros"),
    "Serviços" to listOf("Manutenção", "Consultoria", "Limpeza", "Aulas", "Design", "Programação")
)

// Estado da UI para o fluxo de setup
data class ProfileSetupState(
    val businessName: String = "",
    val businessType: String = "",
    val otherBusinessType: String = "",
    val availableProducts: List<String> = emptyList(),
    val selectedProducts: Set<String> = emptySet(),
    val otherProduct: String = ""
)

class ProfileSetupViewModel(private val saveUserProfileUseCase: SaveUserProfileUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupState())
    val uiState = _uiState.asStateFlow()

    // Evento para navegar para a próxima tela
    private val _navigateToNextScreen = MutableSharedFlow<Unit>()
    val navigateToNextScreen = _navigateToNextScreen.asSharedFlow()

    fun onBusinessNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(businessName = name)
    }

    fun onBusinessTypeSelected(type: String) {
        _uiState.value = _uiState.value.copy(
            businessType = type,
            otherBusinessType = if (type != "Outro") "" else _uiState.value.otherBusinessType, // Limpa se não for "Outro"
            availableProducts = productMap[type] ?: emptyList() // Ainda pode ser útil
        )
    }

    fun onOtherBusinessTypeChanged(otherType: String) {
        _uiState.value = _uiState.value.copy(otherBusinessType = otherType)
    }

    fun onProductToggled(product: String) {
        val currentSelected = _uiState.value.selectedProducts.toMutableSet()
        if (currentSelected.contains(product)) {
            currentSelected.remove(product)
        } else {
            currentSelected.add(product)
        }
        _uiState.value = _uiState.value.copy(selectedProducts = currentSelected)
    }

    fun onOtherProductChanged(otherProd: String) {
        _uiState.value = _uiState.value.copy(otherProduct = otherProd)
    }

    fun onFinishClicked() {
        viewModelScope.launch {
            val state = _uiState.value

            // Lógica para formatar os dados antes de salvar
            val finalType = if (state.businessType == "Outro") {
                "Outro: ${state.otherBusinessType}"
            } else {
                state.businessType
            }

            val finalProducts = state.selectedProducts.toMutableSet()
            if (state.otherProduct.isNotBlank()) {
                finalProducts.remove("Outro") // Remove o placeholder
                finalProducts.add("Outro: ${state.otherProduct}") // Adiciona o valor real
            }

            saveUserProfileUseCase(
                name = state.businessName,
                type = finalType,
                products = finalProducts
            )

            _navigateToNextScreen.emit(Unit)
        }
    }
}