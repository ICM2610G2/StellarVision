package com.example.stellarvision.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.stellarvision.data.repository.ChatRepository
import com.example.stellarvision.model.Mensaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MensajeriaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = ChatRepository()

    var mensajes by mutableStateOf<List<Mensaje>>(emptyList())
        private set

    var chatId by mutableStateOf<String?>(null)
        private set

    var selectedUserId by mutableStateOf<String?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var loading by mutableStateOf(false)
        private set

    private var currentListener: ValueEventListener? = null
    private var currentChatId: String? = null

    fun abrirChatConTelefono(phone: String) {
        val currentUid = auth.currentUser?.uid
        if (currentUid == null) {
            error = "No hay usuario autenticado"
            return
        }

        loading = true
        error = null
        selectedUserId = null
        chatId = null
        mensajes = emptyList()

        repository.findUserIdByPhone(phone) { otherUid ->
            loading = false

            if (otherUid == null) {
                error = "Este contacto no está registrado en StellarVision"
                return@findUserIdByPhone
            }

            if (otherUid == currentUid) {
                error = "No puedes abrir un chat contigo mismo"
                return@findUserIdByPhone
            }

            selectedUserId = otherUid

            val newChatId = repository.buildChatId(currentUid, otherUid)
            repository.ensureChat(newChatId, currentUid, otherUid)

            currentListener?.let { listener ->
                currentChatId?.let { oldChatId ->
                    repository.removeMessagesListener(oldChatId, listener)
                }
            }

            currentChatId = newChatId
            chatId = newChatId

            currentListener = repository.listenMessages(newChatId) { nuevosMensajes ->
                mensajes = nuevosMensajes
            }
        }
    }

    fun enviarMensaje(text: String, miNombreUsuario: String) {
        val currentUid = auth.currentUser?.uid ?: return
        val currentChat = chatId
        val idDestinatario = selectedUserId ?: ""

        if (currentChat == null) {
            error = "Primero selecciona un contacto registrado"
            return
        }

        if (text.isBlank()) return

        val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUid)
        userRef.child("username").get().addOnSuccessListener { userSnapshot ->
            val nombre = userSnapshot.getValue(String::class.java)
                ?: miNombreUsuario.ifBlank { "Un astrónomo" }
            repository.sendMessage(chatId = currentChat, senderId = currentUid, text = text.trim(), receiverId = idDestinatario, senderName = nombre)
        }.addOnFailureListener {
            val nombreRespaldo = miNombreUsuario.ifBlank { "Un astrónomo" }
            repository.sendMessage(currentChat, currentUid, text.trim(), idDestinatario, nombreRespaldo)
        }
    }

    override fun onCleared() {
        currentListener?.let { listener ->
            currentChatId?.let { id ->
                repository.removeMessagesListener(id, listener)
            }
        }
        super.onCleared()
    }
}