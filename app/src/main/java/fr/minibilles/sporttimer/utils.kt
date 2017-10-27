package fr.minibilles.sporttimer

import android.widget.SeekBar
import org.json.JSONArray

/**
 * Shortcut to register a callback for a [SeekBar]
 */
fun SeekBar.onchange(callback: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit) {
    this.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            callback(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    })
}

fun JSONArray.toIntArrayList(): ArrayList<Int> {
    val length = length()
    val result = ArrayList<Int>(length)
    for (i in 0 until length) {
        result.add(getInt(i))
    }
    return result
}

private val intEnding = Regex("^.*([0-9]+)$")

fun newTimerName(timers: List<TimerDescription>): String {
    var higher = 0
    timers.forEach {
        val matchResult = intEnding.matchEntire(it.name)
        if (matchResult != null) {
            val current = matchResult.groups[1]?.value?.toInt()
            if (current != null && current > higher) {
                higher = current
            }
        }
    }
    return "Timer ${higher + 1}"
}

val defaultTimersJson =
"""[
  { "name": "Timer 1", "durations": [30, 30] },
  { "name": "Timer 2", "durations": [60, 30] },
  { "name": "Timer 3", "durations": [120, 30] }
]"""