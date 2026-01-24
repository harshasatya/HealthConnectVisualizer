package com.example.healthconnectvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.healthconnectvisualizer.ui.HealthConnectVisualizerApp
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

class MainActivity : ComponentActivity() {

    private lateinit var healthConnectViewModel: HealthConnectViewModel
    private lateinit var requestPermissions: ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        healthConnectViewModel = ViewModelProvider(this, HealthConnectViewModelFactory(this))
            .get(HealthConnectViewModel::class.java)

        requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            healthConnectViewModel.onPermissionsResult(it)
        }

        healthConnectViewModel.initialCheck()

        setContent {
            HealthConnectVisualizerApp(
                healthConnectViewModel = healthConnectViewModel,
                requestPermissions = { requestPermissions() }
            )
        }
    }

    private fun requestPermissions() {
        healthConnectViewModel.requestPermissions(requestPermissions)
    }
}
