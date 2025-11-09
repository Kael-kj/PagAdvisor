package com.Kenji.pagadvisor.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagYellow

// 1. Data class mockada para a notificação
data class NotificationItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val date: String
)

// 2. Lista mockada de notificações
val mockNotifications = listOf(
    NotificationItem(
        icon = Icons.Default.PriceCheck,
        title = "Meta Semanal Atingida!",
        description = "Parabéns! Você atingiu 105% da sua meta de R$ 5.000,00.",
        date = "Hoje, 09:30"
    ),
    NotificationItem(
        icon = Icons.Default.Campaign,
        title = "Nova Dica de IA",
        description = "Sua vendas de 'Marmitas' estão baixas na quarta-feira. Veja esta dica.",
        date = "Ontem, 14:15"
    ),
    NotificationItem(
        icon = Icons.Default.PriceCheck,
        title = "Venda Recebida",
        description = "Pagamento de R$ 45,50 recebido via Pix.",
        date = "07/Nov, 18:02"
    )
)

// 3. A UI da Tela
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificações") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(mockNotifications) { notification ->
                NotificationListItem(item = notification)
            }
        }
    }
}

// 4. O Composable do item da lista
@Composable
fun NotificationListItem(item: NotificationItem) {
    ListItem(
        headlineContent = { Text(item.title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(item.description) },
        leadingContent = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = PagYellow,
                modifier = Modifier.size(32.dp)
            )
        },
        overlineContent = { Text(item.date) },
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Divider()
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    PagAdvisorTheme {
        NotificationScreen(rememberNavController())
    }
}