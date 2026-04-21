package com.example.stellarvision.Screens.Maps

import androidx.lifecycle.ViewModel
import com.example.stellarvision.Model.mapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.osmdroid.util.GeoPoint

class mapViewModel : ViewModel() {
    private val _state = MutableStateFlow(mapState())
    val state = _state.asStateFlow()

    fun updateUserPoint(point: GeoPoint) {
        _state.value = _state.value.copy(userPoint = point)
    }

    fun updateStartAddress(v: String) {
        _state.value = _state.value.copy(startAddress = v)
    }

    fun updateEndAddress(v: String) {
        _state.value = _state.value.copy(endAddress = v)
    }

    fun setStartPoint(p: GeoPoint?) {
        _state.value = _state.value.copy(startPoint = p, routePoints = emptyList())
    }

    fun setEndPoint(p: GeoPoint?) {
        _state.value = _state.value.copy(endPoint = p, routePoints = emptyList())
    }

    fun setRoute(points: List<GeoPoint>) {
        _state.value = _state.value.copy(routePoints = points)
    }

    fun setDestinationPoint(p: GeoPoint?) {
        _state.value = _state.value.copy(destinationPoint = p, routePoints = emptyList())
    }
}