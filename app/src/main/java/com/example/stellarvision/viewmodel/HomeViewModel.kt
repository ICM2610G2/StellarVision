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

data class ComentarioData(
    val id: String = "",
    val userName: String = "Astrónomo",
    val text: String = "",
    val timestamp: Long = 0L
)

class HomeViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("posts")

    private val _publicaciones = MutableStateFlow<List<PublicacionData>>(emptyList())
    val publicaciones: StateFlow<List<PublicacionData>> = _publicaciones


    private val _comentarios = MutableStateFlow<List<ComentarioData>>(emptyList())
    val comentarios: StateFlow<List<ComentarioData>> = _comentarios

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

                    val groupText = postSnapshot.child("constellation").getValue(String::class.java) ?: "General"
                    val body = postSnapshot.child("description").getValue(String::class.java) ?: ""
                    val title = postSnapshot.child("title").getValue(String::class.java) ?: ""

                    val imageUrl = postSnapshot.child("imageUrl").getValue(String::class.java)
                    val likes = postSnapshot.child("likes").getValue(Int::class.java) ?: 0
                    val comments = postSnapshot.child("comments").getValue(Int::class.java) ?: 0

                    val likedBy = mutableMapOf<String, Boolean>()
                    postSnapshot.child("likedBy").children.forEach { uSnapshot ->
                        uSnapshot.key?.let { uid -> likedBy[uid] = true }
                    }


                    val cuerpoCompleto = if (title.isNotBlank()) "✨ $title\n\n$body" else body

                    lista.add(PublicacionData(id, userName, "🌌 Constelación: $groupText", cuerpoCompleto, imageUrl, likes, comments, likedBy))
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

    fun escucharComentarios(publicacionId: String) {
        database.child(publicacionId).child("commentsList")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lista = mutableListOf<ComentarioData>()
                    for (commentSnapshot in snapshot.children) {
                        val id = commentSnapshot.key ?: ""
                        val userName = commentSnapshot.child("userName").getValue(String::class.java) ?: "Anónimo"
                        val text = commentSnapshot.child("text").getValue(String::class.java) ?: ""
                        val timestamp = commentSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L

                        lista.add(ComentarioData(id, userName, text, timestamp))
                    }

                    _comentarios.value = lista
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    fun agregarComentario(publicacionId: String, texto: String, nombreUsuario: String) {
        if (texto.isBlank()) return

        val postRef = database.child(publicacionId)

        val nuevoComentarioRef = postRef.child("commentsList").push()

        val comentarioMap = mapOf(
            "userName" to nombreUsuario,
            "text" to texto,
            "timestamp" to System.currentTimeMillis()
        )

        nuevoComentarioRef.setValue(comentarioMap).addOnSuccessListener {

            postRef.child("comments").get().addOnSuccessListener { snapshot ->
                val comentariosActuales = snapshot.getValue(Int::class.java) ?: 0
                postRef.child("comments").setValue(comentariosActuales + 1)
            }
        }
    }
}