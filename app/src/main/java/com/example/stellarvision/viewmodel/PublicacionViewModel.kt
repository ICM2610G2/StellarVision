package com.example.stellarvision.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.stellarvision.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PublicacionViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun subirPublicacion(
        title: String,
        description: String,
        constellation: String,
        locationPrivacy: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        repository.crearPublicacion(
            title = title,
            description = description,
            constellation = constellation,
            locationPrivacy = locationPrivacy,
            imageUri = imageUri,
            onSuccess = {
                _isLoading.value = false
                onSuccess()
            },
            onError = { errorMsg ->
                _isLoading.value = false
                onError(errorMsg)
            }
        )
    }
}