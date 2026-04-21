package com.example.stellarvision.Screens.Maps

import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.Locale
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stellarvision.Model.pois
import com.example.stellarvision.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapaPermiso(model: mapViewModel = viewModel(), controller: NavController) {
    val permission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permission.status.isGranted)
            permission.launchPermissionRequest()
    }

    if (!permission.status.isGranted) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Se necesita el permiso de ubicación para mostrar el mapa.")
        }
        return
    }

    MostrarMapa(model, controller)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MostrarMapa(model: mapViewModel, navController: NavController) {
    val context = LocalContext.current
    val state by model.state.collectAsState()
    val scope = rememberCoroutineScope()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val destPoiId = savedStateHandle?.get<String>("poi_destination_id")
    val backStackEntry by navController.currentBackStackEntryAsState()
    val handle = backStackEntry?.savedStateHandle

    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val roadManager = remember { OSRMRoadManager(context, "ANDROID") }
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = remember { createLocationRequest() }

    val locationCallback = remember {
        createLocationCallback { result ->
            result.lastLocation?.let { loc ->
                model.updateUserPoint(GeoPoint(loc.latitude, loc.longitude))
            }
        }
    }

    DisposableEffect(Unit) {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, android.os.Looper.getMainLooper())
        onDispose { locationClient.removeLocationUpdates(locationCallback) }
    }

    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            this.controller.setZoom(18.0)
            this.controller.setCenter(state.userPoint)
        }
    }

    LaunchedEffect(destPoiId, state.userPoint) {
        if (destPoiId == null) return@LaunchedEffect

        //Limpia la ruta
        handle?.remove<String>("poi_destination_id")

        val poi = pois.firstOrNull { it.id == destPoiId } ?: return@LaunchedEffect

        //OSRM pinta la ruta
        scope.launch(Dispatchers.IO) {
            val route = buildRoutePoints(roadManager, state.userPoint, poi.point)
            withContext(Dispatchers.Main) {
                model.setRoute(route)
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mapa") }) },
    ) { padding ->

        Box(Modifier.fillMaxSize().padding(padding)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
                update = { view ->
                    view.overlays.clear()

                    //Ruta
                    if (state.routePoints.size >= 2) {
                        view.overlays.add(
                            Polyline().apply {
                                setPoints(state.routePoints)
                                outlinePaint.strokeWidth = 10f
                                outlinePaint.color = android.graphics.Color.RED
                            }
                        )
                    }

                    //Pointers
                    val userIcon = scaledDrawable(context, R.drawable.person_complete_icon, 56)
                    addMarker(view, state.userPoint, "Mi ubicacion", icon = userIcon)

                    pois.forEach { poi ->
                        val poiIcon = scaledDrawable(context, R.drawable.location_icon, 56)
                        val m = Marker(view).apply {
                            position = poi.point
                            title = poi.title
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = poiIcon

                            setOnMarkerClickListener { _, _ ->
                                navController.navigate("poi/${poi.id}")
                                true
                            }
                        }
                        view.overlays.add(m)
                    }
                    view.invalidate()
                }

            )

        }
    }
}

private fun addMarker(mapView: MapView, point: GeoPoint, title: String, icon: android.graphics.drawable.Drawable? = null
) {
    mapView.overlays.add(
        Marker(mapView).apply {
            position = point
            this.title = title
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            if (icon != null) this.icon = icon
        }
    )
}