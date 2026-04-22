package com.example.stellarvision

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigation
import com.example.stellarvision.Navigation.Navigation
import com.example.stellarvision.ui.theme.StellarVisionTheme

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
