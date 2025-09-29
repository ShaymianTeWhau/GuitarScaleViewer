package com.example.guitarscaleviewer.model

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RawScale(
    val name: String,
    val intervals: List<String>
)

fun loadScalesFromAssets(context: Context): List<Scale> {
    val json = context.assets.open("scales.json")
        .bufferedReader().use { it.readText() }

    val rawScales: List<RawScale> = Json.decodeFromString(json)

    return rawScales.map { raw ->
        Scale(
            name = raw.name,
            intervals = raw.intervals.map(::parseIntervalToken).toSet()
        )
    }
}

fun parseIntervalToken(token: String): Interval =
    Interval(
        degree = token.filter { it.isDigit() }.toInt(),
        modifier = when {
            'b' in token -> IntervalModifier.FLAT
            '#' in token -> IntervalModifier.SHARP
            else -> IntervalModifier.NATURAL
        }
    )

