package com.example.stellarvision.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_GYROSCOPE
import android.hardware.Sensor.TYPE_LIGHT
import android.hardware.Sensor.TYPE_ROTATION_VECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigationevent.NavigationEventDispatcher
import com.example.stellarvision.Model.Star
import com.example.stellarvision.R
import com.example.stellarvision.Util.getStarsFromHYG
import com.example.stellarvision.Util.visibleStars
import com.example.stellarvision.sensorManager
import com.example.stellarvision.ui.atoms.CameraButton
import com.example.stellarvision.ui.atoms.iconsNavBar
import com.example.stellarvision.ui.templates.BottomBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

@Composable
fun Vista360(controller: NavController) {
    var stars by remember { mutableStateOf<List<Star>>(emptyList()) }
    val context = LocalContext.current

    var x_rot by remember { mutableFloatStateOf(0.0F) }
    var y_rot by remember { mutableFloatStateOf(0.0F) }
    var z_rot by remember { mutableFloatStateOf(0.0F) }

    var lightLevel by remember { mutableStateOf(0.0F)}


    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = createLocationRequest()
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var altitude by remember { mutableDoubleStateOf(0.0) }
    val locationCallback = createLocationCallback{result ->
        result.lastLocation?.let {
            latitude = it.latitude
            longitude = it.longitude
            altitude = it.altitude
        }
    }
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    val sensorListener = object : SensorEventListener{
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type == TYPE_ROTATION_VECTOR){
                x_rot = event.values[0]
                y_rot = event.values[1]
                z_rot = event.values[2]
            }
            if(event?.sensor?.type == TYPE_LIGHT){
                lightLevel = event.values[0]
            }
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            stars = getStarsFromHYG(context)
        }
    }

    DisposableEffect(Unit) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        rotationVectorSensor?.let{
            sensorManager.registerListener(sensorListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
        lightSensor?.let{
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }

        onDispose {
            locationClient.removeLocationUpdates { locationCallback }
            sensorManager.unregisterListener(sensorListener)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Column(modifier = Modifier.weight(3F).padding(10.dp), verticalArrangement = Arrangement.Center) {
            Text("La latitud es: $latitude", fontSize = 20.sp)
            Text("La longitud es: $longitude", fontSize = 20.sp)
            Text("La altitud es: $altitude", fontSize = 20.sp)
            Text("Los valores de vector de rotacion son x: ${x_rot}, y: ${y_rot}, z: ${z_rot} ", fontSize = 20.sp)
            Text("El valor de luz detectado por el sensor es: ${lightLevel}", fontSize = 20.sp)
        }

        LazyColumn(modifier = Modifier.weight(7F)) {
            items(visibleStars(stars, latitude, longitude).sortedBy { it.visualMagnitude }) { star ->
                val starInfo: String = "Nombre: ${star.properName}, Magnitud: ${star.visualMagnitude}"
                MyRow(starInfo)
            }
        }

    }

}

@Composable
fun MyRow(text: String){
    Row(
        modifier = Modifier.fillMaxSize()
    ){
        Icon(Icons.Default.Star, "Icon", Modifier.weight(1F).align(Alignment.CenterVertically).size(18.dp))
        Text (text, modifier = Modifier.weight(8F).align(Alignment.CenterVertically), fontSize = 18.sp)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Vista360Screen(controller: NavController){
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)
    var showButton by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        if(!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if(locationPermissionState.status.isGranted){
        Vista360(controller)
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (locationPermissionState.status.shouldShowRationale){
                "Stellar Vision necesita la ubicación para poder mostrar las estrellas que están encima tuyo"
            }else{
                "Permiso de ubicación es necesario para la vista 360. Puedes darlo en la configuración de la aplicación"
            }

            Text(textToShow, modifier = Modifier.padding(15.dp), fontSize = 21.sp)
            Button(onClick = { locationPermissionState.launchPermissionRequest() }, enabled = locationPermissionState.status.shouldShowRationale) {
                Text("Pide permiso")
            }
        }
    }
}

fun createLocationRequest(): LocationRequest {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(5000)
        .build()
    return locationRequest
}

fun createLocationCallback(onLocationChange: (LocationResult)->Unit): LocationCallback {
    val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult){
            super.onLocationResult(locationResult)
            onLocationChange(locationResult)
        }
    }
    return locationCallback
}


// Funcion de preview, va lag por ahora
/*@Preview(showBackground = true)
@Composable
fun Vista360Preview() {
    Vista360(controller = rememberNavController())
}*/