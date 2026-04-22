package com.example.stellarvision.screens

import android.Manifest
import android.content.Context
import android.provider.ContactsContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.stellarvision.common.BottomBar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.stellarvision.common.iconsNavBar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stellarvision.common.AppButton
import com.example.stellarvision.common.AppText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

data class ContactoSimple(
    val nombre: String,
    val telefono: String
)

data class MensajeDummy(
    val contenido: String,
    val enviadoPorMi: Boolean
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Mensajeria(controller: NavController) {
    val context = LocalContext.current
    val contactsPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    var contactos by remember { mutableStateOf<List<ContactoSimple>>(emptyList()) }
    var contactoSeleccionado by remember { mutableStateOf<ContactoSimple?>(null) }
    var mensaje by remember { mutableStateOf("") }

    // Esto luego se reemplaza por mensajes reales de Firebase
    var mensajes by remember {
        mutableStateOf(
            listOf(
                MensajeDummy("Hola, vi tu publicación en StellarVision", false),
                MensajeDummy("¡Gracias! Luego subo más observaciones", true)
            )
        )
    }

    LaunchedEffect(contactsPermissionState.status.isGranted) {
        if (contactsPermissionState.status.isGranted) {
            contactos = obtenerContactos(context)
        }
    }
    BottomBar(controller, iconsNavBar){paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(32.dp)
    ) {
        AppText(
            text = "Mensajería",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppText(
            text = "Selecciona un contacto para iniciar una conversación relacionada con StellarVision.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!contactsPermissionState.status.isGranted) {

            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AppText(
                        text = "Permiso de contactos requerido",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppText(
                        text = if (contactsPermissionState.status.shouldShowRationale) {
                            "Necesitamos acceso a tus contactos para que puedas iniciar conversaciones con personas desde tu dispositivo."
                        } else {
                            "Para usar la mensajería con contactos del dispositivo, debes conceder el permiso."
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppButton(
                        text = "Conceder permiso",
                        onClick = {
                            contactsPermissionState.launchPermissionRequest()
                        }
                    )
                }
            }

        } else {

            if (contactoSeleccionado == null) {
                AppText(
                    text = "Tus contactos",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contactos) { contacto ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    contactoSeleccionado = contacto

                                    // Logica Firebase:
                                    // Aquí luego se cargara la conversación del contacto seleccionado
                                    // desde Firestore o Realtime Database.
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                AppText(
                                    text = contacto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                AppText(
                                    text = contacto.telefono,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            } else {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AppText(
                            text = contactoSeleccionado!!.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AppText(
                            text = contactoSeleccionado!!.telefono,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppText(
                    text = "Conversación",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mensajes) { item ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                AppText(
                                    text = if (item.enviadoPorMi) "Tú" else contactoSeleccionado!!.nombre,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                AppText(
                                    text = item.contenido,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { AppText("Escribe un mensaje") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppButton(
                        text = "Volver",
                        onClick = {
                            contactoSeleccionado = null
                        },
                        modifier = Modifier.weight(1f)
                    )

                    AppButton(
                        text = "Enviar",
                        onClick = {
                            if (mensaje.isNotBlank()) {
                                // Logica Firebase:
                                // Aquí luego se guardara el mensaje en Firestore o Realtime Database
                                // asociado al usuario actual y al contacto seleccionado.

                                mensajes = mensajes + MensajeDummy(mensaje, true)
                                mensaje = ""
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
}

fun obtenerContactos(context: Context): List<ContactoSimple> {
    val lista = mutableListOf<ContactoSimple>()

    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val nombre = it.getString(nameIndex) ?: "Sin nombre"
            val telefono = it.getString(numberIndex) ?: ""
            if (telefono.isNotBlank()) {
                lista.add(ContactoSimple(nombre, telefono))
            }
        }
    }

    return lista.distinctBy { it.telefono }
}