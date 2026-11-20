package com.christhperalta.activosfijos.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.core.utils.SessionExpiredException
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSummary()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Retry -> loadSummary()
            HomeEvent.SessionExpiredAccepted -> {
                sessionRepository.clearSession()
                _uiState.update { it.copy(sessionExpired = false) }
            }
        }
    }

    private fun loadSummary() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            homeRepository.getAssetCountSummary()
                .onSuccess { summary ->
                    _uiState.update { it.copy(isLoading = false, summary = summary) }
                }
                .onFailure { error ->
                    when (error) {
                        is SessionExpiredException -> {
                            // Se evalúa primero y deja error en null: el modal es la única cosa
                            // que debe aparecer, no el mensaje con botón de reintentar.
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = null,
                                    sessionExpired = true
                                )
                            }
                        }

                        is SapException -> {
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = "Error de SAP: ${error.code} - ${error.errorMessage}"
                                )
                            }
                        }

                        else -> {
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
