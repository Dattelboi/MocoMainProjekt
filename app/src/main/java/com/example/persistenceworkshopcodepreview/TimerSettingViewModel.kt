package com.example.persistenceworkshopcodepreview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerSettingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TimerSettingRepository
    val defaultTimerTime: MutableLiveData<Int> = MutableLiveData()

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
            withContext(Dispatchers.Main) {
                defaultTimerTime.value = newTimerTime
            }
        }
    }

    fun getTimerSetting(callback: (TimerSetting?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val timerSetting = repository.getTimerSetting()
            withContext(Dispatchers.Main) {
                callback(timerSetting)
            }
        }
    }

    fun updateTimerSetting(newTimerTime: Int) {
        saveTimerSetting(newTimerTime)
    }
}

class TimerSettingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerSettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerSettingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
