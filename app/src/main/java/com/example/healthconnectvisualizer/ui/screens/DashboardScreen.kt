package com.example.healthconnectvisualizer.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    navController: NavController,
    healthConnectViewModel: HealthConnectViewModel
) {
    val heartRateData    by healthConnectViewModel.heartRateData.collectAsState()
    val bloodGlucoseData by healthConnectViewModel.bloodGlucoseData.collectAsState()
    val stepsData        by healthConnectViewModel.stepsData.collectAsState()
    val weightData       by healthConnectViewModel.weightData.collectAsState()
    val sleepData        by healthConnectViewModel.sleepData.collectAsState()
    val bloodPressureData by healthConnectViewModel.bloodPressureData.collectAsState()
    val healthScore      by healthConnectViewModel.healthScore.collectAsState()

    // Derived display values
    val todaySteps    = healthConnectViewModel.getTodaySteps(stepsData)
    val avgHR         = healthConnectViewModel.getAvgHeartRate(heartRateData)
    val latestBG      = healthConnectViewModel.getLatestBloodGlucose(bloodGlucoseData)
    val bgTIR         = healthConnectViewModel.getBloodGlucoseTIR(bloodGlucoseData)
    val sleepHours    = healthConnectViewModel.getLastSleepHours(sleepData)
    val latestWeight  = healthConnectViewModel.getLatestWeight(weightData)
    val latestBP      = healthConnectViewModel.getLatestBloodPressure(bloodPressureData)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { GreetingHeaderCard(healthScore = healthScore) }
        item { HealthScoreCard(score = healthScore) }
        item {
            Text(
                text = "Today's Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BloodGlucoseCard(latestBG = latestBG, tir = bgTIR, modifier = Modifier.weight(1f))
                HeartRateCard(avgHR = avgHR, modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StepsCard(steps = todaySteps, modifier = Modifier.weight(1f))
                SleepCard(hours = sleepHours, modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                WeightCard(weightKg = latestWeight, modifier = Modifier.weight(1f))
                BloodPressureCard(bp = latestBP, modifier = Modifier.weight(1f))
            }
        }
        item {
            InsightsCard(
                avgHR = avgHR,
                latestBG = latestBG,
                bgTIR = bgTIR,
                todaySteps = todaySteps,
                sleepHours = sleepHours
            )
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ── Greeting header ────────────────────────────────────────────────────────────

@Composable
private fun GreetingHeaderCard(healthScore: Int) {
    val now = LocalDateTime.now()
    val greeting = when (now.hour) {
        in 5..11  -> "Good morning"
        in 12..16 -> "Good afternoon"
        else      -> "Good evening"
    }
    val dateStr = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
    val insight = when {
        healthScore >= 80 -> "You're doing great — keep it up!"
        healthScore >= 60 -> "Solid day so far. Stay on track."
        healthScore >= 40 -> "Some metrics need attention today."
        else              -> "Let's work on improving your health today."
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = insight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
            )
        }
    }
}

// ── Health score card ──────────────────────────────────────────────────────────

@Composable
private fun HealthScoreCard(score: Int) {
    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "healthScore"
    )
    val scoreLabel = when {
        score >= 80 -> "Excellent"
        score >= 60 -> "Good"
        score >= 40 -> "Fair"
        else        -> "Needs Attention"
    }
    val scoreColor = when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> MaterialTheme.colorScheme.primary
        score >= 40 -> Color(0xFFFFC107)
        else        -> Color(0xFFF44336)
    }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(96.dp),
                    strokeWidth = 8.dp,
                    color = scoreColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor
                    )
                    Text(
                        text = "/ 100",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Health Score",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = scoreLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    color = scoreColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Based on glucose, heart rate,\nsleep & activity data",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ── Reusable metric card ───────────────────────────────────────────────────────

@Composable
private fun MetricCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    subtitle: String = "",
    trendUp: Boolean? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    extraContent: @Composable (() -> Unit)? = null
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
                if (trendUp != null) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = if (trendUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = if (trendUp) Color(0xFF4CAF50) else Color(0xFFF44336),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            extraContent?.invoke()
        }
    }
}

// ── Individual metric cards ────────────────────────────────────────────────────

@Composable
private fun BloodGlucoseCard(latestBG: Double?, tir: Int, modifier: Modifier = Modifier) {
    val value = latestBG?.let { "%.0f".format(it) } ?: "--"
    val subtitle = if (latestBG != null) "$tir% in range" else "No data"
    MetricCard(
        title = "Blood Glucose",
        value = value,
        unit = if (latestBG != null) "mg/dL" else "",
        icon = Icons.Default.Bloodtype,
        subtitle = subtitle,
        accentColor = Color(0xFFE53935),
        modifier = modifier
    )
}

@Composable
private fun HeartRateCard(avgHR: Int?, modifier: Modifier = Modifier) {
    MetricCard(
        title = "Heart Rate",
        value = avgHR?.toString() ?: "--",
        unit = if (avgHR != null) "bpm" else "",
        icon = Icons.Default.FavoriteBorder,
        subtitle = if (avgHR != null) "7-day average" else "No data",
        accentColor = Color(0xFFE91E63),
        modifier = modifier
    )
}

@Composable
private fun StepsCard(steps: Long, modifier: Modifier = Modifier) {
    val goal = 10_000L
    val progress = (steps.toFloat() / goal).coerceIn(0f, 1f)
    val subtitle = "${"%,d".format(steps)} / ${"%,d".format(goal)}"
    MetricCard(
        title = "Steps",
        value = "%,d".format(steps),
        unit = "",
        icon = Icons.Default.DirectionsWalk,
        subtitle = subtitle,
        accentColor = Color(0xFF43A047),
        modifier = modifier
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = Color(0xFF43A047),
            trackColor = Color(0xFF43A047).copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun SleepCard(hours: Double?, modifier: Modifier = Modifier) {
    val displayHours = hours?.let {
        val h = it.toInt()
        val m = ((it - h) * 60).toInt()
        "${h}h ${m}m"
    } ?: "--"
    val subtitle = when {
        hours == null       -> "No data"
        hours in 7.0..9.0   -> "Great sleep"
        hours in 6.0..10.0  -> "Decent sleep"
        else                -> "Below target"
    }
    MetricCard(
        title = "Sleep",
        value = displayHours,
        unit = "",
        icon = Icons.Default.Hotel,
        subtitle = subtitle,
        accentColor = Color(0xFF5C6BC0),
        modifier = modifier
    )
}

@Composable
private fun WeightCard(weightKg: Double?, modifier: Modifier = Modifier) {
    MetricCard(
        title = "Weight",
        value = weightKg?.let { "%.1f".format(it) } ?: "--",
        unit = if (weightKg != null) "kg" else "",
        icon = Icons.Default.MonitorWeight,
        subtitle = if (weightKg != null) "Latest reading" else "No data",
        accentColor = Color(0xFF8D6E63),
        modifier = modifier
    )
}

@Composable
private fun BloodPressureCard(bp: Pair<Int, Int>?, modifier: Modifier = Modifier) {
    val value = bp?.let { "${it.first}/${it.second}" } ?: "--"
    val subtitle = bp?.let {
        when {
            it.first < 120 && it.second < 80 -> "Normal"
            it.first < 130 && it.second < 80 -> "Elevated"
            it.first < 140 || it.second < 90 -> "Stage 1"
            else                              -> "Stage 2"
        }
    } ?: "No data"
    val accentColor = bp?.let {
        when {
            it.first < 120 && it.second < 80 -> Color(0xFF43A047)
            it.first < 130                   -> Color(0xFFFFC107)
            it.first < 140                   -> Color(0xFFFF9800)
            else                             -> Color(0xFFF44336)
        }
    } ?: MaterialTheme.colorScheme.primary

    MetricCard(
        title = "Blood Pressure",
        value = value,
        unit = if (bp != null) "mmHg" else "",
        icon = Icons.Default.Vaccines,
        subtitle = subtitle,
        accentColor = accentColor,
        modifier = modifier
    )
}

// ── Insights card ──────────────────────────────────────────────────────────────

@Composable
private fun InsightsCard(
    avgHR: Int?,
    latestBG: Double?,
    bgTIR: Int,
    todaySteps: Long,
    sleepHours: Double?
) {
    val insights = buildList {
        if (latestBG != null) {
            if (bgTIR >= 70) add("Your blood glucose is ${bgTIR}% in target range — excellent control.")
            else if (bgTIR >= 50) add("Blood glucose is in range $bgTIR% of the time. Target is 70%+.")
            else add("Only $bgTIR% time-in-range for blood glucose. Consider reviewing patterns.")
        }
        if (avgHR != null) {
            when {
                avgHR in 60..80 -> add("Resting heart rate of ${avgHR} bpm is in a healthy range.")
                avgHR > 100     -> add("Average heart rate of ${avgHR} bpm is elevated. Consider rest.")
                avgHR < 50      -> add("Heart rate of ${avgHR} bpm is low — monitor if symptomatic.")
                else            -> add("Heart rate averaging ${avgHR} bpm over 7 days.")
            }
        }
        if (todaySteps > 0) {
            when {
                todaySteps >= 10_000 -> add("You've hit your 10,000 step goal today!")
                todaySteps >= 5_000  -> add("${"%,d".format(todaySteps)} steps so far. ${"%,d".format(10_000 - todaySteps)} to go.")
                else                 -> add("Only ${"%,d".format(todaySteps)} steps today. Try a short walk.")
            }
        }
        if (sleepHours != null) {
            when {
                sleepHours in 7.0..9.0 -> add("Last night's ${"%.1f".format(sleepHours)}h sleep is in the optimal range.")
                sleepHours < 7.0       -> add("You slept ${"%.1f".format(sleepHours)}h last night. Aim for 7–9 hours.")
                else                   -> add("You slept ${"%.1f".format(sleepHours)}h last night. Slightly above ideal range.")
            }
        }
        if (isEmpty()) add("Sync your health apps with Health Connect to see personalised insights here.")
    }

    if (insights.isEmpty()) return

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Insights",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            insights.forEach { insight ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = insight,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
