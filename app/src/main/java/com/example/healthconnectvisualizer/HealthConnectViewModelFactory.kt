package com.example.healthconnectvisualizer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthconnectvisualizer.viewmodel.HealthConnectViewModel

class HealthConnectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthConnectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthConnectViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
