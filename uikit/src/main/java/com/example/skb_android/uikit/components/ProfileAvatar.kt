package com.example.skb_android.uikit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.skb_android.uikit.R

@Composable
fun ProfileAvatar(
    avatarURI: String?,
    onClick: (() -> Unit)? = null
) {
    val modifier = Modifier
        .clip(CircleShape)
        .size(128.dp)

    val final = if (onClick != null) modifier.clickable { onClick() } else modifier

    AsyncImage(
        model = avatarURI,
        contentDescription = "Avatar",
        contentScale = ContentScale.Crop,
        modifier = final,
        error = painterResource(com.example.skb_android.core.R.drawable.material_icon_profile)
    )
}
