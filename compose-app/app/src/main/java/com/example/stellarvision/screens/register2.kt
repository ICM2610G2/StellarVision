package com.example.stellarvision.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.stellarvision.R
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppHeader
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.RegisterViewModel
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

@Composable
fun Register2(controller: NavController, model : RegisterViewModel = viewModel())
{
    val user by model.registerState.collectAsState()
    var showPassword by remember{mutableStateOf(false)}

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }


    val cameraUri = remember {
        val imagePath = File(context.cacheDir, "camera_photos")
        if (!imagePath.exists()) {
            imagePath.mkdirs()
        }
        val file = File(
            imagePath,
            "avatar_${System.currentTimeMillis()}.jpg"
        )
        if (!file.exists()) {
            file.createNewFile()
        }

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraUri
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(
                context,
                "Debes conceder permiso para usar la cámara",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(
            "Stellar Vision",
            "Completa tu Registro",
            R.drawable.vectorsvlogo,
            "Crea tu perfil de usuario"
        )

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .size(90.dp)
                .clickable { showDialog = true },
            contentAlignment = Alignment.BottomEnd
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.vectorsvlogo),
                    contentDescription = "Foto por defecto",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Editar Foto",
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            user.username,
            {model.updateUsername(it)},
            placeholder = "Nombre de usuario",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuario"
                )
            },
            supportingText = {Text(user.usernameError, color = Color.Red)}
        )

        Spacer(modifier = Modifier.height(4.dp))

        AppTextField(
            user.phoneNumber,
            {model.updatePhoneNumber(it)},
            placeholder = "Número de celular",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Celular"
                )
            },
            supportingText = {Text(user.phoneNumberError, color = Color.Red)}
        )

        Spacer(modifier = Modifier.height(4.dp))

        AppTextField(
            user.password,
            {model.updatePassword(it)},
            placeholder = "Contraseña",
            isPassword = !showPassword,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña"
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        ),
                        contentDescription = "Visibilidad Contraseña"
                    )
                }
            },
            supportingText = {Text(user.passwordError, color = Color.Red)}
        )

        Spacer(modifier = Modifier.height(4.dp))

        AppTextField(
            user.confirmPassword,
            {model.updateConfirmPassword(it)},
            placeholder = "Confirmar contraseña",
            isPassword = !showPassword,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirmar Contraseña"
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            if (showPassword) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        ),
                        contentDescription = "Visibilidad Confirmar Contraseña"
                    )
                }
            },
            supportingText = {Text(user.confirmPasswordError, color = Color.Red)}
        )

        AppButton(
            "Crear Cuenta",
            onClick = {
                model.register(
                    email = user.email,
                    password = user.password,
                    username = user.username,
                    confirmPassword = user.confirmPassword,
                    phoneNumber = user.phoneNumber,
                    profilePictureUrl = imageUri?.toString() ?: "",
                    onSuccess = { controller.navigate(AppScreens.Homepage.name) },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.padding(16.dp)
        )
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("Elige una opción") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false

                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {

                                cameraLauncher.launch(cameraUri)
                            }

                            else -> {
                                cameraPermissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            }
                        }
                    }
                ) {
                    Text("Cámara")
                }
            }
        )
    }
}
