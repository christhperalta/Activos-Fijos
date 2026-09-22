package com.christhperalta.activosfijos.feature.home.presentation.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_manual_search_product_id_label
import activosfijos.composeapp.generated.resources.home_manual_search_product_id_placeholder
import activosfijos.composeapp.generated.resources.home_manual_search_title
import com.christhperalta.activosfijos.core.presentation.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManualSearchDialog(
    productId: String,
    onProductIdChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomAlertDialog(
        actions = CustomAlertDialogActions(
            onDismissRequest = onDismiss,
            onConfirmation = onConfirm,
        ),
        dialogTitle = stringResource(Res.string.home_manual_search_title),
        icon = Icons.Default.Search
    ) {
        CustomInput(
            value = productId,
            config = CustomInputConfig(
                label = stringResource(Res.string.home_manual_search_product_id_label),
                placeholder =  stringResource(Res.string.home_manual_search_product_id_placeholder),
                icon = Icons.Default.Search
            ),
            onValueChange = onProductIdChange
        )
    }
}