package sporttimer.minibilles.fr.sporttimer

data class TimerDescription(
        var name: String = "New timer",
        var durations: MutableList<Int> = arrayListOf(30, 30),

        var currentTime: Double = 0.0,
        var globalCount: Int = 0,
        var counts: MutableList<Int> = ArrayList()
)