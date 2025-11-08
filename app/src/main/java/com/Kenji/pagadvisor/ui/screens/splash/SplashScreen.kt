package com.Kenji.pagadvisor.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.Kenji.pagadvisor.R
import com.Kenji.pagadvisor.ui.navigation.Screen
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme

@Composable
fun SplashScreen(navController: NavController) {

    // Lógica para decidir para onde navegar
    LaunchedEffect(key1 = true) {
        delay(2000L) // Simula 3s de carregamento

        navController.navigate(Screen.Onboarding.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    // UI da Splash
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(

            painter = painterResource(id = R.drawable.logo_pagadvisor),
            contentDescription = "Logo PagAdvisor",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    PagAdvisorTheme {
        SplashScreen(navController = rememberNavController())
    }
}