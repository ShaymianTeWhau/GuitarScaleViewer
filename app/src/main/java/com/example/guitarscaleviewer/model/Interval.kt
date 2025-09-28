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
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Interval) return false

        // normalize this degree
        var thisDegreeNormalized = if(modifier == IntervalModifier.FLAT) degree - 1 else if(modifier == IntervalModifier.SHARP) degree + 1 else degree
        if(thisDegreeNormalized == 0) thisDegreeNormalized = 7
        if(thisDegreeNormalized == 8) thisDegreeNormalized = 1

        // normalize other degree
        var otherDegreeNormalized = if(modifier == IntervalModifier.FLAT) degree - 1 else if(modifier == IntervalModifier.SHARP) degree + 1 else degree
        if(otherDegreeNormalized == 0) otherDegreeNormalized = 7
        if(otherDegreeNormalized == 8) otherDegreeNormalized = 1

        // compare notes
        return thisDegreeNormalized == otherDegreeNormalized
    }

    override fun hashCode(): Int {
        var result = degree
        result = 31 * result + modifier.hashCode()
        return result
    }
}