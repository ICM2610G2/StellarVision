package com.example.stellarvision.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class StellarFirebaseMessagingService : FirebaseMessagingService() {



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_Stellar", "Nuevo FCM Token generado por Firebase: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_Stellar", "Llego un mensaje de: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            Log.d("FCM_Stellar", "Cuerpo de la notificacion: ${it.body}")

        }
    }

}