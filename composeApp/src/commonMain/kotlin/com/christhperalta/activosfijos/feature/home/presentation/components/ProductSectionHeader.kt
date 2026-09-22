package com.christhperalta.activosfijos.feature.home.presentation.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_product_section_header_quaantity_title
import activosfijos.composeapp.generated.resources.home_product_section_header_search_manual_button
import activosfijos.composeapp.generated.resources.home_product_section_header_search_scan
import activosfijos.composeapp.generated.resources.home_quotation_search_button
import com.christhperalta.activosfijos.core.presentation.AdaptiveLayout
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.LayoutType
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductSectionHeader(
    itemLineCount: Int,
    onScanner: () -> Unit,
    onManualSearch: () -> Unit,
    onSearchByName: () -> Unit,
) {

    AdaptiveLayout { layoutType ->
        when (layoutType) {
            LayoutType.MOBILE_PORTRAIT -> {
                MobilePortraitProductSectionHeader(
                    itemLineCount = itemLineCount,
                    onScanner = onScanner,
                    onManualSearch = onManualSearch,
                    onSearchByName = onSearchByName
                )
            }

            LayoutType.TABLET_PORTRAIT -> {
                PortraitProductSectionHeader(
                    itemLineCount = itemLineCount,
                    onScanner = onScanner,
                    onManualSearch = onManualSearch,
                    onSearchByName = onSearchByName
                )
            }

            LayoutType.LANDSCAPE -> {
                LandscapeProductSectionHeader(
                    itemLineCount = itemLineCount,
                    onScanner = onScanner,
                    onManualSearch = onManualSearch,
                    onSearchByName = onSearchByName
                )
            }
        }

    }
}


@Composable
private fun MobilePortraitProductSectionHeader(
    itemLineCount: Int,
    onScanner: () -> Unit,
    onManualSearch: () -> Unit,
    onSearchByName: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.home_product_section_header_quaantity_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            ItemsBadge(count = itemLineCount)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                onClick = { onManualSearch() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_manual_button),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF6b2ea5),
                onClick = {onSearchByName() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = "Buscar por nombre",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF01a3b8),
                onClick = { onScanner() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = stringResource(Res.string.home_product_section_header_search_scan)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_scan),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}

@Composable
private fun PortraitProductSectionHeader(
    itemLineCount: Int,
    onScanner: () -> Unit,
    onManualSearch: () -> Unit,
    onSearchByName: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.home_product_section_header_quaantity_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            ItemsBadge(count = itemLineCount)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                onClick = { onManualSearch() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_manual_button),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF6b2ea5),
                onClick = {onSearchByName() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = "Buscar por nombre",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF01a3b8),
                onClick = { onScanner() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = stringResource(Res.string.home_product_section_header_search_scan)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_scan),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}

@Composable
private fun LandscapeProductSectionHeader(
    itemLineCount: Int,
    onScanner: () -> Unit,
    onManualSearch: () -> Unit,
    onSearchByName: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.home_product_section_header_quaantity_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            ItemsBadge(count = itemLineCount)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                onClick = { onManualSearch() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_manual_button),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF6b2ea5),
                onClick = {onSearchByName() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(Res.string.home_quotation_search_button)
                    )
                    Text(
                        text = "Buscar por nombre",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            CustomPrimaryButton(
                modifier = Modifier.height(38.dp),
                color = Color(0xFF01a3b8),
                onClick = { onScanner() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = stringResource(Res.string.home_product_section_header_search_scan)
                    )
                    Text(
                        text = stringResource(Res.string.home_product_section_header_search_scan),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}