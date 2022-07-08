package com.example.cooltimer

import android.os.CountDownTimer
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

    fun isCountStarted(): Boolean {
        return stateLiveData.value!!.isStarted
    }

    fun startCount(): Unit {
        val oldState = stateLiveData.value
        timer = object : CountDownTimer(stateLiveData.value!!.progress * 1000L, 1000) {
            override fun onTick(p0: Long) {
                stateLiveData.value = oldState!!.copy(progress = (p0 / 1000).toInt(), isStarted = true)
            }

            override fun onFinish() {
                stateLiveData.value = oldState!!.copy(isFinished = true, progress = 0)
            }
        }
        timer.start()
    }

    fun stopCount(): Unit {
        val oldState = stateLiveData.value
        timer.cancel()
        stateLiveData.value = oldState!!.copy(isStarted = false,
                                              isFinished = false,
                                              progress = 0)
    }
}

data class State(
        var isStarted: Boolean = false,
        var progress: Int = 0,
        var isFinished: Boolean = false)