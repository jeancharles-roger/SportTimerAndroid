package fr.minibilles.sporttimer

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
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

    /** Tone sound id */
    var toneId = -1

    /** List of timers */
    val timers: MutableList<TimerDescription> = ArrayList()

    /** Sound pool for alarms */ @Suppress("DEPRECATION")
    private val soundPool: SoundPool = SoundPool(1, AudioManager.STREAM_ALARM, 0)

    /** Page adapter for timers */
    private val mSectionsPagerAdapter: SectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, timers)

    private fun loadSounds() {
        toneId = soundPool.load(this, R.raw.tone, 1)
    }

    private fun reloadTimers() {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val timersJson = preferences.getString("timers", defaultTimersJson)
        val timersArray = JSONTokener(timersJson).nextValue() as JSONArray
        (0 until timersArray.length()).mapTo(timers) {
            TimerDescription.read(timersArray.getJSONObject(it))
        }
    }

    private fun saveTimers() {
        val preferences = getPreferences(Context.MODE_PRIVATE)

        val timersArray = JSONArray(timers.map { it.json })
        val editor = preferences.edit()
        editor.putString("timers", timersArray.toString())
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadSounds()

        reloadTimers()

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
    }

    fun notifyTimersChanged() {
        // Saves timer each time, may change to a later save
        saveTimers()
    }

    fun addTimer() {
        val newTimer = TimerDescription(newTimerName(timers))
        timers.add(newTimer)
        container.adapter = mSectionsPagerAdapter
        container.currentItem = timers.size - 1

        notifyTimersChanged()
    }

    fun deleteTimer() {
        if (timers.size > 1) {
            timers.removeAt(container.currentItem)
            container.adapter = mSectionsPagerAdapter

            notifyTimersChanged()
        }
    }

    fun playWarning() {
        soundPool.play(toneId, 1F, 1F, 1, 0, 1F)
    }

    fun playEnd() {
        soundPool.play(toneId, 1F, 1F, 1, 0, 0.8F)
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

        if (id == R.id.add_timer) {
            addTimer()
            return true
        }
        if (id == R.id.delete_timer) {
            deleteTimer()
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
