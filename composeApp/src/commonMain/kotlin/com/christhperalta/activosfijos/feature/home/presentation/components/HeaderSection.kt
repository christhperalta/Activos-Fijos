package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.SendTimeExtension
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.home_header_new_button
import activosfijos.composeapp.generated.resources.home_header_section_complete_send
import com.christhperalta.activosfijos.core.presentation.AdaptiveLayout
import com.christhperalta.activosfijos.core.presentation.CustomButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.core.presentation.LayoutType
import org.jetbrains.compose.resources.stringResource


@Composable
fun HeaderSection(onFiscalInfoDialog: () -> Unit, onNew: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AdaptiveLayout { layoutType ->
            when (layoutType) {
                LayoutType.MOBILE_PORTRAIT -> {
                    MobilePortraitHeaderSection(
                        onFiscalInfoDialog = onFiscalInfoDialog,
                        onNew = onNew
                    )
                }

                LayoutType.TABLET_PORTRAIT -> {
                    PortraitHeaderSection(
                        onFiscalInfoDialog = onFiscalInfoDialog,
                        onNew = onNew
                    )
                }

                LayoutType.LANDSCAPE -> {
                    LandscapeHeaderSection(
                        onFiscalInfoDialog = onFiscalInfoDialog,
                        onNew = onNew
                    )
                }
            }
        }
    }


}

@Composable
private fun PortraitHeaderSection(
    onFiscalInfoDialog: () -> Unit,
    onNew: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(0.5.dp, Color(0xFF323232).copy(alpha = 0.08f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            B1PosLogo()
        }
        Row(
            modifier = Modifier.padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomButton(
                onClick = { onNew() },
                containerColor = Color(0xFFf94b05)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_new_button),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            CustomButton(
                onClick = { onFiscalInfoDialog() },
                containerColor = Color(0xFF16a34a)
            ) {
                Icon(
                    imageVector = Icons.Default.SendTimeExtension,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_section_complete_send),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun LandscapeHeaderSection(
    onFiscalInfoDialog: () -> Unit,
    onNew: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(0.5.dp, Color(0xFF323232).copy(alpha = 0.08f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            B1PosLogo()
        }
        Row(
            modifier = Modifier.padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomButton(
                onClick = { onNew() },
                containerColor = Color(0xFFf94b05)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_new_button),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            CustomButton(
                onClick = { onFiscalInfoDialog() },
                containerColor = Color(0xFF16a34a)
            ) {
                Icon(
                    imageVector = Icons.Default.SendTimeExtension,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_section_complete_send),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
private fun MobilePortraitHeaderSection(
    modifier: Modifier = Modifier,
    onFiscalInfoDialog: () -> Unit,
    onNew: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        B1PosLogo()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomButton(
                onClick = { onNew() },
                containerColor = Color(0xFFf94b05)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_new_button),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            CustomButton(
                onClick = { onFiscalInfoDialog() },
                containerColor = Color(0xFF16a34a)
            ) {
                Icon(
                    imageVector = Icons.Default.SendTimeExtension,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomText(
                    text = stringResource(Res.string.home_header_section_complete_send),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}