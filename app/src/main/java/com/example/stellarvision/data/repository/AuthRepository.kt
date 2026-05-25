package com.example.stellarvision.data.repository

import com.example.stellarvision.data.firebase.FirebaseAuthDataSource
import com.example.stellarvision.model.MyUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthRepository(
    private val firebase : FirebaseAuthDataSource = FirebaseAuthDataSource()
) {

    private val database = FirebaseDatabase.getInstance()

    fun login(email : String, password : String) = firebase.login(email, password)

    fun registerWithDatabase(
        email: String,
        password: String,
        username: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        firebase.register(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val myRef = database.getReference("users/$userId")
                        val datosPersona = MyUser(username = username, email = email)

                        myRef.setValue(datosPersona)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener {
                                onError()
                            }
                    } else {
                        onError()
                    }
                } else {
                    onError()
                }
            }
    }

    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

    fun logout() = FirebaseAuth.getInstance().signOut()

}