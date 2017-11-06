package fr.minibilles.sporttimer

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import kotlinx.android.synthetic.main.duration_item.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class TimerFragment : Fragment() {

    val safeView: View
        get() = (view as View)

    val timer: TimerDescription
        get() = (context as MainActivity).timers[arguments.getInt(ARG_TIMER_ID)]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Find the timer
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.timer_name.text = timer.name

        val durationAdapter = DurationAdapter(context)
        rootView.timers.adapter = durationAdapter
        rootView.time_view.text = "0"

        rootView.add_duration.setOnClickListener {
            timer.durations.add(30)
            durationAdapter.notifyDataSetInvalidated()
            (context as MainActivity).notifyTimersChanged()
        }

        rootView.start_button.setOnClickListener {
            startTimer()
        }

        rootView.stop_button.setOnClickListener {
            stopTimer()
            if (timer.durations.size > 0) {
                safeView.time_view.text = formatDuration(timer.durations[0])
            }
        }

        // TODO Localized string
        // getString(R.string.section_format, arguments.getInt(ARG_TIMER_ID))
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()

    }

    inner class DurationTimer(duration: Int): CountDownTimer(duration*1000L, 100) {

        private var beepCount = 3

        override fun onFinish() {
            (context as MainActivity).playTone()

            // reset beep count
            beepCount = 3

            val previousStatus = safeView.timers.getChildAt(timer.current).status
            previousStatus.setImageResource(android.R.drawable.presence_invisible)

            timer.current = if (timer.current == timer.durations.size - 1) 0 else (timer.current+1)

            val status = safeView.timers.getChildAt(timer.current).status
            status.setImageResource(android.R.drawable.presence_online)

            safeView.time_view.text = "${timer.currentDuration}"

            currentDurationTimer = DurationTimer(timer.currentDuration)
            currentDurationTimer?.start()
        }

        override fun onTick(millisUntilFinished: Long) {
            (view as View).time_view.text = formatMillisecond(millisUntilFinished)

            // time to beep
            if (millisUntilFinished > (beepCount - 1 * 1000) + 500 && millisUntilFinished < (beepCount * 1000)) {
                (context as MainActivity).playBeep()
                beepCount -= 1
            }
        }

    }

    var currentDurationTimer : DurationTimer? = null

    private fun startTimer() {
        if (currentDurationTimer == null && timer.durations.size > 0) {
            timer.current = 0
            currentDurationTimer = DurationTimer(timer.currentDuration)
            currentDurationTimer?.start()

            val status = safeView.timers.getChildAt(timer.current).status
            status.setImageResource(android.R.drawable.presence_online)
        }
    }

    private fun stopTimer() {
        if (currentDurationTimer != null) {
            currentDurationTimer?.cancel()
            currentDurationTimer = null
            timer.current = 0

            for (i in 0 until safeView.timers.childCount) {
                val status = safeView.timers.getChildAt(i).status
                status.setImageResource(android.R.drawable.presence_invisible)
            }
        }
    }

    companion object {

        private val ARG_TIMER_ID = "timer_id"

        fun newInstance(timerId: Int): TimerFragment {
            val fragment = TimerFragment()
            val args = Bundle()
            args.putInt(ARG_TIMER_ID, timerId)
            fragment.arguments = args
            return fragment
        }
    }

    inner class DurationAdapter(context: Context):
        ArrayAdapter<Int>(context, R.layout.duration_item, timer.durations) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val durationView = inflater.inflate(R.layout.duration_item, parent, false)
            updateDuration(position, timer.durations[position], durationView)
            durationView.duration_seek.progress = timer.durations[position]
            durationView.duration_seek.onchange { _: SeekBar?, progress: Int, fromUser: Boolean ->
                if (fromUser) updateDuration(position, progress, durationView)
            }

            durationView.delete.setOnClickListener {
                timer.durations.removeAt(position)
                notifyDataSetInvalidated()
                (context as MainActivity).notifyTimersChanged()
            }

            return durationView
        }

        private fun updateDuration(index: Int, newValue: Int, durationView: View) {
            timer.durations[index] = newValue
            durationView.duration.text = formatDuration(newValue)
            (context as MainActivity).notifyTimersChanged()
        }
    }
}