package com.Kenji.pagadvisor.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun ConfirmPasswordScreen(
    navController: NavController,
    viewModel: ConfirmPasswordViewModel = viewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.navigateToLoginEvent.collect {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    ConfirmPasswordScreenContent(
        onConfirmClicked = { viewModel.onConfirmNewPasswordClicked() }
    )
}

@Composable
fun ConfirmPasswordScreenContent(
    onConfirmClicked: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordError by remember { mutableStateOf(false) }

    val isFormValid by remember(password, confirmPassword, isPasswordError) {
        derivedStateOf {
            password.isNotBlank() &&
                    confirmPassword.isNotBlank() &&
                    password == confirmPassword
        }
    }

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
        Spacer(modifier = Modifier.height(50.dp))

        PagOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordError = false
            },
            label = "Nova Senha",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        PagOutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isPasswordError = password != confirmPassword // Validação de senhas
            },
            label = "Confirmar Nova Senha",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = isPasswordError,
            supportingText = {
                if (isPasswordError) {
                    Text("As senhas não coincidem", color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        PagPrimaryButton(
            onClick = onConfirmClicked,
            text = "Confirmar",
            enabled = isFormValid
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmPasswordScreenPreview() {
    PagAdvisorTheme {
        ConfirmPasswordScreenContent(onConfirmClicked = {})
    }
}