package com.christhperalta.activosfijos.feature.login.presentation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.activosfijos.core.constants.Constants
import com.christhperalta.activosfijos.core.utils.sha256
import com.christhperalta.activosfijos.feature.login.presentation.components.ConfigPasswordDialog
import com.christhperalta.activosfijos.feature.login.presentation.components.LoginContent
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    onHome: () -> Unit,
    onConfig: () -> Unit,
    vm: LoginViewModel = koinInject(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    var isDialogOpen by remember { mutableStateOf(false) }
    var configPassword by remember { mutableStateOf("") }
    var isConfigError by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loginResponse) {
        if (uiState.loginResponse != null) {
            onHome()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        LoginContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onUserChanged = {
                vm.onEvent(LoginEvents.OnUserNameChanged(it))
            },
            onPasswordChanged = {
                vm.onEvent(LoginEvents.OnPasswordChanged(it))
            },
            onLoginClick = {
                vm.onEvent(LoginEvents.OnLoginClicked)
            },
            onConfigClick = {
                isDialogOpen = true
            }
        )

        ConfigPasswordDialog(
            visible = isDialogOpen,
            password = configPassword,
            isError = isConfigError,
            onPasswordChanged = {
                configPassword = it
                isConfigError = false
            },
            onDismiss = {
                isDialogOpen = false
            },
            onConfirm = {
                val isValid =
                    configPassword.isNotBlank() &&
                            sha256(configPassword) == Constants.ACCESS_CODE_HASH

                if (isValid) {
                    isDialogOpen = false
                    onConfig()
                } else {
                    isConfigError = true
                }
            }
        )
    }
}


