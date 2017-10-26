package sporttimer.minibilles.fr.sporttimer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_main.view.*
/**
 * A placeholder fragment containing a simple view.
 */
class TimerFragment : Fragment() {

    private lateinit var timer: TimerDescription

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.timer_name.text = SpannableStringBuilder(timer.name)

        rootView.timers.adapter = DurationListAdapter(timer.durations)

        // TODO Localized string
        // getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(timer: TimerDescription, sectionNumber: Int): TimerFragment {
            val fragment = TimerFragment()
            fragment.timer = timer
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}