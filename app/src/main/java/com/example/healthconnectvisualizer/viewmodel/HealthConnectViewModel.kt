package com.example.healthconnectvisualizer.viewmodel

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class HealthConnectViewModel(private val context: Context) : ViewModel() {

    private val healthConnectClient = HealthConnectClient.getOrCreate(context)

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted

    private val _heartRateData = MutableStateFlow<List<HeartRateRecord>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateRecord>> = _heartRateData

    private val _bloodGlucoseData = MutableStateFlow<List<BloodGlucoseRecord>>(emptyList())
    val bloodGlucoseData: StateFlow<List<BloodGlucoseRecord>> = _bloodGlucoseData

    private val _stepsData = MutableStateFlow<List<StepsRecord>>(emptyList())
    val stepsData: StateFlow<List<StepsRecord>> = _stepsData

    private val _weightData = MutableStateFlow<List<WeightRecord>>(emptyList())
    val weightData: StateFlow<List<WeightRecord>> = _weightData

    private val _sleepData = MutableStateFlow<List<SleepSessionRecord>>(emptyList())
    val sleepData: StateFlow<List<SleepSessionRecord>> = _sleepData

    private val _bloodPressureData = MutableStateFlow<List<BloodPressureRecord>>(emptyList())
    val bloodPressureData: StateFlow<List<BloodPressureRecord>> = _bloodPressureData

    /** Composite health score 0–100, recomputed whenever any source data changes. */
    val healthScore: StateFlow<Int> = combine(
        _heartRateData,
        _bloodGlucoseData,
        _stepsData,
        _sleepData
    ) { hr, bg, steps, sleep ->
        computeHealthScore(hr, bg, steps, sleep)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 50
    )

    private val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(BloodGlucoseRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(LeanBodyMassRecord::class),
        HealthPermission.getReadPermission(BasalMetabolicRateRecord::class),
        HealthPermission.getReadPermission(NutritionRecord::class),
        HealthPermission.getReadPermission(HydrationRecord::class),
    )

    fun initialCheck() {
        viewModelScope.launch {
            _permissionsGranted.value = hasAllPermissions()
            if (_permissionsGranted.value) startDataSync()
        }
    }

    fun requestPermissions(launcher: ActivityResultLauncher<Array<String>>) {
        viewModelScope.launch {
            launcher.launch(permissions.map { it.toString() }.toTypedArray())
        }
    }

    fun onPermissionsResult(result: Map<String, Boolean>) {
        _permissionsGranted.value = result.values.all { it }
        if (_permissionsGranted.value) {
            viewModelScope.launch { startDataSync() }
        }
    }

    private suspend fun hasAllPermissions(): Boolean {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return granted.containsAll(permissions.map { it.toString() })
    }

    private fun startDataSync() {
        viewModelScope.launch {
            while (isActive) {
                readAllData()
                delay(60_000)
            }
        }
    }

    private suspend fun readAllData() {
        val now = Instant.now()
        val sevenDaysAgo = now.minus(7, ChronoUnit.DAYS)
        readHeartRateData(sevenDaysAgo, now)
        readBloodGlucoseData(sevenDaysAgo, now)
        readStepsData(sevenDaysAgo, now)
        readWeightData(sevenDaysAgo, now)
        readSleepData(sevenDaysAgo, now)
        readBloodPressureData(sevenDaysAgo, now)
    }

    private suspend fun readHeartRateData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(HeartRateRecord::class, TimeRangeFilter.between(start, end))
        )
        _heartRateData.value = response.records
    }

    private suspend fun readBloodGlucoseData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(BloodGlucoseRecord::class, TimeRangeFilter.between(start, end))
        )
        _bloodGlucoseData.value = response.records
    }

    private suspend fun readStepsData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(StepsRecord::class, TimeRangeFilter.between(start, end))
        )
        _stepsData.value = response.records
    }

    private suspend fun readWeightData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(WeightRecord::class, TimeRangeFilter.between(start, end))
        )
        _weightData.value = response.records
    }

    private suspend fun readSleepData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(SleepSessionRecord::class, TimeRangeFilter.between(start, end))
        )
        _sleepData.value = response.records
    }

    private suspend fun readBloodPressureData(start: Instant, end: Instant) {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(BloodPressureRecord::class, TimeRangeFilter.between(start, end))
        )
        _bloodPressureData.value = response.records
    }

    // ── Computation helpers (called from ViewModel and Dashboard) ──────────────

    private fun computeHealthScore(
        hrData: List<HeartRateRecord>,
        bgData: List<BloodGlucoseRecord>,
        stepsData: List<StepsRecord>,
        sleepData: List<SleepSessionRecord>
    ): Int {
        var points = 0
        var maxPoints = 0

        // Blood glucose TIR (max 30 pts)
        if (bgData.isNotEmpty()) {
            maxPoints += 30
            val tir = getBloodGlucoseTIR(bgData)
            points += when {
                tir >= 70 -> 30
                tir >= 50 -> 20
                tir >= 30 -> 10
                else -> 3
            }
        }

        // Resting heart rate (max 20 pts)
        val samples = hrData.flatMap { it.samples }
        if (samples.isNotEmpty()) {
            maxPoints += 20
            val avg = (samples.sumOf { it.beatsPerMinute } / samples.size).toInt()
            points += when (avg) {
                in 60..80 -> 20
                in 50..59, in 81..100 -> 12
                else -> 4
            }
        }

        // Steps today (max 25 pts)
        if (stepsData.isNotEmpty()) {
            maxPoints += 25
            val today = getTodaySteps(stepsData)
            points += when {
                today >= 10_000 -> 25
                today >= 7_500  -> 18
                today >= 5_000  -> 12
                today >= 2_500  -> 6
                else            -> 1
            }
        }

        // Sleep hours last night (max 25 pts)
        if (sleepData.isNotEmpty()) {
            maxPoints += 25
            val hrs = getLastSleepHours(sleepData)
            points += when {
                hrs == null             -> 0
                hrs in 7.0..9.0         -> 25
                hrs in 6.0..10.0        -> 15
                else                    -> 5
            }
        }

        return if (maxPoints > 0)
            (points.toFloat() / maxPoints * 100).toInt().coerceIn(0, 100)
        else 50
    }

    fun getTodaySteps(records: List<StepsRecord>): Long {
        val today = LocalDate.now()
        return records
            .filter { it.startTime.atZone(ZoneId.systemDefault()).toLocalDate() == today }
            .sumOf { it.count }
    }

    fun getAvgHeartRate(records: List<HeartRateRecord>): Int? {
        val samples = records.flatMap { it.samples }
        return if (samples.isEmpty()) null
        else (samples.sumOf { it.beatsPerMinute } / samples.size).toInt()
    }

    fun getLatestBloodGlucose(records: List<BloodGlucoseRecord>): Double? =
        records.maxByOrNull { it.time }?.level?.inMilligramsPerDeciliter

    fun getBloodGlucoseTIR(records: List<BloodGlucoseRecord>): Int {
        if (records.isEmpty()) return 0
        val inRange = records.count {
            val v = it.level.inMilligramsPerDeciliter
            v in 70.0..180.0
        }
        return inRange * 100 / records.size
    }

    fun getLastSleepHours(records: List<SleepSessionRecord>): Double? {
        val cutoff = Instant.now().minus(24, ChronoUnit.HOURS)
        return records
            .filter { it.endTime.isAfter(cutoff) }
            .maxByOrNull { it.endTime }
            ?.let { Duration.between(it.startTime, it.endTime).toMinutes() / 60.0 }
    }

    fun getLatestWeight(records: List<WeightRecord>): Double? =
        records.maxByOrNull { it.time }?.weight?.inKilograms

    fun getLatestBloodPressure(records: List<BloodPressureRecord>): Pair<Int, Int>? {
        val r = records.maxByOrNull { it.time } ?: return null
        return Pair(
            r.systolic.inMillimetersOfMercury.toInt(),
            r.diastolic.inMillimetersOfMercury.toInt()
        )
    }
}
