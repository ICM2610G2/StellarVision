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
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.stellarvision.common.createLocationCallback
import com.example.stellarvision.common.createLocationRequest
import com.example.stellarvision.common.getStarsFromHYG
import com.example.stellarvision.common.visibleStars
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stellarvision.common.AppFab
import com.example.stellarvision.common.CameraXControls
import com.example.stellarvision.common.CameraXPhotoSheetContent
import com.example.stellarvision.common.CameraXPreview
import com.example.stellarvision.common.ScreenStar
import com.example.stellarvision.common.StarOverlay
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.ui.theme.PinkStarList
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.ui.theme.Purple40
import com.example.stellarvision.ui.theme.Secondary
import com.example.stellarvision.ui.theme.Surface
import com.example.stellarvision.viewmodel.CameraXViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.stellarvision.viewmodel.WeatherUiState
import com.example.stellarvision.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalTime::class)
@Composable
fun Vista360(
    controller: NavController,
    weatherViewModel: WeatherViewModel = viewModel()) {
    var stars by remember { mutableStateOf<List<Star>>(emptyList()) }
    var visibleStars by remember { mutableStateOf<List<ScreenStar>>(emptyList())}
    val context = LocalContext.current
    val weatherState by weatherViewModel.state.collectAsState()

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

    LaunchedEffect(latitude, longitude) {
        if (latitude != 0.0 || longitude != 0.0) {
            weatherViewModel.loadCurrentWeather(
                latitude = latitude,
                longitude = longitude
            )
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
                    utcTime = System.currentTimeMillis(),
                    60.0f,
                    40.0f
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

            WeatherPanel(
                weatherState = weatherState,
                onRetry = {
                    weatherViewModel.loadCurrentWeather(
                        latitude = latitude,
                        longitude = longitude,
                        force = true
                    )
                }
            )
        }

        LazyColumn(modifier = Modifier.weight(6F)) {
            items(visibleStars.sortedBy { it.star.visualMagnitude }) { star ->

                if(!star.star.properName.isNullOrEmpty()){
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = PinkStarList
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        StarInfo(star)
                    }
                }
            }
        }

    }

}

@Composable
fun StarInfo(Sstar: ScreenStar){
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        MyRow("Nombre: ", "${Sstar.star.properName}", Icons.Default.Star)
        MyRow("Constelación: ","${Sstar.star.constelation}", Icons.Default.Star)
        MyRow("","Es ${Sstar.star.luminosity?.toInt()} veces mas brillante que el sol!", Icons.Default.Info)
        MyRow("","Se encuentra a ${Sstar.star.distance?.times(3262)?.toInt()} años luz de nosotros!", Icons.Default.Info)
    }
}
@Composable
fun WeatherPanel(
    weatherState: WeatherUiState,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(280.dp)
                .height(220.dp),
            shape = RoundedCornerShape(50),
            color = PinkStarList,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            border = BorderStroke(2.dp, Color(0xFFB39DDB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    weatherState.isLoading -> {
                        Text(
                            text = "Cargando clima...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    weatherState.error != null -> {
                        Text(
                            text = weatherState.error,
                            fontSize = 18.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            modifier = Modifier.padding(top = 10.dp),
                            onClick = onRetry
                        ) {
                            Text("Reintentar")
                        }
                    }

                    weatherState.temperatureC != null -> {
                        Text(
                            text = weatherState.condition,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Purple40
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${weatherState.temperatureC.roundToInt()} °C",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1A1A1A)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Nubosidad: ${weatherState.cloudCover ?: 0}%",
                            fontSize = 18.sp,
                            color = Color.DarkGray
                        )

                        Text(
                            text = "Humedad: ${weatherState.humidity ?: 0}%",
                            fontSize = 18.sp,
                            color = Color.DarkGray
                        )
                    }

                    else -> {
                        Text(
                            text = "Esperando ubicación...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

fun weatherInfo(code: Int?): String {
    return when (code) {
        0 -> "Cielo despejado"
        1 -> "Mayormente despejado"
        2 -> "Parcialmente nublado"
        3 -> "Nublado"
        45, 48 -> "Niebla"
        51, 53, 55, 56, 57 -> "Llovizna"
        61, 63, 65, 66, 67, 80, 81, 82 -> "Lluvia"
        71, 73, 75, 77, 85, 86 -> "Nieve"
        95, 96, 99 -> "Tormenta"
        else -> "No se detecta el clima"
    }
}

@Composable
fun MyRow(atr: String, info: String, icon: ImageVector){
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Icon(imageVector = icon, "Icon", Modifier.weight(1F).align(Alignment.Top))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                ) {
                    append(atr)
                }

                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                ) {
                    append(info)
                }
            },
            modifier = Modifier.weight(8f).align(Alignment.CenterVertically)
        )
    }
}

/*
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
 */



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Vista360Screen(controller: NavController){
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    val locationPermissionState = rememberPermissionState(locationPermission)

    LaunchedEffect(Unit) {
        if(!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        floatingActionButton = {
            AppFab(
                onClick = { controller.navigate(AppScreens.CameraX.name) },
                icon = Icons.Default.PhotoCamera,
                contentDescription = "Abrir CameraX"
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if(locationPermissionState.status.isGranted){
                Vista360(controller)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textToShow = if (locationPermissionState.status.shouldShowRationale){
                        "Stellar Vision necesita la ubicación para poder mostrar las estrellas que están encima tuyo"
                    } else {
                        "El permiso de ubicación es necesario para mostrar las estrellas en la camara o aqui. \nPuedes darlo en la configuración de la aplicación"
                    }

                    Text(textToShow, modifier = Modifier.padding(15.dp), fontSize = 21.sp, fontWeight = FontWeight.Medium)
                    Button(
                        onClick = { locationPermissionState.launchPermissionRequest() },
                        enabled = locationPermissionState.status.shouldShowRationale
                    ) {
                        Text("Pide permiso")
                    }
                }
            }
        }
    }
}


// Funcion de preview, va lag por ahora
/*@Preview(showBackground = true)
@Composable
fun Vista360Preview() {
    Vista360(controller = rememberNavController())
}*/