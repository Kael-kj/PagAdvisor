package com.Kenji.pagadvisor.ui.screens.home.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    viewModel: GoalViewModel,
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
        // Usamos LazyColumn para a tela inteira rolar (incluindo o histórico)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- ITEM 1: Abas de Seleção ---
            item {
                TabRow(
                    selectedTabIndex = uiState.selectedTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    GoalType.values().forEach { tab ->
                        Tab(
                            selected = uiState.selectedTab == tab,
                            onClick = { viewModel.onTabSelected(tab) },
                            text = { Text(tab.name) }
                        )
                    }
                }
            }

            // --- ITEM 2: Painel do Slider ---
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (uiState.suggestedGoal > 0) {
                        Text(
                            "Sugerimos uma meta de ${uiState.suggestedGoal.toCurrency()} para este período.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val currentGoal = when(uiState.selectedTab) {
                            GoalType.Semanal -> uiState.weeklyGoal
                            GoalType.Mensal -> uiState.monthlyGoal
                            GoalType.Anual -> uiState.annualGoal
                        }
                        Text(
                            "Meta ${uiState.selectedTab.name} Atual",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.sliderValue.toDouble().toCurrency(),
                        style = MaterialTheme.typography.displayMedium,
                        color = PagYellow,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Slider(
                        value = uiState.sliderValue,
                        onValueChange = { viewModel.onSliderValueChanged(it) },
                        // Range dinâmico, remove o limite de 20k
                        valueRange = 0f..100000f, // (Ex: 0 a 100k)
                        // steps = 199, // Removido para ajuste fino
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    PagPrimaryButton(
                        onClick = { viewModel.onSaveGoalClicked() },
                        text = "Salvar Meta ${uiState.selectedTab.name}"
                    )
                }
            }

            // --- ITEM 3: Histórico de Metas ---
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 24.dp))
                    Text(
                        "Histórico de Metas",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Lista mockada de histórico
            items(5) { index ->
                GoalHistoryItem(
                    title = "Meta Semanal (0${index + 1}/Nov)",
                    value = "R$ 5.000,00",
                    status = if (index % 2 == 0) "Atingida (110%)" else "Não Atingida (85%)",
                    isAchieved = index % 2 == 0
                )
            }
        }
    }
}

@Composable
fun GoalHistoryItem(title: String, value: String, status: String, isAchieved: Boolean) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(status) },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(value, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Atingida",
                    tint = if (isAchieved) Color(0xFF006D5E) else Color.Transparent, // Verde PagBank
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GoalScreenPreview() {
    PagAdvisorTheme {
        // Preview da nova tela com abas
        Column {
            TabRow(selectedTabIndex = 0) {
                Tab(selected = true, onClick = {}, text = { Text("Semanal") })
                Tab(selected = false, onClick = {}, text = { Text("Mensal") })
                Tab(selected = false, onClick = {}, text = { Text("Anual") })
            }
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Meta Semanal Atual", style = MaterialTheme.typography.titleMedium)
                Text("R$ 5.000,00", style = MaterialTheme.typography.displayMedium, color = PagYellow)
                Slider(value = 0.5f, onValueChange = {})
                PagPrimaryButton(onClick = {}, text = "Salvar Meta")
                Divider(modifier = Modifier.padding(vertical = 24.dp))
                GoalHistoryItem("Meta Semanal (01/Nov)", "R$ 5.000", "Atingida (110%)", true)
            }
        }
    }
}