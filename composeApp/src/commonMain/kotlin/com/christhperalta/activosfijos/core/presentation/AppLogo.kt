
package com.christhperalta.activosfijos.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.app_logo_description
import activosfijos.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    width: Int = 200,
    height: Int = 110
) {
    Image(
        painter = painterResource(Res.drawable.logo),
        contentDescription = stringResource(Res.string.app_logo_description),
        modifier = modifier.width(width.dp).height(height.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Preview(name = "AppLogo")
@Composable
private fun PreviewAppLogo() {
    AppLogo()
}