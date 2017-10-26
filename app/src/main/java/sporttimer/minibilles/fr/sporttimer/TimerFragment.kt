package sporttimer.minibilles.fr.sporttimer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class TimerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Find the timer
        val timer: TimerDescription = (context as MainActivity).timers[arguments.getInt(ARG_TIMER_ID)]

        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.timer_name.text = timer.name

        rootView.timers.adapter = DurationAdapter(context, timer.durations)

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

    inner class DurationAdapter(context: Context, durations: MutableList<Double>):
        ArrayAdapter<Double>(context, R.layout.duration_item, durations) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflater.inflate(R.layout.duration_item, parent, false)
        }
    }
}