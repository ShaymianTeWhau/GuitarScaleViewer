package com.example.guitarscaleviewer.viewmodel

import com.example.guitarscaleviewer.model.FretNote
import com.example.guitarscaleviewer.model.Instrument
import com.example.guitarscaleviewer.model.Scale
import com.example.guitarscaleviewer.model.getScales

data class FretboardUiState(
    val tonicNote: String = "C",
    val instrument: Instrument = Instrument.GUITAR,
    val scale: Scale = Scale(name = "None", intervals = emptySet()),
    var tuning: List<String> = listOf("E", "A", "D", "G", "B", "E"),
    val numStrings: Int = 6,
    val numFrets: Int = 15,
    var showScaleNum: Boolean = false,
    val fretNotes: Set<FretNote> = emptySet()
)