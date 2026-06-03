package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stellarvision.auth
import com.example.stellarvision.database
import com.example.stellarvision.common.validEmailAddress
import com.example.stellarvision.common.validPassword
import com.example.stellarvision.common.validUsername
import com.example.stellarvision.data.firebase.FCMTokenDataSource
import com.example.stellarvision.data.repository.AuthRepository
import com.example.stellarvision.model.RegisterState
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel(){
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    private val repository = AuthRepository()
    val registerState = _registerState.asStateFlow()

    fun updatePhoneNumber(phone: String) {
        _registerState.update { it.copy(phoneNumber = phone, phoneNumberError = "") }
    }

    fun updateEmail(newValue: String){
        _registerState.update { it.copy(email = newValue) }
    }
    fun updatePassword(newValue: String){
        _registerState.update { it.copy(password = newValue) }
    }
    fun updateUsername(newValue: String){
        _registerState.update { it.copy(username = newValue) }
    }
    fun updateConfirmPassword(newValue: String){
        _registerState.update { it.copy(confirmPassword = newValue) }
    }
    fun updateEmailError(newValue: String){
        _registerState.update { it.copy(emailError = newValue) }
    }
    fun updatePasswordError(newValue: String){
        _registerState.update { it.copy(passwordError = newValue) }
    }
    fun updateUsernameError(newValue: String){
        _registerState.update { it.copy(usernameError = newValue) }
    }
    fun updateConfirmPasswordError(newValue: String){
        _registerState.update { it.copy(confirmPasswordError = newValue) }
    }

    fun updatePhoneNumberError(newValue: String){
        _registerState.update { it.copy(phoneNumberError = newValue) }
    }

    fun validatePhoneNumber(phone: String): Boolean {
        return if (phone.isBlank() || phone.length < 10) {
            updatePhoneNumberError("Ingresa un número de celular válido de 10 dígitos")
            false
        } else {
            updatePhoneNumberError("")
            true
        }
    }

    fun validateForm(
        email: String,
        password: String,
        username: String = "",
        confirmPassword : String = "",
        phoneNumber: String = ""
    ): Boolean {
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
        if(username.isEmpty()){
            updateUsernameError("Username is empty")
            return false
        }else{
            updateUsernameError("")
        }
        if(!validUsername(username)){
            updateUsernameError("Username must be 3-20 characters long and not contain special characters.")
            return false
        }else{
            updateUsernameError("")
        }


        if (phoneNumber.isEmpty()) {
            updatePhoneNumberError("Phone number is empty")
            return false
        } else if (phoneNumber.length < 10) {
            updatePhoneNumberError("Phone number must be at least 10 digits")
            return false
        } else {
            updatePhoneNumberError("")
        }

        if (password.isEmpty()) {
            updatePasswordError("Password is Empty")
            return false
        } else {
            updatePasswordError("")
        }
        if(!validPassword(password)) {
            updatePasswordError("Password must contain an uppercase, a lowercase, a number and a symbol.")
            return false
        }else{
            updatePasswordError("")
        }
        if(password != confirmPassword){
            updateConfirmPasswordError("The password must be the same.")
            return false
        }else{
            updateConfirmPasswordError("")
        }
        return true
    }

    fun validateEmail(email : String) : Boolean {
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
        return true
    }

    fun register(
        email: String,
        password: String,
        username: String = "",
        confirmPassword: String = "",
        phoneNumber: String = "",
        profilePictureUrl: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){

        if(!validateForm(email, password, username, confirmPassword, phoneNumber)){
            return
        }
        repository.registerWithDatabase(
            email = email,
            password = password,
            username = username,
            phoneNumber = phoneNumber,
            profilePictureUrl = profilePictureUrl,
            onSuccess = {
                saveUser(email, username, phoneNumber, profilePictureUrl)
                onSuccess()
            },
            onError = {
                onError("Error al completar el registro en Firebase")
            }
        )
    }

    private fun saveUser(email : String, username : String, phoneNumber: String, profilePictureUrl: String) {
        val uid = auth.currentUser?.uid ?: return

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                val token = if (task.isSuccessful) task.result else null

                val newUser = mutableMapOf(
                    "uid" to uid,
                    "email" to email,
                    "username" to username,
                    "phoneNumber" to phoneNumber,
                    "profilePictureUrl" to profilePictureUrl
                )

                if (token != null) {
                    newUser["fcmToken"] = token
                }

                val myRef = database.getReference("users/${uid}")
                myRef.setValue(newUser)
            }
    }
}