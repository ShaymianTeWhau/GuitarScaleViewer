package com.example.guitarscaleviewer.model

import android.util.Log

enum class IntervalModifier(val offset: Int){
    FLAT(-1),
    NATURAL(0),
    SHARP(1)
}

data class Interval(
    val degree: Int,
    val modifier: IntervalModifier = IntervalModifier.NATURAL
) {
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Interval) return false

        // Convert both intervals to semitones
        return this.toSemitone() == other.toSemitone()
    }

    override fun hashCode(): Int {
        return toSemitone()
    }

    private fun toSemitone(): Int {
        val baseSemitone = when ((degree - 1) % 7 + 1) {
            1 -> 0  // unison
            2 -> 2  // major 2nd
            3 -> 4  // major 3rd
            4 -> 5  // perfect 4th
            5 -> 7  // perfect 5th
            6 -> 9  // major 6th
            7 -> 11 // major 7th
            else -> error("Invalid degree: $degree")
        }
        return (baseSemitone + modifier.offset + 12) % 12
    }
}