package com.example.healthconnectvisualizer.viewmodel

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class HealthConnectViewModel(private val context: Context) : ViewModel() {

    private val healthConnectClient = HealthConnectClient.getOrCreate(context)

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted

    private val _heartRateData = MutableStateFlow<List<HeartRateRecord>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateRecord>> = _heartRateData
    private val _bloodGlucoseData = MutableStateFlow<List<BloodGlucoseRecord>>(emptyList())
    val bloodGlucoseData: StateFlow<List<BloodGlucoseRecord>> = _bloodGlucoseData
    // TODO: Add state flows for other data types

    private val permissions = setOf(
        // General
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),

        // Vitals
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(BloodGlucoseRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),

        // Body Composition
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(LeanBodyMassRecord::class),
        HealthPermission.getReadPermission(BasalMetabolicRateRecord::class),

        // Nutrition
        HealthPermission.getReadPermission(NutritionRecord::class),
        HealthPermission.getReadPermission(HydrationRecord::class),
    )

    fun initialCheck() {
        viewModelScope.launch {
            _permissionsGranted.value = hasAllPermissions()
            if (_permissionsGranted.value) {
                startDataSync()
            }
        }
    }

    fun requestPermissions(launcher: ActivityResultLauncher<Set<String>>) {
        viewModelScope.launch {
            launcher.launch(permissions.map { it.toString() }.toSet())
        }
    }

    fun onPermissionsResult(result: Map<String, Boolean>) {
        _permissionsGranted.value = result.values.all { it }
        if (_permissionsGranted.value) {
            viewModelScope.launch {
                startDataSync()
            }
        }
    }

    private suspend fun hasAllPermissions(): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions(permissions).containsAll(permissions)
    }

    private fun startDataSync() {
        viewModelScope.launch {
            while (isActive) {
                readAllData()
                delay(60000) // Sync every minute
            }
        }
    }

    private suspend fun readAllData() {
        readHeartRateData()
        readBloodGlucoseData()
        // TODO: Read other data types
    }

    private suspend fun readHeartRateData() {
        val now = Instant.now()
        val sevenDaysAgo = now.minus(7, ChronoUnit.DAYS)
        val request = ReadRecordsRequest(
            recordType = HeartRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(sevenDaysAgo, now)
        )
        val response = healthConnectClient.readRecords(request)
        _heartRateData.value = response.records
    }

    private suspend fun readBloodGlucoseData() {
        val now = Instant.now()
        val sevenDaysAgo = now.minus(7, ChronoUnit.DAYS)
        val request = ReadRecordsRequest(
            recordType = BloodGlucoseRecord::class,
            timeRangeFilter = TimeRangeFilter.between(sevenDaysAgo, now)
        )
        val response = healthConnectClient.readRecords(request)
        _bloodGlucoseData.value = response.records
    }
}
