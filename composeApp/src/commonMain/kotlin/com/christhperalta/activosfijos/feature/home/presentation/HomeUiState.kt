package com.christhperalta.activosfijos.feature.home.presentation

import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary

data class HomeUiState(
    val isLoading: Boolean = false,
    val summary: AssetCountSummary? = null,
    val error: String? = null,
    /**
     * Campo aparte de [error] a propósito: una sesión expirada se resuelve con el modal que
     * manda al login, no con el mensaje en línea y el botón de reintentar.
     */
    val sessionExpired: Boolean = false,
)
