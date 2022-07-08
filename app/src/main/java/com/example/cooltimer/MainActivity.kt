package com.example.cooltimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.cooltimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val stateViewModel by viewModels<StateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (stateViewModel.state.value == null) {
            stateViewModel.initState(State(false, 0, false))
        }

        binding!!.seekBar.max = 900
        binding!!.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                stateViewModel.setProgress(i)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding!!.startStop.setOnClickListener {
            if (!stateViewModel.isStarted()) {
                stateViewModel.start()
            } else {
                stateViewModel.stop()
            }
        }

        stateViewModel.state.observe(this, Observer<State> { renderState(it)})
    }

    private fun renderState(state: State) {
        binding!!.seekBar.isEnabled = !state.isStarted
        binding!!.seekBar.progress = state.progress
        binding!!.timer.text = state.progress.toString()
        binding!!.startStop.text = if (state.isStarted) "Stop" else "Start"
        if (state.isFinished) playAlarm()
    }

    fun playAlarm(): Unit {
        MediaPlayer.create(applicationContext, R.raw.bell_sound).start();
    }
}