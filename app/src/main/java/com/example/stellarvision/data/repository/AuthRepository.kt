package com.example.stellarvision.data.repository

import android.net.Uri
import com.example.stellarvision.data.firebase.FirebaseAuthDataSource
import com.example.stellarvision.model.MyUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AuthRepository(
    private val firebase : FirebaseAuthDataSource = FirebaseAuthDataSource()
) {

    private val database = FirebaseDatabase.getInstance()

    fun login(email : String, password : String) = firebase.login(email, password)

    fun registerWithDatabase(
        email: String,
        password: String,
        username: String,
        phoneNumber: String,
        profilePictureUrl: String, // Recibe el path local temporal (Uri en String)
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        firebase.register(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {

                        // Lógica de subida de archivos integrada basada en tu ejemplo
                        if (profilePictureUrl.isNotEmpty()) {
                            val storageRef = FirebaseStorage.getInstance().reference.child("avatars/$userId.jpg")
                            val localFileUri = Uri.parse(profilePictureUrl)

                            storageRef.putFile(localFileUri)
                                .addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                        // Guardamos en la base de datos con la URL de Firebase Storage final
                                        guardarDatosFinales(userId, username, email, phoneNumber, downloadUri.toString(), onSuccess, onError)
                                    }
                                }
                                .addOnFailureListener {
                                    // Si falla la subida, crea la cuenta pero guarda la foto vacía
                                    guardarDatosFinales(userId, username, email, phoneNumber, "", onSuccess, onError)
                                }
                        } else {
                            // Si no seleccionó imagen, guardamos directo con foto vacía
                            guardarDatosFinales(userId, username, email, phoneNumber, "", onSuccess, onError)
                        }

                    } else {
                        onError()
                    }
                } else {
                    onError()
                }
            }
    }

    // Función auxiliar interna estructurada bajo tu mismo patrón de guardado
    private fun guardarDatosFinales(
        userId: String,
        username: String,
        email: String,
        phoneNumber: String,
        photoUrl: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val myRef = database.getReference("users/$userId")
        val datosPersona = MyUser(
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            profilePictureUrl = photoUrl
        )

        myRef.setValue(datosPersona)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

    fun logout() = FirebaseAuth.getInstance().signOut()

}