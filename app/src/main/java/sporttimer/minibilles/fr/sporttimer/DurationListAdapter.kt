package sporttimer.minibilles.fr.sporttimer

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DurationListAdapter(val durations: List<Double>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(parent?.context)
        textView.text = "Duration $position"
        return textView
    }

    override fun getItem(position: Int): Any = durations[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = durations.size

}