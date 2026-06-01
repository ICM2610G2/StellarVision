package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PublicacionData(
    val id: String = "",
    val userName: String = "Usuario Anónimo",
    val groupText: String = "",
    val body: String = "",
    val imageUrl: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val likedBy: List<String> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _publicaciones = MutableStateFlow<List<PublicacionData>>(emptyList())
    val publicaciones: StateFlow<List<PublicacionData>> = _publicaciones

    init {
        escucharPublicaciones()
    }
    private fun escucharPublicaciones() {
        db.collection("publicaciones")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val lista = snapshot.documents.mapNotNull { doc ->
                    val pub = doc.toObject(PublicacionData::class.java)
                    pub?.copy(id = doc.id)
                }
                _publicaciones.value = lista
            }
    }


    fun darLike(publicacionId: String, userId: String) {
        val docRef = db.collection("publicaciones").document(publicacionId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val likedBy = snapshot.get("likedBy") as? List<*> ?: emptyList<Any>()
            val currentLikes = snapshot.getLong("likes")?.toInt() ?: 0

            if (likedBy.contains(userId)) {

                transaction.update(docRef, "likedBy", likedBy - userId)
                transaction.update(docRef, "likes", (currentLikes - 1).coerceAtLeast(0))
            } else {

                transaction.update(docRef, "likedBy", likedBy + userId)
                transaction.update(docRef, "likes", currentLikes + 1)
            }
        }.addOnFailureListener {

        }
    }
}