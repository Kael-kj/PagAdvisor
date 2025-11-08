package com.Kenji.pagadvisor.ui.screens.home.chat

// Classe simples para representar uma mensagem na UI
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)