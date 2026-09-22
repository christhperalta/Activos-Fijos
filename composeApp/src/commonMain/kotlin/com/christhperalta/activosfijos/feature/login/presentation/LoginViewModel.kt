package com.christhperalta.activosfijos.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.login.domain.use_case.LogInUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class LoginViewModel(
    private val logInUserUseCase: LogInUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvents) {
        when (event) {
            LoginEvents.OnLoginClicked -> {
                if (_uiState.value.user.isNullOrBlank() || _uiState.value.password.isNullOrBlank())
                    return
                
                loginUser()
            }
            is LoginEvents.OnPasswordChanged -> _uiState.update { it.copy(password = event.password) }
            is LoginEvents.OnUserNameChanged -> _uiState.update { it.copy(user = event.userName) }
        }
    }

    private fun loginUser() {
        val user = _uiState.value.user ?: return
        val password = _uiState.value.password ?: return
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            logInUserUseCase(
                userName = user,
                password = password
            )
                .onSuccess { response ->
                    _uiState.update { it.copy(isLoading = false, loginResponse = response) }
                }
                .onFailure { error ->
                    when (error) {
                        is SapException -> {
                            // Error controlado de SAP B1
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = "Error de SAP: ${error.code} - ${error.errorMessage}"
                                )
                            }
                        }
                        else -> {
                            // Error de red, timeout, etc.
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = "Error de conexión: ${error.message}"
                                )
                            }
                        }

                    }

                }
        }
    }
}