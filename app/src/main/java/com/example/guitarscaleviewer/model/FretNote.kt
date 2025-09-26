package com.example.guitarscaleviewer.model

import androidx.compose.ui.graphics.Color

data class FretNote(
    val fret: Int,
    val string: Int,
    val note: String,
    val backgroundColor: Color = Color.White,
    val borderColor: Color = Color.Gray,
    val textColor: Color = Color.Black
)