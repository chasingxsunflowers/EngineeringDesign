package com.example.coffeedivider


import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.coffeedivider.presentation.Navigation
import com.example.coffeedivider.ui.theme.CoffeeDividerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoffeeDividerTheme {
                Navigation(
                    onBluetoothStateChanged = {
                        showBluetoothDialog()
                    }
                )
            }
        }

    }

    override fun onStart() {
        super.onStart()
        showBluetoothDialog()
    }

    private var isBluetoothDialogAlreadyShown = false
    private fun showBluetoothDialog() {
        if (!bluetoothAdapter.isEnabled) {
            if (!isBluetoothDialogAlreadyShown) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startBluetoothIntentForResult.launch(enableBluetoothIntent)
                isBluetoothDialogAlreadyShown = true
            }
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            isBluetoothDialogAlreadyShown = false
            if (result.resultCode != Activity.RESULT_OK) {
                showBluetoothDialog()
            }
        }


}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun App(){
//    var currentStep by remember { mutableStateOf(2) }
//    val deadline = remember { mutableStateOf("") }
//    val cupSize = remember { mutableStateOf("") }
//
//
//    Scaffold { innerPadding ->
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(MaterialTheme.colorScheme.tertiaryContainer),
//            color = MaterialTheme.colorScheme.background
//        ){
//            when (currentStep) {
//                1 -> {
//                    Page1(
//                        onImageClick = {
//                            currentStep = 2
//                        }
//                    )
//                }
//                2 -> {
//                    Page2(onImageClickPrevious = {
//                            currentStep = 1
//                        },
//                        cupSize)
//
//                }
//                4 -> {
//                    //settings
//                    Page4(
//                        onImageClickPrevious = {
//                            currentStep = 2
//                        },
//                        deadline
//                    )
//                }
//                5 -> {
//                    BluetoothPage()
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun BluetoothPage(modifier: Modifier = Modifier){
//
////    Column(
////        modifier = Modifier.fillMaxWidth(),
////        horizontalAlignment = Alignment.CenterHorizontally,
////        verticalArrangement = Arrangement.Center
////    ){
////        OutlinedButton(onClick = {
////            //takePermission.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
////        }){
////            Text(
////                text = "Bluetooth ON",
////                color = Color(0xFFC89B78)
////            )
////        }
////        Spacer(modifier = Modifier.height(20.dp))
////
////        OutlinedButton(onClick = {
////            //bluetoothAdapter.disable()
////        }) {
////            Text(text = "Bluetooth OFF",
////                color = Color(0xFFC89B78))
////        }
////    }
//
//}
//
//
//
////@Preview(showBackground = true)
////@Composable
////fun IntroductionPagePreview() {
////    CoffeeDividerTheme {
////        //App()
////        Navigation()
////    }
////}