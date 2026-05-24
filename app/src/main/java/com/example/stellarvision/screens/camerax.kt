package com.example.stellarvision.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.CameraXControls
import com.example.stellarvision.common.CameraXPhotoSheetContent
import com.example.stellarvision.common.CameraXPreview
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.CameraXViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraXScreen(
    controller: NavController,
    viewModel: CameraXViewModel = viewModel()
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val photoUris by viewModel.photoUris.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(message) {
        if (message != null) {
            delay(2500)
            message = null
        }
    }

    if (!permissionState.status.isGranted) {
        CameraXPermissionContent(
            showRationale = permissionState.status.shouldShowRationale,
            onRequestPermission = { permissionState.launchPermissionRequest() }
        )
        return
    }

    val cameraController = remember(appContext) {
        LifecycleCameraController(appContext).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CameraXPhotoSheetContent(
                photoUris = photoUris,
                onPhotoClick = { uri ->
                    navigateToCreatePublication(controller, uri)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraXPreview(
                controller = cameraController,
                modifier = Modifier.fillMaxSize()
            )

            CameraXTopBar(
                onBack = { controller.popBackStack() },
                onSwitchCamera = {
                    cameraController.cameraSelector =
                        if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )

            message?.let { text ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 72.dp, start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            CameraXControls(
                onOpenPhotos = {
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                },
                onTakePhoto = {
                    takePhoto(
                        context = context,
                        controller = cameraController,
                        onPhotoSaved = { uri ->
                            viewModel.onPhotoSaved(uri)
                            message = "Foto guardada. Puedes usarla en la publicación."
                        },
                        onError = { error ->
                            message = error
                        }
                    )
                },
                onUseLastPhoto = {
                    photoUris.lastOrNull()?.let { uri ->
                        navigateToCreatePublication(controller, uri)
                    }
                },
                canUsePhoto = photoUris.isNotEmpty(),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun CameraXTopBar(
    onBack: () -> Unit,
    onSwitchCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallFloatingActionButton(
            onClick = onBack,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }

        SmallFloatingActionButton(
            onClick = onSwitchCamera,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Cambiar cámara"
            )
        }
    }
}

@Composable
private fun CameraXPermissionContent(
    showRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = "Permiso de cámara requerido",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(12.dp))

        AppText(
            text = if (showRationale) {
                "Stellar Vision necesita acceso a la cámara para tomar fotos desde CameraX."
            } else {
                "Activa el permiso de cámara para usar esta pantalla."
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(18.dp))

        AppButton(
            text = "Conceder permiso",
            onClick = onRequestPermission
        )
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoSaved: (Uri) -> Unit,
    onError: (String) -> Unit
) {
    val photoFile = File(
        context.filesDir,
        "camerax_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                onPhotoSaved(uri)
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception.message ?: "No se pudo tomar la foto.")
            }
        }
    )
}

private fun navigateToCreatePublication(
    controller: NavController,
    uri: Uri
) {
    controller.currentBackStackEntry
        ?.savedStateHandle
        ?.set("imageUri", uri.toString())

    controller.navigate(AppScreens.Crearpublicacion.name)
}
