package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stellarvision.model.mapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {
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

    fun updateLightLevel(value: Float) {
        _state.value = _state.value.copy(
            lightLevel = value,
            isDarkMap = value < 2000f
        )
    }

    fun setDarkMap(value: Boolean) {
        _state.value = _state.value.copy(isDarkMap = value)
    }

    fun setSelectedDistance(value: String) {
        _state.value = _state.value.copy(selectedDistance = value)
    }
}