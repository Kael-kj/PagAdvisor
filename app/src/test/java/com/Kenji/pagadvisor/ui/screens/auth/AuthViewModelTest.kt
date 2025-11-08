package com.Kenji.pagadvisor.ui.screens.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Exemplo de Teste Unitário para o AuthViewModel.
 * (Neste caso, como nosso ViewModel de Auth é simples,
 * a lógica de validação está na UI, mas o princípio é o mesmo).
 *
 * Vamos testar a única lógica do ViewModel:
 * "Quando onLoginClicked() é chamado, ele deve emitir um evento de navegação."
 */
@ExperimentalCoroutinesApi
class AuthViewModelTest {

    // Regra para executar tarefas de LiveData/Flow instantaneamente
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Configura um dispatcher de teste para Coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        // Define o dispatcher principal (Main) como o dispatcher de teste
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel()
    }

    @Test
    fun `quando onLoginClicked é chamado, o evento de navegação deve ser emitido`() = runTest(testDispatcher) {
        // Arrange (Organiza)
        var eventEmitted = false
        val job = launch {
            viewModel.navigationEvent.collect {
                eventEmitted = true
            }
        }

        // Act (Age)
        viewModel.onLoginClicked()

        // Faz a coroutine de teste avançar
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert (Verifica)
        assert(eventEmitted)
        job.cancel()
    }

    @After
    fun tearDown() {
        // Limpa o dispatcher principal
        Dispatchers.resetMain()
    }
}