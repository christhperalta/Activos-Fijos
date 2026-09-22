package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_document_type_rnc
import activosfijos.composeapp.generated.resources.home_rnc_search_description
import com.christhperalta.activosfijos.core.presentation.CustomIconButton
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import org.jetbrains.compose.resources.stringResource


@Composable
fun RncInput(
    value: String,
    label: String = stringResource(Res.string.home_document_type_rnc),
    icon: ImageVector = Icons.Default.Person,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomInput(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            config = CustomInputConfig(label = label, placeholder = label, icon = icon)
        )
        CustomIconButton(
            onClick = onSearch,
            modifier = Modifier.size(50.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(Res.string.home_rnc_search_description, label),
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}