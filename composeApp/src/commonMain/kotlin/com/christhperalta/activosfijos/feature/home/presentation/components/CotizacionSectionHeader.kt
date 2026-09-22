package com.christhperalta.activosfijos.feature.home.presentation.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_quotation_search_button
import activosfijos.composeapp.generated.resources.home_quotation_section_title
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun CotizacionSectionHeader(onQuotation: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = stringResource(Res.string.home_quotation_section_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                onClick =  { onQuotation() }
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(Res.string.home_quotation_search_button))
                    Text(text = stringResource(Res.string.home_quotation_search_button), fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}