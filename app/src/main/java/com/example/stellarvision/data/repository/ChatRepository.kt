package com.example.stellarvision.data.repository

import com.example.stellarvision.data.firebase.FirebaseChatDataSource
import com.example.stellarvision.model.Mensaje
import com.google.firebase.database.ValueEventListener

class ChatRepository(
    private val firebase: FirebaseChatDataSource = FirebaseChatDataSource()
) {
    fun buildChatId(uid1: String, uid2: String) = firebase.buildChatId(uid1, uid2)

    fun findUserIdByPhone(phone: String, onResult: (String?) -> Unit) {
        firebase.findUserIdByPhone(phone, onResult)
    }

    fun ensureChat(chatId: String, currentUid: String, otherUid: String) {
        firebase.ensureChat(chatId, currentUid, otherUid)
    }

    fun listenMessages(chatId: String, onChange: (List<Mensaje>) -> Unit): ValueEventListener {
        return firebase.listenMessages(chatId, onChange)
    }

    fun removeMessagesListener(chatId: String, listener: ValueEventListener) {
        firebase.removeMessagesListener(chatId, listener)
    }

    fun sendMessage(chatId: String, senderId: String, text: String, receiverId: String, senderName: String) {
        firebase.sendMessage(chatId, senderId, text, receiverId, senderName)
    }
}