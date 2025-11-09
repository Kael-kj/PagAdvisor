package com.Kenji.pagadvisor.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.Kenji.pagadvisor.ui.components.MarkdownText
import com.Kenji.pagadvisor.ui.theme.PagYellow // Importe a sua cor PagYellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use a cor de fundo padrão
    ) {
        // Lista de Mensagens
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.messages) { message ->
                MessageBubble(message)
            }
            if (uiState.isLoading) {
                item {
                    // Placeholder para "Digitando..."
                    MessageBubble(ChatMessage("Digitando...", false))
                }
            }
        }

        // Chips de Sugestão
        if (uiState.suggestedReplies.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface), // Fundo para os chips
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(uiState.suggestedReplies) { planTitle ->
                    SuggestionChip(
                        onClick = {
                            viewModel.onSendMessage(planTitle)
                            // Não limpa o textInput aqui, o ViewModel cuida disso
                        },
                        label = { Text(planTitle) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = PagYellow.copy(alpha = 0.1f), // Cor mais suave
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = null // Remove a borda padrão para um visual mais limpo
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant) // Linha sutil
        }

        // Barra de Entrada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Fundo da barra de entrada
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TextField com estilo mais moderno
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Pergunte-me algo...") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp), // Cantos mais arredondados
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                    cursorColor = PagYellow
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    viewModel.onSendMessage(textInput)
                    textInput = "" // Limpa o campo após o envio
                },
                enabled = !uiState.isLoading && textInput.isNotBlank(),
                modifier = Modifier
                    .size(48.dp) // Tamanho maior para o botão
                    .clip(CircleShape) // Botão redondo
                    .background(PagYellow) // Cor amarela
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Enviar Mensagem",
                    tint = MaterialTheme.colorScheme.onPrimary // Cor do ícone
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromUser) PagYellow.copy(alpha = 0.2f) else MaterialTheme.colorScheme.secondary // Cores mais suaves
    val textColor = if (message.isFromUser) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface // Sempre preto/cinza escuro
    val shape = if (message.isFromUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp) // Canto inferior direito mais reto
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp) // Canto inferior esquerdo mais reto
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            modifier = Modifier.widthIn(max = 300.dp) // Limita a largura do balão
        ) {
            MarkdownText(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}