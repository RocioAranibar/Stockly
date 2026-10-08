package com.stockly.app.presentation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
actual fun ProductPhoto(
    uri: String?,
    emoji: String,
    modifier: Modifier,
    emojiSize: Int
) {
    val context = LocalContext.current

    val bitmap = remember(uri) {
        uri?.let { value ->
            runCatching {
                context.contentResolver
                    .openInputStream(Uri.parse(value))
                    ?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
            }.getOrNull()
        }
    }

    Box(
        modifier = modifier.background(
            Color(0xFFF0F7F2),
            RoundedCornerShape(14.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Foto del producto",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else if (emoji.isNotBlank()) {
            Text(
                text = emoji,
                fontSize = emojiSize.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = "Agregar foto del producto",
                tint = Color(0xFF08785D),
                modifier = Modifier.size(52.dp)
            )
        }
    }
}
@Composable
actual fun ProductPhotoPicker(onSelected: (String) -> Unit) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onSelected(it.toString())
        }
    }

    Button(
        onClick = { launcher.launch("image/*") },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text("Elegir foto de la galería")
    }
}
