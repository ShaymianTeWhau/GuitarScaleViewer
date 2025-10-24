package com.example.guitarscaleviewer.model

import androidx.compose.ui.graphics.Color

// represents a scale note and it's position on the fretboard
// these are the circles that appear on the diagram
data class FretNote(
    val fret: Int, // horizontal location on fretboard
    val string: Int, // vertical location on fretboard
    val note: String, // note value e.g "C", "F#" ...
    val scaleNum: String = note, // scale number value e.g "1", "2", "b3" // so it is a String not Int
    val backgroundColor: Color = Color.White,
    val borderColor: Color = Color.Gray,
    val textColor: Color = Color.Black
)