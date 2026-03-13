package com.example.skb_android.ui.kit

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.example.skb_android.R

@Composable
fun EmployerLogo(url: String?, size: Dp) {
    val isPreview = LocalInspectionMode.current;

    if (isPreview || url == null) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = ImageVector.vectorResource(R.drawable.material_icon_android),
            contentDescription = "Лого работодателя"
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = "Загруженное лого работодателя",
            modifier = Modifier.size(size)
        )
    }

}