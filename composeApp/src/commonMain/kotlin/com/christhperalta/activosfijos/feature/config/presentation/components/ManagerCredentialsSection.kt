package com.christhperalta.activosfijos.feature.config.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.config_btn_save
import activosfijos.composeapp.generated.resources.config_btn_saved
import activosfijos.composeapp.generated.resources.config_manager_credetials_section_input_password
import activosfijos.composeapp.generated.resources.config_manager_credetials_section_input_username
import activosfijos.composeapp.generated.resources.config_manager_credetials_section_title
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import com.christhperalta.activosfijos.core.presentation.CustomPrimaryButton
import com.christhperalta.activosfijos.core.presentation.CustomText
import com.christhperalta.activosfijos.feature.config.presentation.ConfigUiState
import org.jetbrains.compose.resources.stringResource


@Composable
fun ManagerCredentialsSection(state: ConfigUiState, onUsernameChange: (String) -> Unit, onPasswordChange: (String) -> Unit, onSave: () -> Unit) {

    val (focus1, focus2) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current

    ConfigSectionCard(
        title = stringResource(Res.string.config_manager_credetials_section_title),
        icon = Icons.Default.Person,
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomInput(
                modifier = Modifier.focusRequester(focus1),
                value = state.username,
                onValueChange = onUsernameChange,
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_manager_credetials_section_input_username),
                    icon = Icons.Default.Person,
                    isError = state.username?.isBlank() == true && state.isManagerSaved,
                    imeAction = ImeAction.Next
                ),
                fieldAction = { focus2.requestFocus() }
            )

            CustomInput(
                modifier = Modifier.focusRequester(focus2),
                value = state.password,
                onValueChange = onPasswordChange,
                config = CustomInputConfig(
                    label = stringResource(Res.string.config_manager_credetials_section_input_password),
                    icon = Icons.Default.Person,
                    isError = state.password?.isBlank() == true && state.isManagerSaved,
                    imeAction = ImeAction.Done,
                    isPassword = true,
                    isOnlyPassword = true
                ),
                fieldAction = { keyboardController?.hide() }
            )

            CustomPrimaryButton(
                modifier = Modifier.align(Alignment.End).height(46.dp).padding(horizontal = 0.dp)
                    .fillMaxWidth(0.5f),
                enabled = state.username?.isNotBlank() == true && state.password?.isNotBlank() == true,
                onClick = onSave
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                CustomText(
                    text = if (state.isManagerSaved) stringResource(Res.string.config_btn_saved) else stringResource(
                        Res.string.config_btn_save
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}