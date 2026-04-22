package com.example.stellarvision

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.stellarvision.navigation.Navigation

lateinit var sensorManager: SensorManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        setContent {
                Navigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StellarVisionPreview(){
    Navigation()
}
