package com.example.stellarvision.screens

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_LIGHT
import android.hardware.Sensor.TYPE_PRESSURE
import android.hardware.Sensor.TYPE_ROTATION_VECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.stellarvision.model.Star
import com.example.stellarvision.R
import com.example.stellarvision.Util.getStarsFromHYG
import com.example.stellarvision.Util.visibleStars
import com.example.stellarvision.sensorManager
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
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun Vista360(controller: NavController) {
    var stars by remember { mutableStateOf<List<Star>>(emptyList()) }
    var visibleStars by remember { mutableStateOf<List<Star>>(emptyList())}
    val context = LocalContext.current

    var x_rot by remember { mutableFloatStateOf(0.0F) }
    var y_rot by remember { mutableFloatStateOf(0.0F) }
    var z_rot by remember { mutableFloatStateOf(0.0F) }

    var azimuth by remember { mutableFloatStateOf(0.0F) }
    var lastAzimuth by remember { mutableFloatStateOf(0.0F) }
    var pitch by remember { mutableFloatStateOf(0.0F) }
    var lastPitch by remember { mutableFloatStateOf(0.0F) }
    var roll by remember { mutableFloatStateOf(0.0F) }

    var newAltitude by remember { mutableFloatStateOf(0.0F) }
    var pressure by remember { mutableFloatStateOf(760.0F) }

    newAltitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)

    var lightLevel by remember { mutableStateOf(0.0F)}
    var tieneSensor by remember { mutableStateOf(false)}

    val now = Clock.System.now()

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
    val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    if (pressureSensor == null) {
        Log.e("SensorsLog", "This device has no Barometer hardware.")
        tieneSensor = false
    }else{
        Log.e("SensorsLog", "This device has Barometer hardware.")
        tieneSensor = true
    }

    val sensorListener = object : SensorEventListener{
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type == TYPE_ROTATION_VECTOR){
                x_rot = event.values[0]
                y_rot = event.values[1]
                z_rot = event.values[2]
                val tempAzimuth: Float

                val rotationMatrix = FloatArray(9)
                val remappedMatrix = FloatArray(9)
                val orientationAngles = FloatArray(3)

                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, remappedMatrix)
                SensorManager.getOrientation(remappedMatrix, orientationAngles)

                val rawAzimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                val rawPitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                roll = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

                azimuth = (rawAzimuth + 360) % 360
                pitch = rawPitch

            }
            if(event?.sensor?.type == TYPE_PRESSURE){
                if(tieneSensor){
                    pressure = event.values[0]
                }
                newAltitude = SensorManager.getAltitude(
                    SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                    pressure
                )
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

    LaunchedEffect(azimuth, pitch, latitude, stars) {
        if(abs(azimuth - lastAzimuth) > 0.5f || abs(pitch - lastPitch) > 0.5f){
            withContext(Dispatchers.Default){
                val filtered = visibleStars(
                    stars,
                    latitude,
                    longitude,
                    altitude,
                    azimuth,
                    -pitch,
                    utcTime = System.currentTimeMillis()
                )

                visibleStars = filtered
                lastAzimuth = azimuth
                lastPitch = pitch

            }
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
        pressureSensor?.let{
            sensorManager.registerListener(sensorListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
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

        Column(modifier = Modifier.weight(3F).padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            /*Text("La latitud es: $latitude", fontSize = 20.sp)
            Text("La longitud es: $longitude", fontSize = 20.sp)
            Text("Altitudes: $altitude, $newAltitude", fontSize = 20.sp)
            //Text("Los valores de vector de rotacion son x: ${x_rot}, y: ${y_rot}, z: ${z_rot} ", fontSize = 20.sp)
            Text("Azimuth: $azimuth, Pitch: $pitch, Roll: $roll", fontSize = 20.sp)
            //Text("Fecha y hora: $now", fontSize = 20.sp)
            //Text("El valor de luz detectado por el sensor es: ${lightLevel}", fontSize = 20.sp)
            Text("Test presion atmos: $testPressure ($tieneSensor)", fontSize = 20.sp)*/

            Compass(azimuth)

        }

        LazyColumn(modifier = Modifier.weight(6F)) {
            items(visibleStars.sortedBy { it.visualMagnitude }) { star ->
                StarInfo(star)
            }
        }

    }

}

@Composable
fun StarInfo(star: Star){
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        MyRow("Nombre: ${star.properName}, Constelación: ${star.constelation}", Icons.Default.Star)
        MyRow("Es ${star.luminosity?.toInt()} veces mas brillante que el sol!", Icons.Default.Info)
        MyRow("Se encuentra a ${star.distance?.times(3262)?.toInt()} años luz de nosotros!", Icons.Default.Info)
    }
}

@Composable
fun Compass(azimuth: Float){
    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(R.drawable.circle),
            contentDescription = "Circulo brujula",
            modifier = Modifier.fillMaxSize(),
            tint = MaterialTheme.colorScheme.outline
        )

        Icon(
            painter = painterResource(R.drawable.north_arrow),
            contentDescription = "Indicador del Norte",
            modifier = Modifier.size(64.dp).rotate(-azimuth),
            tint = Color.Red
        )

    }

    CompassText(azimuth)
}

@Composable
fun CompassText(azimuth: Float){
    val direction = getCardinalDirection(azimuth)
    val degrees = azimuth.toInt()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = "$degrees",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = direction,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }

}

fun getCardinalDirection(azimuth: Float): String{
    val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((azimuth + 45/2) % 360 / 45).toInt()
    return directions[index]
}

@Composable
fun MyRow(text: String, icon: ImageVector){
    Row(
        modifier = Modifier.fillMaxSize()
    ){
        Icon(imageVector = icon, "Icon", Modifier.weight(2F).align(Alignment.CenterVertically))
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