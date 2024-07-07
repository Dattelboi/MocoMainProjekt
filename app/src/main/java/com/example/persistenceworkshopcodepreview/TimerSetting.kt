package com.example.persistenceworkshopcodepreview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_settings")
data class TimerSetting(
    @PrimaryKey val id: Int,
    val defaultTimerTime: Long
)
