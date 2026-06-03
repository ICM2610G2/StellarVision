package com.example.stellarvision.data.firebase

import com.example.stellarvision.model.Mensaje
import com.google.firebase.database.*

class FirebaseChatDataSource {

    private val database = FirebaseDatabase.getInstance()

    private fun chatsRef() = database.getReference("chats")
    private fun usersRef() = database.getReference("users")
    private fun notificationsRef() = database.getReference("notifications")

    private fun normalizePhone(phone: String): String {
        val digits = phone.filter { it.isDigit() }

        return if (digits.length >= 10)
            digits.takeLast(10)
        else
            digits
    }

    fun buildChatId(uid1: String, uid2: String): String {
        return listOf(uid1, uid2).sorted().joinToString("_")
    }

    fun findUserIdByPhone(phone: String, onResult: (String?) -> Unit) {
        val targetPhone = normalizePhone(phone)

        usersRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var foundUserId: String? = null

                for (child in snapshot.children) {
                    val dbPhone = child.child("phoneNumber")
                        .value
                        ?.toString()
                        .orEmpty()
                    if (normalizePhone(dbPhone) == targetPhone) {
                        foundUserId = child.key
                        break
                    }
                }

                onResult(foundUserId)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    fun ensureChat(chatId: String, currentUid: String, otherUid: String) {
        val chatData = mapOf(
            "participants" to mapOf(
                currentUid to true,
                otherUid to true
            )
        )

        chatsRef().child(chatId).updateChildren(chatData)
    }

    fun listenMessages(chatId: String, onChange: (List<Mensaje>) -> Unit): ValueEventListener {
        val ref = chatsRef().child(chatId).child("messages")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mensajes = snapshot.children.mapNotNull { child ->
                    child.getValue(Mensaje::class.java)?.copy(id = child.key ?: "")
                }.sortedBy { it.timestamp }

                onChange(mensajes)
            }

            override fun onCancelled(error: DatabaseError) {
                onChange(emptyList())
            }
        }

        ref.addValueEventListener(listener)
        return listener
    }

    fun removeMessagesListener(chatId: String, listener: ValueEventListener) {
        chatsRef().child(chatId).child("messages").removeEventListener(listener)
    }

    fun sendMessage(chatId: String, senderId: String, text: String, receiverId: String = "", senderName: String = "Alguien") {
        val ref = chatsRef().child(chatId).child("messages").push()

        val mensaje = Mensaje(
            id = ref.key ?: "",
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis(),

        )

        ref.setValue(mensaje).addOnSuccessListener {

            if (receiverId.isNotBlank() && receiverId != senderId) {
                val nuevaNotiRef = notificationsRef().child(receiverId).push()

                val datosNotificacion = mapOf(
                    "fromUsername" to senderName,
                    "chatId" to chatId,
                    "type" to "chat"
                )
                nuevaNotiRef.setValue(datosNotificacion).addOnFailureListener { error ->
                    println("Error al colgar la notificación push de chat: ${error.message}")
                }
            }
        }.addOnFailureListener { error ->
            println("Error al guardar el mensaje de chat: ${error.message}")
        }

    }
}