package com.example.guitarscaleviewer.model

enum class IntervalModifier{
    FLAT,
    NATURAL,
    SHARP
}

data class Interval(
    val degree: Int,
    val modifier: IntervalModifier = IntervalModifier.NATURAL
) {
}