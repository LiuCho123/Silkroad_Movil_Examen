package com.example.hollow_knight_silkroad.View.Components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun rememberBase64Painter(base64String: String?): Painter {
    return remember(base64String) {
        if (base64String.isNullOrBlank()) {
            return@remember BitmapPainter(ImageBitmap(1, 1))
        }
        try {
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                BitmapPainter(ImageBitmap(1, 1))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BitmapPainter(ImageBitmap(1, 1))
        }
    }
}