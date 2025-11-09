package com.Kenji.pagadvisor.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.screens.auth.BusinessDropdown
import com.Kenji.pagadvisor.ui.screens.auth.productCategories
import com.Kenji.pagadvisor.ui.screens.home.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    factory: HomeViewModelFactory // Recebe a factory para criar o ViewModel
) {
    val viewModel: ProfileViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    // Navega para Login após o logout
    LaunchedEffect(key1 = true) {
        viewModel.navigateToLogin.collect {
            navController.navigate(Screen.Login.route) {
                // Limpa toda a pilha de navegação
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil e Negócio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Seção 1: Dados do Negócio ---
            Text("Meu Negócio", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            PagOutlinedTextField(
                value = uiState.businessName,
                onValueChange = { viewModel.onBusinessNameChanged(it) },
                label = "Nome do Negócio"
            )
            Spacer(modifier = Modifier.height(16.dp))

            BusinessDropdown(
                selectedType = uiState.businessType,
                onTypeSelected = { viewModel.onBusinessTypeSelected(it) },
                modifier = Modifier.fillMaxWidth()
            )

            // (Aqui falta a lógica de "Outro Ramo", pode ser adicionada depois)

            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            Spacer(modifier = Modifier.height(32.dp))

            // --- Seção 2: Produtos/Serviços ---
            Text("Meus Produtos/Serviços", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

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
                        label = { Text(category) },
                        // (Estilos do FilterChip)
                    )
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            // --- Seção 3: Ações ---
            PagPrimaryButton(
                onClick = { viewModel.onSaveClicked() },
                text = "Salvar Alterações"
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { viewModel.onLogoutClicked() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = SolidColor(MaterialTheme.colorScheme.error)
                )
            ) {
                Text("Sair (Logout)")
            }
        }
    }
}