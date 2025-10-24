package com.example.guitarscaleviewer.model

// intervals come in 3 kinds - flat, natural and sharp.
enum class IntervalModifier(val offset: Int){
    FLAT(-1), // lowers by 1 semitone (or 1 fret)
    NATURAL(0),
    SHARP(1) // increases by 1 semitone (or 1 fret)
}

// represents a musical interval by
// degree (1-7)
// modifier (flat, natural, sharp)
data class Interval(
    val degree: Int,
    val modifier: IntervalModifier = IntervalModifier.NATURAL
) {

    // two degrees are equal if they have the same number of semitones
    // this is so we can treat intervals such as b3 and #2 as equal
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
        // wrap within 12 tones
        return (baseSemitone + modifier.offset + 12) % 12
    }
}