package com.example.stellarvision.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stellarvision.model.MyUser
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SeguimientoState(
    val trackedUser: MyUser? = null,
    val trackedUserLocation: LatLng? = null,
    val myLocation: LatLng? = null,
    val distanceMeters: Float? = null,
    val routePoints: List<LatLng> = emptyList()
)

class SeguimientoUserViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(SeguimientoState())
    val state = _state.asStateFlow()

    private var trackedUserRef = database.getReference("users")
    private var trackedUserListener: ValueEventListener? = null

    private val movementThresholdMeters = 10f

    private var lastFirebaseLocation: LatLng? = null
    private var lastTrackedLocationAccepted: LatLng? = null

    fun startTrackingUser(userId: String) {
        stopTrackingUser()

        val ref = database.getReference("users").child(userId)
        trackedUserRef = ref

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(MyUser::class.java) ?: return

                val hasValidLocation = user.latitude != 0.0 || user.longitude != 0.0

                val newTrackedLocation = if (hasValidLocation) {
                    LatLng(user.latitude, user.longitude)
                } else {
                    null
                }

                if (newTrackedLocation == null) {
                    _state.update {
                        it.copy(
                            trackedUser = user,
                            trackedUserLocation = null,
                            distanceMeters = null,
                            routePoints = emptyList()
                        )
                    }
                    return
                }

                val previousTrackedLocation = lastTrackedLocationAccepted

                val shouldAcceptTrackedLocation =
                    previousTrackedLocation == null ||
                            distanceBetween(previousTrackedLocation, newTrackedLocation) >= movementThresholdMeters

                if (!shouldAcceptTrackedLocation) {
                    Log.d(
                        "SeguimientoUser",
                        "Usuario seguido se movió menos de $movementThresholdMeters m. No se actualiza marcador ni cámara."
                    )

                    _state.update { current ->
                        current.copy(
                            trackedUser = user,
                            distanceMeters = calculateDistance(
                                from = current.myLocation,
                                to = current.trackedUserLocation
                            )
                        )
                    }

                    return
                }

                lastTrackedLocationAccepted = newTrackedLocation

                Log.d(
                    "SeguimientoUser",
                    "Usuario seguido superó $movementThresholdMeters m. Se actualiza marcador y cámara."
                )

                _state.update { current ->
                    current.copy(
                        trackedUser = user,
                        trackedUserLocation = newTrackedLocation,
                        distanceMeters = calculateDistance(
                            from = current.myLocation,
                            to = newTrackedLocation
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SeguimientoUser", "Error leyendo usuario seguido", error.toException())
            }
        }

        trackedUserListener = listener
        ref.addValueEventListener(listener)
    }

    fun updateMyLocation(location: Location) {
        val newMyLocation = LatLng(location.latitude, location.longitude)
        val previousFirebaseLocation = lastFirebaseLocation

        val shouldUpdateFirebase =
            previousFirebaseLocation == null ||
                    distanceBetween(previousFirebaseLocation, newMyLocation) >= movementThresholdMeters

        if (!shouldUpdateFirebase) {
            Log.d(
                "SeguimientoUser",
                "Mi ubicación cambió menos de $movementThresholdMeters m. No se actualiza Firebase."
            )
            return
        }

        lastFirebaseLocation = newMyLocation

        Log.d(
            "SeguimientoUser",
            "Mi ubicación superó $movementThresholdMeters m. Se actualiza Firebase."
        )

        _state.update { current ->
            current.copy(
                myLocation = newMyLocation,
                distanceMeters = calculateDistance(
                    from = newMyLocation,
                    to = current.trackedUserLocation
                )
            )
        }

        updateMyLocationInFirebase(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun updateMyLocationInFirebase(
        latitude: Double,
        longitude: Double
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            Log.e("SeguimientoUser", "No hay usuario autenticado. No se actualiza ubicación.")
            return
        }

        val updates = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "locationUpdatedAt" to ServerValue.TIMESTAMP
        )

        database.getReference("users")
            .child(uid)
            .updateChildren(updates)
            .addOnSuccessListener {
                Log.d("SeguimientoUser", "Ubicación actualizada en Firebase.")
            }
            .addOnFailureListener { e ->
                Log.e("SeguimientoUser", "Error actualizando ubicación en Firebase", e)
            }
    }

    private fun calculateDistance(
        from: LatLng?,
        to: LatLng?
    ): Float? {
        if (from == null || to == null) return null
        return distanceBetween(from, to)
    }

    private fun distanceBetween(
        from: LatLng,
        to: LatLng
    ): Float {
        val results = FloatArray(1)

        Location.distanceBetween(
            from.latitude,
            from.longitude,
            to.latitude,
            to.longitude,
            results
        )

        return results[0]
    }

    fun setRoutePoints(points: List<LatLng>) {
        _state.update {
            it.copy(routePoints = points)
        }
    }

    fun clearRoutePoints() {
        _state.update {
            it.copy(routePoints = emptyList())
        }
    }

    fun stopTrackingUser() {
        trackedUserListener?.let { listener ->
            trackedUserRef.removeEventListener(listener)
        }

        trackedUserListener = null
    }

    override fun onCleared() {
        stopTrackingUser()
        super.onCleared()
    }
}