package com.christhperalta.activosfijos.feature.login.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.login_config_dialog_error
import activosfijos.composeapp.generated.resources.login_config_dialog_title
import activosfijos.composeapp.generated.resources.login_form_password_label
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialog
import com.christhperalta.activosfijos.core.presentation.CustomAlertDialogActions
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import org.jetbrains.compose.resources.stringResource

@Composable
 fun ConfigPasswordDialog(
    visible: Boolean,
    password: String,
    isError: Boolean,
    onPasswordChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AnimatedVisibility(visible) {

        CustomAlertDialog(
            actions = CustomAlertDialogActions(
                onDismissRequest = onDismiss,
                onConfirmation = onConfirm
            ),
            dialogTitle = stringResource(Res.string.login_config_dialog_title),
            icon = Icons.Default.Password
        ) {

            CustomInput(
                config = CustomInputConfig(
                    isPassword = true,
                    label = stringResource(Res.string.login_form_password_label),
                    isError = isError,
                    errorMessage = if (isError) {
                        stringResource(Res.string.login_config_dialog_error)
                    } else {
                        null
                    }
                ),
                value = password,
                onValueChange = onPasswordChanged
            )
        }
    }
}