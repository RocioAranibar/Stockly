package com.stockly.app.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Imagen local. Una URL persistente en la nube se agregará en la siguiente etapa. */
@Composable
expect fun ProductPhoto(uri: String?, emoji: String, modifier: Modifier = Modifier, emojiSize: Int = 26)

@Composable
expect fun ProductPhotoPicker(onSelected: (String) -> Unit)
