package com.example.stellarvision.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class FCMTokenDataSource {

    private val auth = FirebaseAuth.getInstance()
    private val databaseRef = FirebaseDatabase.getInstance().reference

    fun saveCurrentToken() {
        val uid = auth.currentUser?.uid ?: return
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            saveToken(token, uid)
        }.addOnFailureListener { e ->
            Log.e("FCMTokenDataSource", "Error", e)
        }
    }

    fun saveToken(token: String, uid: String? = auth.currentUser?.uid) {
        uid ?: return

        databaseRef.child("users").child(uid).child("fcmToken")
            .setValue(token)
            .addOnSuccessListener {
                Log.d("FCMTokenDataSource", "Success: $uid")
            }
            .addOnFailureListener { e ->
                Log.e("FCMTokenDataSource", "Error", e)
            }
    }

    fun clearToken(uid: String) {
        if (uid.isEmpty()) return

        databaseRef.child("users").child(uid).child("fcmToken")
            .setValue(null)
            .addOnSuccessListener {
                Log.d("FCMTokenDataSource", "Success")
            }
            .addOnFailureListener { e ->
                Log.e("FCMTokenDataSource", "Error", e)
            }
    }
}