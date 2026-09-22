package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_document_type_cedula
import activosfijos.composeapp.generated.resources.home_document_type_passport
import activosfijos.composeapp.generated.resources.home_document_type_rnc
import com.christhperalta.activosfijos.feature.home.presentation.DocumentType
import org.jetbrains.compose.resources.stringResource

@Composable
fun DocumentTypeSelector(
    selectedType: DocumentType,
    onTypeSelected: (DocumentType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DocumentType.entries.forEach { type ->
            val isSelected = selectedType == type
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.background
                    )
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onTypeSelected(type) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when (type) {
                        DocumentType.RNC -> stringResource(Res.string.home_document_type_rnc)
                        DocumentType.CEDULA -> stringResource(Res.string.home_document_type_cedula)
                        DocumentType.PASAPORTE -> stringResource(Res.string.home_document_type_passport)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF111827)
                )
            }
        }
    }
}