package com.christhperalta.activosfijos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.christhperalta.activosfijos.core.navigation.AppNavigation
import com.christhperalta.activosfijos.core.navigation.Login
import com.christhperalta.activosfijos.core.navigation.Onboarding
import com.christhperalta.activosfijos.core.theme.AppTheme
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App(viewModel: AppViewModel = koinViewModel()) {
    AppTheme {
        val startKey by viewModel.startKey.collectAsStateWithLifecycle()

        if (startKey == null) {
            LoadingScreen()
            return@AppTheme
        }

        AppNavigation(startKey = startKey!!)
    }
}


class AppViewModel(
    private val serverConfigRepository: ServerConfigRepository,
) : ViewModel() {

    private val _startKey = MutableStateFlow<NavKey?>(null)
    val startKey: StateFlow<NavKey?> = _startKey.asStateFlow()

    init {
        viewModelScope.launch {
            _startKey.value = resolveStartKey()
        }
    }

    private suspend fun resolveStartKey(): NavKey =
        if (hasConfig()) Login else Onboarding

    private suspend fun hasConfig(): Boolean = try {
        val baseUrl = getBaseUrl()
        baseUrl.isNotBlank()
    } catch (_: IllegalStateException) {
        false
    }

    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }


}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}