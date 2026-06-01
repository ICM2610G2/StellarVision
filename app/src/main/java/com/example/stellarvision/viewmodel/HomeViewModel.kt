package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PublicacionData(
    val id: String = "",
    val userName: String = "Astrónomo",
    val groupText: String = "",
    val body: String = "",
    val imageUrl: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val likedBy: Map<String, Boolean> = emptyMap()
)

class HomeViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("posts")

    private val _publicaciones = MutableStateFlow<List<PublicacionData>>(emptyList())
    val publicaciones: StateFlow<List<PublicacionData>> = _publicaciones

    init {
        cargarPublicaciones()
    }

    private fun cargarPublicaciones() {

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<PublicacionData>()
                for (postSnapshot in snapshot.children) {
                    val id = postSnapshot.key ?: ""
                    val userName = postSnapshot.child("userName").getValue(String::class.java) ?: "Anónimo"
                    val groupText = postSnapshot.child("groupText").getValue(String::class.java) ?: ""
                    val body = postSnapshot.child("body").getValue(String::class.java) ?: ""
                    val imageUrl = postSnapshot.child("imageUrl").getValue(String::class.java)
                    val likes = postSnapshot.child("likes").getValue(Int::class.java) ?: 0
                    val comments = postSnapshot.child("comments").getValue(Int::class.java) ?: 0


                    val likedBy = mutableMapOf<String, Boolean>()
                    postSnapshot.child("likedBy").children.forEach { uSnapshot ->
                        uSnapshot.key?.let { uid -> likedBy[uid] = true }
                    }

                    lista.add(PublicacionData(id, userName, groupText, body, imageUrl, likes, comments, likedBy))
                }

                _publicaciones.value = lista.reversed()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun conmutarLike(publicacionId: String, currentUserId: String) {
        if (currentUserId == "anonimo") return

        val postRef = database.child(publicacionId)

        postRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val likedByNode = snapshot.child("likedBy").child(currentUserId)


                val likesActuales = snapshot.child("likes").getValue(Int::class.java) ?: 0

                if (likedByNode.exists()) {

                    likedByNode.ref.removeValue().addOnSuccessListener {
                        postRef.child("likes").setValue((likesActuales - 1).coerceAtLeast(0))
                    }
                } else {

                    likedByNode.ref.setValue(true).addOnSuccessListener {
                        postRef.child("likes").setValue(likesActuales + 1)
                    }
                }
            }
        }.addOnFailureListener { error ->

            println("Error en Firebase Database: ${error.message}")
        }
    }
}