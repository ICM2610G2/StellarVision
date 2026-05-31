package com.example.stellarvision.viewmodel


import androidx.lifecycle.ViewModel
import com.example.stellarvision.model.MyUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PerfilViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val _userState = MutableStateFlow<MyUser?>(null)
    val userState = _userState.asStateFlow()

    init {
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference("users/$userId")


            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usuario = snapshot.getValue(MyUser::class.java)
                    _userState.value = usuario
                }

                override fun onCancelled(error: DatabaseError) {
                    
                }
            })
        }
    }

    fun logout() {
        auth.signOut()
    }
}