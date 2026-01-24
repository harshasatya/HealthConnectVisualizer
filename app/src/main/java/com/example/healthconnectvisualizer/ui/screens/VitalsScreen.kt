package com.example.healthconnectvisualizer.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun VitalsScreen(
    navController: NavController,
    healthConnectViewModel: HealthConnectViewModel = viewModel()
) {
    val heartRateData by healthConnectViewModel.heartRateData.collectAsState()

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
            }
        },
        update = { chart ->
            val entries = heartRateData.flatMap { record ->
                record.samples.map { sample ->
                    Entry(sample.time.toEpochMilli().toFloat(), sample.beatsPerMinute.toFloat())
                }
            }
            val dataSet = LineDataSet(entries, "Heart Rate")
            chart.data = LineData(dataSet)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}
