package com.example.stellarvision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.stellarvision.navigation.Navigation
import com.example.stellarvision.ui.theme.StellarVisionTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


lateinit var auth : FirebaseAuth
lateinit var database : FirebaseDatabase
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
       database = FirebaseDatabase.getInstance()
        setContent {
            StellarVisionTheme() {
                Navigation()
            }
        }
    }
}
