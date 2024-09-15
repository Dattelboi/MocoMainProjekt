package com.example.persistenceworkshopcodepreview

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TimerState(
    val isRunning: Boolean,
    val timeLeft: Long,
    val title: String? = null
)

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TimerSettingRepository
    val defaultTimerTime: LiveData<Int>
        get() = _defaultTimerTime
    private val _defaultTimerTime = MutableLiveData<Int>()

    val timerState: LiveData<TimerState>
        get() = _timerState
    private val _timerState = MutableLiveData<TimerState>()

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
                    _defaultTimerTime.value = timerSetting?.defaultTimerTime?.toInt() ?: 25
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

    fun startTimer(todoTitle: String? = null, todoDuration: Int? = null) {
        val duration = todoDuration ?: (_defaultTimerTime.value ?: 25)
        val time = duration * 60 * 1000L
        _timerState.value = TimerState(true, time, todoTitle)

        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = _timerState.value?.copy(timeLeft = millisUntilFinished)
            }

            override fun onFinish() {
                _timerState.value = TimerState(false, 0L, todoTitle)
            }
        }.start()
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        _timerState.value = _timerState.value?.copy(isRunning = false)
    }

    fun sendTimerNotification() {
        val channelId = "timer_channel"
        val notificationId = 1

        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for Timer Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getApplication<Application>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(getApplication(), channelId)
            .setSmallIcon(R.drawable.pomidorek)
            .setContentTitle("Timer still running!")
            .setContentText("Your timer is still running in the background.") // Add time left
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        println(countDownTimer?.toString())
        // Show the notification
        with(NotificationManagerCompat.from(getApplication())) {
            notify(notificationId, builder.build())
        }
    }

}
