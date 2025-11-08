package com.Kenji.pagadvisor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.Kenji.pagadvisor.ui.navigation.AppNavigation // ✅ Import correto
import com.Kenji.pagadvisor.ui.theme.PagAdvisorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagAdvisorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // A MainActivity agora SÓ chama o AppNavigation
                    AppNavigation()
                }
            }
        }
    }
}