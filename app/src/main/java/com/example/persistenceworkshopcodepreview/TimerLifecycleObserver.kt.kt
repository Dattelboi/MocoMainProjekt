package com.example.persistenceworkshopcodepreview

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class TimerLifecycleObserver(private val timerViewModel: TimerViewModel) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        timerViewModel.stopTimer()
    }
}