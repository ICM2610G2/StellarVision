package com.example.stellarvision.common

fun validEmailAddress(email:String):Boolean{
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
    return email.matches(regex.toRegex())
}

fun validPassword(password: String): Boolean {
    val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\S]{8,}$"
    return password.matches(regex.toRegex())
}