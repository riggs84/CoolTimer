package com.example.cooltimer

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.cooltimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val stateViewModel by viewModels<StateViewModel>()
    private var isSoundEnabled = true
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (stateViewModel.state.value == null) {
            stateViewModel.initState(State(false, 0, false))
        }

        updateMaxLimit()
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                stateViewModel.setProgress(i)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.startStop.setOnClickListener {
            if (!stateViewModel.isCountStarted()) {
                stateViewModel.startCount()
            } else {
                stateViewModel.stopCount()
            }
        }

        stateViewModel.state.observe(this, Observer<State> { renderState(it)})
    }

    private fun renderState(state: State) {
        binding.seekBar.isEnabled = !state.isStarted
        binding.seekBar.progress = state.progress
        binding.timer.text = state.progress.toString()
        binding.startStop.text = if (state.isStarted) "Stop" else "Start"
        if (state.isFinished) playAlarm()
    }

    private fun playAlarm(): Unit {
        if (isSoundEnabled) {
            MediaPlayer.create(applicationContext, R.raw.bell_sound).start()
        }
    }

    private fun updateMaxLimit() {
        binding.seekBar.max = sharedPreferences.getString(MAX_INTERVAL, "0")!!.toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.timer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val openAbout = Intent(this, AboutActivity::class.java)
        val openSettings = Intent(this, SettingsActivity::class.java)

        when(item.itemId) {
            R.id.menu_about -> { startActivity(openAbout); return true }
            R.id.menu_settings -> { startActivity(openSettings); return true }
        }

        return false
    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences?, preferenceKey: String?) {
        when(preferenceKey) {
            MAX_INTERVAL -> updateMaxLimit()
            ENABLE_SOUND -> isSoundEnabled = !isSoundEnabled
        }
    }

    override fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    companion object {
        const val MAX_INTERVAL = "max_interval_pref"
        const val ENABLE_SOUND = "enable_sound_pref"
    }
}