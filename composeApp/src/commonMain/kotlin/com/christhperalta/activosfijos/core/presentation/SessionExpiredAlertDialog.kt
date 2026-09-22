package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.session_expired_alert_title
import org.jetbrains.compose.resources.stringResource


@Composable
fun SessionExpiredAlertDialog (
    onConfirmation : () -> Unit,
    errorMessage : String? = null
) {
    CustomAlertDialog(
        dialogTitle = stringResource(Res.string.session_expired_alert_title),
        icon = Icons.Outlined.AddAlert,
        actions = CustomAlertDialogActions(
            onDismissRequest = { },
            enableBottomDismiss = false,
            onConfirmation = onConfirmation
        )
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CustomText(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
    }
}