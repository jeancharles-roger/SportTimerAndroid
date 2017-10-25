package sporttimer.minibilles.fr.sporttimer

data class TimerDescription(
        var name: String = "New timer",
        var durations: MutableList<Double> = arrayListOf(30.0, 30.0),

        var currentTime: Double = 0.0,
        var globalCount: Int = 0,
        var counts: MutableList<Int> = ArrayList()
)