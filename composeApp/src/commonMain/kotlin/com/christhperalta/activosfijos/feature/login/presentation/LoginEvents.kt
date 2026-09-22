package com.christhperalta.activosfijos.feature.login.presentation



sealed class LoginEvents {
    data class OnUserNameChanged(val userName: String) : LoginEvents()
    data class OnPasswordChanged(val password: String) : LoginEvents()
    object OnLoginClicked : LoginEvents()
}
