package com.example.coffeedivider.data

import com.example.coffeedivider.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface CharacteristicReceiveManager {

    val data: MutableSharedFlow<Resource<CharacteristicResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}