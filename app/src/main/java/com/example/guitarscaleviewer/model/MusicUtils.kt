package com.example.guitarscaleviewer.model

import android.util.Log
import androidx.compose.ui.graphics.Color

val MINOR_SCALE_EXAMPLE = createFretNotesScale(
    tonicNote = "E",
    intervals = setOf(
        Interval(1),
        Interval(2),
        Interval(3, IntervalModifier.FLAT),
        Interval(4),
        Interval(5),
        Interval(6, IntervalModifier.FLAT),
        Interval(7, IntervalModifier.FLAT),
    )
)

val MAJOR_SCALE_EXAMPLE = createFretNotesScale(
    tonicNote = "C",
    intervals = setOf(
        Interval(1),
        Interval(2),
        Interval(3),
        Interval(4),
        Interval(5),
        Interval(6),
        Interval(7),
    )
)

val allKeys = listOf("C", "C#", "Db", "D", "Eb", "E", "F", "F#", "Gb", "G", "Ab", "A", "Bb", "B")

fun createFretNotesScale(totalFrets: Int = 15, stringTuning:List<String> = listOf("E", "A", "D", "G", "B", "E"), tonicNote:String = "C", intervals:Set<Interval>): Set<FretNote> {
    Log.d(",","createFretNotesScale for tuning: $stringTuning - tonic:$tonicNote")
    val fretNoteScale: MutableSet<FretNote> = mutableSetOf()

    val useFlats:Boolean = tonicNote.endsWith('b')
    val flats = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
    val sharps = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val allNotes = if(useFlats) flats else sharps

    val allChromaticIntervals = setOf(
        Interval(1),
        Interval(2, IntervalModifier.FLAT),
        Interval(2),
        Interval(3, IntervalModifier.FLAT),
        Interval(3),
        Interval(4),
        Interval(5, IntervalModifier.FLAT),
        Interval(5),
        Interval(6, IntervalModifier.FLAT),
        Interval(6),
        Interval(7, IntervalModifier.FLAT),
        Interval(7)
    )

    val noteIndex: Map<String, Int> = mapOf(
        "C" to 0,
        "C#" to 1, "Db" to 1,
        "D" to 2,
        "D#" to 3, "Eb" to 3,
        "E" to 4,
        "F" to 5,
        "F#" to 6, "Gb" to 6,
        "G" to 7,
        "G#" to 8, "Ab" to 8,
        "A" to 9,
        "A#" to 10, "Bb" to 10,
        "B" to 11
    )

    // create FretNotes for whole fretboard
    val chromaticFretNotes: MutableSet<FretNote> = mutableSetOf() // a chromatic scale contains all notes
    var stringNumber = 1
    for(openStringNote in stringTuning.reversed()){
        var curFret = 0
        var curNote = openStringNote
        if (useFlats) curNote = flats[noteIndex[curNote]!!]
        val openStringNoteIndex = allNotes.indexOf(curNote)
        val tonicNoteIndex = allNotes.indexOf(tonicNote)
        var lastIntervalMatch = 0
        while (curFret <= totalFrets){

            // calc current note
            var curNoteIndex = openStringNoteIndex + curFret
            while (curNoteIndex >= (allNotes.size)) curNoteIndex -= allNotes.size
            curNote = allNotes.elementAt(curNoteIndex)

            // calc current scale num
            var curChromaticIntervalIndex = curNoteIndex - tonicNoteIndex
            if (curNoteIndex < tonicNoteIndex) curChromaticIntervalIndex += allChromaticIntervals.size
            val curChromaticInterval = allChromaticIntervals.elementAt(curChromaticIntervalIndex)
            val tag: String = if (curChromaticInterval.modifier == IntervalModifier.FLAT) "b" else if (curChromaticInterval.modifier == IntervalModifier.SHARP) "#" else ""
            val curScaleNum = "$tag ${curChromaticInterval.degree}"


            // make color red if curNote == tonic
            val color = if(curNote == tonicNote) Color(0xffff9c9c) else Color.White

            // create FretNote for cur fret and string
            val newFretNote = FretNote(
                fret = curFret,
                string = stringNumber,
                note = curNote,
                scaleNum = curScaleNum,
                backgroundColor = color
            )
            chromaticFretNotes.add(newFretNote)

            // if current interval matches any scale interval then add it to fretNoteScale
            var i = lastIntervalMatch
            var intervalMatchFound = false
            while(i < intervals.size){
                if (curChromaticInterval == intervals.elementAt(i) || curChromaticInterval == Interval(1)){
                    intervalMatchFound = true
                    lastIntervalMatch = i
                    fretNoteScale.add(newFretNote)
                    break
                }
                i++
            }

            if (!intervalMatchFound){
                lastIntervalMatch = 0

            }
            curFret++
        }
        stringNumber++
    }

    val result: Set<FretNote> = fretNoteScale
    Log.d(",","createFretNotesScale finished")
    return result
}

fun getScales(): List<Scale> {
    // temp hard coded scales
    val majorScale = Scale(
        name = "Ionian (Major)",
        intervals = setOf(
            Interval(1),
            Interval(2),
            Interval(3),
            Interval(4),
            Interval(5),
            Interval(6),
            Interval(7)
        )
    )

    val minorScale = Scale(
        name = "Aeolian (minor)",
        intervals = setOf(
            Interval(1),
            Interval(2),
            Interval(3, IntervalModifier.FLAT),
            Interval(4),
            Interval(5),
            Interval(6, IntervalModifier.FLAT),
            Interval(7, IntervalModifier.FLAT),
        )
    )

    return listOf(majorScale, minorScale)
}