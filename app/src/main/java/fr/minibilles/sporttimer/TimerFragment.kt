package fr.minibilles.sporttimer

import android.content.Context
import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Find the timer
        val timer: TimerDescription = (context as MainActivity).timers[arguments.getInt(ARG_TIMER_ID)]

        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.timer_name.text = timer.name

        val durationAdapter = DurationAdapter(context, timer.durations)
        rootView.timers.adapter = durationAdapter

        rootView.add_duration.setOnClickListener {
            timer.durations.add(30)
            durationAdapter.notifyDataSetInvalidated()
        }
        // TODO Localized string
        // getString(R.string.section_format, arguments.getInt(ARG_TIMER_ID))
        return rootView
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

    inner class DurationAdapter(context: Context, val durations: MutableList<Int>):
        ArrayAdapter<Int>(context, R.layout.duration_item, durations) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val durationView = inflater.inflate(R.layout.duration_item, parent, false)
            updateDuration(position, durations[position], durationView)
            durationView.duration_seek.progress = durations[position]
            durationView.duration_seek.onchange { seekBar: SeekBar?, progress: Int, fromUser: Boolean ->
                if (fromUser) updateDuration(position, progress, durationView)
            }

            durationView.delete.setOnClickListener {
                durations.removeAt(position)
                notifyDataSetInvalidated()
            }

            return durationView
        }

        fun updateDuration(index: Int, newValue: Int, durationView: View) {
            durations[index] = newValue
            val text = "$newValue"
            durationView.duration.text = text
            durationView.current_time.text = text
        }
    }
}