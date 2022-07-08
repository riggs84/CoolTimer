package com.example.cooltimer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateViewModel : ViewModel() {

    val state: LiveData<State> get() = stateLiveData
    private val stateLiveData = MutableLiveData<State>()
    private lateinit var timer: CountDownTimer

    fun initState(state: State): Unit {
        stateLiveData.value = state
    }

    fun setProgress(progressValue: Int): Unit {
        val oldState = stateLiveData.value
        stateLiveData.value = oldState!!.copy(progress = progressValue)
    }

    fun isStarted(): Boolean {
        return stateLiveData.value!!.isStarted
    }

    fun start(): Unit {
        val oldState = stateLiveData.value
        timer = object : CountDownTimer(stateLiveData.value!!.progress * 1000L, 1000) {
            override fun onTick(p0: Long) {
                Log.d("azaza", "tick: ${stateLiveData.value!!.progress * 1000L}")
                stateLiveData.value = oldState!!.copy(secondsRemains = (p0 / 60 / 1000).toInt(), isStarted = true)
            }

            override fun onFinish() {
                stateLiveData.value = oldState!!.copy(isFinished = true, secondsRemains = 0, progress = 0)
            }
        }
        timer.start()
    }

    fun stop(): Unit {
        val oldState = stateLiveData.value
        timer.cancel()
        stateLiveData.value = oldState!!.copy(isStarted = false,
                                              isFinished = false,
                                              secondsRemains = 0,
                                              progress = 0)
    }
}

data class State(
        var isStarted: Boolean = false,
        var progress: Int = 0,
        var secondsRemains: Int = 0,
        var isFinished: Boolean = false,
                );