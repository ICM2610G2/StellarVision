package com.example.stellarvision.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppText
import com.example.stellarvision.common.AppTextField

@Composable
fun CrearPublicacion(
    controller: NavController,
) {
    val imageUriString = controller.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("imageUri")

    val imageUri = imageUriString?.toUri()
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
                constelacion.isNotBlank()

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
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AppTextField(
            value = titulo,
            onValueChange = { titulo = it },
            placeholder = "Título de la publicación"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
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
                    onClick = { constelacion = opcion }
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
                        .clickable { privacidadUbicacion = opcion },
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

        AppButton(
            text = "Publicar",
            onClick = {
                // TODO:
                // Aquí luego conectamos Firebase Storage + Realtime DB / Firestore
            },
            enabled = formularioCompleto
        )
    }
}