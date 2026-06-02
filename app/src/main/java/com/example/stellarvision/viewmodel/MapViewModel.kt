package com.example.stellarvision.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stellarvision.model.Poi
import com.example.stellarvision.model.mapState
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.os.SystemClock

private var filteredLux: Float? = null
private var pendingDarkMode: Boolean? = null
private var pendingSince: Long = 0L

private val darkEnterThreshold = 800f
private val lightExitThreshold = 300f
private val smoothingFactor = 0.12f
private val stableTimeMs = 2000L

class MapViewModel : ViewModel() {

    private val _state = MutableStateFlow(mapState())
    val state = _state.asStateFlow()

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    init {
        loadPoisFromFirebase()
    }

    fun loadPoisFromFirebase() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingPois = true,
                    poisError = null
                )
            }

            try {
                val snapshot = firestore.collection("places").get().await()

                Log.d("FirebasePOI", "Documentos encontrados en Firestore: ${snapshot.documents.size}")

                val firebasePois = snapshot.documents.mapNotNull { doc ->
                    val id = doc.id
                    val title = doc.getString("title") ?: id
                    val imagePath = doc.getString("imagePath")?.trim().orEmpty()
                    val position = doc.getGeoPoint("position")

                    Log.d(
                        "FirebasePOI",
                        "DOC=$id | title=$title | imagePath=$imagePath | position=$position"
                    )

                    if (position == null) {
                        Log.e(
                            "FirebasePOI",
                            "Documento $id descartado: position no es GeoPoint o está vacío"
                        )
                        return@mapNotNull null
                    }

                    if (imagePath.isBlank()) {
                        Log.e(
                            "FirebasePOI",
                            "Documento $id tiene imagePath vacío"
                        )
                    }

                    val imageUrl = getImageDownloadUrl(imagePath)

                    Log.d(
                        "FirebasePOI",
                        "POI cargado: id=$id | title=$title | lat=${position.latitude} | lng=${position.longitude} | imageUrl=$imageUrl"
                    )

                    Poi(
                        id = id,
                        title = title,
                        point = LatLng(position.latitude, position.longitude),
                        imagePath = imagePath,
                        imageUrl = imageUrl
                    )
                }

                _state.update {
                    it.copy(
                        pois = firebasePois,
                        isLoadingPois = false,
                        poisError = null
                    )
                }

            } catch (e: Exception) {
                Log.e("FirebasePOI", "Error cargando places desde Firebase", e)

                _state.update {
                    it.copy(
                        isLoadingPois = false,
                        poisError = e.message ?: "Error cargando lugares"
                    )
                }
            }
        }
    }

    private suspend fun getImageDownloadUrl(imagePath: String): String {
        if (imagePath.isBlank()) return ""

        return try {
            val cleanPath = imagePath.trim()

            val ref = if (
                cleanPath.startsWith("gs://") ||
                cleanPath.startsWith("https://")
            ) {
                storage.getReferenceFromUrl(cleanPath)
            } else {
                storage.reference.child(cleanPath)
            }

            ref.downloadUrl.await().toString()

        } catch (e: Exception) {
            Log.e("FirebasePOI", "Error cargando imagen desde Storage: $imagePath", e)
            ""
        }
    }

    fun updateUserPoint(point: LatLng) {
        _state.update { it.copy(userPoint = point) }
    }

    fun updateStartAddress(v: String) {
        _state.update { it.copy(startAddress = v) }
    }

    fun updateEndAddress(v: String) {
        _state.update { it.copy(endAddress = v) }
    }

    fun setStartPoint(p: LatLng?) {
        _state.update { it.copy(startPoint = p, routePoints = emptyList()) }
    }

    fun setEndPoint(p: LatLng?) {
        _state.update { it.copy(endPoint = p, routePoints = emptyList()) }
    }

    fun setRoute(points: List<LatLng>) {
        _state.update { it.copy(routePoints = points) }
    }

    fun setDestinationPoint(p: LatLng?) {
        _state.update { it.copy(destinationPoint = p, routePoints = emptyList()) }
    }

    fun updateLightLevel(rawLux: Float) {
        val now = SystemClock.elapsedRealtime()

        val smoothedLux = filteredLux?.let { previous ->
            previous + smoothingFactor * (rawLux - previous)
        } ?: rawLux

        filteredLux = smoothedLux

        val currentDarkMode = _state.value.isDarkMap

        val desiredDarkMode = when {
            currentDarkMode && smoothedLux > lightExitThreshold -> false

            !currentDarkMode && smoothedLux < darkEnterThreshold -> true

            else -> currentDarkMode
        }

        if (desiredDarkMode != currentDarkMode) {
            if (pendingDarkMode != desiredDarkMode) {
                pendingDarkMode = desiredDarkMode
                pendingSince = now
            }

            val hasBeenStable = now - pendingSince >= stableTimeMs

            if (hasBeenStable) {
                pendingDarkMode = null

                _state.update {
                    it.copy(
                        lightLevel = smoothedLux,
                        isDarkMap = desiredDarkMode
                    )
                }
            } else {
                _state.update {
                    it.copy(lightLevel = smoothedLux)
                }
            }
        } else {
            pendingDarkMode = null

            _state.update {
                it.copy(lightLevel = smoothedLux)
            }
        }
    }

    fun setDarkMap(value: Boolean) {
        _state.update { it.copy(isDarkMap = value) }
    }

    fun setSelectedDistance(value: String) {
        _state.update { it.copy(selectedDistance = value) }
    }
}