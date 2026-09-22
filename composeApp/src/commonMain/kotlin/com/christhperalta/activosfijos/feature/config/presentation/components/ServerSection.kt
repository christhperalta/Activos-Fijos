package com.christhperalta.activosfijos.feature.config.presentation.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.DataSaverOff
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.config_btn_save
import activosfijos.composeapp.generated.resources.config_btn_saved
import activosfijos.composeapp.generated.resources.config_server_section_circular_progress_indicator
import activosfijos.composeapp.generated.resources.config_server_section_dropdown_fild_protocol
import activosfijos.composeapp.generated.resources.config_server_section_input_database
import activosfijos.composeapp.generated.resources.config_server_section_input_host
import activosfijos.composeapp.generated.resources.config_server_section_input_host_placeholder
import activosfijos.composeapp.generated.resources.config_server_section_input_port
import activosfijos.composeapp.generated.resources.config_server_section_input_port_placeholder
import activosfijos.composeapp.generated.resources.config_server_section_title
import com.christhperalta.activosfijos.core.presentation.CustomDropdownField
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.feature.config.presentation.ConfigUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ServerSection(
    state: ConfigUiState,
    onProtocolChange: (String) -> Unit,
    onHostChange: (String) -> Unit,
    onPortChange: (String) -> Unit,
    onTestConnection: () -> Unit,
    onSave: () -> Unit,
    onDatabaseNameChange: (String) -> Unit,
) {
    ConfigSectionCard(
        title = stringResource(Res.string.config_server_section_title),
        icon = Icons.Default.Dns,
    ) {

        val (focus1, focus2, focus3) = remember { FocusRequester.createRefs() }
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomDropdownField(
                icon = Icons.Default.Http,
                label = stringResource(Res.string.config_server_section_dropdown_fild_protocol),
                selected = state.protocol,
                options = listOf("http", "https"),
                onSelect = onProtocolChange,
                itemLabel = { it },

            )

            CustomInput(
                modifier = Modifier.focusRequester(focus1),
                value = state.host,
                onValueChange = onHostChange,
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_server_section_input_host),
                    placeholder = stringResource(Res.string.config_server_section_input_host_placeholder),
                    icon = Icons.Default.Storage,
                    isError = state.host?.isBlank() == true && state.isServerSaved,
                ),
                fieldAction = {focus2.requestFocus()}
            )

            CustomInput(
                modifier = Modifier.focusRequester(focus2),
                value = state.port,
                onValueChange = onPortChange,
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_server_section_input_port),
                    placeholder = stringResource(Res.string.config_server_section_input_port_placeholder),
                    icon = Icons.Default.Router,
                    keyboardType = KeyboardType.Number,
                    isError = state.port?.isBlank() == true && state.isServerSaved,
                ),
                fieldAction = {focus3.requestFocus()}
            )

            CustomInput(
                modifier = Modifier.focusRequester(focus3),
                value = state.databaseName,
                onValueChange = onDatabaseNameChange,
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_server_section_input_database),
                    icon = Icons.Default.DataSaverOff,
                    isError = state.databaseName?.isBlank() == true && state.isStoreSaved,
                    imeAction = ImeAction.Done
                ),
                fieldAction = { keyboardController?.hide() }
            )
        }



        AnimatedVisibility(visible = state.testResult != null) {
            state.testResult?.let { TestResultBanner(result = it) }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = onTestConnection,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                enabled = state.host?.isNotBlank() == true && state.port?.isNotBlank() == true && !state.isTesting,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                if (state.isTesting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomText(
                        text = stringResource(Res.string.config_server_section_circular_progress_indicator),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            CustomPrimaryButton(
                modifier =  Modifier.height(46.dp).padding(horizontal = 0.dp).weight(1f),
                enabled = state.host?.isNotBlank() == true && state.port?.isNotBlank() == true,
                onClick = onSave
            ){
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                CustomText(
                    text = if (state.isServerSaved) stringResource(Res.string.config_btn_saved) else stringResource(Res.string.config_btn_save),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}