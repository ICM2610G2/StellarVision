package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stellarvision.common.validEmailAddress
import com.example.stellarvision.data.repository.AuthRepository
import com.example.stellarvision.database
import com.example.stellarvision.model.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    private val repository = AuthRepository()
    val loginState = _loginState.asStateFlow()

    fun updateEmail(newValue: String) {
        _loginState.update { it.copy(email = newValue) }
    }

    fun updatePassword(newValue: String) {
        _loginState.update { it.copy(password = newValue) }
    }

    fun updateEmailError(newValue: String) {
        _loginState.update { it.copy(emailError = newValue) }
    }

    fun updatePasswordError(newValue: String) {
        _loginState.update { it.copy(passwordError = newValue) }
    }


    fun validateForm(email: String, password: String, username: String = ""): Boolean {
        if (email.isEmpty()) {
            updateEmailError("Email is empty")
            return false
        } else {
            updateEmailError("")
        }
        if (!validEmailAddress(email)) {
            updateEmailError("Not a valid address")
            return false
        } else {
            updateEmailError("")
        }
        if (password.isEmpty()) {
            updatePasswordError("Password is Empty")
            return false
        } else {
            updatePasswordError("")
        }
        return true
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validateForm(email, password)) {
            return
        }
        repository.login(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Error")
                }
            }
    }
}

