package com.Kenji.pagadvisor.ui.screens.home.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kenji.pagadvisor.ui.theme.PagYellow
import java.text.NumberFormat
import java.util.Locale

// Função helper para formatar moeda (R$)
fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
}

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    "Seu Resumo Semanal",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Cards de Resumo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // --- 👇 CARD ATUALIZADO (Meta Diária) ---
                    SummaryCard(
                        title = "Meta de Hoje",
                        value = uiState.dailyGoal.toCurrency(),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Total Vendido (Semana)",
                        value = uiState.totalSales.toCurrency(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Progresso
            item {
                // Barra de progresso agora se baseia na meta semanal
                val progress = (uiState.totalSales / uiState.salesGoal).toFloat()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Progresso da Meta Semanal (${uiState.salesGoal.toCurrency()})",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = PagYellow
                )
                Text(
                    text = "${(progress * 100).toInt()}% da meta atingida",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Lista de Vendas
            item {
                Text(
                    "Vendas da Semana",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(uiState.salesList) { sale ->
                SaleItem(day = sale.dayOfWeek, amount = sale.totalSold.toCurrency())
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun SaleItem(day: String, amount: String) {
    ListItem(
        headlineContent = { Text(day) },
        trailingContent = { Text(amount, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary) }
    )
    Divider()
}