package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.stellarvision.auth
import com.example.stellarvision.common.validEmailAddress
import com.example.stellarvision.common.validPassword
import com.example.stellarvision.common.validUsername
import com.example.stellarvision.data.repository.AuthRepository
import com.example.stellarvision.database
import com.example.stellarvision.model.RegisterState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel(){
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    private val repository = AuthRepository()
    val registerState = _registerState.asStateFlow()

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

    fun validateForm(email: String, password: String, username: String = "", confirmPassword : String = ""): Boolean {
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
        if(!validPassword(password)) {
            updatePasswordError("Password must contain an uppercase, a lowercase, a number and a symbol.")
            return false
        }else{
            updatePasswordError("")
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

    fun register(email : String, password : String, username : String = "", confirmPassword : String = "", onSuccess: () -> Unit, onError: (String) -> Unit){
        if(!validateForm(email,password,username, confirmPassword)){
            return
        }
        repository.register(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    saveUser(email, username)
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Error al crear la cuenta")
                }
            }
    }

    private fun saveUser(email : String, username : String) {
        val newUser = mapOf(
            "email" to email,
            "username" to username
        )
        val uid = auth.currentUser?.uid ?: return
        var myRef = database.getReference("users/${uid}")
        myRef.setValue(newUser)

    }
}
