package com.example.coffeedivider.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeedivider.data.CharacteristicReceiveManager
import com.example.coffeedivider.data.ConnectionState
import com.example.coffeedivider.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//Bluetooth Low Energy - Model of the characteristic, changes depending on the state of the connection
@HiltViewModel
class CharacteristicModel @Inject constructor(
    private val characteristicReceiveManager: CharacteristicReceiveManager
) : ViewModel() {

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var day by mutableStateOf(0)
        private set

    var weeks by mutableStateOf("")
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges() {
        viewModelScope.launch {
            characteristicReceiveManager.data.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        day = result.data.day
                        weeks = result.data.weeks.toString()
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
        characteristicReceiveManager.disconnect()
    }

    fun reconnect() {
        characteristicReceiveManager.reconnect()
    }

    fun initializeConnection() {
        errorMessage = null
        subscribeToChanges()
        characteristicReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        characteristicReceiveManager.closeConnection()
    }


}