package com.example.coffeedivider.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeedivider.data.ConnectionState
import com.example.coffeedivider.data.TemperatureAndHumidityReceiveManager
import com.example.coffeedivider.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempHumidityViewModel @Inject constructor(
    private val temperatureAndHumidityReceiveManager: TemperatureAndHumidityReceiveManager
) : ViewModel() {

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var day by mutableStateOf(0)
        private set

    var weeks by mutableStateOf(0)
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges() {
        viewModelScope.launch {
            temperatureAndHumidityReceiveManager.data.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        day = result.data.day
                        weeks = result.data.weeks
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun disconnect() {
        temperatureAndHumidityReceiveManager.disconnect()
    }

    fun reconnect() {
        temperatureAndHumidityReceiveManager.reconnect()
    }

    fun initializeConnection() {
        errorMessage = null
        subscribeToChanges()
        temperatureAndHumidityReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        temperatureAndHumidityReceiveManager.closeConnection()
    }


}