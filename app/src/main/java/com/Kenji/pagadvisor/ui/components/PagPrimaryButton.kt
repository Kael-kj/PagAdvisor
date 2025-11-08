package com.Kenji.pagadvisor.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagWhite
import com.Kenji.pagadvisor.ui.theme.PagYellow

@Composable
fun PagPrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // Para podermos desabilitar o botão se necessário
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp), // Uma altura padrão para consistência
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = PagYellow, // Cor de fundo
            contentColor = PagWhite,    // Cor do texto
            disabledContainerColor = PagYellow.copy(alpha = 0.5f), // Cor quando desabilitado
            disabledContentColor = PagWhite.copy(alpha = 0.7f)
        ),
        shape = MaterialTheme.shapes.medium // Você pode mudar para RoundedCornerShape se quiser
    ) {
        Text(text)
    }
}

@Preview
@Composable
fun PagPrimaryButtonPreview() {
    PagAdvisorTheme {
        PagPrimaryButton(onClick = {}, text = "Botão de Teste")
    }
}