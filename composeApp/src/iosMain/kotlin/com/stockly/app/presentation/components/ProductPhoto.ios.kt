package com.stockly.app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
actual fun ProductPhoto(uri: String?, emoji: String, modifier: Modifier, emojiSize: Int) {
    Box(modifier, contentAlignment = Alignment.Center) { Text(emoji, fontSize = emojiSize.sp) }
}

@Composable
actual fun ProductPhotoPicker(onSelected: (String) -> Unit) {
    OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
        Text("Galería iOS: próxima etapa")
    }
}
