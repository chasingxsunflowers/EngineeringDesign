package com.example.coffeedivider.data

import com.example.coffeedivider.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface TemperatureAndHumidityReceiveManager {

    val data: MutableSharedFlow<Resource<TempHumidityResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}