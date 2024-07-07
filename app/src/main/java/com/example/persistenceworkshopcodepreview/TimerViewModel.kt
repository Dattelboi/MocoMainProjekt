package com.example.persistenceworkshopcodepreview

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TimerState(val isRunning: Boolean, val timeLeft: Long, val taskTitle: String?)
class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TimerSettingRepository
    val defaultTimerTime: MutableLiveData<Int> = MutableLiveData()
    private val _timerState = MutableLiveData(TimerState(false, 0L, null))
    val timerState: LiveData<TimerState> = _timerState
    private var countDownTimer: CountDownTimer? = null

    init {
        val timerSettingDao = AppDatabase.getDatabase(application).timerSettingDao()
        repository = TimerSettingRepository(timerSettingDao)
        loadTimerSetting()
    }

    private fun loadTimerSetting() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val timerSetting = repository.getTimerSetting()
                withContext(Dispatchers.Main) {
                    defaultTimerTime.value = timerSetting?.defaultTimerTime?.toInt() ?: 25
                }
            }
        }
    }

    fun saveTimerSetting(newTimerTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val timerSetting = TimerSetting(id = 1, defaultTimerTime = newTimerTime.toLong())
            repository.insertTimerSetting(timerSetting)
        }
    }

    fun updateTimerSetting(newTimerTime: Int) {
        saveTimerSetting(newTimerTime)
    }

    fun getTimerSetting(callback: (TimerSetting?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val timerSetting = repository.getTimerSetting()
            withContext(Dispatchers.Main) {
                callback(timerSetting)
            }
        }
    }

    fun startTimer(task: String? = null, duration: Int? = null) {
        val time = (duration ?: defaultTimerTime.value ?: 25) * 60 * 1000L
        _timerState.value = TimerState(true, time, task)

        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = _timerState.value?.copy(timeLeft = millisUntilFinished)
            }

            override fun onFinish() {
                _timerState.value = TimerState(false, 0L, task)
            }
        }.start()
    }


    fun stopTimer() {
        countDownTimer?.cancel()
        _timerState.value = TimerState(false, 0L, _timerState.value?.taskTitle)
        resetTimer()
    }

    private fun resetTimer() {
        val time = (defaultTimerTime.value ?: 25) * 60 * 1000L
        _timerState.value = TimerState(false, time, _timerState.value?.taskTitle)
    }
}
