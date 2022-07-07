package com.example.cooltimer

import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import android.os.Bundle
import com.example.cooltimer.MainActivity
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import android.media.MediaPlayer
import android.view.View
import com.example.cooltimer.R
import com.example.cooltimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var countDownTimer: CountDownTimer? = null
    private var state: State? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        if (savedInstanceState != null) {
            state = savedInstanceState.getParcelable(STATE_KEY)
        } else {
            state = State()
        }
        binding!!.seekBar.max = 900
        binding!!.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                state!!.progress = i
                binding!!.timer.text = state!!.progress.toString()
                // TODO we create new thread every time we call onCreate() Activity LC. Expensive
                start(state!!.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding!!.startStop.setOnClickListener { view: View? ->
            state!!.isStarted = !state!!.isStarted
            binding!!.seekBar.isEnabled = !state!!.isStarted
            binding!!.startStop.text = if (state!!.isStarted) "Stop" else "Start"
            if (state!!.isStarted) {
                countDownTimer!!.start()
            } else {
                countDownTimer!!.cancel()
            }
        }
    }

    fun start(progress: Int) {
        countDownTimer = object : CountDownTimer(progress * 1000L, 1000) {
            override fun onTick(l: Long) {
                state!!.secondsRemains = l.toInt() / 1000
                binding!!.timer.text = state!!.secondsRemains.toString()
            }

            override fun onFinish() {
                MediaPlayer.create(applicationContext, R.raw.bell_sound).start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_KEY, state)
    }

    companion object {
        private const val STATE_KEY = "State"
    }
}