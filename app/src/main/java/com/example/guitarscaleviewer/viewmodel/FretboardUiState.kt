package com.example.guitarscaleviewer.viewmodel

import com.example.guitarscaleviewer.model.FretNote

data class FretboardUiState(
    val tonicNote: String = "C",
    val tuning: List<String> = listOf("E", "A", "D", "G", "B", "E"),
    val numStrings: Int = 6,
    val numFrets: Int = 15,
    var showScaleNum: Boolean = false,
    val fretNotes: Set<FretNote> = setOf( // should delete this at some stage
        FretNote(fret = 8, string = 6, note = "C", scaleNum = "1"),
        FretNote(fret = 10, string = 5, note = "G", scaleNum = "5"),
        FretNote(fret = 9, string = 4, note = "E", scaleNum = "3"),
        FretNote(fret = 9, string = 3, note = "B", scaleNum = "7"),
        FretNote(fret = 8, string = 2, note = "E", scaleNum = "3"),
        FretNote(fret = 8, string = 1, note = "C", scaleNum = "1"))
)