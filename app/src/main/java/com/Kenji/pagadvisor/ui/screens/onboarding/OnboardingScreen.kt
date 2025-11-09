package com.Kenji.pagadvisor.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Kenji.pagadvisor.R
import com.Kenji.pagadvisor.ui.navigation.Screen
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val pagerState = rememberPagerState(initialPage = currentPage, pageCount = { viewModel.totalPages })

    // Sincroniza o estado do Pager com o ViewModel
    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }
    // Sincroniza o ViewModel COM o Pager
    LaunchedEffect(pagerState.currentPage) {
        if (currentPage != pagerState.currentPage) {
            viewModel.onPageChanged(pagerState.currentPage)
        }
    }

    // Observa o evento de navegação do ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.navigateToNextScreenEvent.collect {
            navController.navigate(Screen.Login.route) { // Navega para Auth após o Onboarding
                popUpTo(Screen.Onboarding.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Espaço do topo

        // Logo PagAdvisor no topo
        Image(
            painter = painterResource(id = R.drawable.logo_pagadvisor),
            contentDescription = "Logo PagAdvisor",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Pager para as páginas de Onboarding
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f) // Ocupa o espaço restante verticalmente
                .fillMaxWidth()
        ) { page ->
            OnboardingPageContent(page = page)
        }

        // Indicadores de página
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Avançar/Começar
        Button(
            onClick = viewModel::onNextPageClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(if (currentPage == viewModel.totalPages - 1) "Começar" else "Avançar")
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Conteúdo de cada página individual do Onboarding.
 * Você pode adicionar imagens e textos aqui.
 */
@Composable
fun OnboardingPageContent(page: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Centraliza o conteúdo verticalmente
    ) {

//        Image(
//            painter = painterResource(id = R.drawable.logo_pagadvisor),
//            contentDescription = "Logo PagAdvisor",
//        )

//        Spacer(modifier = Modifier.height(32.dp))

        // --- TEXTO DO ONBOARDING ---
        Text(
            text = when (page) {
                0 -> "Bem-vindo ao PagAdvisor! Seu assistente financeiro inteligente da PagSeguro. Acompanhe suas vendas e metas de forma intuitiva."
                1 -> "Receba insights personalizados e dicas exclusivas baseadas no seu desempenho. Aumente seus lucros com orientação de IA."
                2 -> "Defina suas metas, monitore seu progresso diário e receba conselhos acionáveis para alcançar o sucesso financeiro. Vamos começar?"
                else -> ""
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    PagAdvisorTheme {
        // Para o preview, apenas instanciamos a tela com um NavController falso
        OnboardingScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageContentPreview1() {
    PagAdvisorTheme {
        OnboardingPageContent(page = 0)
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageContentPreview2() {
    PagAdvisorTheme {
        OnboardingPageContent(page = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageContentPreview3() {
    PagAdvisorTheme {
        OnboardingPageContent(page = 2)
    }
}