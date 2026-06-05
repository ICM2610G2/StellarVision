package com.example.stellarvision.screens

import android.Manifest
import android.R
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stellarvision.common.AppFab
import com.example.stellarvision.common.createLocationCallback
import com.example.stellarvision.common.createLocationRequest
import com.example.stellarvision.ui.theme.Primary
import com.example.stellarvision.viewmodel.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.util.Log
import coil.compose.AsyncImage
import com.example.stellarvision.BuildConfig
import com.example.stellarvision.ui.theme.Purple40
import com.example.stellarvision.ui.theme.PurpleGrey80

@Composable
fun Mapa(controller: NavController) {
    MapaPermiso(controller = controller)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapaPermiso(
    controller: NavController,
    model: MapViewModel = viewModel()
) {
    val permission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) {
            permission.launchPermissionRequest()
        }
    }

    if (!permission.status.isGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "El permiso de ubicación es necesario para mostrar el mapa. \nPuedes darlo en la configuración de la aplicación",
                modifier = Modifier.padding(15.dp),
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium
            )
        }
        return
    }

    MostrarMapa(model = model, navController = controller)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MostrarMapa(
    model: MapViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val state by model.state.collectAsState()
    LaunchedEffect(state.pois) {
        Log.d("FirebasePOI", "POIs cargados: ${state.pois.size}")

        state.pois.forEach { poi ->
            Log.d(
                "FirebasePOI",
                "id=${poi.id}, title=${poi.title}, lat=${poi.point.latitude}, lng=${poi.point.longitude}, imagePath=${poi.imagePath}, imageUrl=${poi.imageUrl}"
            )
        }
    }
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = backStackEntry?.savedStateHandle
    val destPoiId = savedStateHandle?.get<String>("poi_destination_id")

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = remember { createLocationRequest() }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val lightSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) }

    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    model.updateLightLevel(event.values[0])
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
    }

    val locationCallback = remember {
        createLocationCallback { result: LocationResult ->
            result.lastLocation?.let { loc ->
                model.updateUserPoint(LatLng(loc.latitude, loc.longitude))
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.userPoint, 16f)
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        lightSensor?.let {
            sensorManager.registerListener(
                sensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
            sensorManager.unregisterListener(sensorListener)
        }
    }

    LaunchedEffect(destPoiId, state.userPoint) {
        if (destPoiId == null) return@LaunchedEffect

        savedStateHandle?.remove<String>("poi_destination_id")

        val poi = state.pois.firstOrNull { it.id == destPoiId } ?: return@LaunchedEffect

        scope.launch(Dispatchers.IO) {
            val route = buildRoutePoints(context, state.userPoint, poi.point)
            val distance = distanceMeters(state.userPoint, poi.point)

            withContext(Dispatchers.Main) {
                model.setRoute(route)
                model.setSelectedDistance(formatDistance(distance))
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(poi.point, 14f)
                )
            }
        }
    }

    val mapProperties = remember(state.isDarkMap) {
        MapProperties(
            mapType = MapType.NORMAL,
            mapStyleOptions = if (state.isDarkMap) MapStyleOptions(DARK_MAP_STYLE) else null
        )
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false,
            compassEnabled = true,
            mapToolbarEnabled = true
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Lugares con buena visibilidad",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                 )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                if (state.routePoints.size >= 2) {
                    Polyline(
                        points = state.routePoints,
                        color = Color(0xFF4285F4),
                        width = 12f
                    )
                }

                Marker(
                    state = rememberUpdatedMarkerState(position = state.userPoint),
                    title = "Mi ubicación",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )

                state.pois.forEach { poi ->
                    Marker(
                        state = rememberUpdatedMarkerState(position = poi.point),
                        title = poi.title,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        onClick = {
                            val distance = formatDistance(
                                distanceMeters(state.userPoint, poi.point)
                            )

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.apply {
                                    set("poi_distance", distance)
                                    set("selected_poi_title", poi.title)
                                    set("selected_poi_image_url", poi.imageUrl)
                                }

                            navController.navigate("poi/${poi.id}")
                            true
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                FilledTonalButton(
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(state.userPoint, 16f)
                            )
                        }
                    }
                ) {
                    Text("Centrar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointerDetail(
    navController: NavController,
    poiId: String,
    model: MapViewModel = viewModel()
) {
    val state by model.state.collectAsState()
    val previousHandle = navController.previousBackStackEntry?.savedStateHandle

    val poi = state.pois.firstOrNull { it.id == poiId }

    val title = poi?.title
        ?: previousHandle?.get<String>("selected_poi_title")
        ?: poiId

    val imageUrl = poi?.imageUrl.orEmpty()

    val distance = previousHandle?.get<String>("poi_distance") ?: "Sin calcular"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            AppFab(
                contentDescription = "Ruta",
                onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("poi_destination_id", poiId)

                    navController.popBackStack()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Purple40)
        ) {
            if (imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.weight(1f),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cargando imagen...",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Text(
                text = "Distancia: $distance",
                modifier = Modifier.padding(16.dp),
                fontSize = 21.sp,
                style = TextStyle(color = Color.White),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun findLocation(
    geocoder: Geocoder,
    address: String
): LatLng? {
    return try {
        val addresses = geocoder.getFromLocationName(address, 1)
        if (!addresses.isNullOrEmpty()) {
            val a = addresses[0]
            LatLng(a.latitude, a.longitude)
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}

fun distanceMeters(a: LatLng, b: LatLng): Double {
    val results = FloatArray(1)
    Location.distanceBetween(
        a.latitude,
        a.longitude,
        b.latitude,
        b.longitude,
        results
    )
    return results[0].toDouble()
}

fun formatDistance(distanceMeters: Double): String {
    return if (distanceMeters < 1000) {
        "${distanceMeters.toInt()} m"
    } else {
        String.format(Locale.getDefault(), "%.2f km", distanceMeters / 1000.0)
    }
}

fun buildRoutePoints(
    context: Context,
    start: LatLng,
    end: LatLng
): List<LatLng> {
    val apiKey = BuildConfig.MAPS_API_KEY

    if (apiKey.isBlank() || apiKey == "DEFAULT_API_KEY") {
        Log.e("MapaRoute", "MAPS_API_KEY vacía o inválida. Se pinta línea recta.")
        return listOf(start, end)
    }

    return try {
        val url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=${start.latitude},${start.longitude}" +
                "&destination=${end.latitude},${end.longitude}" +
                "&mode=walking" +
                "&key=$apiKey"

        val response = URL(url).readText()
        val json = JSONObject(response)

        val status = json.optString("status")

        if (status != "OK") {
            val error = json.optString("error_message")
            Log.e("MapaRoute", "Directions API error: $status $error. Se pinta línea recta.")

            return listOf(start, end)
        }

        val routes = json.getJSONArray("routes")

        if (routes.length() == 0) {
            Log.e("MapaRoute", "Directions API no devolvió rutas. Se pinta línea recta.")
            return listOf(start, end)
        }

        val route = routes.getJSONObject(0)
        val legs = route.getJSONArray("legs")

        val points = mutableListOf<LatLng>()

        for (i in 0 until legs.length()) {
            val steps = legs
                .getJSONObject(i)
                .getJSONArray("steps")

            for (j in 0 until steps.length()) {
                val encodedStep = steps
                    .getJSONObject(j)
                    .getJSONObject("polyline")
                    .getString("points")

                points.addAll(decodePolyline(encodedStep))
            }
        }

        if (points.isNotEmpty()) {
            points
        } else {
            val encodedOverview = route
                .getJSONObject("overview_polyline")
                .getString("points")

            val overviewPoints = decodePolyline(encodedOverview)

            if (overviewPoints.isNotEmpty()) {
                overviewPoints
            } else {
                listOf(start, end)
            }
        }

    } catch (e: Exception) {
        Log.e("MapaRoute", "Error construyendo ruta. Se pinta línea recta.", e)

        listOf(start, end)
    }
}

@Suppress("DEPRECATION")
private fun getGoogleMapsApiKey(context: Context): String {
    return try {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        appInfo.metaData?.getString("com.google.android.geo.API_KEY").orEmpty()
    } catch (_: Exception) {
        ""
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val polyline = mutableListOf<LatLng>()
    var index = 0
    var latitude = 0
    var longitude = 0

    while (index < encoded.length) {
        var result = 0
        var shift = 0
        var byte: Int

        do {
            byte = encoded[index++].code - 63
            result = result or ((byte and 0x1f) shl shift)
            shift += 5
        } while (byte >= 0x20)

        val deltaLatitude = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        latitude += deltaLatitude

        result = 0
        shift = 0

        do {
            byte = encoded[index++].code - 63
            result = result or ((byte and 0x1f) shl shift)
            shift += 5
        } while (byte >= 0x20)

        val deltaLongitude = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        longitude += deltaLongitude

        polyline.add(
            LatLng(
                latitude / 100000.0,
                longitude / 100000.0
            )
        )
    }

    return polyline
}

private const val DARK_MAP_STYLE = """
[
  {"elementType":"geometry","stylers":[{"color":"#212121"}]},
  {"elementType":"labels.icon","stylers":[{"visibility":"off"}]},
  {"elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
  {"elementType":"labels.text.stroke","stylers":[{"color":"#212121"}]},
  {"featureType":"administrative","elementType":"geometry","stylers":[{"color":"#757575"}]},
  {"featureType":"poi","elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
  {"featureType":"poi.park","elementType":"geometry","stylers":[{"color":"#181818"}]},
  {"featureType":"road","elementType":"geometry.fill","stylers":[{"color":"#2c2c2c"}]},
  {"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#8a8a8a"}]},
  {"featureType":"road.arterial","elementType":"geometry","stylers":[{"color":"#373737"}]},
  {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#3c3c3c"}]},
  {"featureType":"transit","elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
  {"featureType":"water","elementType":"geometry","stylers":[{"color":"#000000"}]},
  {"featureType":"water","elementType":"labels.text.fill","stylers":[{"color":"#3d3d3d"}]}
]
"""