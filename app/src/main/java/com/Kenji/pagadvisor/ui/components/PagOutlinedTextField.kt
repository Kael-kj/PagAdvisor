package com.Kenji.pagadvisor.ui.components

import android.R.attr.enabled
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagWhite
import com.Kenji.pagadvisor.ui.theme.PagYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = supportingText,
        singleLine = singleLine,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = PagWhite,
            unfocusedContainerColor = PagWhite,
            disabledContainerColor = PagWhite,
            focusedBorderColor = PagYellow,
            unfocusedBorderColor = PagYellow,
            focusedLabelColor = PagYellow,
            unfocusedLabelColor = PagYellow,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = PagYellow // Bônus: cursor da mesma cor do tema
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PagOutlinedTextFieldPreview() {
    PagAdvisorTheme {
        PagOutlinedTextField(
            value = "teste@email.com",
            onValueChange = {},
            label = "E-mail"
        )
    }
}