package com.example.coffeedivider.data.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import com.example.coffeedivider.BLEApplication.Companion.globalVar
import com.example.coffeedivider.data.CharacteristicReceiveManager
import com.example.coffeedivider.data.CharacteristicResult
import com.example.coffeedivider.data.ConnectionState
import com.example.coffeedivider.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

//The code behind passing values from the application to the Arduino via Bluetooth
@SuppressLint("MissingPermission")
class CharacteristicBLEReceiveManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter, private val context: Context
) : CharacteristicReceiveManager {

    private val DEVICE_NAME = "Nano_33_IoT" //insert name of arduino
    private val DAY_WEEKS_SERVICE_UIID = "0000aa20-0000-1000-8000-00805f9b34fb"
    private val DAY_WEEKS_CHARACTERISTICS_UUID = "0000aa21-0000-1000-8000-00805f9b34fb"

    override val data: MutableSharedFlow<Resource<CharacteristicResult>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == DEVICE_NAME) {
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if (isScanning) {
                    result.device.connectGatt(
                        context, false, gattCallback
                    )
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@CharacteristicBLEReceiveManager.gatt = gatt
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                    coroutineScope.launch {
//                        data.emit(
//                            Resource.Success(
//                                data = TempHumidityResult(
//                                    0, , ConnectionState.Disconnected
//                                )
//                            )
//                        )
//                    }
                    gatt.close()
                }
            } else {
                gatt.close()
                currentConnectionAttempt += 1
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if (currentConnectionAttempt <= MAXIMUM_CONNECTION_ATTEMPTS) {
                    startReceiving()
                } else {
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt) {
                printGattTable()
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Adjusting MTU space..."))
                }

                //gatt.requestMtu(517) //had to be deleted due to a conflict with writing into the characteristic

                Log.d("Output", "$globalVar")
                val dateToArduino = (globalVar).toByte()
                gatt.writeCharacteristic(gatt.getService(UUID.fromString(DAY_WEEKS_SERVICE_UIID)).getCharacteristic(UUID.fromString(DAY_WEEKS_CHARACTERISTICS_UUID)), byteArrayOf(dateToArduino), BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                Log.d("Output", "Didnt crash")
            }
        }

        override fun onMtuChanged(
            gatt: BluetoothGatt, mtu: Int, status: Int
        ) {
            val characteristic =
                findCharacteristics(DAY_WEEKS_SERVICE_UIID, DAY_WEEKS_CHARACTERISTICS_UUID)

            if (characteristic == null) {
                coroutineScope.launch {
                    data.emit(Resource.Error(errorMessage = "Could not find day and weeks publisher"))
                }
                return
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Log.d("Write", "Success!")
                        val tempHumidityResult = CharacteristicResult(
                            7, "weeks", ConnectionState.Connected
                        )

                        coroutineScope.launch {
                            data.emit(
                                Resource.Success(data = tempHumidityResult)
                            )
                        }
                        disconnect()
                    }
                    BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
                        Log.d("Write", "Fail!")
                        coroutineScope.launch {
                            data.emit(
                                Resource.Error(errorMessage = "Write exceeded connection ATT MTU!")
                            )
                        }
                    }
                    BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                        Log.d("Write", "Fail!")
                        coroutineScope.launch {
                            data.emit(
                                Resource.Error(errorMessage = "Write not permitted for $uuid!")
                            )
                        }
                    }
                    else -> {
                        Log.d("Write", "Fail!")
                        coroutineScope.launch {
                            data.emit(
                                Resource.Error(errorMessage = "Characteristic write failed for $uuid, error: $status!")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray) {

        gatt?.let { gatt ->
            gatt.writeDescriptor(descriptor, payload)
        } ?: error("Not connected to a BLE device!")
    }

    private fun findCharacteristics(
        serviceUUID: String, characteristicsUUID: String
    ): BluetoothGattCharacteristic? {
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning Ble devices..."))
        }
        isScanning = true
        bleScanner.startScan(null, scanSettings, scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }

    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic =
            findCharacteristics(DAY_WEEKS_SERVICE_UIID, DAY_WEEKS_CHARACTERISTICS_UUID)
        if (characteristic != null) {
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, false) == false) {
                Log.d("TempHumidReceiveManager", "set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}