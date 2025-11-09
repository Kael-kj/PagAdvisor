package com.Kenji.pagadvisor.ui.screens.home.dashboard

import androidx.compose.foundation.Image // Importado
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Importado
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource // Importado
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Importado
import com.Kenji.pagadvisor.R // Importado
import com.Kenji.pagadvisor.ui.navigation.Screen // Importado
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagDarkGray
import com.Kenji.pagadvisor.ui.theme.PagYellow
import java.text.NumberFormat
import java.util.Locale

// Função helper para formatar moeda (R$)
fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavController // Recebe o NavController principal
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background), // Fundo cinza claro
            contentPadding = PaddingValues(bottom = 16.dp) // Padding só embaixo
        ) {

            // --- ITEM 1: Card Verde + Ícones Flutuantes ---
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Conteúdo do Card (Saldo)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            .background(Color(0xFF006D5E)) // Verde PagBank
                            .padding(start = 24.dp, end = 24.dp, bottom = 32.dp, top = 80.dp) // Padding superior para os ícones
                    ) {
                        Text(
                            "Saldo disponível",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            "R$ 2.500,00", // Mockado
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Total da conta: R$ 5.303,00", // Mockado
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Ícones Flutuantes (Logo + Ações)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_pagadvisor_no_text ),
                            contentDescription = "PagAdvisor Logo",
                            modifier = Modifier.size(100.dp)
                        )
                        Row {
                            IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                                Icon(Icons.Default.Notifications, "Notificações", tint = Color.White)
                            }
                            IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                                Icon(Icons.Default.AccountCircle, "Perfil", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // --- ITEM 2: Novos Cards de Resumo ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Resumo do Negócio",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    SummaryActionCard(
                        icon = Icons.Default.Payments,
                        title = "Vendas a Receber",
                        value = uiState.receivables.toCurrency(),
                        description = "Total a ser creditado"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryActionCard(
                        icon = Icons.Default.BarChart,
                        title = "Meta de Hoje",
                        value = uiState.dailyGoal.toCurrency(),
                        description = "Meta semanal: ${uiState.salesGoal.toCurrency()}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryActionCard(
                        icon = Icons.Default.Groups,
                        title = "Meus Clientes",
                        value = "${uiState.customerStats.totalCustomers} Clientes",
                        description = "+${uiState.customerStats.newCustomersThisWeek} esta semana"
                    )
                }
            }

            // --- ITEM 3: Lista de Vendas da Semana ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Vendas da Semana",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            items(uiState.salesList) { sale ->
                SaleItem(
                    day = sale.dayOfWeek,
                    amount = sale.totalSold.toCurrency(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

/**
 * O novo card de resumo branco (estilo PagBank)
 */
@Composable
fun SummaryActionCard(
    icon: ImageVector,
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Fundo Branco
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = PagDarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * O item da lista de vendas
 */
@Composable
fun SaleItem(day: String, amount: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(day, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(amount, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = PagDarkGray)
    }
    Divider(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    PagAdvisorTheme {
        // Esta tela é complexa para preview, mas podemos tentar
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Preview do Box com Card e Ícones
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            .background(Color(0xFF006D5E))
                            .padding(start = 24.dp, end = 24.dp, bottom = 32.dp, top = 80.dp)
                    ) {
                        Text("Saldo disponível", color = Color.White.copy(alpha = 0.8f))
                        Text("R$ 2.500,00", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("", color = Color.White) // Placeholder para a logo
                        Row {
                            Icon(Icons.Default.Notifications, "Notificações", tint = Color.White)
                            Icon(Icons.Default.AccountCircle, "Perfil", tint = Color.White)
                        }
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Resumo do Negócio", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
                    SummaryActionCard(
                        icon = Icons.Default.Payments,
                        title = "Vendas a Receber",
                        value = "R$ 4.300,00",
                        description = "Total a ser creditado"
                    )
                }
            }
        }
    }
}