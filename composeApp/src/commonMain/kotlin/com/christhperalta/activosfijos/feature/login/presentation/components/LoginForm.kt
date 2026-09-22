package com.christhperalta.activosfijos.feature.login.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.login_form_password_label
import activosfijos.composeapp.generated.resources.login_form_user_label
import com.christhperalta.activosfijos.core.presentation.CustomInput
import com.christhperalta.activosfijos.core.presentation.CustomInputConfig
import com.christhperalta.activosfijos.feature.login.presentation.LoginUiState
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction

@Composable
 fun LoginForm(
    uiState: LoginUiState,
    onUserChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onConfigClick: () -> Unit,
) {

    val (focus1, focus2) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.widthIn(300.dp, 500.dp)
    ) {

        CustomInput(
            modifier = Modifier.focusRequester(focus1),
            config = CustomInputConfig(
                label = stringResource(Res.string.login_form_user_label),
                icon = Icons.Default.Person,
            ),
            value = uiState.user.orEmpty(),
            onValueChange = onUserChanged,
            fieldAction = {focus2.requestFocus()}
        )

        Spacer(Modifier.height(13.dp))

        CustomInput(
            modifier = Modifier.focusRequester(focus2),
            config = CustomInputConfig(
                isPassword = true,
                label = stringResource(Res.string.login_form_password_label),
                imeAction = ImeAction.Done,
            ),
            value = uiState.password.orEmpty(),
            onValueChange = onPasswordChanged,
            fieldAction = { keyboardController?.hide() }
        )

        Spacer(Modifier.height(7.dp))

        LoginErrorMessage(uiState.error)

        Spacer(Modifier.height(7.dp))

        LoginButton(
            isLoading = uiState.isLoading,
            onClick = onLoginClick
        )

        Spacer(Modifier.height(15.dp))

        ConfigButton(
            onClick = onConfigClick
        )
    }
}