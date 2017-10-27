package fr.minibilles.sporttimer

import org.json.JSONObject
import org.json.JSONStringer

data class TimerDescription(
        var name: String = "New timer",
        var durations: MutableList<Int> = arrayListOf(30, 30),

        var time: Int = 0,
        var count: Int = 0
) {
    val json: String
        get() {
            val b = JSONStringer()
            b.`object`()
            b.key("name"); b.value(name)

            b.key("durations")
            b.array()
            durations.forEach { b.value(it) }
            b.endArray()

            b.key("time"); b.value(time)
            b.key("count"); b.value(count)

            b.endObject()
            return b.toString()
        }

    companion object {
        fun read(json: JSONObject): TimerDescription {
            return TimerDescription(
                    name = json.getString("name"),
                    time = json.optInt("time", 0),
                    count = json.optInt("count", 0),
                    durations = json.getJSONArray("durations").toIntArrayList()
                )
        }
    }
}