package com.Kenji.pagadvisor.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.screens.auth.BusinessDropdown // Reusa o Dropdown
import com.Kenji.pagadvisor.ui.screens.auth.productCategories // Reusa as categorias
import com.Kenji.pagadvisor.ui.screens.home.HomeViewModelFactory
import com.Kenji.pagadvisor.ui.theme.*

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

            // (Aqui falta a lógica de "Outro Produto")

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