package com.example.hollow_knight_silkroad.Model

import androidx.annotation.DrawableRes

sealed interface ContentBlock

data class TextBlock(
    val text: String,
    val isHeading: Boolean = false
) : ContentBlock

data class ImageBlock(
    @DrawableRes val resourceId: Int,
    val contentDescription: String? = null
) : ContentBlock