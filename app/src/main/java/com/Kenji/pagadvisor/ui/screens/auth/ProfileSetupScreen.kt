package com.Kenji.pagadvisor.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.Kenji.pagadvisor.R // Certifique-se de que o R está importado
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.theme.* // Importe todas as suas cores e temas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavController,
    viewModel: ProfileSetupViewModel // Compartilha o ViewModel com a próxima tela
) {
    val uiState by viewModel.uiState.collectAsState()
    val showOtherBusinessTypeField = uiState.businessType == "Outro"

    val isFormValid by remember(
        uiState.businessName,
        uiState.businessType,
        uiState.otherBusinessType // Adiciona o novo campo na validação
    ) {
        derivedStateOf {
            uiState.businessName.isNotBlank() &&
                    (
                            (uiState.businessType.isNotBlank() && uiState.businessType != "Outro") || // Selecionou um tipo padrão
                                    (uiState.businessType == "Outro" && uiState.otherBusinessType.isNotBlank()) // Selecionou "Outro" e preencheu o campo
                            )
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
            .verticalScroll(rememberScrollState()), // Adicione scroll para telas menores
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
                        "Conhecendo seu negócio!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = PagYellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    PagOutlinedTextField(
                        value = uiState.businessName,
                        onValueChange = { viewModel.onBusinessNameChanged(it) },
                        label = "Nome do seu Negócio",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    BusinessDropdown(
                        selectedType = uiState.businessType,
                        onTypeSelected = { viewModel.onBusinessTypeSelected(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    if (showOtherBusinessTypeField) {
                        PagOutlinedTextField(
                            value = uiState.otherBusinessType,
                            onValueChange = { viewModel.onOtherBusinessTypeChanged(it) },
                            label = "Especifique seu Ramo",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    PagPrimaryButton(
                        onClick = { navController.navigate(Screen.ProfileInterests.route) },
                        text = "Continuar",
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
    ) {
        PagOutlinedTextField(
            value = selectedType.ifBlank { "Selecione" },
            onValueChange = {},
            label = "Seu Ramo",
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = PagYellow
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.fillMaxWidth(0.9f) // Controla a largura do menu
        ) {
            businessTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onTypeSelected(type)
                        isExpanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    PagAdvisorTheme {
        ProfileSetupScreen(rememberNavController(), viewModel())
    }
}