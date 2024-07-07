package com.example.persistenceworkshopcodepreview

class TimerSettingRepository(private val timerSettingDao: TimerSettingDao) {

    fun getTimerSetting(): TimerSetting? {
        return timerSettingDao.getTimerSetting()
    }

    suspend fun insertTimerSetting(timerSetting: TimerSetting) {
        timerSettingDao.insertTimerSetting(timerSetting)
    }
}
