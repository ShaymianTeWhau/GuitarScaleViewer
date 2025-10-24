package com.example.guitarscaleviewer.model

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// represents a musical scale in the JSON (found in src/main/assets/scales.json)
@Serializable
data class RawScale(
    val name: String,
    val intervals: List<String>
)

// loads and parses musical scales from scales.json
fun loadScalesFromAssets(context: Context): List<Scale> {
    val json = context.assets.open("scales.json")
        .bufferedReader().use { it.readText() }

    val rawScales: List<RawScale> = Json.decodeFromString(json)

    // Convert each RawScale into a Scale object by parsing its interval tokens
    return rawScales.map { raw ->
        Scale(
            name = raw.name,
            intervals = raw.intervals.map(::parseIntervalToken).toSet()
        )
    }
}

// Converts a string token (like "b3", "#5", or "4") into an Interval object.
fun parseIntervalToken(token: String): Interval =
    Interval(
        degree = token.filter { it.isDigit() }.toInt(),
        modifier = when {
            'b' in token -> IntervalModifier.FLAT
            '#' in token -> IntervalModifier.SHARP
            else -> IntervalModifier.NATURAL
        }
    )

