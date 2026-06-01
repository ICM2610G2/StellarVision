package com.example.stellarvision.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.AppTextField
import com.example.stellarvision.navigation.AppScreens
import com.example.stellarvision.viewmodel.PublicacionViewModel

@Composable
fun CrearPublicacion(
    controller: NavController,
    viewModel: PublicacionViewModel = viewModel()
) {
    val imageUriString = controller.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("imageUri")

    val imageUri = imageUriString?.toUri()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var constelacion by remember { mutableStateOf("") }
    var privacidadUbicacion by remember { mutableStateOf("NINGUNA") }

    val opcionesConstelacion = listOf(
        "Orión",
        "Casiopea",
        "Osa Mayor",
        "Escorpio",
        "Cruz del Sur",
        "Otra"
    )

    val formularioCompleto =
        titulo.isNotBlank() &&
                descripcion.isNotBlank() &&
                constelacion.isNotBlank() &&
                imageUri != null &&
                !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = "Crear publicación",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AppText(
                    text = "Vista previa",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Sin imagen",
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                AppText("No hay imagen seleccionada")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AppButton(
                    text = "Cambiar imagen",
                    onClick = {
                        controller.popBackStack()
                    },
                    enabled = !isLoading
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AppTextField(
            value = titulo,
            onValueChange = { if (!isLoading) titulo = it },
            placeholder = "Título de la publicación"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppTextField(
            value = descripcion,
            onValueChange = { if (!isLoading) descripcion = it },
            placeholder = "Descripción de la observación"
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppText(
            text = "Constelación",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opcionesConstelacion.forEach { opcion ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { if (!isLoading) constelacion = opcion }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = if (constelacion == opcion) "✓ $opcion" else opcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (constelacion == opcion)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AppText(
            text = "Privacidad de ubicación",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("EXACTA", "PARCIAL", "NINGUNA").forEach { opcion ->
                ElevatedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { if (!isLoading) privacidadUbicacion = opcion },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AppText(
                            text = if (privacidadUbicacion == opcion) "✓ $opcion" else opcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (privacidadUbicacion == opcion)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            AppButton(
                text = "Publicar",
                onClick = {
                    if (imageUri != null) {
                        viewModel.subirPublicacion(
                            title = titulo,
                            description = descripcion,
                            constellation = constelacion,
                            locationPrivacy = privacidadUbicacion,
                            imageUri = imageUri,
                            onSuccess = {
                                Toast.makeText(context, "¡Publicado con éxito!", Toast.LENGTH_LONG).show()

                                controller.navigate(AppScreens.Homepage.name) {
                                    popUpTo(AppScreens.Homepage.name) { inclusive = true }
                                }
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = formularioCompleto
            )
        }
    }
}