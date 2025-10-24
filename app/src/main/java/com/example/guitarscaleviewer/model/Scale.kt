package com.example.guitarscaleviewer.model

// represents a musical scale of name and a set of Intervals
data class Scale(
    val name: String,
    val intervals: Set<Interval>
)
