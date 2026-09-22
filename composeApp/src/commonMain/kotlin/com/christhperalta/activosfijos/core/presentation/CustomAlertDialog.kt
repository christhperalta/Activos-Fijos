package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.dialog_cancel_label
import activosfijos.composeapp.generated.resources.dialog_confirm_label
import activosfijos.composeapp.generated.resources.dialog_example_icon_description
import org.jetbrains.compose.resources.stringResource


data class CustomAlertDialogActions(
    val onDismissRequest: () -> Unit = {},
    val enableBottomDismiss: Boolean = true,
    val onConfirmation: () -> Unit = {},
)

@Composable
fun CustomAlertDialog(
    dialogTitle: String,
    icon: ImageVector,
    confirmLabel: String = stringResource(Res.string.dialog_confirm_label),
    dismissLabel: String = stringResource(Res.string.dialog_cancel_label),
    actions: CustomAlertDialogActions = CustomAlertDialogActions(),
    context: @Composable (() -> Unit)
) {


    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = stringResource(Res.string.dialog_example_icon_description)
            )
        },
        title = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomText(
                    text = dialogTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
//                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
        },
        text = {
            context()

        },
        onDismissRequest = {
            actions.onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    actions.onConfirmation()
                }
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            if (actions.enableBottomDismiss) {
                TextButton(
                    onClick = {
                        actions.onDismissRequest()
                    }
                ) {
                    Text(dismissLabel)
                }
            }

        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}