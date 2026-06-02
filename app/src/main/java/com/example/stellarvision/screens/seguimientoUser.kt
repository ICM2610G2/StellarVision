package com.example.stellarvision.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stellarvision.model.MyUser
import com.example.stellarvision.ui.theme.Purple40
import com.example.stellarvision.viewmodel.SeguimientoUserViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoUsuarioScreen(
    navController: NavController,
    userId: String,
    viewModel: SeguimientoUserViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted

        if (!granted) {
            Toast.makeText(
                context,
                "Permiso de ubicación denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(userId) {
        viewModel.startTrackingUser(userId)

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTrackingUser()
        }
    }

    StartRealtimeLocationUpdates(
        context = context,
        hasLocationPermission = hasLocationPermission,
        onLocationChanged = { location ->
            viewModel.updateMyLocation(location)
        }
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(4.60971, -74.08175),
            13f
        )
    }

    LaunchedEffect(state.trackedUserLocation) {
        state.trackedUserLocation?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, 15f)
            )
        }
    }

    LaunchedEffect(state.myLocation, state.trackedUserLocation) {
        val myLocation = state.myLocation
        val trackedLocation = state.trackedUserLocation

        if (myLocation != null && trackedLocation != null) {
            val route = withContext(Dispatchers.IO) {
                buildRoutePoints(
                    context = context,
                    start = myLocation,
                    end = trackedLocation
                )
            }

            viewModel.setRoutePoints(route)
        } else {
            viewModel.clearRoutePoints()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Seguimiento",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Purple40
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                state.trackedUserLocation?.let { trackedLocation ->
                    Marker(
                        state = rememberUpdatedMarkerState(
                            position = trackedLocation
                        ),
                        title = state.trackedUser?.username ?: "Usuario seguido",
                        snippet = "Ubicación en tiempo real",
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED
                        )
                    )
                }

                state.myLocation?.let { myLocation ->
                    Marker(
                        state = rememberUpdatedMarkerState(
                            position = myLocation
                        ),
                        title = "Mi ubicación",
                        snippet = "Usuario que hace seguimiento",
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_VIOLET
                        )
                    )
                }

                if (state.routePoints.size >= 2) {
                    Polyline(
                        points = state.routePoints,
                        color = Color(0xFF4285F4),
                        width = 8f
                    )
                }
            }

            TrackingInfoCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                user = state.trackedUser,
                distanceMeters = state.distanceMeters
            )
        }
    }
}

@Composable
fun TrackingInfoCard(
    modifier: Modifier = Modifier,
    user: MyUser?,
    distanceMeters: Float?
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Usuario seguido",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.username ?: "Cargando..."
            )

            Text(
                text = if (distanceMeters != null) {
                    if (distanceMeters < 1000) {
                        "Distancia: ${distanceMeters.toInt()} m"
                    } else {
                        "Distancia: ${"%.2f".format(distanceMeters / 1000f)} km"
                    }
                } else {
                    "Distancia: calculando..."
                },
                fontWeight = FontWeight.Bold,
                color = Purple40
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun StartRealtimeLocationUpdates(
    context: Context,
    hasLocationPermission: Boolean,
    onLocationChanged: (Location) -> Unit
) {
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            onDispose { }
        } else {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000L
            )
                .setMinUpdateIntervalMillis(1000L)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        onLocationChanged(location)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                request,
                callback,
                context.mainLooper
            )

            onDispose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }
}