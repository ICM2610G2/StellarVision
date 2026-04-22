package com.example.stellarvision.data.repository

import com.example.stellarvision.data.firebase.FirebaseAuthDataSource
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val firebase : FirebaseAuthDataSource = FirebaseAuthDataSource()
){
    fun login(email : String, password : String) = firebase.login(email, password)
    fun register(email : String, password : String) = firebase.register(email, password)

    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

    fun logout() = FirebaseAuth.getInstance().signOut()
}