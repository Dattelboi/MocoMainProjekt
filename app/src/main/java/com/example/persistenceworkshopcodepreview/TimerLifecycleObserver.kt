package com.example.persistenceworkshopcodepreview

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

class TimerLifecycleObserver(private val timerViewModel: TimerViewModel) : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        timerViewModel.sendTimerNotification()
    }
}