package com.Kenji.pagadvisor.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.R
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagDarkGray
import com.Kenji.pagadvisor.ui.theme.PagGradientBottom
import com.Kenji.pagadvisor.ui.theme.PagGradientTop
import com.Kenji.pagadvisor.ui.theme.PagLightGray
import com.Kenji.pagadvisor.ui.theme.PagMediumGray
import com.Kenji.pagadvisor.ui.theme.PagVerde
import com.Kenji.pagadvisor.ui.theme.PagWhite
import com.Kenji.pagadvisor.ui.theme.PagYellow


val productCategories = listOf(
    "Alimentos e Bebidas", "Eletrônicos e Tecnologia", "Roupas e Acessórios",
    "Casa e Decoração", "Saúde e Bem-Estar", "Serviços Digitais",
    "Serviços Pessoais", "Artesanato e Personalizados", "Baixo Ticket", "Médio Ticket", "Alto Ticket",
    "Outro" // Para o usuário digitar
)


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileInterestsScreen(
    navController: NavController,
    viewModel: ProfileSetupViewModel = viewModel() // Compartilha o mesmo ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Novo estado para o campo "Outro Produto"
    val showOtherProductField = uiState.selectedProducts.contains("Outro")

    // Validação do botão
    val isFormValid by remember(uiState.selectedProducts, uiState.otherProduct) {
        derivedStateOf {
            if (uiState.selectedProducts.contains("Outro")) {
                uiState.otherProduct.isNotBlank() // Se selecionou "Outro", o campo deve estar preenchido
            } else {
                uiState.selectedProducts.isNotEmpty() // Se não selecionou "Outro", deve ter pelo menos um chip selecionado
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.navigateToNextScreen.collect {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(PagGradientTop, PagGradientBottom),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PagVerde)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PagWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_pagadvisor),
                        contentDescription = "Logo PagAdvisor",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        "Selecione o tipo de produto",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = PagYellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "Tipo de Negócio: ${uiState.businessType}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        productCategories.forEach { category ->
                            val isSelected = uiState.selectedProducts.contains(category)

                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.onProductToggled(category) },
                                label = { Text(category, style = MaterialTheme.typography.bodyMedium) },
                                enabled = true,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PagYellow.copy(alpha = 0.2f), // Amarelo claro quando selecionado
                                    selectedLabelColor = PagDarkGray, // Texto escuro em fundo claro
                                    containerColor = PagLightGray,
                                    labelColor = PagDarkGray,
                                    disabledContainerColor = PagLightGray.copy(alpha = 0.5f),
                                    disabledLabelColor = PagDarkGray.copy(alpha = 0.5f)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = PagMediumGray,
                                    selectedBorderColor = PagYellow, // Borda amarela mais forte
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (showOtherProductField) {
                        PagOutlinedTextField(
                            value = uiState.otherProduct,
                            onValueChange = { viewModel.onOtherProductChanged(it) },
                            label = "Especifique seu Produto/Serviço",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    PagPrimaryButton(
                        onClick = { viewModel.onFinishClicked() },
                        text = "Finalizar e Entrar",
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileInterestsScreenPreview() {
    PagAdvisorTheme {
        ProfileInterestsScreen(rememberNavController(), viewModel())
    }
}