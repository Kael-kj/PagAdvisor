package com.Kenji.pagadvisor.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.Kenji.pagadvisor.R
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagGradientBottom
import com.Kenji.pagadvisor.ui.theme.PagGradientTop
import com.Kenji.pagadvisor.ui.theme.PagWhite

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val isCodeSent by viewModel.isCodeSent.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigateToConfirmPassword.collect {
            navController.navigate(Screen.ConfirmPassword.route)
        }
    }

    ForgotPasswordScreenContent(
        isCodeSent = isCodeSent,
        onSendCodeClicked = { viewModel.onSendCodeClicked() },
        onConfirmClicked = { viewModel.onConfirmClicked() }
    )
}

@Composable
fun ForgotPasswordScreenContent(
    isCodeSent: Boolean,
    onSendCodeClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    val isEmailValid by remember(email) { derivedStateOf { email.isNotBlank() && email.contains("@") } }
    val isCodeValid by remember(code) { derivedStateOf { code.isNotBlank() && code.length >= 4 } } // Assumindo um código de 4+ dígitos

    val gradientBrush = Brush.linearGradient(
        colors = listOf(PagGradientTop, PagGradientBottom),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PagWhite)
            .padding(horizontal = 50.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pagadvisor),
            contentDescription = "Logo PagAdvisor",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(75.dp))

        PagOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isCodeSent // Desabilita o campo de email após enviar o código
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Botão Enviar/Reenviar Código
        PagPrimaryButton(
            onClick = onSendCodeClicked,
            text = if (isCodeSent) "Reenviar Código" else "Enviar Código",
            enabled = isEmailValid // Habilita o botão apenas se o e-mail for válido
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- APARECE APENAS APÓS ENVIAR O CÓDIGO ---
        AnimatedVisibility(visible = isCodeSent) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                PagOutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = "Código",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PagPrimaryButton(
                    onClick = onConfirmClicked,
                    text = "Confirmar",
                    enabled = isCodeValid // Habilita o botão apenas se o código for válido
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    PagAdvisorTheme {
        ForgotPasswordScreenContent(
            isCodeSent = false,
            onSendCodeClicked = {},
            onConfirmClicked = {}
        )
    }
}