package com.example.guitarscaleviewer.model

import android.util.Log
import androidx.compose.ui.graphics.Color

fun createScale(totalFrets: Int = 12, stringTuning:List<String> = listOf("E", "A", "D", "G", "B", "E"), tonicNote:String = "C", intervals:Set<Interval>): Set<FretNote> {
    val fretNoteScale: MutableSet<FretNote> = mutableSetOf()
    val allNotes = setOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
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

    // create FretNotes for whole fretboard
    val chromaticFretNotes: MutableSet<FretNote> = mutableSetOf() // a chromatic scale contains all notes
    var stringNumber = 1
    for(openStringNote in stringTuning.reversed()){
        var curFret = 0
        var curNote = openStringNote
        val openStringNoteIndex = allNotes.indexOf(openStringNote)
        val tonicNoteIndex = allNotes.indexOf(tonicNote)
        var lastIntervalMatch = 0
        Log.d(".", "tonicNoteIndex:$tonicNoteIndex")
        while (curFret <= totalFrets){

            // calc current note
            var curNoteIndex = openStringNoteIndex + curFret
            if (curNoteIndex >= (allNotes.size)) curNoteIndex -= allNotes.size
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
                Log.d(",", "compare curChromaticInterval:$curChromaticInterval -- interval[$i]: ${intervals.elementAt(i)}")
                if (curChromaticInterval.degree == intervals.elementAt(i).degree && curChromaticInterval.modifier == intervals.elementAt(i).modifier){
                    intervalMatchFound = true
                    lastIntervalMatch = i
                    Log.d(",", "match!")
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

    // temp just return chromatic
    val result: Set<FretNote> = fretNoteScale
    return result
}