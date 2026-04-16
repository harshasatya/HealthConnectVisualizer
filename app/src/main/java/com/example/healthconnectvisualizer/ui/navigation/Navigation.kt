package com.example.healthconnectvisualizer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.healthconnectvisualizer.ui.screens.DashboardScreen
import com.example.healthconnectvisualizer.ui.screens.VitalsScreen
import com.example.healthconnectvisualizer.ui.screens.BloodGlucoseScreen
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Vitals : Screen("vitals")
    object BloodGlucose : Screen("blood_glucose")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    healthConnectViewModel: HealthConnectViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navController = navController,
                healthConnectViewModel = healthConnectViewModel
            )
        }
        composable(Screen.Vitals.route) {
            VitalsScreen(
                navController = navController,
                healthConnectViewModel = healthConnectViewModel
            )
        }
        composable(Screen.BloodGlucose.route) {
            BloodGlucoseScreen(healthConnectViewModel = healthConnectViewModel)
        }
    }
}
