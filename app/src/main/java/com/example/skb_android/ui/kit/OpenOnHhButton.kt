package com.example.skb_android.ui.kit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.example.skb_android.R
import androidx.core.net.toUri

@Composable
fun OpenOnHhButton(url: String, modifier: Modifier) {
    val context = LocalContext.current

    Button(
        modifier = modifier,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Открыть на hh"
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.material_icon_arrow_outward),
                contentDescription = "Назад"
            )
        }
    }
}