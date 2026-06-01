package com.example.stellarvision.data.repository

import android.net.Uri
import com.example.stellarvision.data.firebase.FirebaseAuthDataSource
import com.example.stellarvision.model.MyUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.example.stellarvision.model.Post

class AuthRepository(
    private val firebase : FirebaseAuthDataSource = FirebaseAuthDataSource()
) {

    private val database = FirebaseDatabase.getInstance()

    fun login(email : String, password : String) = firebase.login(email, password)

    fun crearPublicacion(
        title: String,
        description: String,
        constellation: String,
        locationPrivacy: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = getCurrentUser()?.uid
        if (userId == null) {
            onError("Usuario no autenticado")
            return
        }


        val database = FirebaseDatabase.getInstance()
        database.getReference("users/$userId").get().addOnSuccessListener { snapshot ->
            val userName = snapshot.child("username").getValue(String::class.java) ?: "Astrónomo"


            val postRef = database.getReference("posts").push()
            val postId = postRef.key ?: return@addOnSuccessListener


            val storageRef = FirebaseStorage.getInstance().reference.child("posts/$postId.jpg")

            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->

                        val nuevaPublicacion = Post(
                            postId = postId,
                            userId = userId,
                            userName = userName,
                            title = title,
                            description = description,
                            constellation = constellation,
                            locationPrivacy = locationPrivacy,
                            imageUrl = downloadUri.toString(),
                            timestamp = System.currentTimeMillis()
                        )


                        postRef.setValue(nuevaPublicacion)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onError("Error al guardar texto en la base de datos") }
                    }
                }
                .addOnFailureListener {
                    onError("Error al subir la imagen a Firebase Storage")
                }
        }.addOnFailureListener {
            onError("No se pudo verificar el perfil del usuario")
        }
    }

    fun registerWithDatabase(
        email: String,
        password: String,
        username: String,
        phoneNumber: String,
        profilePictureUrl: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        firebase.register(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {

                        if (profilePictureUrl.isNotEmpty()) {
                            val storageRef = FirebaseStorage.getInstance().reference.child("avatars/$userId.jpg")
                            val localFileUri = Uri.parse(profilePictureUrl)

                            storageRef.putFile(localFileUri)
                                .addOnSuccessListener {
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->

                                        guardarDatosFinales(userId, username, email, phoneNumber, downloadUri.toString(), onSuccess, onError)
                                    }
                                }
                                .addOnFailureListener {

                                    guardarDatosFinales(userId, username, email, phoneNumber, "", onSuccess, onError)
                                }
                        } else {

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