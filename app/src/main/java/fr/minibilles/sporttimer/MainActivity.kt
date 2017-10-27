package fr.minibilles.sporttimer

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONTokener

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    /** List of timers */
    val timers: MutableList<TimerDescription> = ArrayList()

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    private val mSectionsPagerAdapter: SectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, timers)

    private fun reloadTimers() {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val timersJson = preferences.getString("timers", defaultTimersJson)
        val timersArray = JSONTokener(timersJson).nextValue() as JSONArray
        (0 until timersArray.length()).mapTo(timers) {
            TimerDescription.read(timersArray.getJSONObject(it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reloadTimers()

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        val deleteButton = delete

        add.setOnClickListener {
            val newTimer = TimerDescription(newTimerName(timers))
            timers.add(newTimer)
            container.adapter = mSectionsPagerAdapter
            container.currentItem = timers.size - 1
            deleteButton.isEnabled = timers.size > 1

            Snackbar.make(it, "Added '${newTimer.name}'", Snackbar.LENGTH_LONG).show()
        }

        delete.setOnClickListener {
            val removedTimer = timers[container.currentItem]
            timers.removeAt(container.currentItem)
            container.adapter = mSectionsPagerAdapter

            deleteButton.isEnabled = timers.size > 1
            Snackbar.make(it, "Deleted '${removedTimer.name}'", Snackbar.LENGTH_LONG).show()
        }
        delete.isEnabled = timers.size > 1

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager, private val timers: List<TimerDescription>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TimerFragment (defined as a static inner class below).
            return TimerFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return timers.size
        }
    }

}
