package com.Kenji.pagadvisor.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.R
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.theme.PagGradientBottom
import com.Kenji.pagadvisor.ui.theme.PagGradientTop
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme
import com.Kenji.pagadvisor.ui.theme.PagWhite // Importe a cor branca do seu tema
import com.Kenji.pagadvisor.ui.theme.PagYellow
import com.Kenji.pagadvisor.ui.components.PagOutlinedTextField
import com.Kenji.pagadvisor.ui.components.PagPrimaryButton
import com.Kenji.pagadvisor.ui.theme.PagAzul
import com.Kenji.pagadvisor.ui.theme.PagDarkGray
import com.Kenji.pagadvisor.ui.theme.PagLightGray
import com.Kenji.pagadvisor.ui.theme.PagMediumGray
import com.Kenji.pagadvisor.ui.theme.PagVerde

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    // Observa o evento de navegação (quando o login der certo)
    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect {
            navController.navigate(Screen.Home.route) {
                // 👇 MUDOU AQUI (de Auth para Login)
                popUpTo(Screen.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    // Passa os eventos de navegação para o Content
    AuthScreenContent(
        onLoginClicked = {
            viewModel.onLoginClicked()
        },
        onRegisterClicked = {
            // Navega para a nova tela de Registro
            navController.navigate(Screen.Register.route)
        },
        onForgotPasswordClicked = {
            // Navega para a nova tela de Esqueceu Senha
            navController.navigate(Screen.ForgotPassword.route)
        }
    )
}

@Composable
fun AuthScreenContent(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isEmailError by remember { mutableStateOf(false) }
    var emailErrorMsg by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(PagGradientTop, PagGradientBottom),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PagWhite)
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pagadvisor),
            contentDescription = "Logo PagAdvisor",
            modifier = Modifier.size(300.dp)
        )

        PagOutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailError = false
            },
            label = "E-mail",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isEmailError,
            supportingText = {
                if (isEmailError) {
                    Text(emailErrorMsg, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        PagOutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordError = false
            },
            label = "Senha",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isPasswordError,
            supportingText = {
                if (isPasswordError) {
                    Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                }
            },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        PagPrimaryButton(
            text = "Entrar",
            onClick = {
                isEmailError = false
                isPasswordError = false
                emailErrorMsg = ""

                if (email.isBlank()) {
                    isEmailError = true
                    emailErrorMsg = "Campo obrigatório"
                } else if (!email.contains("@")) {
                    isEmailError = true
                    emailErrorMsg = "E-mail inválido (precisa de @)"
                }

                if (password.isBlank()) {
                    isPasswordError = true
                }

                if (!isEmailError && !isPasswordError) {
                    onLoginClicked()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PagPrimaryButton(
            text = "Cadastrar",
            onClick = onRegisterClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onForgotPasswordClicked) {
            Text(
                "Esqueceu sua senha?",
                color = PagAzul,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            color = PagAzul
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Login com Google",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(32.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Login com Facebook",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    PagAdvisorTheme {
        AuthScreenContent(
            onLoginClicked = {},
            onRegisterClicked = {},
            onForgotPasswordClicked = {}
        )
    }
}