package com.example.stellarvision.data.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthDataSource{
    private val auth = FirebaseAuth.getInstance()

    fun login(email : String, password : String) = auth.signInWithEmailAndPassword(email,password)
    fun register(email : String, password : String) = auth.createUserWithEmailAndPassword(email,password)
}