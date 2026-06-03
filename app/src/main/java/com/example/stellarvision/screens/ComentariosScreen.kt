package com.example.stellarvision.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stellarvision.common.AppText
import com.example.stellarvision.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentariosScreen(
    publicacionId: String,
    controller: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val listaComentarios by homeViewModel.comentarios.collectAsState()
    var nuevoComentarioTexto by remember { mutableStateOf("") }


    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Astrónomo"
    val nombreMostrar = userEmail.substringBefore("@")


    LaunchedEffect(publicacionId) {
        homeViewModel.escucharComentarios(publicacionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppText("Comentarios", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { controller.popBackStack() }) {
                        AppText("←", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (listaComentarios.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText("Aún no hay comentarios. ¡Sé el primero!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(listaComentarios) { comentario ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                AppText(
                                    text = comentario.userName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                AppText(
                                    text = comentario.text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }


            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = nuevoComentarioTexto,
                        onValueChange = { nuevoComentarioTexto = it },
                        placeholder = { Text("Escribe un comentario...") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (nuevoComentarioTexto.isNotBlank()) {
                                homeViewModel.agregarComentario(publicacionId, nuevoComentarioTexto, nombreMostrar)
                                nuevoComentarioTexto = ""
                            } else {
                                Toast.makeText(context, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        AppText("Enviar", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}