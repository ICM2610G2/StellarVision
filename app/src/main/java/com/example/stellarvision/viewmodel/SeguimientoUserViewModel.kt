package com.example.stellarvision.viewmodel

import android.location.Location
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

    fun startTrackingUser(userId: String) {
        stopTrackingUser()

        val ref = database.getReference("users").child(userId)
        trackedUserRef = ref

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(MyUser::class.java) ?: return

                val hasValidLocation = user.latitude != 0.0 || user.longitude != 0.0

                val trackedLocation = if (hasValidLocation) {
                    LatLng(user.latitude, user.longitude)
                } else {
                    null
                }

                _state.update { current ->
                    current.copy(
                        trackedUser = user,
                        trackedUserLocation = trackedLocation,
                        distanceMeters = calculateDistance(
                            from = current.myLocation,
                            to = trackedLocation
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }

        trackedUserListener = listener
        ref.addValueEventListener(listener)
    }

    fun updateMyLocation(location: Location) {
        val myLatLng = LatLng(location.latitude, location.longitude)

        _state.update { current ->
            current.copy(
                myLocation = myLatLng,
                distanceMeters = calculateDistance(
                    from = myLatLng,
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
        val uid = auth.currentUser?.uid ?: return

        val updates = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "locationUpdatedAt" to ServerValue.TIMESTAMP
        )

        database.getReference("users")
            .child(uid)
            .updateChildren(updates)
    }

    private fun calculateDistance(
        from: LatLng?,
        to: LatLng?
    ): Float? {
        if (from == null || to == null) return null

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