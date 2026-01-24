package com.example.healthconnectvisualizer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

@Composable
fun BloodGlucoseScreen(healthConnectViewModel: HealthConnectViewModel) {
    val bloodGlucoseData = healthConnectViewModel.bloodGlucoseData.collectAsState()

    Column {
        Text("Blood Glucose Data")
        LazyColumn {
            items(bloodGlucoseData.value) { record ->
                Text("Value: ${record.level.inMilligramsPerDeciliter}, Time: ${record.time}")
            }
        }
    }
}
