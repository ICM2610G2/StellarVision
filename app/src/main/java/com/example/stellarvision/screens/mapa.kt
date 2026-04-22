package com.example.stellarvision.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.example.stellarvision.common.createLocationCallback
import com.example.stellarvision.common.createLocationRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stellarvision.model.pois
import com.example.stellarvision.R
import com.example.stellarvision.common.AppFab
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.example.stellarvision.viewmodel.MapViewModel

private const val DARK_LUX_THRESHOLD = 200f

private val LIGHT_TILE_SOURCE = XYTileSource(
    "CartoLight",
    0,
    20,
    256,
    ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/light_all/",
        "https://b.basemaps.cartocdn.com/light_all/",
        "https://c.basemaps.cartocdn.com/light_all/",
        "https://d.basemaps.cartocdn.com/light_all/"
    )
)

private val DARK_TILE_SOURCE = XYTileSource(
    "CartoDark",
    0,
    20,
    256,
    ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/dark_all/",
        "https://b.basemaps.cartocdn.com/dark_all/",
        "https://c.basemaps.cartocdn.com/dark_all/",
        "https://d.basemaps.cartocdn.com/dark_all/"
    )
)

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
            Text("Se necesita el permiso de ubicación para mostrar el mapa.")
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
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = backStackEntry?.savedStateHandle
    val destPoiId = savedStateHandle?.get<String>("poi_destination_id")

    val roadManager = remember { OSRMRoadManager(context, "ANDROID") }
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
                model.updateUserPoint(GeoPoint(loc.latitude, loc.longitude))
            }
        }
    }

    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            setTileSource(LIGHT_TILE_SOURCE)
            controller.setZoom(18.0)
            controller.setCenter(state.userPoint)
        }
    }

    DisposableEffect(Unit) {

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
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

    LaunchedEffect(state.isDarkMap) {
        mapView.setTileSource(if (state.isDarkMap) DARK_TILE_SOURCE else LIGHT_TILE_SOURCE)
        mapView.invalidate()
    }

    LaunchedEffect(destPoiId, state.userPoint) {
        if (destPoiId == null) return@LaunchedEffect

        savedStateHandle?.remove<String>("poi_destination_id")

        val poi = pois.firstOrNull { it.id == destPoiId } ?: return@LaunchedEffect

        scope.launch(Dispatchers.IO) {
            val route = buildRoutePoints(roadManager, state.userPoint, poi.point)
            val distance = distanceMeters(state.userPoint, poi.point)

            withContext(Dispatchers.Main) {
                model.setRoute(route)
                model.setSelectedDistance(formatDistance(distance))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mapa") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
                update = { view ->
                    view.overlays.clear()

                    if (state.routePoints.size >= 2) {
                        view.overlays.add(
                            Polyline().apply {
                                setPoints(state.routePoints)
                                outlinePaint.strokeWidth = 10f
                                outlinePaint.color = Color.RED
                            }
                        )
                    }

                    val userIcon = scaledDrawable(context, R.drawable.person_complete_icon, 56)
                    addMarker(
                        mapView = view,
                        point = state.userPoint,
                        title = "Mi ubicación",
                        icon = userIcon
                    )

                    pois.forEach { poi ->
                        val poiIcon = scaledDrawable(context, R.drawable.location_icon, 56)
                        val marker = Marker(view).apply {
                            position = poi.point
                            title = poi.title
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = poiIcon

                            setOnMarkerClickListener { _, _ ->
                                val distance = formatDistance(
                                    distanceMeters(state.userPoint, poi.point)
                                )

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("poi_distance", distance)

                                navController.navigate("poi/${poi.id}")
                                true
                            }
                        }
                        view.overlays.add(marker)
                    }

                    view.invalidate()
                }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                FilledTonalButton(
                    onClick = {
                        mapView.controller.animateTo(state.userPoint)
                        mapView.controller.setZoom(12.0)
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
    poiId: String
) {
    val poi = pois.first { it.id == poiId }
    val distance =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("poi_distance")
            ?: "Sin calcular"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(poi.title) })
        },
        floatingActionButton = {
            AppFab(
                contentDescription = "Ruta",
                onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("poi_destination_id", poi.id)

                    navController.popBackStack()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(poi.imageRes),
                contentDescription = poi.title,
                modifier = Modifier.weight(1f),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Distancia: $distance",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private fun addMarker(
    mapView: MapView,
    point: GeoPoint,
    title: String,
    icon: Drawable? = null
) {
    mapView.overlays.add(
        Marker(mapView).apply {
            position = point
            this.title = title
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            if (icon != null) {
                this.icon = icon
            }
        }
    )
}

fun findLocation(
    geocoder: Geocoder,
    address: String
): GeoPoint? {
    return try {
        val addresses = geocoder.getFromLocationName(address, 1)
        if (!addresses.isNullOrEmpty()) {
            val a = addresses[0]
            GeoPoint(a.latitude, a.longitude)
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}

fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
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
    roadManager: OSRMRoadManager,
    start: GeoPoint,
    end: GeoPoint
): List<GeoPoint> {
    return try {
        val road = roadManager.getRoad(arrayListOf(start, end))
        val points = road.mRouteHigh
        if (points != null && points.size >= 2) points else listOf(start, end)
    } catch (_: Exception) {
        listOf(start, end)
    }
}

fun scaledDrawable(
    context: Context,
    resId: Int,
    sizePx: Int
): Drawable? {
    val drawable = ContextCompat.getDrawable(context, resId) ?: return null

    val bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        else -> {
            val createdBitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth.coerceAtLeast(1),
                drawable.intrinsicHeight.coerceAtLeast(1),
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(createdBitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            createdBitmap
        }
    }

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, true)
    return BitmapDrawable(context.resources, scaledBitmap)
}