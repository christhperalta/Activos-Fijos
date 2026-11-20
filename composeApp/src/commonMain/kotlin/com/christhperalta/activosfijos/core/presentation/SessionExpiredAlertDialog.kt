package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.dialog_accept_label
import activosfijos.composeapp.generated.resources.session_expired_alert_title
import org.jetbrains.compose.resources.stringResource


@Composable
fun SessionExpiredAlertDialog (
    onConfirmation : () -> Unit,
    errorMessage : String? = null,
    confirmLabel : String = stringResource(Res.string.dialog_accept_label),
    messageColor : Color = MaterialTheme.colorScheme.error
) {
    CustomAlertDialog(
        dialogTitle = stringResource(Res.string.session_expired_alert_title),
        icon = Icons.Outlined.AddAlert,
        confirmLabel = confirmLabel,
        actions = CustomAlertDialogActions(
            onDismissRequest = { },
            enableBottomDismiss = false,
            onConfirmation = onConfirmation
        )
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CustomText(text = errorMessage ?: "", color = messageColor)
        }
    }
}
