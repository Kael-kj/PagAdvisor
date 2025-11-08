package com.Kenji.pagadvisor.ui.screens.home.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagYellow
import java.text.NumberFormat

// Função helper para formatar moeda (R$)
fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(java.util.Locale("pt", "BR")).format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(
    viewModel: GoalViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostra o Snackbar quando a mensagem for atualizada
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarShown() // Limpa a mensagem
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (uiState.suggestedGoal > 0) {
                Text(
                    "Sugerimos uma meta de ${uiState.suggestedGoal.toCurrency()} com base em suas vendas.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    "Meta Semanal Atual",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // O valor grande e centralizado
            Text(
                text = uiState.sliderValue.toDouble().toCurrency(),
                style = MaterialTheme.typography.displayMedium,
                color = PagYellow,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // O Slider
            Slider(
                value = uiState.sliderValue,
                onValueChange = { viewModel.onSliderValueChanged(it) },
                valueRange = 0f..20000f, // Define um range (ex: 0 a 20k)
                steps = 199, // Permite "pulos" (ex: de 100 em 100)
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sugestões Rápidas (Chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val quickSuggestions = listOf(1000f, 2500f, 5000f, 10000f)
                quickSuggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = { viewModel.onSliderValueChanged(suggestion) },
                        label = { Text(suggestion.toInt().toString()) },
                        shape = CircleShape
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            PagPrimaryButton(
                onClick = { viewModel.onSaveGoalClicked() },
                text = "Salvar Nova Meta"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalScreenPreview() {
    PagAdvisorTheme {
        // Você precisará de um ViewModel de preview ou de mockar a factory
        // Por enquanto, apenas o layout básico
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Meta", style = MaterialTheme.typography.titleMedium)
            Text("R$ 5.000,00", style = MaterialTheme.typography.displayMedium, color = PagYellow)
            Slider(value = 0.5f, onValueChange = {})
        }
    }
}