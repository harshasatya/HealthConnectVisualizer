package com.example.healthconnectvisualizer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

@Composable
fun HealthConnectVisualizerApp(
    healthConnectViewModel: HealthConnectViewModel,
    requestPermissions: () -> Unit
) {
    val permissionsGranted by healthConnectViewModel.permissionsGranted.collectAsState()

    if (permissionsGranted) {
        MainScreen(healthConnectViewModel = healthConnectViewModel)
    } else {
        PermissionsRequestScreen { requestPermissions() }
    }
}
