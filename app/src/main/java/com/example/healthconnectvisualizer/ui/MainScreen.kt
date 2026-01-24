package com.example.healthconnectvisualizer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.healthconnectvisualizer.ui.navigation.AppNavigation
import com.example.healthconnectvisualizer.ui.navigation.BottomNavigationBar
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(healthConnectViewModel: HealthConnectViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppNavigation(
                navController = navController,
                healthConnectViewModel = healthConnectViewModel
            )
        }
    }
}
