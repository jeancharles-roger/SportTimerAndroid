package fr.minibilles.sporttimer

import org.json.JSONArray
import org.json.JSONObject

data class TimerDescription(
        var name: String = "New timer",
        var durations: MutableList<Int> = arrayListOf(30, 30),

        var current: Int = 0,
        var count: Int = 0
) {
    val json: JSONObject
        get() {
            val result = JSONObject()
            result.put("name", name)
            result.put("durations", JSONArray(durations))
            result.put("current",current)
            result.put("count",count)
            return result
        }

    companion object {
        fun read(json: JSONObject): TimerDescription {
            return TimerDescription(
                    name = json.getString("name"),
                    current = json.optInt("current", 0),
                    count = json.optInt("count", 0),
                    durations = json.getJSONArray("durations").toIntArrayList()
                )
        }
    }

    val currentDuration: Int
        get() = durations[current]
}