package com.example.stellarvision.data.repository

import com.example.stellarvision.data.firebase.FirebaseAuthDataSource

class AuthRepository(
    private val firebase : FirebaseAuthDataSource = FirebaseAuthDataSource()
){
    fun login(email : String, password : String) = firebase.login(email, password)
}