package com.example.stellarvision.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraXViewModel : ViewModel() {
    private val _photoUris = MutableStateFlow<List<Uri>>(emptyList())
    val photoUris = _photoUris.asStateFlow()

    fun onPhotoSaved(uri: Uri) {
        _photoUris.value = _photoUris.value + uri
    }
}